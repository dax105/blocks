package dax.blocks.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Font;

public class GuiObjectSlider extends GuiObject {

	public static final int SLIDER_WIDTH = 40;

	public int id;
	private GuiScreen parent;

	//%v
	public String formatString;
	protected int x1;
	protected int y1;
	protected int x2;
	protected int y2;

	protected Font font;

	protected float minVal;
	protected float maxVal;
	public float val;

	protected boolean hover = false;
	protected boolean wasHovered = false;
	protected boolean pressed = false;
	protected boolean wasPressed = false;
	protected boolean lock = false;

	public GuiObjectSlider(int x1, int y1, int x2, int y2, Font font, String formatString, int id, GuiScreen parent, float minVal, float maxVal, float val) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.formatString = formatString;
		this.id = id;
		this.font = font;
		this.parent = parent;
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.val = val;
	}

	@Override
	public void render() {
		drawRect(x1, y1, x2, y2, 0xFF8C8C8C);
		drawRect(x1 + 2, y1 + 2, x2 - 2, y2 - 2, 0xFF404040);
		
		int x = x1 + SLIDER_WIDTH/2 +2 + (int)Math.round(((float)(val-minVal) / (maxVal-minVal))*((x2-x1)-SLIDER_WIDTH-4));
		
		drawRect(x - SLIDER_WIDTH / 2 , y1 + 2, x + SLIDER_WIDTH / 2 , y2 - 2, 0xFF6E6E6E);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		String toDraw = formatString.replace("%v", val + "");
		font.drawString(x1 + (x2 - x1) / 2 - font.getWidth(toDraw) / 2, y1 + (y2 - y1) / 2 - font.getHeight(toDraw) / 2, toDraw);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		if (hover) {
			drawRect(x - SLIDER_WIDTH / 2, y1 + 2, x + SLIDER_WIDTH / 2, y2 - 2, 0xA0FFFFFF);
		}
	}

	@Override
	public void update() {
		wasPressed = pressed;
		wasHovered = hover;
		
		if (Mouse.getX() >= x1 && Mouse.getX() <= x2 && Display.getHeight() - Mouse.getY() >= y1 && Display.getHeight() - Mouse.getY() <= y2) {
			
			hover = true;
			
			if (wasHovered && !wasPressed && !lock) {
				pressed = Mouse.isButtonDown(0);
			} else {
				lock = true;
			}

			

		} else {
			hover = false;
		}

		
		if (!Mouse.isButtonDown(0)) {
			pressed = false;
			lock = false;
		}
		
		if (lock) {
			hover = false;
		}
		
		if (pressed) {
			hover = true;
			int width = x2 - x1 - SLIDER_WIDTH+2;
			int clickedX = Mouse.getX() - this.x1 - SLIDER_WIDTH/2+1;
			val = ((float)(clickedX) / (float)width)*(maxVal-minVal)+minVal;
			val = (float) (0.01 * Math.floor(val * 100.0));
			parent.sliderUpdate(this);
		}
		
		if (val < minVal) {
			val = minVal;
		}
		
		if (val > maxVal) {
			val = maxVal;
		}
		
		parent.sliderUpdate(this);
		
	}	

}
