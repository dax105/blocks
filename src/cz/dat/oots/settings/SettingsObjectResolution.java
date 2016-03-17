package cz.dat.oots.settings;

import cz.dat.oots.util.Coord2D;

public class SettingsObjectResolution extends SettingsObject<Coord2D> {

    private boolean fullscreen;

    public SettingsObjectResolution(String name, Coord2D defaultValue, ApplierResolution applier) {
        super(name, defaultValue, "Window resolution", null, applier);
    }

    public void setValue(int width, int height, boolean fullscreen) {
        this.fullscreen = fullscreen;
        super.setValue(new Coord2D(width, height), ApplyRequestSource.INTERNAL);
    }

    @Override
    public String getValueSerialized() {
        return this.getValue().x + "x" + this.getValue().y;
    }

    @Override
    public void deserializeValue(String value, ApplyRequestSource source) {
        int w = Integer.parseInt(value.split("x")[0]);
        int h = Integer.parseInt(value.split("x")[1]);
        this.setValue(w, h, this.fullscreen);
    }

    public int width() {
        return this.getValue().x;
    }

    public int height() {
        return this.getValue().y;
    }

    public boolean fullScreen() {
        return this.fullscreen;
    }

    @Override
    public String getReadableValue() {
        return this.getValue().x + "x" + this.getValue().y;
    }
}
