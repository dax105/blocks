package dax.blocks.item;

import dax.blocks.world.IDRegister;
import dax.blocks.world.World;

public abstract class Item {
	private int id;
	private String name;
	private int texture;
	
	public abstract void onTick(int uniqueIdentifier, World world);
	public abstract void onRenderTick(float partialTickTime, int uniqueIdentifier, World world);
	public abstract void onUse(int mouseButton, int targetX, int targetY, int targetZ, int uniqueIdentifier, World world);
	
	public Item(String name, IDRegister register) {
		this.id = register.getIDForName(name);
		this.name = name;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Item setTexture(int textureID) {
		this.texture = textureID;
		return this;
	}
	
	public int getTexture() {
		return this.texture;
	}
}
