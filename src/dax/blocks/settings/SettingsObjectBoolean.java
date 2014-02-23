package dax.blocks.settings;

public class SettingsObjectBoolean extends SettingsObject {
	
	private boolean value;
	
	public boolean getValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	public SettingsObjectBoolean(boolean val) {
		value = val;
	}
	
}
