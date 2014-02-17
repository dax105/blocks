package dax.blocks.gui;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Font;

public class GuiObjectTitleBar extends GuiObject {
	private String text;

	private int x1;
	private int y1;
	private int x2;
	private int y2;

	private Font font;

	public GuiObjectTitleBar(int x1, int y1, int x2, int y2, Font font, String text) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.text = text;
		this.font = font;
	}

	@Override
	public void render() {
		drawRect(x1, y1, x2, y2, 0xFF004FA3);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		font.drawString(x1 + (x2 - x1) / 2 - font.getWidth(text) / 2, y1 + (y2 - y1) / 2 - font.getHeight(text) / 2, text);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	@Override
	public void update() {

	}

}
