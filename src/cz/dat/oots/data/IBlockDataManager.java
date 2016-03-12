package cz.dat.oots.data;

import cz.dat.oots.util.Coord3D;

import java.io.IOException;

public interface IBlockDataManager {
    public boolean containsData(Coord3D position);

    public boolean containsData(int x, int y, int z);

    public IDataObject getDataForCoord(Coord3D position);

    public IDataObject getDataForCoord(int x, int y, int z);

    public void addDataForCoord(Coord3D position, IDataObject data);

    public void addDataForCoord(int x, int y, int z, IDataObject data);

    public void removeDataForCoord(Coord3D position);

    public void removeDataForCoord(int x, int y, int z);

    public void load() throws IOException;

    public void save() throws IOException;
}
