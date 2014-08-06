package dax.blocks.world;

import dax.blocks.settings.ObjectType;

public class DataValue {

	private ObjectType objectType;
	private String data_String;
	private int data_Integer;
	private float data_Float;
	private boolean data_Boolean;

	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DataValue) {
			DataValue sVal = (DataValue)obj;
			switch(sVal.objectType) {
				case BOOLEAN:
					return sVal.data_Boolean == this.data_Boolean;
				case FLOAT:
					return sVal.data_Float == this.data_Float;
				case INTEGER:
					return sVal.data_Integer == this.data_Integer;
				case STRING:
					return sVal.data_String.equals(this.data_String);
			}
		}
		
		return false;
	}

	public DataValue(String data) {
		this.setData(data);
	}
	
	public DataValue() {
		
	}

	public String getDataString() {
		if(this.objectType != null)
			if(this.objectType == ObjectType.STRING) {
				return data_String;
			}

		return null;
	}

	public int getDataInt() {
		if(this.objectType != null)
			if(this.objectType == ObjectType.INTEGER) {
				return data_Integer;
			}

		return 0;
	}

	public float getDataFloat() {
		if(this.objectType != null)
			if(this.objectType == ObjectType.FLOAT) {
				return data_Float;
			}

		return 0;
	}

	public boolean getDataBoolean() {
		if(this.objectType != null)
			if(this.objectType == ObjectType.BOOLEAN) {
				return data_Boolean;
			}

		return false;
	}

	public String getDataAsString() {
		switch(this.objectType) {
			case BOOLEAN:
				return this.data_Boolean + "";
			case FLOAT:
				return this.data_Float + "";
			case INTEGER:
				return this.data_Integer + "";
			case STRING:
				return this.data_String;
			default:
				return null;
		}
	}
	
	public void setData(String value) {
		if(value.equalsIgnoreCase("true")) {
			this.data_Boolean = true;
			this.objectType = ObjectType.BOOLEAN;
			return;
		}
		
		if(value.equalsIgnoreCase("false")) {
			this.data_Boolean = false;
			this.objectType = ObjectType.BOOLEAN;
			return;
		}
		
		try {
			this.data_Integer = Integer.parseInt(value);
			this.objectType = ObjectType.INTEGER;
			return;
		} catch(Exception e) {
			
		}
		
		try {
			this.data_Float = Float.parseFloat(value);
			this.objectType = ObjectType.FLOAT;
			return;
		} catch(Exception e) {
			
		}
		
		this.data_String = value;
		this.objectType = ObjectType.STRING;
	}

	public void setDataTyped(String value) {
		this.data_String = value;
		this.objectType = ObjectType.STRING;
	}
	
	public void setDataTyped(float value) {
		this.data_Float = value;
		this.objectType = ObjectType.FLOAT;
	}
	
	public void setDataTyped(int value) {
		this.data_Integer = value;
		this.objectType = ObjectType.INTEGER;
	}
	
	public void setDataTyped(boolean value) {
		this.data_Boolean = value;
		this.objectType = ObjectType.BOOLEAN;
	}
}
