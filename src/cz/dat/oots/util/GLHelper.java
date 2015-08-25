package cz.dat.oots.util;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import cz.dat.oots.Game;
import cz.dat.oots.TextureManager;
import cz.dat.oots.settings.Settings;

public class GLHelper {

	public static void setOrtho(int width, int height) {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 0, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	public static void setPerspective(int width, int height) {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(Settings.getInstance().fov.getValue(), (float) width
				/ (float) height, 0.05F, 1000);
		GL11.glViewport(0, 0, width, height);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	public static void initGL(int width, int height) {
		// Set perspective matrix
		GLHelper.setPerspective(width, height);

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
	
	public static void setDisplayMode(int width, int height, boolean fullscreen) {
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
				System.out.println("Failed to find value mode: " + width + "x"
						+ height + " fs=" + fullscreen);
				return;
			}

			Display.setDisplayMode(targetDisplayMode);
			Display.setFullscreen(fullscreen);

			if (!Display.isCreated()) {
				;
				try {
					Display.create(new PixelFormat(8, 8, 0, Settings.getInstance().aaSamples
							.getValue()));
					System.out.println("Display created!");
					// Display.create();
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
			}

		} catch (LWJGLException e) {
			System.out.println("Unable to setup mode " + width + "x" + height
					+ " fullscreen=" + fullscreen + e);
		}
	}
	
	public static void drawTexture(Texture texture, float textureX1,
			float textureX2, float textureY1, float textureY2, float x1,
			float x2, float y1, float y2) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(1, 1, 1);

		texture.bind();
		GL11.glBegin(GL11.GL_QUADS);

		GL11.glTexCoord2f(textureX1, textureY1);
		GL11.glVertex2f(x1, y1);

		GL11.glTexCoord2f(textureX2, textureY1);
		GL11.glVertex2f(x2, y1);

		GL11.glTexCoord2f(textureX2, textureY2);
		GL11.glVertex2f(x2, y2);

		GL11.glTexCoord2f(textureX1, textureY2);
		GL11.glVertex2f(x1, y2);

		GL11.glEnd();

		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	public static void drawTexture(Texture texture, float x, float y) {
		GLHelper.drawTexture(texture, 0, texture.getWidth(), 0, texture.getHeight(), x,
				x + texture.getImageWidth(), y, y + texture.getImageHeight());
	}

	public static void drawTexture(Texture texture, float textureX1,
			float textureX2, float textureY1, float textureY2, float x, float y) {
		GLHelper.drawTexture(texture, textureX1, textureX2, textureY1, textureY2, x, x
				+ texture.getImageWidth(), y, y + texture.getImageHeight());
	}

	public static void drawTexture(Texture texture, float x1, float x2, float y1, float y2) {
		GLHelper.drawTexture(texture, 0, texture.getWidth(), 0, texture.getHeight(), x1, x2, y1, y2);
	}
	
	public static void drawTextureCropped(Texture texture, float x, float y,
			float cropXPercent, float cropYPercent) {
		GLHelper.drawTexture(texture, 0, texture.getWidth() * cropXPercent, 0,
				texture.getHeight() * cropYPercent, x, x + texture.getImageWidth()
						* cropXPercent, y, y + texture.getImageHeight() * cropYPercent);
	}

	public static void drawFromAtlas(int id, float x, float y) {
		GLHelper.drawFromAtlas(id, x, x + 16, y, y + 16);
	}
	
	public static void drawFromAtlas(int id, float x1, float x2, float y1, float y2) {
		GLHelper.drawTexture(TextureManager.atlas, TextureManager.getX1(id),
				TextureManager.getX2(id), TextureManager.getY1(id),
				TextureManager.getY2(id), x1, x2, y1, y2);
	}
	
	public static void drawRectangle(float r, float g, float b, float a, float x1, 
			float x2, float y1, float y2) {
		GL11.glColor4f(r, g, b, a);

		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glVertex2f(x1, y1);

		GL11.glVertex2f(x2, y1);

		GL11.glVertex2f(x2, y2);

		GL11.glVertex2f(x1, y2);

		GL11.glEnd();
	}
	
	public static void drawRectangle(float r, float g, float b, float x1, float x2, 
			float y1, float y2) {
		GLHelper.drawRectangle(r, g, b, 1, x1, x2, y1, y2);
	}
	
	public static void drawRectangle(Color color, float x1, float x2, float y1, float y2) {
		GLHelper.drawRectangle(color.r, color.g, color.b, color.a, x1, x2, y1, y2);
	}
	
	public static void updateFiltering(boolean linear) {
		TextureManager.atlas.bind();
		if(linear) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
					GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
					GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
					GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
					GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		}
	}

	public static void drawLine(float x1, float x2, float y1, float y2, int thickness, 
			float r, float g, float b, float a) {
		GL11.glColor4f(r, g, b, a);
		GL11.glLineWidth(thickness);
		
		GL11.glBegin(GL11.GL_LINES);
		
		GL11.glVertex2f(x1, y1);
		GL11.glVertex2f(x2, y2);
		
		GL11.glEnd();
	}
	
	public static void drawLine(float x1, float x2, float y1, float y2, int thickness) {
		GLHelper.drawLine(x1, x2, y1, y2, thickness, 1, 1, 1, 1);
	}

	public static void renderLinedBox(float x0, float y0, float z0, float x1,
			float y1, float z1) {
		GL11.glBegin(GL11.GL_LINES);

		// front
		GL11.glVertex3f(x0, y1, z1);
		GL11.glVertex3f(x1, y1, z1);

		GL11.glVertex3f(x1, y1, z1);
		GL11.glVertex3f(x1, y0, z1);

		GL11.glVertex3f(x1, y0, z1);
		GL11.glVertex3f(x0, y0, z1);

		GL11.glVertex3f(x0, y0, z1);
		GL11.glVertex3f(x0, y1, z1);

		// right
		GL11.glVertex3f(x1, y1, z1);
		GL11.glVertex3f(x1, y1, z0);

		GL11.glVertex3f(x1, y1, z0);
		GL11.glVertex3f(x1, y0, z0);

		GL11.glVertex3f(x1, y0, z0);
		GL11.glVertex3f(x1, y0, z1);

		// back
		GL11.glVertex3f(x1, y1, z0);
		GL11.glVertex3f(x0, y1, z0);

		GL11.glVertex3f(x0, y0, z0);
		GL11.glVertex3f(x1, y0, z0);

		GL11.glVertex3f(x0, y0, z0);
		GL11.glVertex3f(x0, y1, z0);

		// left
		GL11.glVertex3f(x0, y1, z0);
		GL11.glVertex3f(x0, y1, z1);

		GL11.glVertex3f(x0, y0, z1);
		GL11.glVertex3f(x0, y0, z0);

		GL11.glEnd();
	}
}
