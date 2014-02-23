package dax.blocks.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Font;

public class GuiObjectButton extends GuiObject {

	public int id;
	private GuiScreen parent;
	
	public String text;
	
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	
	private Font font;
	
	private boolean hover = false;
	
	public GuiObjectButton(int x1, int y1, int x2, int y2, Font font, String text, int id, GuiScreen parent) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.text = text;
		this.id = id;
		this.font = font;
		this.parent = parent;
	}
	
	@Override
	public void render() {
		drawRect(x1, y1, x2, y2, 0xFF8C8C8C);
		drawRect(x1+2, y1+2, x2-2, y2-2, 0xFF6E6E6E);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		font.drawString(x1+(x2-x1)/2-font.getWidth(text)/2, y1+(y2-y1)/2-font.getHeight(text)/2, text);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		if (hover) {
			drawRect(x1, y1, x2, y2, 0xA0FFFFFF);
		}
	}

	@Override
	public void update() {
		if (Mouse.getX() >= x1 && Mouse.getX() <= x2 && Display.getHeight()-Mouse.getY() >= y1 && Display.getHeight()-Mouse.getY() <= y2) {
			hover = true;
			while (Mouse.next()) {
				if (Mouse.getEventButtonState()) {
					if (Mouse.getEventButton() == 0) {
						parent.buttonPress(this);
					}
				}
			}
		} else {
			hover = false;
		}

	}

}
