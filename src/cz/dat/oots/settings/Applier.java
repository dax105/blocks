package cz.dat.oots.settings;

import cz.dat.oots.Game;

public abstract class Applier {
    protected SettingsObject<?> applyingObject;
    protected Game game;
    protected Settings settings;

    public Applier(Game game) {
        this.game = game;
        this.settings = game.getSettings();
    }

    public void setApplyingObject(SettingsObject<?> settingsObject) {
        this.applyingObject = settingsObject;
    }

    public abstract boolean apply(Object value);

    public abstract void afterApplying();
}
