package cz.dat.oots.gui;

import cz.dat.oots.settings.SettingsObject;
import org.newdawn.slick.Font;

public class GuiObjectSettingsFloatSlider extends GuiObjectSlider {

    private SettingsObject<Float> object;

    public GuiObjectSettingsFloatSlider(int x1, int y1, int x2, int y2,
                                        Font font, String formatString, int id, GuiScreen parent,
                                        float minVal, float maxVal, SettingsObject<Float> object) {
        super(x1, y1, x2, y2, font, formatString, id, parent, minVal, maxVal,
                object.getValue());

        this.object = object;
    }

    @Override
    public void update() {
        super.update();
        this.object.setValue(val);
    }

}
