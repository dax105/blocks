package dax.blocks.gui;

import dax.blocks.settings.Settings;

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
		objects.add(new GuiObjectRectangle((Settings.getInstance().windowWidth.getValue() - width - overflow) / 2, (Settings.getInstance().windowHeight.getValue() - height - overflow) / 2, (Settings.getInstance().windowWidth.getValue() + width + overflow) / 2, (Settings.getInstance().windowHeight.getValue() + height + overflow) / 2, 0xA0000000));
		objects.add(new GuiObjectTitleBar((Settings.getInstance().windowWidth.getValue() - width) / 2, (Settings.getInstance().windowHeight.getValue() - height) / 2, (Settings.getInstance().windowWidth.getValue() + width) / 2, ((Settings.getInstance().windowHeight.getValue() - height) / 2) + 30, this.f, "Options"));

		//Render distance [INTEGER SETTINGS SLIDER][ID 3]
		objects.add(new GuiObjectSettingsIntegerSlider((Settings.getInstance().windowWidth.getValue() - width) / 2, (Settings.getInstance().windowHeight.getValue() - height) / 2 + 34, (Settings.getInstance().windowWidth.getValue() + width) / 2, ((Settings.getInstance().windowHeight.getValue() - height) / 2) + 58, this.f, "Render distance: %v chunks", 3, this, 2, 30, Settings.getInstance().drawDistance));
		
		//Sound on/off [SETTINGS BUTTON][ID 4]
		objects.add(new GuiObjectSettingsBooleanButton((Settings.getInstance().windowWidth.getValue() - width) / 2, (Settings.getInstance().windowHeight.getValue() - height) / 2 + 62, (Settings.getInstance().windowWidth.getValue() - width - 8) / 2 + 128, (Settings.getInstance().windowHeight.getValue() - height) / 2 + 86, this.f, 4, this, Settings.getInstance().sound));
		
		//Sound volume [FLOAT SETTINGS SLIDER][ID 2]
		objects.add(new GuiObjectSettingsFloatSlider((Settings.getInstance().windowWidth.getValue() - width) / 2 + 128, (Settings.getInstance().windowHeight.getValue() - height) / 2 + 62, (Settings.getInstance().windowWidth.getValue() + width) / 2, ((Settings.getInstance().windowHeight.getValue() - height) / 2) + 86, this.f, "Volume: %v", 2, this, 0f, 1f, Settings.getInstance().soundVolume));
		
		//Linear filtering [SETTINGS BUTTON][ID 3]
		objects.add(new GuiObjectSettingsBooleanButton((Settings.getInstance().windowWidth.getValue() - width) / 2, (Settings.getInstance().windowHeight.getValue() - height) / 2 + 118, Settings.getInstance().windowWidth.getValue() / 2 - 2, ((Settings.getInstance().windowHeight.getValue() - height) / 2) + 144, this.f, 3, this, Settings.getInstance().linearFiltering));
		
		//Transparent leaves [SETTINGS BUTTON][ID 2]
		objects.add(new GuiObjectSettingsBooleanButton(Settings.getInstance().windowWidth.getValue() / 2 + 2, (Settings.getInstance().windowHeight.getValue() - height) / 2 + 118, (Settings.getInstance().windowWidth.getValue() + width) / 2, ((Settings.getInstance().windowHeight.getValue() - height) / 2) + 144, this.f, 2, this, Settings.getInstance().transparentLeaves));
		
		//Two pass transl. [SETTINGS BUTTON][ID 5]
		objects.add(new GuiObjectSettingsBooleanButton((Settings.getInstance().windowWidth.getValue() - width) / 2, (Settings.getInstance().windowHeight.getValue() - height) / 2 + 148, Settings.getInstance().windowWidth.getValue() / 2 - 2, ((Settings.getInstance().windowHeight.getValue() - height) / 2) + 172, this.f, 5, this, Settings.getInstance().twoPassTranslucent));
				
		//Peaceful [SETTINGS BUTTON][ID 6]
		objects.add(new GuiObjectSettingsBooleanButton(Settings.getInstance().windowWidth.getValue() / 2 + 2, (Settings.getInstance().windowHeight.getValue() - height) / 2 + 148, (Settings.getInstance().windowWidth.getValue() + width) / 2, ((Settings.getInstance().windowHeight.getValue() - height) / 2) + 172, this.f, 6, this, Settings.getInstance().peacefulMode));

		//FOV [FLOAT SETTINGS SLIDER][ID 1]
		objects.add(new GuiObjectSettingsFloatSlider((Settings.getInstance().windowWidth.getValue() - width) / 2, (Settings.getInstance().windowHeight.getValue() - height) / 2 + 90, (Settings.getInstance().windowWidth.getValue() + width) / 2, ((Settings.getInstance().windowHeight.getValue() - height) / 2) + 114, this.f, "FOV: %v", 1, this, 30, 160, Settings.getInstance().fov));
		
		//Apply/close [BUTTON][ID 1]
		objects.add(new GuiObjectButton((Settings.getInstance().windowWidth.getValue() - width) / 2, (Settings.getInstance().windowHeight.getValue() + height) / 2 - 24, ((Settings.getInstance().windowWidth.getValue() + width) / 2), ((Settings.getInstance().windowHeight.getValue() + height) / 2), this.f, "Close", 1, this));
	}

	@Override
	public void buttonPress(GuiObjectButton button) {
		if (button.id == 1) {	
			dax.blocks.GLHelper.updateFiltering(Settings.getInstance().linearFiltering.getValue());
			
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
