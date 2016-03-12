package cz.dat.oots.data;

import cz.dat.oots.item.Item;
import cz.dat.oots.world.World;

import javax.activation.UnsupportedDataTypeException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ItemDataManager implements IItemDataManager {

    File data;
    private Map<Integer, IDataObject> values;
    private World world;

    public ItemDataManager(File dataFile, World world) throws IOException {
        this.data = dataFile;
        this.values = new HashMap<>();
        this.world = world;

        if (dataFile.exists()) {
            this.load();
        } else {
            dataFile.createNewFile();
        }
    }

    @Override
    public boolean containsData(int identificator) {
        return (this.values.get(identificator) != null);
    }

    @Override
    public IDataObject getDataForIdentificator(int ident) {
        return this.values.get(ident);
    }

    /*
     * s i;[ITEMID] r;[ITEMIDENTIFICATOR] d;[str/int/float/bool];[DATA] d;.....
     * d;..... r;[ITEMIDENTIFICATOR] ..... i;[ITEMID] .... e
     */
    public void load() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(data));

        String line;
        boolean started = false;

        Integer identificator = null;
        Item currentItem = null;
        IDataObject currentObject = null;
        List<String> passData = null;

        while ((line = br.readLine()) != null) {
            if (!started && line.charAt(0) != 's') {
                br.close();
                throw new UnsupportedDataTypeException(
                        "Invalid file start (x-S-.-E)");
            }

            if (!started && line.charAt(0) == 's') {
                started = true;
                continue;
            }

            if (started && line.charAt(0) == 'i') {
                currentItem = world.getRegister().getItem(
                        Integer.parseInt(line.substring(2)));
                continue;
            }

            if (started && line.charAt(0) == 'r' && passData == null) {
                identificator = Integer.parseInt(line.substring(2));
                passData = new ArrayList<String>();
                currentObject = currentItem.createDataObject();
                continue;
            }

            if (started && (line.charAt(0) == 'r' || line.charAt(0) == 'e')
                    && passData != null) {
                currentObject.load(new ArrayList<String>(passData));
                this.values.put(identificator, currentObject);

                passData = null;
                identificator = null;
            }

            if (started && identificator == null && line.charAt(0) == 'd') {
                br.close();
                throw new UnsupportedDataTypeException(
                        "Invalid file contents (i-R-D-D)!");
            }

            if (started && line.charAt(0) == 'd') {
                passData.add(line);
            }

            if (started && line.charAt(0) == 'e') {
                br.close();
                return;
            }
        }

        br.close();
    }

    public void save() throws IOException {
        if (data.exists()) {
            BufferedWriter bw = new BufferedWriter(new FileWriter(data));

            bw.write("s");
            bw.newLine();

            int currentID = 0;

            for (Entry<Integer, IDataObject> entry : this.values.entrySet()) {
                if (entry.getValue() != null) {

                    if (currentID != entry.getValue().getObjectID()) {
                        currentID = entry.getValue().getObjectID();
                        bw.write("i;" + currentID);
                        bw.newLine();
                    }

                    bw.write(String.format("r;%s", entry.getKey()));
                    bw.newLine();

                    for (String s : entry.getValue().save()) {
                        bw.write(s);
                        bw.newLine();
                    }
                }
            }
            bw.write("e");
            bw.newLine();

            bw.close();
        }
    }

    @Override
    public void addDataForIdentificator(int ident, IDataObject object) {
        this.values.put(ident, object);
    }

    @Override
    public void removeDataForIdentificator(int ident) {
        this.values.remove(ident);
    }

}
