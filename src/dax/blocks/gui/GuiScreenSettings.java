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

		String filteringText;
		if (filter) {
			filteringText = "Texture filtering: Linear";
		} else {
			filteringText = "Texture filtering: Nearest";
		}
		
		String treeText;
		if (trees) {
			treeText = "Tree generator: ON";
		} else {
			treeText = "Tree generator: OFF";
		}

		//TODO SROVNAT
		
		objects.add(new GuiObjectRectangle((game.width - width - overflow) / 2, (game.height - height - overflow) / 2, (game.width + width + overflow) / 2, (game.height + height + overflow) / 2, 0xA0000000));

		objects.add(new GuiObjectTitleBar((game.width - width) / 2, (game.height - height) / 2, (game.width + width) / 2, ((game.height - height) / 2) + 30, this.f, "Options"));

		//objects.add(new GuiObjectSlider((game.width - width) / 2, (game.height - height) / 2 + 34, (game.width + width) / 2, ((game.height - height) / 2) + 58, this.f, "World size: ", 0, this, 4, 120, worldSize, " chunks"));
		
		//objects.add(new GuiObjectSlider((game.width - width) / 2, (game.height - height) / 2 + 62, (game.width + width) / 2, ((game.height - height) / 2) + 86, this.f, "World height multipler: ", 1, this, 0, 100, (int) mult, ""));

		//objects.add(new GuiObjectButton((game.width - width) / 2, (game.height - height) / 2 + 118, (game.width + width) / 2, ((game.height - height) / 2) + 144, this.f, filteringText, 3, this));
		
		//objects.add(new GuiObjectButton((game.width - width) / 2, (game.height - height) / 2 + 148, (game.width + width) / 2, ((game.height - height) / 2) + 172, this.f, treeText, 4, this));
		
		//objects.add(new GuiObjectSlider((game.width - width) / 2, (game.height - height) / 2 + 90, (game.width + width) / 2, ((game.height - height) / 2) + 114, this.f, "FOV: ", 2, this, 30, 160, fov, ""));

		objects.add(new GuiObjectButton((game.width - width) / 2, (game.height + height) / 2 - 24, (game.width + width) / 2, ((game.height + height) / 2), this.f, "Close", 1, this));
		
		objects.add(new GuiObjectText((game.width - width) / 2, (game.height - height) / 2 + 34, (game.width + width) / 2, (game.height + height) / 2 - 28, this.f, "Nope, nothing's here, use console"));
		
		/*if (ingame) {
			objects.add(new GuiObjectButton((game.width + 8) / 2, (game.height + height) / 2 - 24, (game.width + width) / 2, ((game.height + height) / 2), this.f, "Apply (Regenerate world)", 2, this));
		} else {
			objects.add(new GuiObjectButton((game.width + 8) / 2, (game.height + height) / 2 - 24, (game.width + width) / 2, ((game.height + height) / 2), this.f, "Apply", 2, this));
		}*/
	}

	@Override
	public void buttonPress(GuiObjectButton button) {
		if (button.id == 1) {
			close();
		}

		if (button.id == 2) {

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

		if (button.id == 3) {
			if (filter) {
				button.text = "Texture filtering: Nearest";
				filter = false;
			} else {
				button.text = "Texture filtering: Linear";
				filter = true;
			}
		}
		
		if (button.id == 4) {
			if (!trees) {
				button.text = "Tree generator: ON";
				trees = true;
			} else {
				button.text = "Tree generator: OFF";
				trees = false;
			}
		}

	}
	
	@Override
	public void sliderUpdate(GuiObjectSlider slider) {
		if (slider.id == 0) {
			this.worldSize = slider.val;
		}
		
		if (slider.id == 1) {
			this.mult = slider.val;
		}
		
		if (slider.id == 2) {
			this.fov = slider.val;
		}
	}

}
