package dax.blocks;

import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ListIterator;
import java.util.Locale;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import dax.blocks.auth.AuthManager;
import dax.blocks.block.Block;
import dax.blocks.console.Console;
import dax.blocks.gui.GuiObjectBlank;
import dax.blocks.gui.GuiScreen;
import dax.blocks.gui.GuiScreenLoading;
import dax.blocks.gui.GuiScreenMainMenu;
import dax.blocks.gui.GuiScreenMenu;
import dax.blocks.gui.ingame.GuiManager;
import dax.blocks.model.ModelManager;
import dax.blocks.profiler.Profiler;
import dax.blocks.profiler.Section;
import dax.blocks.render.ChunkRendererDisplayList;
import dax.blocks.render.IChunkRenderer;
import dax.blocks.render.RenderEngine;
import dax.blocks.settings.Keyconfig;
import dax.blocks.settings.Settings;
import dax.blocks.sound.SoundManager;
import dax.blocks.util.GLHelper;
import dax.blocks.util.GameUtil;
import dax.blocks.world.World;

public class Game implements Runnable {
	public static final String TITLE = Start.GAME_NAME + " v" + Start.GAME_VERSION;
	
	private File configFile = new File("settings.txt");
	
	public boolean showbg = false;
	public boolean consoleOpen = false;
	public boolean ingame = false;

	public RenderEngine renderEngine;
	public GuiScreen guiScreen;
	public TrueTypeFont font;
	public World world;
	public IChunkRenderer chunkRenderer = new ChunkRendererDisplayList();
	public AuthManager authManager;
	
	
	public static final int TPS = 20;
	public static final double TICK_TIME = 1.0D / TPS;
	public int ticks = 0;
	String ticksString = "N/A";

	float animationProgress = 0;
	float lastProgress = 0;
	long lastFrame;
	int fpsCounter;
	int fps = 0;
	long lastFPS;
	int vertices = 0;

	private String loginString = "Unlogged";
	private String versionString = "version " + Start.GAME_VERSION;
	
	private Profiler profiler = new Profiler();
	
	private static Game instance;

	public Game() {
		instance = this;
	}

	public static Game getInstance() {
		return instance;
	}
	
	public Profiler getProfiler() {
		return this.profiler;
	}

	//.... RUN METHODS ....
	
