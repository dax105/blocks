package dax.blocks.gui;

import dax.blocks.Game;

public class GuiScreenExit extends GuiScreen {

	int width = 400;
	int height = 58;

	int overflow = 8;

	public GuiScreenExit(GuiScreen parent) {
		super(parent);
		objects.add(new GuiObjectRectangle((Game.settings.windowWidth.getValue() - width - overflow) / 2, (Game.settings.windowHeight.getValue() - height - overflow) / 2, (Game.settings.windowWidth.getValue() + width + overflow) / 2, (Game.settings.windowHeight.getValue() + height + overflow) / 2, 0xA0000000));

		objects.add(new GuiObjectTitleBar((Game.settings.windowWidth.getValue() - width) / 2, (Game.settings.windowHeight.getValue() - height) / 2, (Game.settings.windowWidth.getValue() + width) / 2, ((Game.settings.windowHeight.getValue() - height) / 2) + 30, this.f, "Do you really want to exit?"));

		objects.add(new GuiObjectButton((Game.settings.windowWidth.getValue() - width) / 2, (Game.settings.windowHeight.getValue() + height) / 2 - 24, (Game.settings.windowWidth.getValue()) / 2, ((Game.settings.windowHeight.getValue() + height) / 2), this.f, "No", 0, this));
		objects.add(new GuiObjectButton((Game.settings.windowWidth.getValue() + 8) / 2, (Game.settings.windowHeight.getValue() + height) / 2 - 24, (Game.settings.windowWidth.getValue() + width) / 2, ((Game.settings.windowHeight.getValue() + height) / 2), this.f, "Yes", 1, this));
	}

	@Override
	public void buttonPress(GuiObjectButton button) {
		if (button.id == 0) {
			close();
		}

		if (button.id == 1) {
			game.exit();
		}
	}

	@Override
	public void sliderUpdate(GuiObjectSlider slider) {

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
