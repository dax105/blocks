package dax.blocks;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

public class GLHelper {
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
		drawTexture(texture, 0, texture.getWidth(), 0, texture.getHeight(), x,
				x + texture.getImageWidth(), y, y + texture.getImageHeight());
	}

	public static void drawTexture(Texture texture, float textureX1,
			float textureX2, float textureY1, float textureY2, float x, float y) {
		drawTexture(texture, textureX1, textureX2, textureY1, textureY2, x, x
				+ texture.getImageWidth(), y, y + texture.getImageHeight());
	}

	public static void drawTexture(Texture texture, float x1, float x2, float y1, float y2) {
		drawTexture(texture, 0, texture.getWidth(), 0, texture.getHeight(), x1, x2, y1, y2);
	}
	
	
	public static void drawTextureCropped(Texture texture, float x, float y,
			float cropXPercent, float cropYPercent) {
		drawTexture(texture, 0, texture.getWidth() * cropXPercent, 0,
				texture.getHeight() * cropYPercent, x, x + texture.getImageWidth()
						* cropXPercent, y, y + texture.getImageHeight() * cropYPercent);
	}

	public static void drawFromAtlas(int id, float x, float y) {
		drawFromAtlas(id, x, x + 16, y, y + 16);
	}
	
	public static void drawFromAtlas(int id, float x1, float x2, float y1, float y2) {
		drawTexture(TextureManager.atlas, TextureManager.getX1(id),
				TextureManager.getX2(id), TextureManager.getY1(id),
				TextureManager.getY2(id), x1, x2, y1, y2);
	}
	
	public static void drawRectangle(float r, float g, float b, float x1, float x2, float y1, float y2) {
		GL11.glColor3f(r, g, b);

		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glVertex2f(x1, y1);

		GL11.glVertex2f(x2, y1);

		GL11.glVertex2f(x2, y2);

		GL11.glVertex2f(x1, y2);

		GL11.glEnd();
	}
	
	public static void updateFiltering(boolean linear) {
		TextureManager.atlas.bind();
		if (linear) {
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

}
