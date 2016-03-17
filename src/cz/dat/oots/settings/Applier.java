package cz.dat.oots.settings;

import cz.dat.oots.Game;

public abstract class Applier<T> {
    protected SettingsObject<T> applyingObject;
    protected Game game;
    protected Settings settings;

    public Applier(Game game) {
        this.game = game;
        this.settings = game.getSettings();
    }

    public void setApplyingObject(SettingsObject<T> settingsObject) {
        this.applyingObject = settingsObject;
    }

    public abstract boolean apply(T value, ApplyRequestSource source);
}
