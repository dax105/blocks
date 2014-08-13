package dax.blocks.gui;

import org.newdawn.slick.Color;

import dax.blocks.util.GLHelper;

public class GuiObjectRectangle extends GuiObject {

	private int x1;
	private int y1;
	private int x2;
	private int y2;

	private Color color;

	public GuiObjectRectangle(int x1, int y1, int x2, int y2, int color) {
		this.color = new Color(color);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	@Override
	public void render() {
		GLHelper.drawRectangle(this.color, this.x1, this.x2, this.y1, this.y2);
	}

	@Override
	public void update() {

	}

}
