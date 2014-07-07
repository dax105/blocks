package dax.blocks.gui;

import dax.blocks.Game;

public class GuiScreenSettings extends GuiScreen {

	boolean filter;
	int width = 400;
	int height = 200;

	int overflow = 8;

	boolean ingame;
	boolean sound;
	boolean trees;

	public GuiScreenSettings(GuiScreen parent) {
		super(parent);
		ingame = game.ingame;
		
		filter = Game.settings.linear_filtering.getValue();
		trees = Game.settings.tree_generation.getValue();
		sound = Game.settings.sound.getValue();
		
		String filteringText[] = {"Texture filtering: Linear", "Texture filtering: Nearest"};	
		String treeText[] = {"Tree generator: ON", "Tree generator: OFF"};
		String soundText[] = {"Sound: ON", "Sound: OFF"};
	
		//Rectangle
		objects.add(new GuiObjectRectangle((game.width - width - overflow) / 2, (game.height - height - overflow) / 2, (game.width + width + overflow) / 2, (game.height + height + overflow) / 2, 0xA0000000));
		objects.add(new GuiObjectTitleBar((game.width - width) / 2, (game.height - height) / 2, (game.width + width) / 2, ((game.height - height) / 2) + 30, this.f, "Options"));

		//Render distance [INTEGER SETTINGS SLIDER][ID 3]
		objects.add(new GuiObjectSettingsIntegerSlider((game.width - width) / 2, (game.height - height) / 2 + 34, (game.width + width) / 2, ((game.height - height) / 2) + 58, this.f, "Render distance: %v chunks", 3, this, 2, 30, Game.settings.drawDistance));
		
		//Sound on/off [CHANGING BUTTON][ID 4]
		objects.add(new GuiObjectChangingButton((game.width - width) / 2, (game.height - height) / 2 + 62, (game.width - width - 8) / 2 + 128, (game.height - height) / 2 + 86, this.f, soundText, sound ? 0 : 1, 4, this));
		
		//Sound volume [FLOAT SETTINGS SLIDER][ID 2]
		objects.add(new GuiObjectSettingsFloatSlider((game.width - width) / 2 + 128, (game.height - height) / 2 + 62, (game.width + width) / 2, ((game.height - height) / 2) + 86, this.f, "Volume: %v", 2, this, 0f, 1f, Game.settings.sound_volume));
		
		//Texture filtering [CHANGING BUTTON][ID 3]
		objects.add(new GuiObjectChangingButton((game.width - width) / 2, (game.height - height) / 2 + 118, (game.width + width) / 2, ((game.height - height) / 2) + 144, this.f, filteringText, filter ? 0 : 1, 3, this));
		
		//Tree generation [CHANGING BUTTON][ID 2]
		objects.add(new GuiObjectChangingButton((game.width - width) / 2, (game.height - height) / 2 + 148, (game.width + width) / 2, ((game.height - height) / 2) + 172, this.f, treeText, trees ? 0 : 1, 2, this));
		
		//FOV [FLOAT SETTINGS SLIDER][ID 1]
		objects.add(new GuiObjectSettingsFloatSlider((game.width - width) / 2, (game.height - height) / 2 + 90, (game.width + width) / 2, ((game.height - height) / 2) + 114, this.f, "FOV: %v", 1, this, 30, 160, Game.settings.fov));
		
		//Apply/close [BUTTON][ID 1]
		objects.add(new GuiObjectButton((game.width - width) / 2, (game.height + height) / 2 - 24, ((game.width + width) / 2), ((game.height + height) / 2), this.f, "Close", 1, this));
	}

	@Override
	public void buttonPress(GuiObjectButton button) {

		if (button.id == 1) {
			
			Game.settings.linear_filtering.setValue(filter);
			Game.settings.tree_generation.setValue(trees);
			Game.settings.sound.setValue(sound);

			Game.sound.actualizeVolume();
			
			game.updateFiltering();
			
			if (ingame) {
				//game.displayLoadingScreen();
				//game.makeNewWorld(true, game.world.name);
				close();
			} else {
				game.openGuiScreen(parent);
			}
		}


	}
	
	@Override
	public void sliderUpdate(GuiObjectSlider slider) {
		switch(slider.id) {
		case 1:
			//TODO FOV slider
			break;
		case 2:
			Game.sound.actualizeVolume();
			break;
		case 3:
			//TODO Render distance slider
			break;
		}
	}

	@Override
	public void buttonChanged(GuiObjectChangingButton button, int line) {
		switch(button.id) {
		case 3:
			filter = (button.getCurrentLine() == 0); 
			break;
		case 2:
			trees = (button.getCurrentLine() == 0);
			break;
		case 4:
			sound = (button.getCurrentLine() == 0);
			break;
		}
	}

}
