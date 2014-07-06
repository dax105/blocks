package dax.blocks.gui;

import dax.blocks.Game;
import dax.blocks.TextureManager;

import org.lwjgl.opengl.GL11;

public class GuiScreenSettings extends GuiScreen {

	int worldSize;
	boolean filter;

	int width = 400;
	int height = 200;
	float fov;

	int overflow = 8;

	boolean ingame;
	
	boolean trees;

	public GuiScreenSettings(GuiScreen parent) {
		super(parent);
		ingame = game.ingame;
		
		filter = Game.settings.linear_filtering.getValue();
		worldSize = Game.settings.world_size.getValue();
		fov = Game.settings.fov.getValue();
		trees = Game.settings.tree_generation.getValue();
		
		String filteringText[] = {"Texture filtering: Linear", "Texture filtering: Nearest"};	
		String treeText[] = {"Tree generator: ON", "Tree generator: OFF"};
	
		objects.add(new GuiObjectRectangle((game.width - width - overflow) / 2, (game.height - height - overflow) / 2, (game.width + width + overflow) / 2, (game.height + height + overflow) / 2, 0xA0000000));

		objects.add(new GuiObjectTitleBar((game.width - width) / 2, (game.height - height) / 2, (game.width + width) / 2, ((game.height - height) / 2) + 30, this.f, "Options"));

		//World size
		objects.add(new GuiObjectSlider((game.width - width) / 2, (game.height - height) / 2 + 34, (game.width + width) / 2, ((game.height - height) / 2) + 58, this.f, "World size: ", 1, this, 4, 120, worldSize, " chunks"));
		
		//Hole after multiplier
		objects.add(new GuiObjectRectangle((game.width - width) / 2, (game.height - height) / 2 + 62, (game.width + width) / 2, ((game.height - height) / 2) + 86, 0xA0000000));
		
		
		//Texture filtering
		objects.add(new GuiObjectChangingButton((game.width - width) / 2, (game.height - height) / 2 + 118, (game.width + width) / 2, ((game.height - height) / 2) + 144, this.f, filteringText, filter ? 0 : 1, 1, this));
		
		//Tree generation
		objects.add(new GuiObjectChangingButton((game.width - width) / 2, (game.height - height) / 2 + 148, (game.width + width) / 2, ((game.height - height) / 2) + 172, this.f, treeText, trees ? 0 : 1, 2, this));
		
		//FOV
		objects.add(new GuiObjectSlider((game.width - width) / 2, (game.height - height) / 2 + 90, (game.width + width) / 2, ((game.height - height) / 2) + 114, this.f, "FOV: ", 3, this, 30, 160, (int) fov, ""));

		
		objects.add(new GuiObjectButton((game.width - width) / 2, (game.height + height) / 2 - 24, ((game.width + width) / 2 - (width / 2)), ((game.height + height) / 2), this.f, "Close", 3, this));
		objects.add(new GuiObjectButton(((game.width + width) / 2 - (width / 2)), ((game.height + height) / 2) - 24, (game.width + width) / 2, ((game.height + height) / 2), this.f, "Apply", 4, this));

		//Button IDs: 1 - filtering (ch); 2 - tree gen (ch); 3 - close; 4 - apply;
		//Slider IDs: 1 - world size; 2 - xxx; 3 - FOV
		
		//objects.add(new GuiObjectText((game.width - width) / 2, (game.height - height) / 2 + 34, (game.width + width) / 2, (game.height + height) / 2 - 28, this.f, "Nope, nothing's here, use console"));
	}

	@Override
	public void buttonPress(GuiObjectButton button) {
		if (button.id == 3) {
			close();
		}

		if (button.id == 4) {
			
			Game.settings.linear_filtering.setValue(filter);
			Game.settings.world_size.setValue(worldSize);
			Game.settings.tree_generation.setValue(trees);
			Game.settings.fov.setValue(fov);

			game.updateFiltering();
			
			if (ingame) {
				game.displayLoadingScreen();
				game.makeNewWorld(false);
			} else {
				game.openGuiScreen(parent);
			}
		}


	}
	
	@Override
	public void sliderUpdate(GuiObjectSlider slider) {
		switch(slider.id) {
		case 1:
			this.worldSize = slider.val;
			break;
		case 2:
			//TODO
			break;
		case 3:
			this.fov = slider.val;
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
		}
	}

}
