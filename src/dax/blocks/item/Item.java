package dax.blocks.item;

import org.newdawn.slick.opengl.Texture;

import dax.blocks.world.IDRegister;
import dax.blocks.world.World;

public abstract class Item {

	private int id;
	private String name;
	private Texture texture;
	private String showedName;
	
	public abstract void onTick(int uniqueIdentifier, World world);
	public abstract void onRenderTick(float partialTickTime, int uniqueIdentifier, World world);
	public abstract void onUse(int mouseButton, int targetX, int targetY, int targetZ, int uniqueIdentifier, World world);
	
	public Item(String name, IDRegister register) {
		this.id = register.getIDForName(name);
		this.name = name;
		this.showedName = new String(name);
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Item setTexture(Texture texture) {
		this.texture = texture;
		return this;
	}
	
	public Texture getTexture() {
		return this.texture;
	}
	
	public Item setShowedName(String showedName) {
		this.showedName = showedName;
		return this;
	}
	
	public String getShowedName() {
		return this.showedName;
	}
}
