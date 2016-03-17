package cz.dat.oots.gui;

import cz.dat.oots.settings.ApplyRequestSource;
import cz.dat.oots.settings.SettingsObject;
import cz.dat.oots.util.GLHelper;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Font;

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
        GLHelper.drawRectangle(this.c1, this.x1, this.x2, this.y1, this.y2);
        //GLHelper.drawRectangle(this.c2, this.x1 + 2, this.x2 - 2, this.y1 + 2,
        //		this.y2 - 2);

        int x = this.x1
                + GuiObjectSlider.SLIDER_WIDTH
                / 2
                + 2
                + (int) Math
                .round(((float) (this.val - this.minVal) / (this.maxVal - this.minVal))
                        * ((x2 - x1) - GuiObjectSlider.SLIDER_WIDTH - 4));

        GLHelper.drawRectangle(this.c3, x - GuiObjectSlider.SLIDER_WIDTH / 2, x
                + GuiObjectSlider.SLIDER_WIDTH / 2, this.y1 + 2, this.y2 - 2);

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        String toDraw = formatString.replace("%v", (int) this.val + "");
        this.font.drawString(
                this.x1 + (this.x2 - this.x1) / 2 - this.font.getWidth(toDraw)
                        / 2,
                this.y1 + (this.y2 - this.y1) / 2 - this.font.getHeight(toDraw)
                        / 2, toDraw);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        if (this.hover) {
            GLHelper.drawRectangle(this.c4, x - GuiObjectSlider.SLIDER_WIDTH
                            / 2, x + GuiObjectSlider.SLIDER_WIDTH / 2, this.y1 + 2,
                    this.y2 - 2);
        }
    }

    @Override
    public void update() {
        super.update();

        this.object.setValue((int) this.val, ApplyRequestSource.GUI);
    }
}
