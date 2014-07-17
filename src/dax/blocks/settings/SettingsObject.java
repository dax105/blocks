package dax.blocks.settings;

public class SettingsObject<T> {

	private T value;
	private String name;
	private String readableForm;
	private Applier applier;
	private String readableName;
	private boolean inSettings = true;
	
	public static String getConsoleRepresentation(ObjectType t) {
		switch(t) {
		case INTEGER:
			return "[INT]";
		case FLOAT:
			return "[FLOAT]";
		case BOOLEAN:
			return "[BOOL]";
		case STRING:
			return "[STRING]";
		default:
			return "[?]";
		}
	}
	
	public SettingsObject(String name, T defaultValue, String readableName, String readableForm, Applier applier) {
		this.name = name;
		this.value = defaultValue;
		this.applier = applier;
		this.readableForm = readableForm;
		this.readableName = readableName;
	}
	
	public SettingsObject(String name, T defaultValue, Applier applier) {
		this(name, defaultValue, null, null, applier);
	}
	
	public SettingsObject(String name, T defaultValue) {
		this(name, defaultValue, null, null, null);
	}
	
	public ObjectType getObjectType() {
		Class<?> type = value.getClass();
		if(type == Integer.class)
			return ObjectType.INTEGER;
		
		if(type == Float.class)
			return ObjectType.FLOAT;
		
		if(type == Boolean.class)
			return ObjectType.BOOLEAN;
		
		if(type == String.class)
			return ObjectType.STRING;
		
		return null;
	}
	
	public String getReadableValue() {
		if(readableForm == null)
			return value.toString();
		
		if(this.getObjectType() == ObjectType.BOOLEAN) {
			boolean b = (Boolean)value;
			return readableForm.replace("%o", b ? "ON" : "OFF").replace("%v", value.toString());
		}
		return readableForm.replace("%v", value.toString());
	}
	
	public String getRepresentation() {
		return this.getReadableName() + ": " + getReadableValue();
	}
	
	public void setValue(T value) {
		setValue(value, true);
	}
	
	public void setValue(T value, boolean applyApplier) {
		if (applier != null && applyApplier) {
			if(applier.apply(value))
				this.value = value;
		} else {
			this.value = value;
		}
	}
	
	public T getValue() {
		return value;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getReadableForm() {
		return readableForm;
	}

	public void setReadableForm(String readableForm) {
		this.readableForm = readableForm;
	}

	public String getReadableName() {
		return readableName == null ? name : readableName;
	}

	public void setReadableName(String readableName) {
		this.readableName = readableName;
	}

	public boolean isInSettings() {
		return inSettings;
	}

	public void setInSettings(boolean inSettings) {
		this.inSettings = inSettings;
	}

}
