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
		drawRect(x1, y1, x2, y2, 0xFF8C8C8C);
		drawRect(x1 + 2, y1 + 2, x2 - 2, y2 - 2, 0xFF404040);
		
		int x = x1 + SLIDER_WIDTH/2 +2 + (int)Math.round(((float)(val-minVal) / (maxVal-minVal))*((x2-x1)-SLIDER_WIDTH-4));
		
		drawRect(x - SLIDER_WIDTH / 2 , y1 + 2, x + SLIDER_WIDTH / 2 , y2 - 2, 0xFF6E6E6E);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		String toDraw = formatString.replace("%v", (int)val + "");
		font.drawString(x1 + (x2 - x1) / 2 - font.getWidth(toDraw) / 2, y1 + (y2 - y1) / 2 - font.getHeight(toDraw) / 2, toDraw);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		if (hover) {
			drawRect(x - SLIDER_WIDTH / 2, y1 + 2, x + SLIDER_WIDTH / 2, y2 - 2, 0xA0FFFFFF);
		}
	}
	
	@Override
	public void update() {
		super.update();
		
		object.setValue((int)val);
	}
}
