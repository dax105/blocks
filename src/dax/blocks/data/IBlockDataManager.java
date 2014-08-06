package dax.blocks.data;

import java.io.IOException;
import java.util.Map;

import dax.blocks.util.Coord3D;

public interface IBlockDataManager {
	public boolean containsData(Coord3D position);
	public boolean containsData(int x, int y, int z);
	public Map<Integer, DataValue> getValuesForCoord(Coord3D position);
	public Map<Integer, DataValue> getValuesForCoord(int x, int y, int z);
	public void load() throws IOException;
	public void save() throws IOException;
}
