package dax.blocks.settings;

public class SettingsObject<T> {

	private T value;
	private String name;
	private String readableForm;
	private Applier applier;
	private String readableName;
	public static String getConsoleRepresentation(ObjectType t) {
		switch(t) {
		case INTEGER:
			return "[INT]";
		case FLOAT:
			return "[FLOAT]";
		case BOOLEAN:
			return "[BOOL]";
		default:
			return "[?]";
		}
	}
	
	public SettingsObject(String name, T defaultValue, String readableName, String readableForm, Applier applier) {
		this.name = name;
		this.value = defaultValue;
		this.applier = applier;
		this.readableForm = readableForm;
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
		
		return null;
	}
	
	public String getReadableValue() {
		if(readableForm == null)
			return null;
		
		return readableForm.replace("%v", value.toString());
	}
	
	public String getRepresentation() {
		if(readableForm == null)
			return this.getReadableName() + ": " + value.toString();
		
		return this.getReadableName() + ": " + getReadableValue();
	}
	
	public void setValue(T value) {
		this.value = value;
		if (applier != null) {
			applier.apply();
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

}
