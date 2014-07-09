package dax.blocks.gui;

import org.lwjgl.opengl.Display;

import dax.blocks.Game;

public class GuiScreenLoading extends GuiScreen {

	int width = 400;
	int height = 30;

	int overflow = 8;
	
	GuiObjectTitleBar titleBar;

	public GuiScreenLoading(Game game, String text) {
		super(game);
		objects.add(new GuiObjectRectangle((game.width - width - overflow) / 2, (game.height - height - overflow) / 2, (game.width + width + overflow) / 2, (game.height + height + overflow) / 2, 0xA0000000));

		titleBar = new GuiObjectTitleBar((game.width - width) / 2, (game.height - height) / 2, (game.width + width) / 2, ((game.height - height) / 2) + 30, this.f, text);
	}
	
	public GuiScreenLoading(Game game) {
		super(game);
		objects.add(new GuiObjectRectangle((game.width - width - overflow) / 2, (game.height - height - overflow) / 2, (game.width + width + overflow) / 2, (game.height + height + overflow) / 2, 0xA0000000));

		titleBar = new GuiObjectTitleBar((game.width - width) / 2, (game.height - height) / 2, (game.width + width) / 2, ((game.height - height) / 2) + 30, this.f, "Loading...");
	}

	public void update(String text) {
		titleBar.setText(text);
		game.render(0);
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
		titleBar.render();
	}

	@Override
	public void buttonChanged(GuiObjectChangingButton button, int line) {
		
		
	}

	@Override
	public void onClosing() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOpening() {
		// TODO Auto-generated method stub
		
	}
}
