package cz.dat.oots.gui.ingame;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;

import cz.dat.oots.util.Coord2D;
import cz.dat.oots.util.CoordUtil;
import cz.dat.oots.util.GLHelper;
import cz.dat.oots.util.Rectangle;

public class SliderControl extends Control {

	private float actualValue;
	private Color backgroundColor;
	private Color sliderColor;
	private boolean horizontal = true;
	private int sliderSize = 0;
	private Rectangle sliderRectangle;
	private ISliderUpdateCallback callback;
	private int ticks = 0;

	// Creates default vertical slider
	public SliderControl(ISliderUpdateCallback iSliderUpdateCallback,
			GuiScreen screen) {
		this(screen.getWidth() - 20, 5, 10, screen.getHeight() - 10,
				0xFFC0C0C0, 0x90000000, 5, iSliderUpdateCallback, screen);
		this.setHorizontal(false);
	}

	public SliderControl(int relativeX, int relativeY, int width, int height,
			int sliderSize, GuiScreen screen) {
		this(relativeX, relativeY, width, height, 0x80A9A9A9, 0xF1000000,
				sliderSize, null, screen);
	}

	public SliderControl(int relativeX, int relativeY, int width, int height,
			int backColor, int sliderColor, int sliderSize,
			ISliderUpdateCallback updateCallback, GuiScreen screen) {
		super(relativeX, relativeY, width, height, screen);

		this.backgroundColor = new Color(backColor);
		this.sliderColor = new Color(sliderColor);
		this.sliderRectangle = new Rectangle(0, 0, 0, 0);
		this.actualValue = 0.5f;
		this.sliderSize = sliderSize;
		this.callback = updateCallback;
	}

	@Override
	public void onRenderTick(float ptt) {
		super.onRenderTick(ptt);
		int mouseX = Mouse.getX();
		int mouseY = CoordUtil.getProperMouseY(Mouse.getY());

		if(super.rectangle.isInRectangle(new Coord2D(mouseX, mouseY))) {
			this.updateMouse(mouseX, mouseY);
		}

		this.updateSliderRectangle();
	}

	@Override
	public void render() {
		GLHelper.drawRectangle(this.backgroundColor.r, this.backgroundColor.g,
				this.backgroundColor.b, this.backgroundColor.a,
				super.rectangle.getX(), super.rectangle.getTopRight(),
				super.rectangle.getY(), super.rectangle.getBottomLeft());

		GLHelper.drawRectangle(this.sliderColor.r, this.sliderColor.g,
				this.sliderColor.b, this.sliderColor.a,
				this.sliderRectangle.getX(),
				this.sliderRectangle.getTopRight(),
				this.sliderRectangle.getY(),
				this.sliderRectangle.getBottomLeft());
	}

	@Override
	public void onParentOpened() {
		this.ticks = 0;
	}

	public void setUpdateCallback(ISliderUpdateCallback c) {
		this.callback = c;
	}

	public void setBackgroundColor(Color c) {
		this.backgroundColor = c;
	}

	public void setSliderColor(Color c) {
		this.sliderColor = c;
	}

	public void setSliderSize(int size) {
		if(size >= 1
				&& size <= ((this.horizontal ? super.rectangle.getWidth()
						: super.rectangle.getHeight()) / 2)) {
			this.sliderSize = size;
		}
	}

	public void setHorizontal(boolean isHorizontal) {
		this.horizontal = isHorizontal;
	}

	public void setValue(float value) {
		this.setValue(value, false);
	}

	public void setValue(float value, boolean doCallback) {
		if(value >= 0 && value <= 1) {
			this.actualValue = value;

			if(doCallback && this.callback != null) {
				this.callback.onUpdate(this, this.actualValue);
			}
		}
	}

	public float getValue() {
		return this.actualValue;
	}

	private void updateMouse(int mX, int mY) {
		boolean mouseDown = Mouse.isButtonDown(0) && this.ticks >= 20;
		float newValue = this.actualValue;

		if(mouseDown) {

			if(this.horizontal) {
				int width = super.rectangle.getWidth() - this.sliderSize;
				int clickedX = mX - super.rectangle.getX() - this.sliderSize
						/ 2 + 1;
				newValue = ((float) (clickedX) / (float) width);
				newValue = (float) (0.01 * Math.floor(newValue * 100.0));
			} else {
				int width = super.rectangle.getHeight() - this.sliderSize;
				int clickedY = mY - super.rectangle.getY() - this.sliderSize
						/ 2 + 1;
				newValue = ((float) (clickedY / (float) width));
				newValue = ((float) (0.01 * Math.floor(newValue * 100.0)));
			}

			if(newValue > 1) {
				newValue = 1;
			}

			if(newValue < 0) {
				newValue = 0;
			}

			this.setValue(newValue, true);
		}

		if(super.rectangle.isInRectangle(new Coord2D(mX, mY))) {
			int wh = Mouse.getDWheel();

			if(wh < 0 && this.actualValue <= 0.95) {
				this.setValue(this.actualValue + 0.05f, true);
			} else if(wh < 0 && this.actualValue > 0.95) {
				this.setValue(1, this.actualValue != 1);
			} else if(wh > 0 && this.actualValue >= 0.05f) {
				this.setValue(this.actualValue - 0.05f, true);
			} else if(wh > 0 && this.actualValue < 0.05f) {
				this.setValue(0, this.actualValue != 0);
			}
		}

		if(this.ticks < 20) {
			this.ticks++;
		}
	}

	private void updateSliderRectangle() {

		float val = this.actualValue;

		int minPos = this.horizontal ? super.rectangle.getX() : super.rectangle
				.getY();
		int range = (this.horizontal ? super.rectangle.getWidth()
				: super.rectangle.getHeight()) - this.sliderSize;
		int offset = (int) Math.round(range * val);

		this.sliderRectangle
				.set(this.horizontal ? (minPos + offset) : super.rectangle
						.getX(), this.horizontal ? super.rectangle.getY()
						: (minPos + offset), this.horizontal ? this.sliderSize
						: super.rectangle.getWidth(),
						this.horizontal ? super.rectangle.getHeight()
								: this.sliderSize);
	}
}
