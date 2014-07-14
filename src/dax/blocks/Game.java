package dax.blocks;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ListIterator;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.TrueTypeFont;

import dax.blocks.block.Block;
import dax.blocks.console.Console;
import dax.blocks.gui.GuiObjectBlank;
import dax.blocks.gui.GuiScreen;
import dax.blocks.gui.GuiScreenLoading;
import dax.blocks.gui.GuiScreenMainMenu;
import dax.blocks.gui.GuiScreenMenu;
import dax.blocks.render.RenderEngine;
import dax.blocks.settings.Settings;
import dax.blocks.sound.SoundManager;
import dax.blocks.world.World;

public class Game implements Runnable {

	public static Settings settings = new Settings();
	public static Console console = new Console();
	public static WorldsManager worlds = new WorldsManager();
	public static SoundManager sound = new SoundManager();

	private File configFile = new File("settings.txt");
	public boolean showbg = false;

	public RenderEngine renderEngine;

	public boolean consoleOpen = false;

	public static final int TPS = 20;
	public static final double TICK_TIME = 1.0D / TPS;
	public int ticks = 0;

	String ticksString = "N/A";

	public int width = 800;
	public int height = 480;

	public GuiScreen guiScreen;

	public static final String TITLE = Start.GAME_NAME + " v"
			+ Start.GAME_VERSION;

	public boolean isFullscreen = false;

	public TrueTypeFont font;

	long lastFrame;
	int fpsCounter;
	int fps = 0;
	long lastFPS;

	public World world;

	public boolean ingame = false;

	public int vertices = 0;

	private static Game instance;

	public Game() {
		instance = this;
	}

	public static Game getInstance() {
		return instance;
	}

	@Override
	public void run() {
		try {
			if (!this.configFile.exists()) {
				this.configFile.createNewFile();
				Game.settings.saveToFile(configFile);
			}

			Game.settings.loadFromFile(this.configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		setDisplayMode(width, height, isFullscreen);
		load(true);

		renderEngine = new RenderEngine(Game.settings.enable_shaders.getValue());

		long time = System.nanoTime();
		long lastTime = time;
		long lastInfo = time;

		while (!Display.isCloseRequested()) {
			time = System.nanoTime();
			while (time - lastTime >= TICK_TIME * 1000000000) {
				ticks++;

				onTick();
				lastTime += TICK_TIME * 1000000000;
			}

			float partialTickTime = (time - lastTime)
					/ ((float) TICK_TIME * 1000000000);

			if (time - lastInfo >= 1000000000) {
				lastInfo += 1000000000;
				ticksString = "Ticks: " + ticks;
				ticks = 0;
				Game.sound.updatePlaying();
				Game.sound.getMusicProvider().updateMusic();
			}

			onRender(partialTickTime);
			render(partialTickTime);

			Display.update();
			if(Game.settings.fps_limit.getValue() > 0)
				Display.sync(Game.settings.fps_limit.getValue());
		}

		exit();
	}

	public void init() {
		initGL();
		Display.setTitle(TITLE);
	}

	public void exit() {
		if (ingame) {
			this.exitGame();
		}
		Game.sound.shutdown();

		Display.destroy();
		AL.destroy();

		try {
			Game.settings.saveToFile(this.configFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		System.exit(0);
	}

	public void load(boolean toMenu) {
		FontManager.load();
		this.font = FontManager.text;
		displayLoadingScreen("Loading textures...");
		TextureManager.load();
		displayLoadingScreen("Loading models...");
		ModelManager.load();
		displayLoadingScreen("Loading sounds...");

		lastFPS = getTime();

		showbg = true;

		if (toMenu) {
			openGuiScreen(new GuiScreenMainMenu(this));
		} else {
			closeGuiScreen();
			ingame = true;
		}
	}

	public void makeNewWorld(boolean load, String name) {
		ingame = true;
		GLHelper.updateFiltering(Game.settings.linear_filtering.getValue());
		world = new World(settings.tree_generation.getValue(), this, load, name);
		closeGuiScreen();
		//ingame = true;
	}

	public void displayLoadingScreen(String text) {
		// isIngame = false;
		openGuiScreen(new GuiScreenLoading(this, text));
		render(0);
		Display.update();
	}

	public void displayLoadingScreen() {
		// isIngame = false;
		openGuiScreen(new GuiScreenLoading(this));
		render(0);
		Display.update();
	}

	public void createFont() {
		Font awtFont = new Font("Arial", Font.BOLD, 10);
		font = new TrueTypeFont(awtFont, false);
	}

	float animationProgress = 0;

	public void onTick() {

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_F2) {
					GameUtil.screenshot();
				}

				if (Keyboard.getEventKey() == Keyboard.KEY_GRAVE) {
					consoleOpen = !consoleOpen ? true : false;
					if (!consoleOpen) {
						console.clearInput();
					}
				}

				if (consoleOpen) {
					console.charTyped(Keyboard.getEventCharacter(),
							Keyboard.getEventKey());
				}

				if (Keyboard.getEventKey() == Keyboard.KEY_F
						&& this.guiScreen == null && !consoleOpen) {
					toggleFullscreen();
				}

				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
					if (!consoleOpen) {
						if (this.guiScreen != null) {
							closeGuiScreen();
						} else {
							openGuiScreen(new GuiScreenMenu(this));
						}
					} else {
						consoleOpen = false;
					}
				}

			}
		}

		if (!ingame && this.guiScreen == null) {
			openGuiScreen(new GuiScreenMainMenu(this));
		}

		if (this.guiScreen == null && ingame && !consoleOpen) {
			world.update();
		} else if (ingame) {
			world.menuUpdate();
		}

		lastProgress = animationProgress;

		animationProgress += consoleOpen ? 0.150f : -0.150f;

		if (animationProgress < 0) {
			animationProgress = 0;
		}

		if (animationProgress > 1) {
			animationProgress = 1;
		}

		/*Game.sound.getMusicProvider().updateGameMusic();
		Game.sound.getMusicProvider().updateMenuMusic();*/

		// Display.sync(200);
		// updateFPS();
	}

