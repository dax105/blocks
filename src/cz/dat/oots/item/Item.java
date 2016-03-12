package cz.dat.oots.item;

import cz.dat.oots.data.IDataObject;
import cz.dat.oots.world.IDRegister;
import cz.dat.oots.world.World;
import org.newdawn.slick.opengl.Texture;

public abstract class Item {

    private int id;
    private String name;
    private Texture texture;
    private String showedName;

    public Item(String name, IDRegister register) {
        this.id = register.getIDForName(name);
        this.name = name;
        this.showedName = new String(name);
    }

    public abstract void onTick(int uniqueIdentifier, World world);

    public abstract void onRenderTick(float partialTickTime,
                                      int uniqueIdentifier, World world);

    public abstract void onUse(int mouseButton, int targetX, int targetY,
                               int targetZ, int uniqueIdentifier, World world);

    public IDataObject createDataObject() {
        return null;
    }

    public int getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public Item setTexture(Texture texture) {
        this.texture = texture;
        return this;
    }

    public String getShowedName() {
        return this.showedName;
    }

    public Item setShowedName(String showedName) {
        this.showedName = showedName;
        return this;
    }
}
