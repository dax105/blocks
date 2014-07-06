package dax.blocks.settings;

public class SettingsObject<T> {

	private T value;
	private String name;
	
	private Applier applyer;

	public SettingsObject(String name, T defaultValue, Applier applyer) {
		this.name = name;
		this.value = defaultValue;
		this.applyer = applyer;
	}
	
	public SettingsObject(String name, T defaultValue) {
		this.name = name;
		this.value = defaultValue;
		this.applyer = null;
	}
	
	public void setValue(T value) {
		this.value = value;
		if (applyer != null) {
			applyer.apply();
		}
	}

	public T getValue() {
		return value;
	}
	
	public String getName() {
		return this.name;
	}

}
