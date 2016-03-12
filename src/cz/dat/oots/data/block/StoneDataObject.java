package cz.dat.oots.data.block;

import cz.dat.oots.block.BlockStone;
import cz.dat.oots.data.IDataObject;
import cz.dat.oots.settings.ObjectType;
import cz.dat.oots.util.GameUtil;

import java.util.ArrayList;
import java.util.List;

public class StoneDataObject implements IDataObject {

    private int blockID;
    private float colorR;
    private float colorG;
    private float colorB;

    public StoneDataObject(BlockStone stone) {
        this.blockID = stone.getID();
    }

    @Override
    public void load(List<String> values) {
        for (String line : values) {
            String[] parts = line.split(";");
            if (parts[0].equals("d")) {
                if (parts[1].equals(GameUtil
                        .objectTypeAsString(ObjectType.FLOAT))) {
                    switch (parts[2]) {
                        case "r":
                            this.colorR = Float.parseFloat(parts[3].replace(',',
                                    '.'));
                            break;
                        case "g":
                            this.colorG = Float.parseFloat(parts[3].replace(',',
                                    '.'));
                            break;
                        case "b":
                            this.colorB = Float.parseFloat(parts[3].replace(',',
                                    '.'));
                            break;
                    }
                }
            }
        }

    }

    @Override
    public List<String> save() {
        ArrayList<String> ret = new ArrayList<String>();
        String def = "d;" + GameUtil.objectTypeAsString(ObjectType.FLOAT)
                + ";%s;%f";
        ret.add(String.format(def, "r", this.colorR));
        ret.add(String.format(def, "g", this.colorG));
        ret.add(String.format(def, "b", this.colorB));
        return ret;
    }

    @Override
    public int getObjectID() {
        return this.blockID;
    }

    public float getColorR() {
        return colorR;
    }

    public void setColorR(float colorR) {
        this.colorR = colorR;
    }

    public float getColorG() {
        return colorG;
    }

    public void setColorG(float colorG) {
        this.colorG = colorG;
    }

    public float getColorB() {
        return colorB;
    }

    public void setColorB(float colorB) {
        this.colorB = colorB;
    }

}
