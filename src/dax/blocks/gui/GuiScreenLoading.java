package dax.blocks.gui;

import org.lwjgl.opengl.Display;

import dax.blocks.Game;
import dax.blocks.settings.Settings;

public class GuiScreenLoading extends GuiScreen {

	private int width = 400;
	private int height = 30;
	private int overflow = 8;
	private GuiObjectTitleBar titleBar;

	public GuiScreenLoading(Game game, String text) {
		super(game);
		this.objects.add(new GuiObjectRectangle(
					(Settings.getInstance().windowWidth.getValue() - width - overflow) / 2, 
					(Settings.getInstance().windowHeight.getValue() - height - overflow) / 2, 
					(Settings.getInstance().windowWidth.getValue() + width + overflow) / 2, 
					(Settings.getInstance().windowHeight.getValue() + height + overflow) / 2, 
					0xA0000000)
		);

		this.titleBar = new GuiObjectTitleBar(
				(Settings.getInstance().windowWidth.getValue() - width) / 2, 
				(Settings.getInstance().windowHeight.getValue() - height) / 2, 
				(Settings.getInstance().windowWidth.getValue() + width) / 2, 
				((Settings.getInstance().windowHeight.getValue() - height) / 2) + 30, 
				this.f, text
		);
	}
	
	public GuiScreenLoading(Game game) {
		this(game, "Loading...");
	}

	public void update(String text) {
		this.titleBar.setText(text);
		this.game.render(0);
		Display.update();
	}
	
	@Override
	public void buttonPress(GuiObjectButton button) {

	}
	
	@Override
	public void sliderUpdate(GuiObjectSlider slider) {

	}
	
	@Override
	public void render() {
		super.render();
		this.titleBar.render();
	}

	@Override
	public void buttonChanged(GuiObjectChangingButton button, int line) {
		
		
	}

	@Override
	public void onClosing() {	
	}

	@Override
	public void onOpening() {
	}
}
