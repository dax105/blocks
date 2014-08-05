package dax.blocks.gui;

import dax.blocks.Game;
import dax.blocks.render.RenderEngine;
import dax.blocks.settings.Settings;
import dax.blocks.sound.SoundManager;

public class GuiScreenMenu extends GuiScreen {

	int width = 400;
	int height = 142;

	int overflow = 8;

	public GuiScreenMenu(Game game) {
		super(game);
		objects.add(new GuiObjectRectangle((Settings.getInstance().windowWidth.getValue() - width - overflow) / 2, (Settings.getInstance().windowHeight.getValue() - height - overflow) / 2, (Settings.getInstance().windowWidth.getValue() + width + overflow) / 2, (Settings.getInstance().windowHeight.getValue() + height + overflow) / 2, 0xA0000000));

		objects.add(new GuiObjectTitleBar((Settings.getInstance().windowWidth.getValue() - width) / 2, (Settings.getInstance().windowHeight.getValue() - height) / 2, (Settings.getInstance().windowWidth.getValue() + width) / 2, ((Settings.getInstance().windowHeight.getValue() - height) / 2) + 30, this.f, "Menu"));

		objects.add(new GuiObjectButton((Settings.getInstance().windowWidth.getValue() - width) / 2, (Settings.getInstance().windowHeight.getValue() - height) / 2 + 34, (Settings.getInstance().windowWidth.getValue() + width) / 2, ((Settings.getInstance().windowHeight.getValue() - height) / 2) + 58, this.f, "Back to game", 0, this));
		objects.add(new GuiObjectButton((Settings.getInstance().windowWidth.getValue() - width) / 2, (Settings.getInstance().windowHeight.getValue() - height) / 2 + 62, (Settings.getInstance().windowWidth.getValue() + width) / 2, ((Settings.getInstance().windowHeight.getValue() - height) / 2) + 86, this.f, "Regenerate world", 1, this));
		objects.add(new GuiObjectButton((Settings.getInstance().windowWidth.getValue() - width) / 2, (Settings.getInstance().windowHeight.getValue() - height) / 2 + 90, (Settings.getInstance().windowWidth.getValue() + width) / 2, ((Settings.getInstance().windowHeight.getValue() - height) / 2) + 114, this.f, "Options", 2, this));
		objects.add(new GuiObjectButton((Settings.getInstance().windowWidth.getValue() - width) / 2, (Settings.getInstance().windowHeight.getValue() - height) / 2 + 118, (Settings.getInstance().windowWidth.getValue() + width) / 2, ((Settings.getInstance().windowHeight.getValue() - height) / 2) + 142, this.f, "Quit to title", 3, this));
	}

	@Override
	public void buttonPress(GuiObjectButton button) {
		if (button.id == 0) {
			game.closeGuiScreen();
		} else if (button.id == 1) {
			game.displayLoadingScreen();
			game.makeNewWorld(false, game.world.name);
		} else if (button.id == 2) {
			game.openGuiScreen(new GuiScreenSettings(this));
		} else if (button.id == 3) {
			game.world.saveAllChunks();
			game.world = null;
			game.renderEngine = new RenderEngine(Settings.getInstance().shaders.getValue());
			game.ingame = false;
			game.openGuiScreen(new GuiScreenMainMenu(game));
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
		SoundManager.getInstance().playMusic();	
	}

	@Override
	public void onOpening() {
		SoundManager.getInstance().pauseMusic();
	}


}
