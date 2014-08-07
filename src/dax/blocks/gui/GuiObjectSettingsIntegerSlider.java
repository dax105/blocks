package dax.blocks.gui;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Font;

import dax.blocks.settings.SettingsObject;

public class GuiObjectSettingsIntegerSlider extends GuiObjectSlider {
	private SettingsObject<Integer> object;
	
	public GuiObjectSettingsIntegerSlider(int x1, int y1, int x2, int y2,
			Font font, String formatString, int id, GuiScreen parent,
			float minVal, float maxVal, SettingsObject<Integer> object) {
		super(x1, y1, x2, y2, font, formatString, id, parent, minVal, maxVal,
				object.getValue());

		this.object = object;
	}

	@Override
	public void render() {
		this.drawRect(this.x1, this.y1, this.x2, this.y2, 0xFF8C8C8C);
		this.drawRect(this.x1 + 2, this.y1 + 2, this.x2 - 2, this.y2 - 2, 0xFF404040);
		
		int x = this.x1 + GuiObjectSlider.SLIDER_WIDTH / 2 + 2 + 
			(int)Math.round(
					((float)(this.val - this.minVal) / (this.maxVal - this.minVal)) * 
					((x2 - x1) - GuiObjectSlider.SLIDER_WIDTH - 4)
			);
		
		this.drawRect(
				x - GuiObjectSlider.SLIDER_WIDTH / 2 , 
				this.y1 + 2, 
				x + GuiObjectSlider.SLIDER_WIDTH / 2 , 
				this.y2 - 2, 0xFF6E6E6E
		);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		String toDraw = formatString.replace("%v", (int)this.val + "");
		this.font.drawString(
				this.x1 + (this.x2 - this.x1) / 2 - this.font.getWidth(toDraw) / 2, 
				this.y1 + (this.y2 - this.y1) / 2 - this.font.getHeight(toDraw) / 2, 
				toDraw
		);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		if(this.hover) {
			this.drawRect(
					x - GuiObjectSlider.SLIDER_WIDTH / 2, 
					this.y1 + 2, 
					x + GuiObjectSlider.SLIDER_WIDTH / 2, 
					this.y2 - 2, 
					0xA0FFFFFF
			);
		}
	}
	
	@Override
	public void update() {
		super.update();
		
		this.object.setValue((int)this.val);
	}
}
