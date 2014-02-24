package dax.blocks;

import dax.blocks.block.Block;
import dax.blocks.block.BlockBasic;
import dax.blocks.gui.GuiScreen;
import dax.blocks.gui.GuiScreenLoading;
import dax.blocks.gui.GuiScreenMainMenu;
import dax.blocks.gui.GuiScreenMenu;
import dax.blocks.world.World;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.TrueTypeFont;

public class Game implements Runnable {

	public int width = 800;
	public int height = 480;

	public int worldSize = 4;
	public boolean shouldFilter = true;

	public float heightMultipler = 20;

	GuiScreen guiScreen;

	public int fov = 50;
	public static final String TITLE = "Order of the stone";

	boolean isFullscreen = false;

	public TrueTypeFont font;

	long lastFrame;
	int fpsCounter;
	int fps = 0;
	long lastFPS;

	public World world;

	public boolean isIngame = false;
	
	public int vertices = 0;

	@Override
	public void run() {
		setDisplayMode(width, height, isFullscreen);
		load();
		while (!Display.isCloseRequested()) {
			float delta = getDelta();

			update(delta);
			render();

			Display.update();
		}

		Display.destroy();
		System.exit(0);
	}

	public void init() {
		initGL();
		Display.setTitle(TITLE);
		Mouse.setGrabbed(true);
	}

	public void load() {
		TextureManager.load();
		createFont();
		getDelta();
		lastFPS = getTime();
		openGuiScreen(new GuiScreenMainMenu(this));
	}

	public void makeNewWorld() {
		world = new World(this.worldSize, this.heightMultipler);
		closeGuiScreen();
		isIngame = true;
	}

	public void displayLoadingScreen() {
		openGuiScreen(new GuiScreenLoading(this));
		render();
		Display.update();
	}

	public void createFont() {
		Font awtFont = new Font("Arial", Font.BOLD, 14);
		font = new TrueTypeFont(awtFont, false);
	}

	public void update(float delta) {

		if (!isIngame && this.guiScreen == null) {
			openGuiScreen(new GuiScreenMainMenu(this));
		}

		if (this.guiScreen == null && isIngame) {
			world.update(delta);
		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_F && this.guiScreen == null) {
					toggleFullscreen();
				}

				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
					if (this.guiScreen != null) {
						closeGuiScreen();
					} else {
						openGuiScreen(new GuiScreenMenu(this));
					}
				}
			}
		}
		// Display.sync(50);
		updateFPS();
	}

	public float getDelta() {
		long time = System.nanoTime();
		float delta = (time - lastFrame) / 1000000.0f;
		lastFrame = time;

		return delta;
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
		GL11.glAlphaFunc(GL11.GL_GEQUAL, 0.7F);

		// Clear color
		GL11.glClearColor(0.63f, 0.87f, 1.0f, 1.0f);

		// Set light properties
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		FloatBuffer ambientLight = BufferUtils.createFloatBuffer(4);
		ambientLight.put(3.0f).put(3.0f).put(3.0f).put(1).flip();
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, ambientLight);

		// Fog
		/*
		 * GL11.glEnable(GL11.GL_FOG); FloatBuffer fogColor = BufferUtils.createFloatBuffer(4); fogColor.put(0.63f).put(0.87f).put(1.0f).put(1.0f).flip(); GL11.glFog(GL11.GL_FOG_COLOR, fogColor); GL11.glHint(GL11.GL_FOG_HINT, GL11.GL_DONT_CARE); GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR); GL11.glFogf(GL11.GL_FOG_START, 100.0f); GL11.glFogf(GL11.GL_FOG_END, 200.0f);
		 */

	}

	public void setPerspective() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(fov, (float) width / (float) height, 0.05F, 1000F);
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

	public void render() {
		// Clear old frame

		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		if (isIngame) {
			GL11.glPushMatrix();
			GL11.glRotatef(-world.player.tilt, 1f, 0f, 0f);
			GL11.glRotatef(world.player.heading, 0f, 1f, 0f);
			GL11.glTranslated(-world.player.posX, -world.player.posY - Player.EYES_HEIGHT, -world.player.posZ);

			FloatBuffer lp = BufferUtils.createFloatBuffer(4);
			lp.put(-10).put(10).put(-10).put(0).flip();

			GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, lp);

			TextureManager.atlas.bind();

			world.render();
			GL11.glPopMatrix();

			setOrtho();
			renderOverlay();
		}

		setOrtho();

		if (this.guiScreen != null) {
			guiScreen.update();
			if (this.guiScreen != null) {

				if (!isIngame) {
					GL11.glColor3f(1, 1, 1);

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

					TextureManager.logo.bind();

					GL11.glBegin(GL11.GL_QUADS);

					GL11.glTexCoord2f(0, 0);
					GL11.glVertex2f((width - TextureManager.logo.getImageWidth()) / 2, height / 2 - 170);

					GL11.glTexCoord2f(TextureManager.logo.getWidth(), 0);
					GL11.glVertex2f((width + TextureManager.logo.getImageWidth()) / 2, height / 2 - 170);

					GL11.glTexCoord2f(TextureManager.logo.getWidth(), TextureManager.logo.getHeight());
					GL11.glVertex2f((width + TextureManager.logo.getImageWidth()) / 2, height / 2 - 170 + TextureManager.logo.getImageHeight());

					GL11.glTexCoord2f(0, TextureManager.logo.getHeight());
					GL11.glVertex2f((width - TextureManager.logo.getImageWidth()) / 2, height / 2 - 170 + TextureManager.logo.getImageHeight());

					GL11.glEnd();

					GL11.glDisable(GL11.GL_TEXTURE_2D);

					guiScreen.render();
				}

				guiScreen.render();
			}
		}

		setPerspective();

	}

	public void openGuiScreen(GuiScreen scr) {
		this.guiScreen = scr;
		Mouse.setGrabbed(false);
	}

	public void closeGuiScreen() {
		this.guiScreen = null;
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

		String fpsString = "FPS: " + fps + ", " + world.getVertices() + " vertices";
		int stringWidth = font.getWidth(fpsString);
		font.drawString(width - stringWidth - 2, font.getHeight(), fpsString);

		font.drawString(2, 0, "X Position: " + world.player.posX);
		font.drawString(2, font.getHeight(), "Y Position: " + world.player.posY);
		font.drawString(2, font.getHeight() * 2, "Z Position: " + world.player.posZ);

		String memory = "Used memory: " + (allocatedMemory / (1024 * 1024) - freeMemory / (1024 * 1024)) + "MB" + "/" + allocatedMemory / (1024 * 1024) + "MB";
		int memoryWidth = font.getWidth(memory);
		font.drawString(width - memoryWidth - 2, 0, memory);
		
		String chunks = "Chunks drawn: " + world.chunksDrawn + "/" + worldSize*worldSize;
		font.drawString(width - font.getWidth(chunks) - 2, font.getHeight()*2, chunks);

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
		if ((Display.getDisplayMode().getWidth() == width) && (Display.getDisplayMode().getHeight() == height) && (Display.isFullscreen() == fullscreen)) {
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
				targetDisplayMode = new DisplayMode(width, height);
			}

			if (targetDisplayMode == null) {
				System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
				return;
			}

			Display.setDisplayMode(targetDisplayMode);
			Display.setFullscreen(fullscreen);

			if (!Display.isCreated()) {
				try {
					Display.create();
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
			}

			init();

		} catch (LWJGLException e) {
			System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
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
