package dax.blocks;

import dax.blocks.block.Block;
import dax.blocks.block.BlockBasic;
import dax.blocks.console.Console;
import dax.blocks.gui.GuiObjectBlank;
import dax.blocks.gui.GuiScreen;
import dax.blocks.gui.GuiScreenLoading;
import dax.blocks.gui.GuiScreenMainMenu;
import dax.blocks.gui.GuiScreenMenu;
import dax.blocks.render.RenderEngine;
import dax.blocks.settings.Settings;
import dax.blocks.world.World;
import dax.blocks.world.chunk.Chunk;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ListIterator;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.TrueTypeFont;

public class Game implements Runnable {

	public static Settings settings = new Settings();
	public static Console console = new Console();
	public static WorldsManager worlds = new WorldsManager();
	//--old settings--
	/*
	 * public int worldSize = 4;
	 * public int fov = 85;
	 * public boolean shouldFilter = true;
	 * public float heightMultipler = 20;
	 */
	
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

	
	public static final String TITLE = "Order of the stone";

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

	public static boolean deleteDirectory(File directory) {
	    if(directory.exists()){
	        File[] files = directory.listFiles();
	        if(null!=files){
	            for(int i=0; i<files.length; i++) {
	                if(files[i].isDirectory()) {
	                    deleteDirectory(files[i]);
	                }
	                else {
	                    files[i].delete();
	                }
	            }
	        }
	    }
	    return(directory.delete());
	}
	
	@Override
	public void run() {
		setDisplayMode(width, height, isFullscreen);
		load(true);
		

		renderEngine = new RenderEngine();

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

			float partialTickTime = (time - lastTime) / ((float) TICK_TIME * 1000000000);

			if (time - lastInfo >= 1000000000) {
				lastInfo += 1000000000;
				// Display.setTitle("Ticks: " + ticks);
				ticksString = "Ticks: " + ticks;
				ticks = 0;
			}

			onRender();
			render(partialTickTime);

			Display.update();
			//Display.sync(5);
		}

