package cz.dat.oots.data.block;

import cz.dat.oots.block.BlockPlant;
import cz.dat.oots.data.IDataObject;
import cz.dat.oots.settings.ObjectType;
import cz.dat.oots.util.GameUtil;

import java.util.ArrayList;
import java.util.List;

public class PlantDataObject implements IDataObject {

    private int blockID;
    private boolean hasSpecialTexture;

    public PlantDataObject(BlockPlant plant) {
        this.blockID = plant.getID();
    }

    @Override
    public void load(List<String> values) {
        for (String line : values) {
            if (line.startsWith("d;"
                    + GameUtil.objectTypeAsString(ObjectType.BOOLEAN) + ";")) {
                this.hasSpecialTexture = Boolean
                        .parseBoolean(line.substring(8));
            }
        }
    }

    @Override
    public List<String> save() {
        List<String> r = new ArrayList<String>();
        r.add("d;" + GameUtil.objectTypeAsString(ObjectType.BOOLEAN) + ";"
                + this.hasSpecialTexture);
        return r;
    }

    @Override
    public int getObjectID() {
        return this.blockID;
    }

    public boolean isTextured() {
        return this.hasSpecialTexture;
    }

    public void setTextured(boolean b) {
        this.hasSpecialTexture = b;
    }

}
