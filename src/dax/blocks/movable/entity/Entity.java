package dax.blocks.movable.entity;

import dax.blocks.collisions.AABB;
import dax.blocks.movable.Movable;
import dax.blocks.world.World;

public abstract class Entity extends Movable {

	protected AABB bb;
	protected World world;
	protected int lifes;
	
	public Entity(World world, float x, float y, float z) {
		super(x, y, z);
		this.setWorld(world);
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
	
	public AABB getAABB() {
		return bb;
	}

	public void setPosition(float x, float y, float z) {
		this.setPosX(x);
		this.setPosY(y);
		this.setPosZ(z);
	}

}
