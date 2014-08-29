package cz.dat.oots.settings;

import cz.dat.oots.Game;

public abstract class Applier {
	protected SettingsObject<?> applyingObject;
	protected Game game;
	
	public Applier(Game game) {
		this.game = game;
	}
	
	public void setApplyingObject(SettingsObject<?> settingsObject) {
		this.applyingObject = settingsObject;
	}
	
	public abstract boolean apply(Object value);
	public abstract void afterApplying();
}
