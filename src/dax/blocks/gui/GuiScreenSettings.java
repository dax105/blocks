package dax.blocks.gui;

import dax.blocks.Game;

public class GuiScreenSettings extends GuiScreen {

	boolean filter;
	int width = 400;
	int height = 200;
	int overflow = 8;
	boolean ingame;

	public GuiScreenSettings(GuiScreen parent) {
		super(parent);
		ingame = game.ingame;
	
		//Rectangle
		objects.add(new GuiObjectRectangle((game.width - width - overflow) / 2, (game.height - height - overflow) / 2, (game.width + width + overflow) / 2, (game.height + height + overflow) / 2, 0xA0000000));
		objects.add(new GuiObjectTitleBar((game.width - width) / 2, (game.height - height) / 2, (game.width + width) / 2, ((game.height - height) / 2) + 30, this.f, "Options"));

		//Render distance [INTEGER SETTINGS SLIDER][ID 3]
		objects.add(new GuiObjectSettingsIntegerSlider((game.width - width) / 2, (game.height - height) / 2 + 34, (game.width + width) / 2, ((game.height - height) / 2) + 58, this.f, "Render distance: %v chunks", 3, this, 2, 30, Game.settings.drawDistance));
		
		//Sound on/off [SETTINGS BUTTON][ID 4]
		objects.add(new GuiObjectSettingsBooleanButton((game.width - width) / 2, (game.height - height) / 2 + 62, (game.width - width - 8) / 2 + 128, (game.height - height) / 2 + 86, this.f, 4, this, Game.settings.sound));
		
		//Sound volume [FLOAT SETTINGS SLIDER][ID 2]
		objects.add(new GuiObjectSettingsFloatSlider((game.width - width) / 2 + 128, (game.height - height) / 2 + 62, (game.width + width) / 2, ((game.height - height) / 2) + 86, this.f, "Volume: %v", 2, this, 0f, 1f, Game.settings.sound_volume));
		
		//Linear filtering [SETTINGS BUTTON][ID 3]
		objects.add(new GuiObjectSettingsBooleanButton((game.width - width) / 2, (game.height - height) / 2 + 118, game.width / 2 - 2, ((game.height - height) / 2) + 144, this.f, 3, this, Game.settings.linear_filtering));
		
		//Transparent leaves [SETTINGS BUTTON][ID 2]
		objects.add(new GuiObjectSettingsBooleanButton(game.width / 2 + 2, (game.height - height) / 2 + 118, (game.width + width) / 2, ((game.height - height) / 2) + 144, this.f, 2, this, Game.settings.transparent_leaves));
		
		//Two pass transl. [SETTINGS BUTTON][ID 5]
		objects.add(new GuiObjectSettingsBooleanButton((game.width - width) / 2, (game.height - height) / 2 + 148, game.width / 2 - 2, ((game.height - height) / 2) + 172, this.f, 5, this, Game.settings.two_pass_translucent));
				
		//Peaceful [SETTINGS BUTTON][ID 6]
		objects.add(new GuiObjectSettingsBooleanButton(game.width / 2 + 2, (game.height - height) / 2 + 148, (game.width + width) / 2, ((game.height - height) / 2) + 172, this.f, 6, this, Game.settings.peaceful_mode));

		//FOV [FLOAT SETTINGS SLIDER][ID 1]
		objects.add(new GuiObjectSettingsFloatSlider((game.width - width) / 2, (game.height - height) / 2 + 90, (game.width + width) / 2, ((game.height - height) / 2) + 114, this.f, "FOV: %v", 1, this, 30, 160, Game.settings.fov));
		
		//Apply/close [BUTTON][ID 1]
		objects.add(new GuiObjectButton((game.width - width) / 2, (game.height + height) / 2 - 24, ((game.width + width) / 2), ((game.height + height) / 2), this.f, "Close", 1, this));
	}

	@Override
	public void buttonPress(GuiObjectButton button) {
		if (button.id == 1) {	
			dax.blocks.GLHelper.updateFiltering(Game.settings.linear_filtering.getValue());
			
			if (ingame) {
				close();
			} else {
				game.openGuiScreen(parent);
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
