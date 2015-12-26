package cz.dat.oots.gui;

import cz.dat.oots.util.GLHelper;

public class GuiScreenSettings extends GuiScreen {
	;
	private int width = 400;
	private int height = 200;
	private int overflow = 8;
	private boolean ingame;

	public GuiScreenSettings(GuiScreen parent) {
		super(parent);
		this.ingame = this.game.getWorldsManager().isInGame();

		// Rectangle
		/*this.objects
				.add(new GuiObjectRectangle(
						(parent.game.s().windowWidth.getValue() - width - overflow) / 2,
						(parent.game.s().windowHeight.getValue() - height - overflow) / 2,
						(parent.game.s().windowWidth.getValue() + width + overflow) / 2,
						(parent.game.s().windowHeight.getValue() + height + overflow) / 2,
						0xA0000000));*/
		this.objects.add(new GuiObjectTitleBar((parent.game.s().windowWidth
				.getValue() - width) / 2, (parent.game.s().windowHeight
				.getValue() - height) / 2, (parent.game.s().windowWidth
				.getValue() + width) / 2, ((parent.game.s().windowHeight
				.getValue() - height) / 2) + 30, this.f, "Options"));

		// Render distance [INTEGER SETTINGS SLIDER][ID 3]
		this.objects.add(new GuiObjectSettingsIntegerSlider(
				(parent.game.s().windowWidth.getValue() - width) / 2,
				(parent.game.s().windowHeight.getValue() - height) / 2 + 34,
				(parent.game.s().windowWidth.getValue() + width) / 2,
				((parent.game.s().windowHeight.getValue() - height) / 2) + 58,
				this.f, "Render distance: %v chunks", 3, this, 2, 30,
				parent.game.s().drawDistance));

		// Sound on/off [SETTINGS BUTTON][ID 4]
		this.objects.add(new GuiObjectSettingsBooleanButton(
				(parent.game.s().windowWidth.getValue() - width) / 2,
				(parent.game.s().windowHeight.getValue() - height) / 2 + 62,
				(parent.game.s().windowWidth.getValue() - width - 8) / 2 + 128,
				(parent.game.s().windowHeight.getValue() - height) / 2 + 86,
				this.f, 4, this, parent.game.s().sound));

		// Sound volume [FLOAT SETTINGS SLIDER][ID 2]
		this.objects.add(new GuiObjectSettingsFloatSlider(
				(parent.game.s().windowWidth.getValue() - width) / 2 + 128,
				(parent.game.s().windowHeight.getValue() - height) / 2 + 62,
				(parent.game.s().windowWidth.getValue() + width) / 2,
				((parent.game.s().windowHeight.getValue() - height) / 2) + 86,
				this.f, "Volume: %v", 2, this, 0f, 1f,
				parent.game.s().soundVolume));

		// Linear filtering [SETTINGS BUTTON][ID 3]
		this.objects.add(new GuiObjectSettingsBooleanButton(
				(parent.game.s().windowWidth.getValue() - width) / 2,
				(parent.game.s().windowHeight.getValue() - height) / 2 + 118,
				parent.game.s().windowWidth.getValue() / 2 - 2, ((parent.game
						.s().windowHeight.getValue() - height) / 2) + 144,
				this.f, 3, this, parent.game.s().linearFiltering));

		// Transparent leaves [SETTINGS BUTTON][ID 2]
		this.objects.add(new GuiObjectSettingsBooleanButton(
				parent.game.s().windowWidth.getValue() / 2 + 2, (parent.game
						.s().windowHeight.getValue() - height) / 2 + 118,
				(parent.game.s().windowWidth.getValue() + width) / 2,
				((parent.game.s().windowHeight.getValue() - height) / 2) + 144,
				this.f, 2, this, parent.game.s().transparentLeaves));

		// Two pass transl. [SETTINGS BUTTON][ID 5]
		this.objects.add(new GuiObjectSettingsBooleanButton(
				(parent.game.s().windowWidth.getValue() - width) / 2,
				(parent.game.s().windowHeight.getValue() - height) / 2 + 148,
				parent.game.s().windowWidth.getValue() / 2 - 2, ((parent.game
						.s().windowHeight.getValue() - height) / 2) + 172,
				this.f, 5, this, parent.game.s().twoPassTranslucent));

		// Peaceful [SETTINGS BUTTON][ID 6]
		this.objects.add(new GuiObjectSettingsBooleanButton(
				parent.game.s().windowWidth.getValue() / 2 + 2, (parent.game
						.s().windowHeight.getValue() - height) / 2 + 148,
				(parent.game.s().windowWidth.getValue() + width) / 2,
				((parent.game.s().windowHeight.getValue() - height) / 2) + 172,
				this.f, 6, this, parent.game.s().peacefulMode));

		// FOV [FLOAT SETTINGS SLIDER][ID 1]
		this.objects.add(new GuiObjectSettingsFloatSlider(
				(parent.game.s().windowWidth.getValue() - width) / 2,
				(parent.game.s().windowHeight.getValue() - height) / 2 + 90,
				(parent.game.s().windowWidth.getValue() + width) / 2,
				((parent.game.s().windowHeight.getValue() - height) / 2) + 114,
				this.f, "FOV: %v", 1, this, 30, 160, parent.game.s().fov));

		// Apply/close [BUTTON][ID 1]
		this.objects.add(new GuiObjectButton((parent.game.s().windowWidth
				.getValue() - width) / 2, (parent.game.s().windowHeight
				.getValue() + height) / 2 - 24, ((parent.game.s().windowWidth
				.getValue() + width) / 2), ((parent.game.s().windowHeight
				.getValue() + height) / 2), this.f, "Close", 1, this));
	}

	@Override
	public void buttonPress(GuiObjectButton button) {
		if(button.id == 1) {
			GLHelper.updateFiltering(parent.game.s().linearFiltering.getValue());

			if(this.ingame) {
				this.close();
			} else {
				this.game.openGuiScreen(super.parent);
			}
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
