package cz.dat.oots.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;

import cz.dat.oots.util.GLHelper;

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
	
	protected Color c1 = new Color(0xFF8C8C8C);
	protected Color c2 = new Color(0xFF404040);
	protected Color c3 = new Color(0xFF6E6E6E);
	protected Color c4 = new Color(0xA0FFFFFF);

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
		GLHelper.drawRectangle(this.c1, this.x1, this.x2, this.y1, this.y2);
		GLHelper.drawRectangle(this.c2, this.x1 + 2, this.x2 - 2, this.y1 + 2, this.y2 - 2);
		
		int x = this.x1 + GuiObjectSlider.SLIDER_WIDTH / 2 + 2 + 
			(int)Math.round(
					((float)(this.val - this.minVal) / (this.maxVal - this.minVal)) * 
					((x2 - x1) - GuiObjectSlider.SLIDER_WIDTH - 4)
			);
		
		GLHelper.drawRectangle(this.c3, x - GuiObjectSlider.SLIDER_WIDTH / 2,
				x + GuiObjectSlider.SLIDER_WIDTH / 2, this.y1 + 2, this.y2 - 2);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		String toDraw = formatString.replace("%v", this.val + "");
		this.font.drawString(
				this.x1 + (this.x2 - this.x1) / 2 - this.font.getWidth(toDraw) / 2, 
				this.y1 + (this.y2 - this.y1) / 2 - this.font.getHeight(toDraw) / 2, 
				toDraw
		);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		if(this.hover) {
			GLHelper.drawRectangle(this.c4, x - GuiObjectSlider.SLIDER_WIDTH / 2,
					x + GuiObjectSlider.SLIDER_WIDTH / 2, this.y1 + 2, this.y2 - 2);
		}
	}

	@Override
	public void update() {
		this.wasPressed = this.pressed;
		this.wasHovered = this.hover;
		
		if(Mouse.getX() >= x1 && Mouse.getX() <= x2 && Display.getHeight() - Mouse.getY() >= y1 && Display.getHeight() - Mouse.getY() <= y2) {
			
			this.hover = true;
			
			if(this.wasHovered && !this.wasPressed && !this.lock) {
				this.pressed = Mouse.isButtonDown(0);
			} else {
				this.lock = true;
			}

			

		} else {
			this.hover = false;
		}

		
		if(!Mouse.isButtonDown(0)) {
			this.pressed = false;
			this.lock = false;
		}
		
		if(this.lock) {
			this.hover = false;
		}
		
		if(this.pressed) {
			this.hover = true;
			int width = this.x2 - this.x1 - GuiObjectSlider.SLIDER_WIDTH + 2;
			int clickedX = Mouse.getX() - this.x1 - GuiObjectSlider.SLIDER_WIDTH / 2 + 1;
			this.val = ((float)(clickedX) / (float)width) * (this.maxVal - this.minVal) + this.minVal;
			this.val = (float) (0.01 * Math.floor(this.val * 100.0));
			this.parent.sliderUpdate(this);
		}
		
		if(this.val < this.minVal) {
			this.val = this.minVal;
		}
		
		if (this.val > this.maxVal) {
			this.val = this.maxVal;
		}
		
		this.parent.sliderUpdate(this);
		
	}	
}