	@Override
	public void run() {
		try {
			if (!this.configFile.exists()) {
				this.configFile.createNewFile();
				Settings.getInstance().saveToFile(configFile);
			}

			Settings.getInstance().loadFromFile(this.configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		GLHelper.setDisplayMode(Settings.getInstance().windowWidth.getValue(), Settings.getInstance().windowHeight.getValue(), Settings.getInstance().fullscreen.getValue());
		init();
		load(true);

		renderEngine = new RenderEngine(Settings.getInstance().shaders.getValue());

		long time = System.nanoTime();
		long lastTime = time;
		long lastInfo = time;

		while (!Display.isCloseRequested()) {
			time = System.nanoTime();
			while (time - lastTime >= TICK_TIME * 1000000000) {
				ticks++;
				profiler.tick.start();
				onTick();
				profiler.tick.end();
				lastTime += TICK_TIME * 1000000000;
			}

			if (time - lastInfo >= 1000000000) {
				lastInfo += 1000000000;
				ticksString = "Ticks: " + ticks;
				ticks = 0;
				SoundManager.getInstance().updatePlaying();
				SoundManager.getInstance().getMusicProvider().updateMusic();
			}
			
			profiler.render.start();
			float partialTickTime = (time - lastTime) / ((float) TICK_TIME * 1000000000);

			onRender(partialTickTime);
			render(partialTickTime);

			Display.update();
			profiler.render.end();
			
			if(Settings.getInstance().fpsLimit.getValue() > 0)
				Display.sync(Settings.getInstance().fpsLimit.getValue());
		}

		exit();
	}

	public void init() {
		GLHelper.initGL(Settings.getInstance().windowWidth.getValue(), Settings.getInstance().windowHeight.getValue());
		Display.setTitle(TITLE);
	}

	public void exit() {
		if (ingame) {
			this.exitGame();
		}
		SoundManager.getInstance().shutdown();

		Display.destroy();
		AL.destroy();

		try {
			Settings.getInstance().saveToFile(this.configFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		System.exit(0);
	}

	public void load(boolean toMenu) {
		FontManager.getInstance().load();
		this.font = FontManager.getInstance().text;
		displayLoadingScreen("Loading textures...");
		TextureManager.load();
		displayLoadingScreen("Loading models...");
		ModelManager.getInstance().load();
		displayLoadingScreen("Loading keyconfig...");
		Keyconfig.load();
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

	public void createFont() {
		Font awtFont = new Font("Arial", Font.BOLD, 10);
		font = new TrueTypeFont(awtFont, false);
	}

	
	//.... GAME METHODS ....

	public void makeNewWorld(boolean load, String name) {
		ingame = true;
		GLHelper.updateFiltering(Settings.getInstance().linearFiltering.getValue());
		world = new World(Settings.getInstance().treeGeneration.getValue(), this, load, name);
		closeGuiScreen();
		//ingame = true;
	}

	public void exitGame() {
		world.saveAllChunks();
		world = null;
		renderEngine = new RenderEngine(Settings.getInstance().shaders.getValue());
		ingame = false;
		openGuiScreen(new GuiScreenMainMenu(this));
	}
	
	//.... RENDER/UPDATE METHODS ....

	float lastFov = 0;
	public void onTick() {

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyconfig.screenshot) {
					GameUtil.screenshot();
				}
				
				if (consoleOpen) {
					Console.getInstance().charTyped(Keyboard.getEventCharacter(),
							Keyboard.getEventKey());
				}


				if (Keyconfig.isDownEvent(Keyconfig.console)) {
					if (!consoleOpen) {
						Console.getInstance().clearInput();
						consoleOpen = true;
					}		
				}
				
				if (Keyboard.getEventKey() == Keyconfig.fullscreen
						&& this.guiScreen == null && !consoleOpen) {
					toggleFullscreen();
				}

				if (Keyboard.getEventKey() == Keyconfig.exit) {
					if (!consoleOpen) {
						if (GuiManager.getInstance().isOpened()) {
							GuiManager.getInstance().closeScreen();
						} else {
							if (this.guiScreen != null) {
								closeGuiScreen();
							} else {
								openGuiScreen(new GuiScreenMenu(this));
							}
						}
					} else {
						consoleOpen = false;
					}
				}

			}
		}
		
		if (Keyboard.isKeyDown(Keyconfig.zoom)) {
			if(lastFov == 0) {
				lastFov = Settings.getInstance().fov.getValue();
				Settings.getInstance().fov.setValue(15f);
			}
		} else {
			if(lastFov > 0) {
				Settings.getInstance().fov.setValue(lastFov);
				lastFov = 0;
			}
		}

		if (!ingame && this.guiScreen == null) {
			openGuiScreen(new GuiScreenMainMenu(this));
		}

		if (this.guiScreen == null && ingame && !consoleOpen) {
			world.onTick();
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
	}

	public void onRender(float ptt) {
		if (this.guiScreen == null && ingame && !consoleOpen) {
			world.onRenderTick(ptt);
		}
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
			
			GLHelper.setOrtho(Settings.getInstance().windowWidth.getValue(), Settings.getInstance().windowHeight.getValue());
			
			world.renderGui(ptt);
		
			renderOverlay();

			updateFPS();
		}

		GLHelper.setOrtho(Settings.getInstance().windowWidth.getValue(), Settings.getInstance().windowHeight.getValue());

		renderGuiScreen(ptt);

		renderConsole(pttbackup);

		GLHelper.setPerspective(Settings.getInstance().windowWidth.getValue(), Settings.getInstance().windowHeight.getValue());

	}
	
	public void renderGuiScreen(float ptt) {
		if (this.guiScreen != null) {
			guiScreen.update();
			if (this.guiScreen != null) {

				if (!ingame) {
					if (TextureManager.menuBg != null && showbg) {
						GLHelper.drawTexture(TextureManager.menuBg, 0, Settings.getInstance().windowWidth.getValue(),
								0, Settings.getInstance().windowWidth.getValue());
					} else {
						GLHelper.drawRectangle(0.2f, 0.2f, 0.2f, 0, Settings.getInstance().windowWidth.getValue(), 0,
								Settings.getInstance().windowWidth.getValue());
					}

					if (TextureManager.logo != null && showbg) {
						GLHelper.drawTexture(TextureManager.logo, (Settings.getInstance().windowWidth.getValue() / 2) - (TextureManager.logo.getImageWidth() / 2) , 32);

						font.drawString(5, Settings.getInstance().windowHeight.getValue() - font.getHeight(),
								this.versionString);
						font.drawString(15 + font.getWidth(this.versionString), Settings.getInstance().windowHeight.getValue() - font.getHeight(), this.loginString, 
									authManager.isAuthenticated() ? Color.white : Color.red);
					}
					guiScreen.render();
				}

				guiScreen.render();
			}
		}
	}
	
	public void renderConsole(float ptt) {
		GL11.glPushMatrix();

		int cHeight = Settings.getInstance().consoleHeight.getValue();

		float lerp = lastProgress + (animationProgress - lastProgress)
				* ptt;

		GL11.glTranslatef(0, -((1 - lerp) * cHeight), 0);

		if (lastProgress > 0) {
			GuiObjectBlank gui = new GuiObjectBlank();
			gui.drawRect(0, 0, Settings.getInstance().windowWidth.getValue(), cHeight, 0xD0000000);
			gui.drawRect(0, cHeight - font.getLineHeight(), Settings.getInstance().windowWidth.getValue(),
					cHeight, 0x500030A0);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			String cursor = (ticks % TPS >= TPS / 2) ? "_" : "";
			font.drawString(0, cHeight - font.getLineHeight(), "> "
					+ Console.getInstance().currentCommand + cursor);
			String info = TITLE;
			font.drawString(Settings.getInstance().windowWidth.getValue() - font.getWidth(info) - 2,
					cHeight - font.getLineHeight() * 2, info,
					new org.newdawn.slick.Color(120, 120, 120));

			ListIterator<String> li = Console.getInstance().lines.listIterator(Console.getInstance().lines
					.size());

			int offset = 0;

			while (li.hasPrevious()) {
				offset += font.getLineHeight();

				font.drawString(0, cHeight - font.getLineHeight() - offset
						- Console.getInstance().getTranslation(), li.previous(),
						new org.newdawn.slick.Color(200, 200, 200));
			}

		}

		GL11.glPopMatrix();
	}
	
	public void renderOverlay() {
		if (Settings.getInstance().debug.getValue()) {
			GL11.glLineWidth(1);
			
			GL11.glBegin(GL11.GL_LINES);
			
			int offset = Display.getWidth() - Section.MAX_RECORDS;
			
			float[] tick = this.profiler.tick.getTimes();
			float[] render = this.profiler.render.getTimes();
			float[] build = this.profiler.build.getTimes();
			
			for (int i = 0; i < Section.MAX_RECORDS; i++) {
				GL11.glColor4f(0, 1, 0, 1.0f);
				GL11.glVertex2f(offset+i, Display.getHeight());
				GL11.glVertex2f(offset+i, Display.getHeight()-tick[i]*10);
				
				GL11.glColor4f(0, 0, 1, 0.5f);
				GL11.glVertex2f(offset+i, Display.getHeight());
				GL11.glVertex2f(offset+i, Display.getHeight()-render[i]*10);
				
				GL11.glColor4f(1, 0, 0, 0.4f);
				GL11.glVertex2f(offset+i, Display.getHeight());
				GL11.glVertex2f(offset+i, Display.getHeight()-build[i]*10);
			}
			
			float avgTick = this.profiler.tick.avg();
			float avgRender = this.profiler.render.avg();
			float avgBuild = this.profiler.build.avg();
			
			GL11.glColor4f(0.3f, 1.0f, 0, 1.0f);
			GL11.glVertex2f(Display.getWidth()-Section.MAX_RECORDS, Display.getHeight()-avgTick*10);
			GL11.glVertex2f(Display.getWidth(), Display.getHeight()-avgTick*10);
			
			GL11.glColor4f(0.3f, 0, 1.0f, 1.0f);
			GL11.glVertex2f(Display.getWidth()-Section.MAX_RECORDS, Display.getHeight()-avgRender*10);
			GL11.glVertex2f(Display.getWidth(), Display.getHeight()-avgRender*10);
			
			GL11.glColor4f(1.0f, 0, 0.3f, 1.0f);
			GL11.glVertex2f(Display.getWidth()-Section.MAX_RECORDS, Display.getHeight()-avgBuild*10);
			GL11.glVertex2f(Display.getWidth(), Display.getHeight()-avgBuild*10);
			
			GL11.glEnd(); 
			
			String tickText = "avg tick " + String.format(Locale.ENGLISH, "%.2f", avgTick) + "ms";
			FontManager.getFont().drawString(offset-FontManager.getFont().getWidth(tickText)-2, (int)(Display.getHeight()-avgTick*10-FontManager.getFont().getLineHeight()*0.75f), tickText);
			
			String renderText = "avg render " + String.format(Locale.ENGLISH, "%.2f", avgRender) + "ms";
			FontManager.getFont().drawString(offset-FontManager.getFont().getWidth(renderText)-2, (int)(Display.getHeight()-avgRender*10-FontManager.getFont().getLineHeight()*0.75f), renderText);
			
			String buildText = "avg build" + String.format(Locale.ENGLISH, "%.2f", avgBuild) + "ms";
			FontManager.getFont().drawString(offset-FontManager.getFont().getWidth(buildText)-2, (int)(Display.getHeight()-avgBuild*10-FontManager.getFont().getLineHeight()*0.75f), buildText);
		}
		
		Block b = world.getBlockObject(world.getPlayer().getSelectedBlockID());

		Runtime runtime = Runtime.getRuntime();

		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();

		String fpsString = "FPS: " + fps + ", " + ticksString;
		int stringWidth = font.getWidth(fpsString);
		font.drawString(Settings.getInstance().windowWidth.getValue() - stringWidth - 2, font.getHeight() * 2,
				fpsString);

		font.drawString(2, 0, "X Position: " + world.getPlayer().getPosX() + " (laX: " + world.getPlayer().getLookingAtX() + ")");
		font.drawString(2, font.getHeight(),
				"Y Position: " + world.getPlayer().getPosY() + " (laY: " + world.getPlayer().getLookingAtY() + ")");
		font.drawString(2, font.getHeight() * 2,
				"Z Position: " + world.getPlayer().getPosZ() + " (laZ: " + world.getPlayer().getLookingAtZ() + ")");
		font.drawString(
				2,
				font.getHeight() * 3,
				"Biome: "
						+ world.getChunkProvider().getBiomeAtLocation(
								(int) world.getPlayer().getPosX(),
								(int) world.getPlayer().getPosZ()).getName());
		font.drawString(2, font.getHeight() * 4, "Lives: "
				+ ((int) (world.getPlayer().getLifes() * 100)));

		String memory = "Used memory: "
				+ (allocatedMemory / (1024 * 1024) - freeMemory / (1024 * 1024))
				+ "MB" + "/" + allocatedMemory / (1024 * 1024) + "MB";
		int memoryWidth = font.getWidth(memory);
		font.drawString(Settings.getInstance().windowWidth.getValue() - memoryWidth - 2, 0, memory);

		String chunks = "Chunks drawn: " + renderEngine.chunksDrawn + "/"
				+ renderEngine.chunksLoaded;
		font.drawString(Settings.getInstance().windowWidth.getValue() - font.getWidth(chunks) - 2, font.getHeight(),
				chunks);

		if (world.getChunkProvider().loading) {
			font.drawString(Settings.getInstance().windowWidth.getValue() - font.getWidth("Loading chunks...") - 2,
					Settings.getInstance().windowHeight.getValue() - font.getHeight(), "Loading chunks...",
					new org.newdawn.slick.Color(255, 255, 255, 255));
		}

		if (renderEngine.building) {
			font.drawString(Settings.getInstance().windowWidth.getValue() - font.getWidth("Building chunks...") - 2,
					Settings.getInstance().windowHeight.getValue() - font.getHeight()
							* (world.getChunkProvider().loading ? 2 : 1),
					"Building chunks...", new org.newdawn.slick.Color(255, 255,
							255, 255));
		}

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GLHelper.drawLine(Settings.getInstance().windowWidth.getValue() / 2, Settings.getInstance().windowWidth.getValue() / 2, (Settings.getInstance().windowHeight.getValue() / 2) - 10, (Settings.getInstance().windowHeight.getValue() / 2) + 10, 2, 0, 0, 0, 0.5f);
		GLHelper.drawLine((Settings.getInstance().windowWidth.getValue() / 2) - 10, (Settings.getInstance().windowWidth.getValue() / 2) + 10, Settings.getInstance().windowHeight.getValue() / 2, Settings.getInstance().windowHeight.getValue() / 2, 2, 0, 0, 0, 0.5f);

	}


	//.... GUI METHODS ....

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


	//.... NEMAMZDANIKCEMU METHODS ....

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

	public void toggleFullscreen() {
		Settings.getInstance().fullscreen.setValue(!Settings.getInstance().fullscreen.getValue());
	}


	//.... LOGIN METHODS ....
	public void doLogin(String userName, String password, String token) {
		this.authManager = new AuthManager(userName, password, token);
	
		if(authManager.isAuthenticated())
			this.loginString = userName + " is logged in with token " + token;
		else
			this.loginString = "Bad login for " + userName;
	}
	
	public void dummyLogin() {
		this.authManager = new AuthManager();
		this.authManager.setDummyName("Player");
		this.loginString = "User is not logged in, using name Player";
	}

}