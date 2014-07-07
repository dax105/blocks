package dax.blocks.gui;

import dax.blocks.Game;
import dax.blocks.SoundManager;
import dax.blocks.TextureManager;

import org.lwjgl.opengl.GL11;

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
	
		objects.add(new GuiObjectRectangle((game.width - width - overflow) / 2, (game.height - height - overflow) / 2, (game.width + width + overflow) / 2, (game.height + height + overflow) / 2, 0xA0000000));

		objects.add(new GuiObjectTitleBar((game.width - width) / 2, (game.height - height) / 2, (game.width + width) / 2, ((game.height - height) / 2) + 30, this.f, "Options"));

		//Render distance
		objects.add(new GuiObjectSettingsIntegerSlider((game.width - width) / 2, (game.height - height) / 2 + 34, (game.width + width) / 2, ((game.height - height) / 2) + 58, this.f, "Render distance: %v chunks", 3, this, 5, 80, Game.settings.drawDistance));
		
		//Hole after multiplier
		//objects.add(new GuiObjectRectangle((game.width - width) / 2, (game.height - height) / 2 + 62, (game.width + width) / 2, ((game.height - height) / 2) + 86, 0xA0051B7F));
		objects.add(new GuiObjectChangingButton((game.width - width) / 2, (game.height - height) / 2 + 62, (game.width - width) / 2 + 128, (game.height - height) / 2 + 86, this.f, soundText, sound ? 0 : 1, 5, this));
		objects.add(new GuiObjectSettingsFloatSlider((game.width - width) / 2 + 128, (game.height - height) / 2 + 62, (game.width + width) / 2, ((game.height - height) / 2) + 86, this.f, "Volume: %v", 2, this, 0f, 1f, Game.settings.sound_volume));
		
		//Texture filtering
		objects.add(new GuiObjectChangingButton((game.width - width) / 2, (game.height - height) / 2 + 118, (game.width + width) / 2, ((game.height - height) / 2) + 144, this.f, filteringText, filter ? 0 : 1, 1, this));
		
		//Tree generation
		objects.add(new GuiObjectChangingButton((game.width - width) / 2, (game.height - height) / 2 + 148, (game.width + width) / 2, ((game.height - height) / 2) + 172, this.f, treeText, trees ? 0 : 1, 2, this));
		
		//FOV
		objects.add(new GuiObjectSettingsFloatSlider((game.width - width) / 2, (game.height - height) / 2 + 90, (game.width + width) / 2, ((game.height - height) / 2) + 114, this.f, "FOV: %v", 3, this, 30, 160, Game.settings.fov));
		
		//Apply, close
		objects.add(new GuiObjectButton((game.width - width) / 2, (game.height + height) / 2 - 24, ((game.width + width) / 2 - (width / 2)), ((game.height + height) / 2), this.f, "Close", 3, this));
		objects.add(new GuiObjectButton(((game.width + width) / 2 - (width / 2)), ((game.height + height) / 2) - 24, (game.width + width) / 2, ((game.height + height) / 2), this.f, "Apply", 4, this));

		//Button IDs: 1 - filtering (ch); 2 - tree gen (ch); 3 - close; 4 - apply; 5 - sound on/off (ch)
		//Slider IDs: 1 - xxx; 2 - volume; 3 - FOV
		
		//objects.add(new GuiObjectText((game.width - width) / 2, (game.height - height) / 2 + 34, (game.width + width) / 2, (game.height + height) / 2 - 28, this.f, "Nope, nothing's here, use console"));
	}

	@Override
	public void buttonPress(GuiObjectButton button) {
		if (button.id == 3) {
			close();
		}

		if (button.id == 4) {
			
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
			//TODO
			break;
		case 2:
			Game.sound.actualizeVolume();
			break;
		case 3:
			//TODO
			break;
		}
	}

	@Override
	public void buttonChanged(GuiObjectChangingButton button, int line) {
		switch(button.id) {
		case 1:
			filter = (button.getCurrentLine() == 0); 
			break;
		case 2:
			trees = (button.getCurrentLine() == 0);
			break;
		case 5:
			sound = (button.getCurrentLine() == 0);
			break;
		}
	}

}
