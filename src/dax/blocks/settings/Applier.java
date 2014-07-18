package dax.blocks.settings;

public abstract class Applier {
	protected SettingsObject<?> applyingObject;
	public void setApplyingObject(SettingsObject<?> settingsObject) {
		this.applyingObject = settingsObject;
	}
	
	public abstract boolean apply(Object value);
}
