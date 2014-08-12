package dax.blocks.gui.ingame;

import dax.blocks.util.GLHelper;
import dax.blocks.util.Rectangle;

public class SliderControl extends Control {
	
	private float actualValue;
	private int backgroundColor;
	private int sliderColor;
	private boolean horizontal = true;
	private final int sliderSize = 20;
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
		this.actualValue = 1.0f;
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
		
		float val = this.actualValue;
		
		int minPos = this.horizontal ? super.rectangle.getX() : super.rectangle.getY();
		int range = (this.horizontal ? super.rectangle.getWidth() : super.rectangle.getHeight()) - this.sliderSize;
		int offset = (int) Math.round(range * val);
		
		this.sliderRectangle.set(
				this.horizontal ? (minPos+offset) : super.rectangle.getX(),
				this.horizontal ? super.rectangle.getY() : (minPos+offset),
				this.horizontal ? this.sliderSize : super.rectangle.getWidth(),
				this.horizontal ? super.rectangle.getHeight() : this.sliderSize
		);
	}
}
