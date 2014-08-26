package dax.blocks.gui;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;

import dax.blocks.util.GLHelper;

public class GuiObjectTitleBar extends GuiObject {
	private String text;

	private int x1;
	private int y1;
	private int x2;
	private int y2;

	private Font font;
	
	private Color c = new Color(0xFF004FA3);

	public void setText(String text) {
		this.text = text;
	}
	
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
		GLHelper.drawRectangle(this.c, this.x1, this.x2, this.y1, this.y2);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		this.font.drawString(
				this.x1 + (this.x2 - this.x1) / 2 - this.font.getWidth(this.text) / 2, 
				this.y1 + (this.y2 - this.y1) / 2 - this.font.getHeight(this.text) / 2, 
				this.text
		);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	@Override
	public void update() {

	}

}
