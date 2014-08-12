package dax.blocks.gui.ingame;

import dax.blocks.util.GLHelper;
import dax.blocks.util.Rectangle;

public class SliderControl extends Control {
	
	private float actualValue;
	private int backgroundColor;
	private int sliderColor;
	private boolean horizontal = false;
	private final int sliderWidth = 20;
	private Rectangle sliderRectangle;
	
	public SliderControl(int relativeX, int relativeY, int width, int height, GuiScreen screen) {
		this(relativeX, relativeY, width, height, 0x80A9A9A9, 0xF1000000, screen);
	}
	
	public SliderControl(int relativeX, int relativeY, int width, int height, int backColor, int sliderColor,
			GuiScreen screen) {
		super(relativeX, relativeY, width, height, screen);
		this.backgroundColor = backColor;
		this.sliderColor = sliderColor;
		this.sliderRectangle = new Rectangle(0, 0, 0, 0);
	}
	
	public void setHorizontal(boolean isHorizontal) {
		this.horizontal = isHorizontal;
	}
	
	public float getValue() {
		return this.actualValue;
	}

	@Override
	public void onTick() {
		super.onTick();
		this.updateSliderRectangle();
	}

	@Override
	public void render() {
		GLHelper.drawRectangle(this.backgroundColor, super.rectangle.getX(), super.rectangle.getTopRight(),
				super.rectangle.getY(), super.rectangle.getBottomLeft());
		
		GLHelper.drawRectangle(this.sliderColor, this.sliderRectangle.getX(), this.sliderRectangle.getTopRight(),
				this.sliderRectangle.getY(), this.sliderRectangle.getBottomLeft());
	}

	private void updateSliderRectangle() {
		int usefulSize = horizontal ? super.rectangle.getWidth() - this.sliderWidth : 
			super.rectangle.getHeight() - this.sliderWidth;
		int sliderCenter = (int)(usefulSize * (this.actualValue / 100));
		this.sliderRectangle.set(horizontal ? this.sliderWidth / 2 + sliderCenter : this.rectangle.getWidth() / 2,
				horizontal ? this.rectangle.getHeight() / 2 : this.sliderWidth / 2 - sliderCenter, 
				horizontal ? this.sliderWidth : this.rectangle.getWidth(), 
						horizontal ? this.rectangle.getHeight() : this.sliderWidth);
	}
}
