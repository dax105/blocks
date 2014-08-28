package dax.blocks;

import java.io.File;
import java.io.IOException;
import java.util.ListIterator;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import dax.blocks.auth.AuthManager;
import dax.blocks.console.Console;
import dax.blocks.gui.GuiScreen;
import dax.blocks.gui.GuiScreenLoading;
import dax.blocks.gui.GuiScreenMainMenu;
import dax.blocks.gui.GuiScreenMenu;
import dax.blocks.model.ModelManager;
import dax.blocks.overlay.OverlayManager;
import dax.blocks.profiler.Profiler;
import dax.blocks.render.ChunkRendererVBO;
import dax.blocks.render.IChunkRenderer;
import dax.blocks.settings.Keyconfig;
import dax.blocks.settings.Settings;
import dax.blocks.sound.SoundManager;
import dax.blocks.util.GLHelper;
import dax.blocks.util.GameUtil;
import dax.blocks.world.World;
import dax.blocks.world.WorldManager;

public class Game implements Runnable {
	public static final String TITLE = Start.GAME_NAME + " v"
			+ Start.GAME_VERSION;

	private File configFile = new File("settings.txt");

	public boolean showbg = false;
	public boolean consoleOpen = false;

	private OverlayManager overlayManager = new OverlayManager();
	public GuiScreen guiScreen;
	public IChunkRenderer chunkRenderer = new ChunkRendererVBO();
	public AuthManager authManager;

	private static final int TPS = 20;
	private static final double TICK_TIME = 1.0D / TPS;
	private int ticks = 0;
	private int lastTPS = 0;
	String ticksString = "N/A";

	private float consoleAnimationProgress = 0;
	private float lastConsoleAnimationProgress = 0;
	private int fps = 0;
	private long lastFPS;
	private float lastFov = 0;

	private String loginString = "Unlogged";
	private String versionString = "version " + Start.GAME_VERSION;

	private Profiler profiler = new Profiler();
	private WorldManager worldsManager = new WorldManager(this);
	private TrueTypeFont font;
	private Keyconfig keyconfigLoader = new Keyconfig();
	private Console console;
	
	public Game() {
		Settings.setGame(this);
	}

	public OverlayManager getOverlayManager() {
		return this.overlayManager;
	}

	public Profiler getProfiler() {
		return this.profiler;
	}

	public WorldManager getWorldsManager() {
		return this.worldsManager;
	}

	public World getCurrentWorld() {
		return this.worldsManager.getWorld();
	}
	
	public Console getConsole() {
		return this.console;
	}

	// .... RUN METHODS ....

