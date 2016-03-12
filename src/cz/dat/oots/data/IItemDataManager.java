package cz.dat.oots.data;

import java.io.IOException;

public interface IItemDataManager {
    public boolean containsData(int identificator);

    public IDataObject getDataForIdentificator(int ident);

    public void addDataForIdentificator(int ident, IDataObject object);

    public void removeDataForIdentificator(int ident);

    public void load() throws IOException;

    public void save() throws IOException;
}
