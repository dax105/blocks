package dax.blocks.gui;

import dax.blocks.TextureManager;

import org.lwjgl.opengl.GL11;

public class GuiScreenSettings extends GuiScreen {

	int worldSize;
	float mult;
	boolean filter;

	int width = 400;
	int height = 142;

	int overflow = 8;

	boolean ingame;

	public GuiScreenSettings(GuiScreen parent) {
		super(parent);
		ingame = game.isIngame;
		worldSize = game.worldSize;
		filter = game.shouldFilter;
		mult = game.heightMultipler;

		String filteringText;
		if (filter) {
			filteringText = "Texture filtering: Linear";
		} else {
			filteringText = "Texture filtering: Nearest";
		}

		objects.add(new GuiObjectRectangle((game.width - width - overflow) / 2, (game.height - height - overflow) / 2, (game.width + width + overflow) / 2, (game.height + height + overflow) / 2, 0xA0000000));

		objects.add(new GuiObjectTitleBar((game.width - width) / 2, (game.height - height) / 2, (game.width + width) / 2, ((game.height - height) / 2) + 30, this.f, "Options"));

		objects.add(new GuiObjectSlider((game.width - width) / 2, (game.height - height) / 2 + 34, (game.width + width) / 2, ((game.height - height) / 2) + 58, this.f, "World size: ", 0, this, 4, 40, worldSize, " chunks"));
		
		objects.add(new GuiObjectSlider((game.width - width) / 2, (game.height - height) / 2 + 62, (game.width + width) / 2, ((game.height - height) / 2) + 86, this.f, "World height multipler: ", 1, this, 0, 100, (int) mult, ""));

		objects.add(new GuiObjectButton((game.width - width) / 2, (game.height - height) / 2 + 90, (game.width + width) / 2, ((game.height - height) / 2) + 114, this.f, filteringText, 3, this));

		objects.add(new GuiObjectButton((game.width - width) / 2, (game.height + height) / 2 - 24, (game.width) / 2, ((game.height + height) / 2), this.f, "Cancel", 1, this));
		
		if (ingame) {
			objects.add(new GuiObjectButton((game.width + 8) / 2, (game.height + height) / 2 - 24, (game.width + width) / 2, ((game.height + height) / 2), this.f, "Apply (Regenerate world)", 2, this));
		} else {
			objects.add(new GuiObjectButton((game.width + 8) / 2, (game.height + height) / 2 - 24, (game.width + width) / 2, ((game.height + height) / 2), this.f, "Apply", 2, this));
		}
	}

	@Override
	public void buttonPress(GuiObjectButton button) {
		if (button.id == 1) {
			close();
		}

		if (button.id == 2) {

			game.heightMultipler = mult;
			game.shouldFilter = filter;

			TextureManager.atlas.bind();
			if (game.shouldFilter) {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			} else {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			}

			game.worldSize = worldSize;

			if (ingame) {
				game.displayLoadingScreen();
				game.makeNewWorld();
			} else {
				game.openGuiScreen(parent);
			}
		}

		if (button.id == 3) {
			if (filter) {
				button.text = "Texture filtering: Nearest";
				TextureManager.texel_offset = 0;
				filter = false;
			} else {
				button.text = "Texture filtering: Linear";
				TextureManager.calculateTexOffset();
				filter = true;
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
	}

}
