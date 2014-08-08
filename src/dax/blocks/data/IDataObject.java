package dax.blocks.data;

import java.util.List;

public interface IDataObject {
	public void load(List<String> values);
	public List<String> save();
	public int getObjectID();
}
