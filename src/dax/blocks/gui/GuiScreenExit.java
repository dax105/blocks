package dax.blocks.gui;

public class GuiScreenExit extends GuiScreen {

	int width = 400;
	int height = 58;

	int overflow = 8;

	public GuiScreenExit(GuiScreen parent) {
		super(parent);
		objects.add(new GuiObjectRectangle((game.width - width - overflow) / 2, (game.height - height - overflow) / 2, (game.width + width + overflow) / 2, (game.height + height + overflow) / 2, 0xA0000000));

		objects.add(new GuiObjectTitleBar((game.width - width) / 2, (game.height - height) / 2, (game.width + width) / 2, ((game.height - height) / 2) + 30, this.f, "Do you really want to exit?"));

		objects.add(new GuiObjectButton((game.width - width) / 2, (game.height + height) / 2 - 24, (game.width) / 2, ((game.height + height) / 2), this.f, "No", 0, this));
		objects.add(new GuiObjectButton((game.width + 8) / 2, (game.height + height) / 2 - 24, (game.width + width) / 2, ((game.height + height) / 2), this.f, "Yes", 1, this));
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