	public void onRender(float ptt) {
		if (this.guiScreen == null && ingame && !consoleOpen) {
			world.onRender(ptt);
		}
	}

	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			fps = fpsCounter;
			fpsCounter = 0;
			lastFPS += 1000;
		}
		fpsCounter++;
	}


	public void initGL() {
		// Set perspective matrix
		setPerspective();

		// Enable depth test
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);

		// Enable back face culling
		GL11.glEnable(GL11.GL_CULL_FACE);

		// Enable textures
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		// Blending
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		// Setup alpha test
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GEQUAL, 0.01F);

		// Clear color
		GL11.glClearColor(0.63f, 0.87f, 1.0f, 1.0f);

		// Set light properties
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		FloatBuffer ambientLight = BufferUtils.createFloatBuffer(4);
		ambientLight.put(0.8f).put(0.8f).put(0.8f).put(1).flip();
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, ambientLight);

		GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK,
				GL11.GL_AMBIENT_AND_DIFFUSE);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		// GL11.glEnable(GL11.GL_NORMALIZE);

		// Fog
		// GL11.glEnable(GL11.GL_FOG);
		FloatBuffer fogColor = BufferUtils.createFloatBuffer(4);
		fogColor.put(0.43f).put(0.67f).put(1.0f).put(0.0f).flip();
		GL11.glFog(GL11.GL_FOG_COLOR, fogColor);
		GL11.glHint(GL11.GL_FOG_HINT, GL11.GL_DONT_CARE);
		GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP2);
		GL11.glFogf(GL11.GL_FOG_DENSITY, 0.01f);
		// GL11.glFogf(GL11.GL_FOG_START, 100.0f);
		// GL11.glFogf(GL11.GL_FOG_END, 160.0f);

		// Nicest perspective correction
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
	}

	public void setPerspective() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(settings.fov.getValue(), (float) width
				/ (float) height, 0.05F, 1000);
		GL11.glViewport(0, 0, width, height);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public void setOrtho() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 0, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	public void render(float ptt) {

		float pttbackup = ptt;

		if (guiScreen != null || consoleOpen) {
			ptt = 1;
		}

		// Clear old frame
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		if (ingame) {

			TextureManager.atlas.bind();

			renderEngine.renderWorld(world, ptt);

			setOrtho();

			world.player.renderGui(ptt);
			renderOverlay();

			updateFPS();
		}

		setOrtho();

		if (this.guiScreen != null) {
			guiScreen.update();
			if (this.guiScreen != null) {

				if (!ingame) {
					if (TextureManager.menuBg != null && showbg) {
						GLHelper.drawTexture(TextureManager.menuBg, 0, width,
								0, width);
					} else {
						GLHelper.drawRectangle(0.2f, 0.2f, 0.2f, 0, width, 0,
								width);
					}

					if (TextureManager.logo != null && showbg) {
						GLHelper.drawTexture(TextureManager.logo, (width / 2) - (TextureManager.logo.getImageWidth() / 2) , 32);

						font.drawString(5, this.height - font.getHeight(),
								"version " + Start.GAME_VERSION);
					}
					guiScreen.render();
				}

				guiScreen.render();
			}
		}

		GL11.glPushMatrix();

		int cHeight = settings.consoleHeight.getValue();

		float lerp = lastProgress + (animationProgress - lastProgress)
				* pttbackup;

		GL11.glTranslatef(0, -((1 - lerp) * cHeight), 0);

		if (lastProgress > 0) {
			GuiObjectBlank gui = new GuiObjectBlank();
			gui.drawRect(0, 0, this.width, cHeight, 0xD0000000);
			gui.drawRect(0, cHeight - font.getLineHeight(), this.width,
					cHeight, 0x500030A0);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			String cursor = (ticks % TPS >= TPS / 2) ? "_" : "";
			font.drawString(0, cHeight - font.getLineHeight(), "> "
					+ console.currentCommand + cursor);
			String info = TITLE;
			font.drawString(width - font.getWidth(info) - 2,
					cHeight - font.getLineHeight() * 2, info,
					new org.newdawn.slick.Color(120, 120, 120));

			ListIterator<String> li = console.lines.listIterator(console.lines
					.size());

			int offset = 0;

			while (li.hasPrevious()) {
				offset += font.getLineHeight();

				font.drawString(0, cHeight - font.getLineHeight() - offset
						- console.getTranslation(), li.previous(),
						new org.newdawn.slick.Color(200, 200, 200));
			}

		}

		GL11.glPopMatrix();

		setPerspective();

	}

	float lastProgress = 0;

	public void openGuiScreen(GuiScreen scr) {
		if (this.guiScreen != null)
			this.guiScreen.onClosing();
		this.guiScreen = scr;
		scr.onOpening();
		Mouse.setGrabbed(false);
	}

	public void closeGuiScreen() {
		this.guiScreen.onClosing();
		this.guiScreen = null;

		if (consoleOpen) {
			consoleOpen = false;
		}

		Mouse.setGrabbed(true);
	}

	public void renderOverlay() {
		Block b = Block.getBlock(world.player.getSelectedBlockID());
		int textureid = b.sideTexture;

		GLHelper.drawFromAtlas(textureid, 25, 75, height - 75, height - 25);

		Runtime runtime = Runtime.getRuntime();

		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();

		String fpsString = "FPS: " + fps + ", " + ticksString;
		int stringWidth = font.getWidth(fpsString);
		font.drawString(width - stringWidth - 2, font.getHeight() * 2,
				fpsString);

		font.drawString(2, 0, "X Position: " + world.player.getPosX());
		font.drawString(2, font.getHeight(),
				"Y Position: " + world.player.getPosY());
		font.drawString(2, font.getHeight() * 2,
				"Z Position: " + world.player.getPosZ());
		font.drawString(
				2,
				font.getHeight() * 3,
				"Biome: "
						+ world.chunkProvider.getBiomeAtLocation(
								(int) world.player.getPosX(),
								(int) world.player.getPosZ()).getName());
		font.drawString(2, font.getHeight() * 4, "Lives: "
				+ ((int) (world.player.getLifes() * 100)));

		String memory = "Used memory: "
				+ (allocatedMemory / (1024 * 1024) - freeMemory / (1024 * 1024))
				+ "MB" + "/" + allocatedMemory / (1024 * 1024) + "MB";
		int memoryWidth = font.getWidth(memory);
		font.drawString(width - memoryWidth - 2, 0, memory);

		String chunks = "Chunks drawn: " + renderEngine.chunksDrawn + "/"
				+ renderEngine.chunksLoaded;
		font.drawString(width - font.getWidth(chunks) - 2, font.getHeight(),
				chunks);

		if (world.chunkProvider.loading) {
			font.drawString(width - font.getWidth("Loading chunks...") - 2,
					height - font.getHeight(), "Loading chunks...",
					new org.newdawn.slick.Color(255, 255, 255, 255));
		}

		if (renderEngine.building) {
			font.drawString(width - font.getWidth("Building chunks...") - 2,
					height - font.getHeight()
							* (world.chunkProvider.loading ? 2 : 1),
					"Building chunks...", new org.newdawn.slick.Color(255, 255,
							255, 255));
		}

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glLineWidth(2);

		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(width / 2, (height / 2) - 10);
		GL11.glVertex2f(width / 2, (height / 2) + 10);

		GL11.glVertex2f((width / 2) - 10, height / 2);
		GL11.glVertex2f((width / 2) + 10, height / 2);
		GL11.glEnd();
	}

	/**
	 * Set the display mode to be used
	 * 
	 * @param width
	 *            The width of the display required
	 * @param height
	 *            The height of the display required
	 * @param fullscreen
	 *            True if we want fullscreen mode
	 * @author NinjaCave
	 */
	public void setDisplayMode(int width, int height, boolean fullscreen) {
		if (Display.isCreated()
				&& (Display.getDisplayMode().getWidth() == width)
				&& (Display.getDisplayMode().getHeight() == height)
				&& (Display.isFullscreen() == fullscreen)) {
			return;
		}

		try {
			DisplayMode targetDisplayMode = null;

			if (fullscreen) {
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				int freq = 0;

				for (int i = 0; i < modes.length; i++) {
					DisplayMode current = modes[i];

					if ((current.getWidth() == width)
							&& (current.getHeight() == height)) {
						if ((targetDisplayMode == null)
								|| (current.getFrequency() >= freq)) {
							if ((targetDisplayMode == null)
									|| (current.getBitsPerPixel() > targetDisplayMode
											.getBitsPerPixel())) {
								targetDisplayMode = current;
								freq = targetDisplayMode.getFrequency();
							}
						}

						if ((current.getBitsPerPixel() == Display
								.getDesktopDisplayMode().getBitsPerPixel())
								&& (current.getFrequency() == Display
										.getDesktopDisplayMode().getFrequency())) {
							targetDisplayMode = current;
							break;
						}
					}
				}
			} else {
				targetDisplayMode = new DisplayMode(width, height);
			}

			if (targetDisplayMode == null) {
				Game.console.out("Failed to find value mode: " + width + "x"
						+ height + " fs=" + fullscreen);
				return;
			}

			Display.setDisplayMode(targetDisplayMode);
			Display.setFullscreen(fullscreen);

			if (!Display.isCreated()) {
				;
				try {
					Display.create(new PixelFormat(8, 8, 0, settings.aa_samples
							.getValue()));
					Game.console.out("Display created!");
					// Display.create();
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
			}

			init();

		} catch (LWJGLException e) {
			Game.console.out("Unable to setup mode " + width + "x" + height
					+ " fullscreen=" + fullscreen + e);
		}
	}

	public void exitGame() {
		world.saveAllChunks();
		world = null;
		renderEngine = new RenderEngine(Game.settings.enable_shaders.getValue());
		ingame = false;
		openGuiScreen(new GuiScreenMainMenu(this));
	}

	public void toggleFullscreen() {
		if (isFullscreen) {
			width = 800;
			height = 480;
			isFullscreen = false;
		} else {
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension screensize = toolkit.getScreenSize();
			width = screensize.width;
			height = screensize.height;
			isFullscreen = true;
		}
		setDisplayMode(width, height, isFullscreen);
	}

}
