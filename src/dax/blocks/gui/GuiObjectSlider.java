package dax.blocks.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Font;

public class GuiObjectSlider extends GuiObject {

	public static final int SLIDER_WIDTH = 40;

	public int id;
	private GuiScreen parent;

	public String text;
	public String unit;

	private int x1;
	private int y1;
	private int x2;
	private int y2;

	private Font font;

	private int minVal;
	private int maxVal;
	public int val;

	private boolean lockup = false;
	private boolean hover = false;

	public GuiObjectSlider(int x1, int y1, int x2, int y2, Font font, String text, int id, GuiScreen parent, int minVal, int maxVal, int val, String unit) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.text = text;
		this.id = id;
		this.font = font;
		this.parent = parent;
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.val = val;
		this.unit = unit;
	}

	@Override
	public void render() {
		drawRect(x1, y1, x2, y2, 0xFF8C8C8C);
		drawRect(x1 + 2, y1 + 2, x2 - 2, y2 - 2, 0xFF404040);
		
		int x = x1 + SLIDER_WIDTH/2 +2 + (int)Math.round(((float)(val-minVal) / (maxVal-minVal))*((x2-x1)-SLIDER_WIDTH-4));
		
		drawRect(x - SLIDER_WIDTH / 2 , y1 + 2, x + SLIDER_WIDTH / 2 , y2 - 2, 0xFF6E6E6E);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		String toDraw = text + val + unit;
		font.drawString(x1 + (x2 - x1) / 2 - font.getWidth(toDraw) / 2, y1 + (y2 - y1) / 2 - font.getHeight(toDraw) / 2, toDraw);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		if (hover) {
			drawRect(x - SLIDER_WIDTH / 2, y1 + 2, x + SLIDER_WIDTH / 2, y2 - 2, 0xA0FFFFFF);
		}
	}

	@Override
	public void update() {
		if (Mouse.getX() >= x1 && Mouse.getX() <= x2 && Display.getHeight() - Mouse.getY() >= y1 && Display.getHeight() - Mouse.getY() <= y2) {
			
			if (Mouse.isButtonDown(0) && !hover) {
				lockup = true;
			} else {
				hover = true;
			}

			if (Mouse.isButtonDown(0) && hover) {
				int width = x2 - x1 - SLIDER_WIDTH+2;
				int clickedX = Mouse.getX() - this.x1 - SLIDER_WIDTH/2+1;
				val = Math.round(((float)(clickedX) / (float)width)*(maxVal-minVal)+minVal);
				parent.sliderUpdate(this);
			}

		} else {
			hover = false;
		}

		if (val < minVal) {
			val = minVal;
		}
		
		if (val > maxVal) {
			val = maxVal;
		}	
		
	}

}
