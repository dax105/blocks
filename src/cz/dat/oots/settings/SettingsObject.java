package cz.dat.oots.settings;

public class SettingsObject<T> {

    private T value;
    private String name;
    private String readableForm;
    private Applier<T> applier;
    private String readableName;

    public SettingsObject(String name, T defaultValue, String readableName,
                          String readableForm, Applier<T> applier) {
        this.name = name;
        this.value = defaultValue;
        this.applier = applier;
        this.readableForm = readableForm;
        this.readableName = readableName;
    }

    public SettingsObject(String name, T defaultValue, Applier<T> applier) {
        this(name, defaultValue, null, null, applier);
    }

    public SettingsObject(String name, T defaultValue) {
        this(name, defaultValue, null, null, null);
    }

    public static String getConsoleRepresentation(ObjectType t) {
        switch (t) {
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

    public ObjectType getObjectType() {
        Class<?> type = this.value.getClass();
        if (type == Integer.class)
            return ObjectType.INTEGER;

        if (type == Float.class)
            return ObjectType.FLOAT;

        if (type == Boolean.class)
            return ObjectType.BOOLEAN;

        if (type == String.class)
            return ObjectType.STRING;

        return ObjectType.OTHER;
    }

    public String getReadableValue() {
        if (this.readableForm == null)
            return this.value.toString();

        if (this.getObjectType() == ObjectType.BOOLEAN) {
            boolean b = (Boolean) this.value;
            return this.readableForm.replace("%o", b ? "ON" : "OFF").replace(
                    "%v", this.value.toString());
        }
        return this.readableForm.replace("%v", this.value.toString());
    }

    public String getRepresentation() {
        return this.getReadableName() + ": " + this.getReadableValue();
    }

    public void setValue(T value, ApplyRequestSource source) {
        if (this.applier != null) {
            this.applier.setApplyingObject(this);

            if (this.applier.apply(value, source))
                this.value = value;
        } else {
            this.value = value;
        }
    }

    public String getValueSerialized() {
        return this.getValue().toString();
    }

    public void deserializeValue(String value, ApplyRequestSource source) {
        if(this.value.getClass() == Integer.class)
            this.setValue((T)(Integer)Integer.parseInt(value), source);
        else if(this.value.getClass() == Float.class)
            this.setValue((T)(Float)Float.parseFloat(value), source);
        else if(this.value.getClass() == Boolean.class)
            this.setValue((T)(Boolean)Boolean.parseBoolean(value), source);
        else if(this.value.getClass() == String.class)
            this.setValue((T)value, source);
        else
            throw new UnsupportedOperationException();
    }

    public T getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public String getReadableForm() {
        return this.readableForm;
    }

    public void setReadableForm(String readableForm) {
        this.readableForm = readableForm;
    }

    public String getReadableName() {
        return this.readableName == null ? this.name : this.readableName;
    }

    public void setReadableName(String readableName) {
        this.readableName = readableName;
    }
}
