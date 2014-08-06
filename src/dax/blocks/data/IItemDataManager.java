package dax.blocks.data;

import java.io.IOException;
import java.util.Map;

public interface IItemDataManager {
	public boolean containsData(int identificator);
	public Map<Integer, DataValue> getValuesForIdentificator(int ident);
	public void load() throws IOException;
	public void save() throws IOException;
}
