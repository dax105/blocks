package dax.blocks.gui;

import dax.blocks.Game;

public class GuiScreenMainMenu extends GuiScreen {

	int width = 400;
	int height = 114;

	int overflow = 8;

	public GuiScreenMainMenu(Game game) {
		super(game);
		objects.add(new GuiObjectRectangle((game.width - width - overflow) / 2, (game.height - height - overflow) / 2, (game.width + width + overflow) / 2, (game.height + height + overflow) / 2, 0xA0000000));

		objects.add(new GuiObjectTitleBar((game.width - width) / 2, (game.height - height) / 2, (game.width + width) / 2, ((game.height - height) / 2) + 30, this.f, "Main menu"));

		objects.add(new GuiObjectButton((game.width - width) / 2, (game.height - height) / 2 + 34, (game.width + width) / 2, ((game.height - height) / 2) + 58, this.f, "Start game", 0, this));
		objects.add(new GuiObjectButton((game.width - width) / 2, (game.height - height) / 2 + 62, (game.width + width) / 2, ((game.height - height) / 2) + 86, this.f, "Options", 1, this));
		objects.add(new GuiObjectButton((game.width - width) / 2, (game.height - height) / 2 + 90, (game.width + width) / 2, ((game.height - height) / 2) + 114, this.f, "Exit", 2, this));
	}

	@Override
	public void buttonPress(GuiObjectButton button) {
		if (button.id == 0) {
			Game.sound.getMusicProvider().startGameMusic();
			game.displayLoadingScreen();
			game.makeNewWorld(true,"this_will_be_changable");
		} else if (button.id == 1) {
			game.openGuiScreen(new GuiScreenSettings(this));
		} else if (button.id == 2) {
			game.openGuiScreen(new GuiScreenExit(this));
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
		Game.sound.getMusicProvider().startMenuMusic();
	}

}