	@Override
	public void run() {
		
		this.console = new Console(this);
		
		try {
			if (!this.configFile.exists()) {
				this.configFile.createNewFile();
				Settings.getInstance().saveToFile(configFile);
			}

			Settings.getInstance().loadFromFile(this.configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		GLHelper.setDisplayMode(Settings.getInstance().windowWidth.getValue(),
				Settings.getInstance().windowHeight.getValue(),
				Settings.getInstance().fullscreen.getValue());
		this.init();
		this.load(true);

		long time = System.nanoTime();
		long lastTime = time;
		long lastInfo = time;

		while (!Display.isCloseRequested()) {
			
			long loopStart = System.nanoTime();
			
			int e = GL11.glGetError();
			if(e != 0) {
				System.err.println("GL ERROR: " + e + " - " + GLU.gluErrorString(e));
			}	

			this.profiler.render.start();
			float partialTickTime = (time - lastTime)
					/ ((float) TICK_TIME * 1000000000);

			this.onRender(partialTickTime);
			this.render(partialTickTime);

			Display.update();
			this.profiler.render.end();
			
			time = System.nanoTime();
			while (time - lastTime >= Game.TICK_TIME * 1000000000) {
				this.ticks++;
				this.profiler.tick.start();
				this.onTick();
				this.profiler.tick.end();
				lastTime += Game.TICK_TIME * 1000000000;
			}

			if (time - lastInfo >= 1000000000) {
				lastInfo += 1000000000;
				this.lastTPS = this.ticks;
				this.ticks = 0;
				SoundManager.getInstance().updatePlaying();
				SoundManager.getInstance().getMusicProvider().updateMusic(this);
			}
			
			long loopEnd = System.nanoTime();
			
			this.fps = (int) (1000000000 / (loopEnd - loopStart));

			if (Settings.getInstance().fpsLimit.getValue() > 0)
				Display.sync(Settings.getInstance().fpsLimit.getValue());
		}

		this.exit();
	}

	public void init() {
		GLHelper.initGL(Settings.getInstance().windowWidth.getValue(),
				Settings.getInstance().windowHeight.getValue());
		Display.setTitle(TITLE);
	}

	public void exit() {
		if (this.worldsManager.isInGame()) {
			this.worldsManager.exitWorld();
		}

		SoundManager.getInstance().shutdown();

		Display.destroy();
		AL.destroy();
		
		try {
			Settings.getInstance().saveToFile(this.configFile);
			this.keyconfigLoader.save();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.exit(0);
	}

	public void load(boolean toMenu) {
		FontManager.getInstance().load();
		this.font = FontManager.getFont();

		this.displayLoadingScreen("Loading textures...");
		TextureManager.load(this);

		this.displayLoadingScreen("Loading models...");
		ModelManager.getInstance().load();

		this.displayLoadingScreen("Loading keyconfig...");
		this.keyconfigLoader.load(new File("keyconfig"));

		this.displayLoadingScreen("Creating world config");
		this.worldsManager.load();

		this.displayLoadingScreen("Loading sounds...");

		this.lastFPS = getTime();

		this.showbg = true;

		if (toMenu) {
			this.openGuiScreen(new GuiScreenMainMenu(this));
		} else {
			this.closeGuiScreen();
		}
	}

	// .... RENDER/UPDATE METHODS ....

	public void onTick() {

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyconfig.screenshot) {
					GameUtil.screenshot();
				}

				if (this.consoleOpen) {
					this.console.charTyped(
							Keyboard.getEventCharacter(),
							Keyboard.getEventKey());
				}

				if (Keyconfig.isDownEvent(Keyconfig.console)) {
					if (!this.consoleOpen) {
						this.console.clearInput();
						this.consoleOpen = true;
					}
				}

				if (Keyboard.getEventKey() == Keyconfig.fullscreen
						&& this.guiScreen == null && !this.consoleOpen) {
					this.toggleFullscreen();
				}

				if(Keyboard.getEventKey() == Keyconfig.toggleNoClip) {
					Settings.getInstance().noclip.setValue(
							!Settings.getInstance().noclip.getValue());
				}
				
				if (Keyboard.getEventKey() == Keyconfig.exit) {
					if (!this.consoleOpen) {
						if (this.getCurrentWorld().getGui().isOpened()) {
							this.getCurrentWorld().getGui().closeScreen();
						} else {
							if (this.guiScreen != null) {
								this.closeGuiScreen();
							} else {
								this.openGuiScreen(new GuiScreenMenu(this));
							}
						}
					} else {
						this.consoleOpen = false;
					}
				}

			}
		}

		if (Keyboard.isKeyDown(Keyconfig.zoom)) {
			if (this.lastFov == 0) {
				this.lastFov = Settings.getInstance().fov.getValue();
				Settings.getInstance().fov.setValue(15f);
			}
		} else {
			if (this.lastFov > 0) {
				Settings.getInstance().fov.setValue(lastFov);
				this.lastFov = 0;
			}
		}

		if (!this.worldsManager.isInGame() && this.guiScreen == null) {
			this.openGuiScreen(new GuiScreenMainMenu(this));
		}

		if (this.guiScreen == null && this.worldsManager.isInGame()
				&& !this.consoleOpen) {
			this.worldsManager.getWorld().onTick();
		} else if (this.worldsManager.isInGame()) {
			this.worldsManager.getWorld().menuUpdate();
		}

		this.lastConsoleAnimationProgress = this.consoleAnimationProgress;

		this.consoleAnimationProgress += this.consoleOpen ? 0.150f : -0.150f;

		if (this.consoleAnimationProgress < 0) {
			this.consoleAnimationProgress = 0;
		}

		if (this.consoleAnimationProgress > 1) {
			this.consoleAnimationProgress = 1;
		}
	}

	public void onRender(float ptt) {
		if (this.guiScreen == null && this.worldsManager.isInGame()
				&& !this.consoleOpen) {
			this.worldsManager.getWorld().onRenderTick(ptt);
		}
	}

	public void render(float ptt) {

		float pttbackup = ptt;

		if (this.guiScreen != null || this.consoleOpen) {
			ptt = 1;
		}
		
		// Clear old frame
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		if (this.worldsManager.isInGame()) {

			TextureManager.atlas.bind();
			this.worldsManager.getWorld().getRenderEngine().renderWorld(ptt);

			GLHelper.setOrtho(Settings.getInstance().windowWidth.getValue(),
					Settings.getInstance().windowHeight.getValue());
			this.getOverlayManager().renderOverlays(ptt);

		}

		GLHelper.setOrtho(Settings.getInstance().windowWidth.getValue(),
				Settings.getInstance().windowHeight.getValue());

		this.renderGuiScreen(ptt);

		this.renderConsole(pttbackup);

		GLHelper.setPerspective(Settings.getInstance().windowWidth.getValue(),
				Settings.getInstance().windowHeight.getValue());

	}

