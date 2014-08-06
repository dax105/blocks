package dax.blocks.data;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import dax.blocks.util.Coord3D;

public class DataManager implements IBlockDataManager, IItemDataManager {
	IBlockDataManager blockDataManager;
	IItemDataManager itemDataManager;
	
	
	public DataManager(File blockDataFile, File itemDataFile) throws IOException {
		this.blockDataManager = new BlockDataManager(blockDataFile);
		this.itemDataManager = new ItemDataManager(itemDataFile);
	}
	
	public boolean containsData(Coord3D coord) {
		return blockDataManager.containsData(coord);
	}
	
	public boolean containsData(int x, int y, int z) {
		return blockDataManager.containsData(x , y, z);
	}
	
	public boolean containsData(int identificator) {
		return itemDataManager.containsData(identificator);
	}
	
	public Map<Integer, DataValue> getValuesForCoord(Coord3D position) {
		return blockDataManager.getValuesForCoord(position);
	}
	
	public Map<Integer, DataValue> getValuesForCoord(int x, int y, int z) {
		return blockDataManager.getValuesForCoord(x, y, z);
	}
	
	public Map<Integer, DataValue> getValuesForIdentificator(int ident) {
		return itemDataManager.getValuesForIdentificator(ident);
	}
	
	public void load() throws IOException {
		blockDataManager.load();
		itemDataManager.load();
	}
	
	public void save() throws IOException {
		blockDataManager.save();
		itemDataManager.save();
	}
}
