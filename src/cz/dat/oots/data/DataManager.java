package cz.dat.oots.data;

import java.io.File;
import java.io.IOException;

import cz.dat.oots.util.Coord3D;
import cz.dat.oots.world.World;

public class DataManager implements IBlockDataManager, IItemDataManager {
	IBlockDataManager blockDataManager;
	IItemDataManager itemDataManager;

	public DataManager(File blockDataFile, File itemDataFile, World world)
			throws IOException {
		this.blockDataManager = new BlockDataManager(blockDataFile, world);
		this.itemDataManager = new ItemDataManager(itemDataFile, world);
	}

	public boolean containsData(Coord3D coord) {
		return blockDataManager.containsData(coord);
	}

	public boolean containsData(int x, int y, int z) {
		return blockDataManager.containsData(x, y, z);
	}

	public boolean containsData(int identificator) {
		return itemDataManager.containsData(identificator);
	}

	public void load() throws IOException {
		blockDataManager.load();
		itemDataManager.load();
	}

	public void save() throws IOException {
		this.blockDataManager.save();
		this.itemDataManager.save();
	}

	@Override
	public IDataObject getDataForIdentificator(int ident) {
		return this.itemDataManager.getDataForIdentificator(ident);
	}

	@Override
	public void addDataForIdentificator(int ident, IDataObject object) {
		this.itemDataManager.addDataForIdentificator(ident, object);
	}

	@Override
	public IDataObject getDataForCoord(Coord3D position) {
		return this.blockDataManager.getDataForCoord(position);
	}

	@Override
	public IDataObject getDataForCoord(int x, int y, int z) {
		return this.blockDataManager.getDataForCoord(x, y, z);
	}

	@Override
	public void addDataForCoord(Coord3D position, IDataObject data) {
		this.blockDataManager.addDataForCoord(position, data);
	}

	@Override
	public void addDataForCoord(int x, int y, int z, IDataObject data) {
		this.blockDataManager.addDataForCoord(x, y, z, data);
	}

	@Override
	public void removeDataForIdentificator(int ident) {
		this.itemDataManager.removeDataForIdentificator(ident);
	}

	@Override
	public void removeDataForCoord(Coord3D position) {
		this.blockDataManager.removeDataForCoord(position);
	}

	@Override
	public void removeDataForCoord(int x, int y, int z) {
		this.blockDataManager.removeDataForCoord(x, y, z);
	}
}
