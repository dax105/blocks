package dax.blocks.gui.ingame;

import dax.blocks.gui.GuiObjectSlider;
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
		this.actualValue = 0.5f;
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
		
		int minPos = super.rectangle.getX();
		int maxPos = super.rectangle.getTopRight();
		int diff = maxPos - minPos - this.sliderWidth;
		int offset = (int) Math.round(diff * val);
		
		this.sliderRectangle.set(minPos+offset, super.rectangle.getY(), this.sliderWidth, super.rectangle.getHeight());
	}
}
