package dax.blocks.gui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import dax.blocks.util.GameUtil;

public abstract class GuiObject {

	public abstract void render();
	public abstract void update();

	public void drawRect(int x1, int y1, int x2, int y2, int color) {
		Color col = GameUtil.getColorFromInt(color);
		GL11.glColor4ub((byte) col.getRed(), (byte) col.getGreen(), (byte) col.getBlue(), (byte) col.getAlpha());

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(x1, y1);
		GL11.glVertex2f(x2, y1);
		GL11.glVertex2f(x2, y2);
		GL11.glVertex2f(x1, y2);
		GL11.glEnd();

	}
}
