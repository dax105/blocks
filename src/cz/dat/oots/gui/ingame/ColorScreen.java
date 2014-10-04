package cz.dat.oots.gui.ingame;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import cz.dat.oots.FontManager;

public class ColorScreen extends GuiScreen {
	
	private SliderControl r;
	private SliderControl g;
	private SliderControl b;
	private Color mixColor;
	private final Color sliderBackColor;
	private IColorChangeCallback callback;
	private TextControl t;
	
	public ColorScreen(Color initialColor, IColorChangeCallback callback, GuiManager guiManager) {
		super(300, 100, 1, 1, 1, 0.5f, guiManager);
		this.callback = callback;
		this.sliderBackColor = new Color(Color.darkGray);
		this.sliderBackColor.a = 0.6f;
		
		
		this.initTitle();
		this.initSliders();
		
		this.r.setValue(initialColor.r);
		this.g.setValue(initialColor.g);
		this.b.setValue(initialColor.b);
		
		this.mixColor = initialColor;
		
		this.addControl(r);
		this.addControl(g);
		this.addControl(b);
	}
	
	private void initTitle() {
		TrueTypeFont f = FontManager.getFont();
		String title = "Change block color";
		t = new TextControl((this.getWidth() / 2) - (f.getWidth(title) / 2), 5, f, title, this);
		
		this.addControl(t);
	}
	
	private void initSliders() {
		int x = 5;
		int fY = this.t.rectangle.getHeight() + 10;
		int xW = this.getWidth() - 10;
		
		this.r = new SliderControl(x, fY, xW, 10, 5, this);
		this.g = new SliderControl(x, fY + 15, xW, 10, 5, this);
		this.b = new SliderControl(x, fY + 30, xW, 10, 5, this);
		
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
				
				backColor.r = mixColor.r;
				backColor.g = mixColor.g;
				backColor.b = mixColor.b;
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
		this.r.onParentOpened();
		this.g.onParentOpened();
		this.b.onParentOpened();
	}

	@Override
	public void onClosing() {
		callback.onColorChanged(this, this.mixColor);
	}
}

