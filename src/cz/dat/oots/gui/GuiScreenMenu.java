package cz.dat.oots.gui;

import cz.dat.oots.Game;
import cz.dat.oots.sound.SoundManager;

public class GuiScreenMenu extends GuiScreen {

	private int width = 400;
	private int height = 142;
	private int overflow = 8;

	public GuiScreenMenu(Game game) {
		super(game);
		this.objects.add(new GuiObjectRectangle((game.s().windowWidth
				.getValue() - width - overflow) / 2, (game.s().windowHeight
				.getValue() - height - overflow) / 2, (game.s().windowWidth
				.getValue() + width + overflow) / 2, (game.s().windowHeight
				.getValue() + height + overflow) / 2, 0xA0000000));

		this.objects.add(new GuiObjectTitleBar(
				(game.s().windowWidth.getValue() - width) / 2,
				(game.s().windowHeight.getValue() - height) / 2,
				(game.s().windowWidth.getValue() + width) / 2,
				((game.s().windowHeight.getValue() - height) / 2) + 30, this.f,
				"Menu"));

		this.objects.add(new GuiObjectButton(
				(game.s().windowWidth.getValue() - width) / 2,
				(game.s().windowHeight.getValue() - height) / 2 + 34,
				(game.s().windowWidth.getValue() + width) / 2,
				((game.s().windowHeight.getValue() - height) / 2) + 58, this.f,
				"Back to game", 0, this));

		/*
		 * this.objects.add(new GuiObjectButton(
		 * (game.s().windowWidth.getValue() - width) / 2,
		 * (game.s().windowHeight.getValue() - height) / 2 + 62,
		 * (game.s().windowWidth.getValue() + width) / 2,
		 * ((game.s().windowHeight.getValue() - height) / 2) + 86, this.f,
		 * "Regenerate world", 1, this) );
		 */

		this.objects.add(new GuiObjectButton(
				(game.s().windowWidth.getValue() - width) / 2,
				(game.s().windowHeight.getValue() - height) / 2 + 90,
				(game.s().windowWidth.getValue() + width) / 2,
				((game.s().windowHeight.getValue() - height) / 2) + 114,
				this.f, "Options", 2, this));

		this.objects.add(new GuiObjectButton(
				(game.s().windowWidth.getValue() - width) / 2,
				(game.s().windowHeight.getValue() - height) / 2 + 118, (game
						.s().windowWidth.getValue() + width) / 2,
				((game.s().windowHeight.getValue() - height) / 2) + 142,
				this.f, "Quit to title", 3, this));
	}

	@Override
	public void buttonPress(GuiObjectButton button) {

		if(button.id == 0) {
			game.closeGuiScreen();
		} else if(button.id == 1) {
		} else if(button.id == 2) {
			game.openGuiScreen(new GuiScreenSettings(this));
		} else if(button.id == 3) {
			this.game.getWorldsManager().exitWorld();
			this.game.openGuiScreen(new GuiScreenMainMenu(this.game));
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
