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
		ingame = base.isIngame;
		worldSize = base.worldSize;
		filter = base.shouldFilter;
		mult = base.heightMultipler;
		String worldSizeText;	
		if (worldSize == 4) {
			worldSizeText = "World size: small";
		} else if (worldSize == 8){
			worldSizeText = "World size: medium";
		} else {
			worldSizeText = "World size: large";
		}
		
		String filteringText;
		if (filter) {
			filteringText = "Texture filtering: Linear";
		} else {
			filteringText = "Texture filtering: Nearest";
		}
		
		String multText;
		if (mult == 20) {
			multText = "Height multipler: low";
		} else if (mult == 40){
			multText = "Height multipler: medium";
		} else {
			multText = "Height multipler: high";
		}
		
		
		objects.add(new GuiObjectRectangle((base.width-width-overflow)/2, (base.height-height-overflow)/2, (base.width+width+overflow)/2, (base.height+height+overflow)/2, 0xA0000000));
		
		objects.add(new GuiObjectTitleBar((base.width-width)/2, (base.height-height)/2, (base.width+width)/2, ((base.height-height)/2)+30, this.f, "Options"));
		objects.add(new GuiObjectButton((base.width-width)/2, (base.height-height)/2+34, (base.width+width)/2, ((base.height-height)/2)+58, this.f, worldSizeText, 0, this));
		
		objects.add(new GuiObjectButton((base.width-width)/2, (base.height-height)/2+62, (base.width+width)/2, ((base.height-height)/2)+86, this.f, multText, 4, this));
		
		objects.add(new GuiObjectButton((base.width-width)/2, (base.height-height)/2+90, (base.width+width)/2, ((base.height-height)/2)+114, this.f, filteringText, 3, this));
		
		
		objects.add(new GuiObjectButton((base.width-width)/2, (base.height+height)/2-24, (base.width)/2, ((base.height+height)/2), this.f, "Cancel", 1, this));
		if (ingame) {
			objects.add(new GuiObjectButton((base.width+8)/2, (base.height+height)/2-24, (base.width+width)/2, ((base.height+height)/2), this.f, "Apply (Regenerate world)", 2, this));
		} else {
			objects.add(new GuiObjectButton((base.width+8)/2, (base.height+height)/2-24, (base.width+width)/2, ((base.height+height)/2), this.f, "Apply", 2, this));
		}
	}
		

	@Override
	public void buttonPress(GuiObjectButton button) {
		if (button.id == 4) {
			if (mult == 20) {
				mult = 40;
				button.text = "Height multipler: medium";
			} else if (mult == 40){
				mult = 60;
				button.text = "Height multipler: high";
			} else {
				mult = 20;
				button.text = "Height multipler: low";
			}
		}
		
		
		if (button.id == 0) {
			if (worldSize == 4) {
				worldSize = 8;
				button.text = "World size: medium";
			} else if (worldSize == 8){
				worldSize = 16;
				button.text = "World size: large";
			} else {
				worldSize = 4;
				button.text = "World size: small";
			}
		}
		
		
		if (button.id == 1) {
			close();
		}
		
		if (button.id == 2) {			
			
			base.heightMultipler = mult;
			base.shouldFilter = filter;			
		
			TextureManager.atlas.bind();
			if (base.shouldFilter) {	
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			} else {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			}
		
			base.worldSize = worldSize;
			
			if (ingame) {
	            base.displayLoadingScreen();
	            base.makeNewWorld();
			} else {
				base.openGuiScreen(parent);
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

}
