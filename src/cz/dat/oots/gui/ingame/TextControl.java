package cz.dat.oots.gui.ingame;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;

public class TextControl extends Control {

	private String text;
	private Font font;
	private Color color = new Color(0f, 0f, 0f, 1f);

	public TextControl(int relativeX, int relativeY, Font font, String text,
			GuiScreen screen) {
		super(relativeX, relativeY, font.getWidth(text), font.getHeight(text),
				screen);

		this.font = font;
		this.text = text;
	}

	public void setText(String text, boolean centerToActualPosition) {
		this.text = text;
		if(centerToActualPosition) {
			int centerX = this.rectangle.getX() + this.rectangle.getWidth() / 2;
			int newX = centerX - (font.getWidth(text) / 2);

			this.rectangle.set(newX, this.rectangle.getY(),
					this.font.getWidth(text), this.font.getHeight(text));
		} else {
			this.rectangle.set(this.rectangle.getX(), this.rectangle.getY(),
					this.font.getWidth(text), this.font.getHeight(text));
		}
	}

	public Color getColor() {
		return this.color;
	}

	@Override
	public void render() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		this.font.drawString(this.rectangle.getX(), this.rectangle.getY(),
				this.text, this.color);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

}
