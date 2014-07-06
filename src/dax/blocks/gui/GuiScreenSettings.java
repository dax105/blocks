package dax.blocks.gui;

import dax.blocks.TextureManager;

import org.lwjgl.opengl.GL11;

public class GuiScreenSettings extends GuiScreen {

	int worldSize;
	float mult;
	boolean filter;

	int width = 400;
	int height = 200;
	int fov;

	int overflow = 8;

	boolean ingame;
	
	boolean trees;

	public GuiScreenSettings(GuiScreen parent) {
		super(parent);
		ingame = game.ingame;
		worldSize = game.worldSize;
		filter = game.shouldFilter;
		mult = game.heightMultipler;
		fov = game.fov;
		trees = game.treeGen;
		
		String filteringText[] = {"Texture filtering: Linear", "Texture filtering: Nearest"};	
		String treeText[] = {"Tree generator: ON", "Tree generator: OFF"};
	
		objects.add(new GuiObjectRectangle((game.width - width - overflow) / 2, (game.height - height - overflow) / 2, (game.width + width + overflow) / 2, (game.height + height + overflow) / 2, 0xA0000000));

		objects.add(new GuiObjectTitleBar((game.width - width) / 2, (game.height - height) / 2, (game.width + width) / 2, ((game.height - height) / 2) + 30, this.f, "Options"));

		//World size
		objects.add(new GuiObjectSlider((game.width - width) / 2, (game.height - height) / 2 + 34, (game.width + width) / 2, ((game.height - height) / 2) + 58, this.f, "World size: ", 1, this, 4, 120, worldSize, " chunks"));
		
		//Height multiplier
		objects.add(new GuiObjectSlider((game.width - width) / 2, (game.height - height) / 2 + 62, (game.width + width) / 2, ((game.height - height) / 2) + 86, this.f, "World height multipler: ", 2, this, 0, 100, (int) mult, ""));

		//Texture filtering
		objects.add(new GuiObjectChangingButton((game.width - width) / 2, (game.height - height) / 2 + 118, (game.width + width) / 2, ((game.height - height) / 2) + 144, this.f, filteringText, filter ? 0 : 1, 1, this));
		
		//Tree generation
		objects.add(new GuiObjectChangingButton((game.width - width) / 2, (game.height - height) / 2 + 148, (game.width + width) / 2, ((game.height - height) / 2) + 172, this.f, treeText, trees ? 0 : 1, 2, this));
		
		//FOV
		objects.add(new GuiObjectSlider((game.width - width) / 2, (game.height - height) / 2 + 90, (game.width + width) / 2, ((game.height - height) / 2) + 114, this.f, "FOV: ", 3, this, 30, 160, fov, ""));

		
		objects.add(new GuiObjectButton((game.width - width) / 2, (game.height + height) / 2 - 24, ((game.width + width) / 2 - (width / 2)), ((game.height + height) / 2), this.f, "Close", 3, this));
		objects.add(new GuiObjectButton(((game.width + width) / 2 - (width / 2)), ((game.height + height) / 2) - 24, (game.width + width) / 2, ((game.height + height) / 2), this.f, "Apply", 4, this));

		//Button IDs: 1 - filtering (ch); 2 - tree gen (ch); 3 - close; 4 - apply;
		//Slider IDs: 1 - world size; 2 - multiplier; 3 - FOV
		
		//objects.add(new GuiObjectText((game.width - width) / 2, (game.height - height) / 2 + 34, (game.width + width) / 2, (game.height + height) / 2 - 28, this.f, "Nope, nothing's here, use console"));
	}

	@Override
	public void buttonPress(GuiObjectButton button) {
		if (button.id == 3) {
			close();
		}

		if (button.id == 4) {

			game.heightMultipler = mult;
			game.shouldFilter = filter;
			game.treeGen = trees;

			TextureManager.atlas.bind();
			if (game.shouldFilter) {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			} else {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			}

			game.worldSize = worldSize;
			game.fov = fov;

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
			this.mult = slider.val;
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
