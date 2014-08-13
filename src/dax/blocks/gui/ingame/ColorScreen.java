package dax.blocks.gui.ingame;

import org.newdawn.slick.Color;

public class ColorScreen extends GuiScreen {
	
	private SliderControl r;
	private SliderControl g;
	private SliderControl b;
	private Color mixColor;
	private final Color sliderBackColor = Color.darkGray;
	private IColorChangeCallback callback;
	
	public ColorScreen(int width, int height, Color initialColor, IColorChangeCallback callback, GuiManager guiManager) {
		super(width, height, 1, 1, 1, 0.5f, guiManager);
		this.callback = callback;
		
		this.initSliders();
		
		this.r.setValue(initialColor.r);
		this.g.setValue(initialColor.g);
		this.b.setValue(initialColor.b);
		
		this.mixColor = initialColor;
		
		this.addControl(r);
		this.addControl(g);
		this.addControl(b);
	}
	
	private void initSliders() {
		int x = 5;
		int xW = super.getWidth() - 2 * x;
		int hO = super.getHeight() / 6;
		
		this.r = new SliderControl(x, hO, xW, 10, 5, this);
		this.g = new SliderControl(x, hO * 2, xW, 10, 5, this);
		this.b = new SliderControl(x, hO * 3, xW, 10, 5, this);
		
		ISliderUpdateCallback c = new ISliderUpdateCallback() {

			@Override
			public void onUpdate(SliderControl caller, float value) {
				if(caller == r) {
					mixColor.r = value;
				} else if(caller == g) {
					mixColor.g = value;
				} else if(caller == b) {
					mixColor.b = value;
				}
			}
			
		};
		
		this.r.setUpdateCallback(c);
		this.g.setUpdateCallback(c);
		this.b.setUpdateCallback(c);
		
		this.r.setBackgroundColor(this.sliderBackColor);
		this.g.setBackgroundColor(this.sliderBackColor);
		this.b.setBackgroundColor(this.sliderBackColor);
		
		this.r.setSliderColor(Color.red);
		this.g.setSliderColor(Color.green);
		this.b.setSliderColor(Color.blue);
	}
	
	public Color getColor() {
		return this.mixColor;
	}
	
	@Override
	public void onOpening() {
	}

	@Override
	public void onClosing() {
		callback.onColorChanged(this, this.mixColor);
	}
}