		exit();
	}

	public void init() {
		initGL();
		Display.setTitle(TITLE);
		Mouse.setGrabbed(true);
	}

	public void exit() {
		if (ingame) {
			world.saveAllChunks();
		}
		Display.destroy();
		System.exit(0);
	}

	public void load(boolean toMenu) {
		createFont();
		displayLoadingScreen("Loading textures...");
		TextureManager.load();
		displayLoadingScreen("Loading models...");
		ModelManager.load();
		displayLoadingScreen("Loading sounds...");
		SoundManager.load();
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
		ingame = false;
		world = new World(settings.tree_generation.getValue(), this, load, name);
		closeGuiScreen();
		ingame = true;
	}

	public void displayLoadingScreen(String text) {
		//isIngame = false;
		openGuiScreen(new GuiScreenLoading(this, text));
		render(0);
		Display.update();
	}

	public void displayLoadingScreen() {
		//isIngame = false;
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
				if (Keyboard.getEventKey() == Keyboard.KEY_M) {
					ModelManager.load();
				}

				if (Keyboard.getEventKey() == Keyboard.KEY_F2) {
					screenshot();
				}

				if (Keyboard.getEventKey() == Keyboard.KEY_GRAVE) {
					consoleOpen = !consoleOpen ? true : false;
					if (!consoleOpen) {
						console.clearInput();
					}
				}

				if (consoleOpen) {
					console.charTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
				}

				if (Keyboard.getEventKey() == Keyboard.KEY_F && this.guiScreen == null && !consoleOpen) {
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

		// TODO
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

		// Display.sync(200);
		// updateFPS();
	}

	public void onRender() {
		if (this.guiScreen == null && ingame && !consoleOpen) {
			world.onRender();
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

	public void updateFiltering() {
		TextureManager.atlas.bind();
		if (settings.linear_filtering.getValue()) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		}
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

		GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);
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
		GLU.gluPerspective(settings.fov.getValue(), (float) width / (float) height, 0.05F, 1000);
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
			// world.render(ptt);

			setOrtho();
			renderOverlay();

			updateFPS();
		}

		setOrtho();

		if (this.guiScreen != null) {
			guiScreen.update();
			if (this.guiScreen != null) {

				if (!ingame) {
					GL11.glColor3f(1, 1, 1);

					if (TextureManager.menuBg != null && showbg) {
						GL11.glEnable(GL11.GL_TEXTURE_2D);
						TextureManager.menuBg.bind();

						GL11.glBegin(GL11.GL_QUADS);

						GL11.glTexCoord2f(0, 0);
						GL11.glVertex2f(0, 0);

						GL11.glTexCoord2f(TextureManager.menuBg.getWidth(), 0);
						GL11.glVertex2f(width, 0);

						GL11.glTexCoord2f(TextureManager.menuBg.getWidth(), TextureManager.menuBg.getHeight());
						GL11.glVertex2f(width, width);

						GL11.glTexCoord2f(0, TextureManager.menuBg.getHeight());
						GL11.glVertex2f(0, width);

						GL11.glEnd();

					} else {
						GL11.glColor3f(0.2f, 0.2f, 0.2f);

						GL11.glDisable(GL11.GL_TEXTURE_2D);
						
						GL11.glBegin(GL11.GL_QUADS);

						GL11.glVertex2f(0, 0);

						GL11.glVertex2f(width, 0);

						GL11.glVertex2f(width, width);

						GL11.glVertex2f(0, width);

						GL11.glEnd();
					}

					if (TextureManager.logo != null && showbg) {
						TextureManager.logo.bind();

						GL11.glBegin(GL11.GL_QUADS);

						GL11.glTexCoord2f(0, 0);
						GL11.glVertex2f((width - TextureManager.logo.getImageWidth()) / 2, height / 2 - 180);

						GL11.glTexCoord2f(TextureManager.logo.getWidth(), 0);
						GL11.glVertex2f((width + TextureManager.logo.getImageWidth()) / 2, height / 2 - 180);

						GL11.glTexCoord2f(TextureManager.logo.getWidth(), TextureManager.logo.getHeight());
						GL11.glVertex2f((width + TextureManager.logo.getImageWidth()) / 2, height / 2 - 180 + TextureManager.logo.getImageHeight());

						GL11.glTexCoord2f(0, TextureManager.logo.getHeight());
						GL11.glVertex2f((width - TextureManager.logo.getImageWidth()) / 2, height / 2 - 180 + TextureManager.logo.getImageHeight());

						GL11.glEnd();

						GL11.glDisable(GL11.GL_TEXTURE_2D);
					}

					guiScreen.render();
				}

				guiScreen.render();
			}
		}

		GL11.glPushMatrix();

		int cHeight = settings.consoleHeight.getValue();

		float lerp = lastProgress + (animationProgress - lastProgress) * pttbackup;

		GL11.glTranslatef(0, -((1 - lerp) * cHeight), 0);

		if (lastProgress > 0) {
			GuiObjectBlank gui = new GuiObjectBlank();
			gui.drawRect(0, 0, this.width, cHeight, 0xD0000000);
			gui.drawRect(0, cHeight - font.getLineHeight(), this.width, cHeight, 0x500030A0);
			GL11.glEnable(GL11.GL_TEXTURE_2D);

			String cursor = (ticks % TPS >= TPS / 2) ? "_" : "";
			font.drawString(0, cHeight - font.getLineHeight(), "> " + console.currentCommand + cursor);
			String info = "Order of the Stone a_0.0.1";
			font.drawString(width - font.getWidth(info) - 2, cHeight - font.getLineHeight() * 2, info, new org.newdawn.slick.Color(120, 120, 120));

			
			ListIterator<String> li = console.lines.listIterator(console.lines.size());

			int offset = 0;

			while (li.hasPrevious()) {
				offset += font.getLineHeight();

				font.drawString(0, cHeight - font.getLineHeight() - offset - console.getTranslation(), li.previous(), new org.newdawn.slick.Color(200, 200, 200));
			}

		}

		GL11.glPopMatrix();

		setPerspective();

	}

	float lastProgress = 0;

	public void screenshot() {
		GL11.glReadBuffer(GL11.GL_FRONT);
		int width = Display.getDisplayMode().getWidth();
		int height = Display.getDisplayMode().getHeight();
		int bpp = 4; // Assuming a 32-bit display with a byte each for red,
						// green, blue, and alpha.
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

		File dir = new File("screenshots");

		if (!dir.exists()) {
			dir.mkdir();
		}

		String filename = "screenshot";

		int num = 0;

		File file = new File(dir, filename + num + ".png"); 

		while (file.exists()) {
			num++;
			file = new File(dir, filename + num + ".png"); 
		}

		String format = "PNG"; // Can be changed to JPG or any other supported format
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int i = (x + (width * y)) * bpp;
				int r = buffer.get(i) & 0xFF;
				int g = buffer.get(i + 1) & 0xFF;
				int b = buffer.get(i + 2) & 0xFF;
				image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
			}
		}

		try {
			ImageIO.write(image, format, file);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void openGuiScreen(GuiScreen scr) {
		this.guiScreen = scr;
		Mouse.setGrabbed(false);
	}

	public void closeGuiScreen() {
		this.guiScreen = null;
		
		if (consoleOpen) {
			consoleOpen = false;
		}
		
		Mouse.setGrabbed(true);
	}

	public void renderOverlay() {
		BlockBasic b = (BlockBasic) Block.getBlock(world.player.selectedBlockID);
		int textureid = b.sideTexture;

		GL11.glColor3f(1, 1, 1);

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(TextureManager.getX1(textureid), TextureManager.getY1(textureid));
		GL11.glVertex2f(25, height - 75);
		GL11.glTexCoord2f(TextureManager.getX2(textureid), TextureManager.getY1(textureid));
		GL11.glVertex2f(75, height - 75);
		GL11.glTexCoord2f(TextureManager.getX2(textureid), TextureManager.getY2(textureid));
		GL11.glVertex2f(75, height - 25);
		GL11.glTexCoord2f(TextureManager.getX1(textureid), TextureManager.getY2(textureid));
		GL11.glVertex2f(25, height - 25);
		GL11.glEnd();

		Runtime runtime = Runtime.getRuntime();

		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();

		String fpsString = "FPS: " + fps + ", " + ticksString;
		int stringWidth = font.getWidth(fpsString);
		font.drawString(width - stringWidth - 2, font.getHeight() * 2, fpsString);

		font.drawString(2, 0, "X Position: " + world.player.posX);
		font.drawString(2, font.getHeight(), "Y Position: " + world.player.posY);
		font.drawString(2, font.getHeight() * 2, "Z Position: " + world.player.posZ);

		String memory = "Used memory: " + (allocatedMemory / (1024 * 1024) - freeMemory / (1024 * 1024)) + "MB" + "/" + allocatedMemory / (1024 * 1024) + "MB";
		int memoryWidth = font.getWidth(memory);
		font.drawString(width - memoryWidth - 2, 0, memory);

		String chunks = "Chunks drawn: " + renderEngine.chunksDrawn + "/" + renderEngine.chunksLoaded;
		font.drawString(width - font.getWidth(chunks) - 2, font.getHeight(), chunks);
		
		if (world.chunkProvider.loading) {
			font.drawString(width - font.getWidth("Loading chunks...") - 2, height - font.getHeight(), "Loading chunks...", new org.newdawn.slick.Color(255, 255, 255, 255));
		}
		
		if (renderEngine.building) {
			font.drawString(width - font.getWidth("Building chunks...") - 2, height - font.getHeight() * (world.chunkProvider.loading ? 2 : 1), "Building chunks...", new org.newdawn.slick.Color(255, 255, 255, 255));
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
		if (Display.isCreated() && (Display.getDisplayMode().getWidth() == width) && (Display.getDisplayMode().getHeight() == height) && (Display.isFullscreen() == fullscreen)) {
			return;
		}

		try {
			DisplayMode targetDisplayMode = null;

			if (fullscreen) {
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				int freq = 0;

				for (int i = 0; i < modes.length; i++) {
					DisplayMode current = modes[i];

					if ((current.getWidth() == width) && (current.getHeight() == height)) {
						if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
							if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
								targetDisplayMode = current;
								freq = targetDisplayMode.getFrequency();
							}
						}

						if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) && (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
							targetDisplayMode = current;
							break;
						}
					}
				}
			} else {
				/*
				 * Canvas c = new Canvas(); JFrame f = new JFrame("frame");
				 * 
				 * f.setSize(1280, 720);
				 * f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				 * f.setBackground(Color.BLACK); c.setBackground(Color.BLACK);
				 * f.getContentPane().add(c); f.setLocationRelativeTo(null);
				 * f.setUndecorated(true); f.setVisible(true);
				 * 
				 * Display.setParent(c);
				 */

				targetDisplayMode = new DisplayMode(width, height);
			}

			if (targetDisplayMode == null) {
				Game.console.out("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
				return;
			}

			Display.setDisplayMode(targetDisplayMode);
			Display.setFullscreen(fullscreen);

			if (!Display.isCreated()) {;
				try {
					Display.create(new PixelFormat(/* Alpha Bits */8, /*
																		 * Depth
																		 * bits
																		 */8, /*
																			 * Stencil
																			 * bits
																			 */0, /* samples */settings.aa_samples.getValue()));
					Game.console.out("Display created!");
					// Display.create();
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
			}

			init();

		} catch (LWJGLException e) {
			Game.console.out("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
		}
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