	public void renderGuiScreen(float ptt) {
		if (this.guiScreen != null) {
			this.guiScreen.update();
			if (this.guiScreen != null) {

				if (!this.worldsManager.isInGame()) {
					if (TextureManager.menuBg != null && this.showbg) {
						GLHelper.drawTexture(TextureManager.menuBg, 0,
								Settings.getInstance().windowWidth.getValue(),
								0,
								Settings.getInstance().windowWidth.getValue());
					} else {
						GLHelper.drawRectangle(0.2f, 0.2f, 0.2f, 0,
								Settings.getInstance().windowWidth.getValue(),
								0,
								Settings.getInstance().windowWidth.getValue());
					}

					if (TextureManager.logo != null && this.showbg) {
						GLHelper.drawTexture(
								TextureManager.logo,
								(Settings.getInstance().windowWidth.getValue() / 2)
										- (TextureManager.logo.getImageWidth() / 2),
								32);

						this.font.drawString(5,
								Settings.getInstance().windowHeight.getValue()
										- font.getHeight(), this.versionString);
						this.font
								.drawString(
										15 + this.font
												.getWidth(this.versionString),
										Settings.getInstance().windowHeight
												.getValue() - font.getHeight(),
										this.loginString,
										this.authManager.isAuthenticated() ? Color.white
												: Color.red);
					}
					this.guiScreen.render();
				}

				this.guiScreen.render();
			}
		}
	}
	
	private Color consoleC1 = new Color(0xD0000000);
	private Color consoleC2 = new Color(0x500030A0);

	public void renderConsole(float ptt) {
		GL11.glPushMatrix();

		int cHeight = Settings.getInstance().consoleHeight.getValue();

		float lerp = this.lastConsoleAnimationProgress
				+ (this.consoleAnimationProgress - this.lastConsoleAnimationProgress) * ptt;

		GL11.glTranslatef(0, -((1 - lerp) * cHeight), 0);

		if (this.lastConsoleAnimationProgress > 0) {
			GLHelper.drawRectangle(this.consoleC1, 0,
					Settings.getInstance().windowWidth.getValue(), 0, cHeight);
			
			GLHelper.drawRectangle(this.consoleC2, 0,
					Settings.getInstance().windowWidth.getValue(), cHeight - this.font.getLineHeight(), cHeight);
			
			
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			String cursor = (this.ticks % Game.TPS >= Game.TPS / 2) ? "_" : "";
			this.font.drawString(0, cHeight - this.font.getLineHeight(), "> "
					+ this.console.currentCommand + cursor);
			String info = Game.TITLE;
			this.font.drawString(Settings.getInstance().windowWidth.getValue()
					- this.font.getWidth(info) - 2,
					cHeight - this.font.getLineHeight() * 2, info,
					new org.newdawn.slick.Color(120, 120, 120));

			ListIterator<String> li = this.console.lines
					.listIterator(this.console.lines.size());

			int offset = 0;

			while (li.hasPrevious()) {
				offset += this.font.getLineHeight();

				this.font
						.drawString(0, cHeight - this.font.getLineHeight()
								- offset
								- this.console.getTranslation(), li
								.previous(), new org.newdawn.slick.Color(200,
								200, 200));
			}

		}

		GL11.glPopMatrix();
	}

	// .... GUI METHODS ....

	public void openGuiScreen(GuiScreen scr) {
		if (this.guiScreen != null)
			this.guiScreen.onClosing();
		this.guiScreen = scr;
		scr.onOpening();
		Mouse.setGrabbed(false);
	}

	public void closeGuiScreen() {
		if (this.guiScreen != null) {
			this.guiScreen.onClosing();
			this.guiScreen = null;

			if (this.consoleOpen) {
				this.consoleOpen = false;
			}

			Mouse.setGrabbed(true);
		}
	}

	public void displayLoadingScreen(String text) {
		// isIngame = false;
		this.openGuiScreen(new GuiScreenLoading(this, text));
		this.render(0);
		Display.update();
	}

	public void displayLoadingScreen() {
		// isIngame = false;
		this.openGuiScreen(new GuiScreenLoading(this));
		this.render(0);
		Display.update();
	}

	// .... NEMAMZDANIKCEMU METHODS ....

	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public int getTPS() {
		return this.lastTPS;
	}
	
	public int getFPS() {
		return this.fps;
	}
	
	public void toggleFullscreen() {
		Settings.getInstance().fullscreen
				.setValue(!Settings.getInstance().fullscreen.getValue());
	}

	// .... LOGIN METHODS ....
	public void doLogin(String userName, String password, String token) {
		this.authManager = new AuthManager(userName, password, token);

		if (this.authManager.isAuthenticated()) {
			this.loginString = userName + " is logged in with token " + token;
		} else {
			this.loginString = "Bad login for " + userName;
		}
	}

	public void dummyLogin() {
		this.authManager = new AuthManager();
		this.authManager.setDummyName("Player");
		this.loginString = "User is not logged in, using name Player";
	}

}
