package cz.dat.oots.movable.entity;

import cz.dat.oots.collisions.AABB;
import cz.dat.oots.movable.Movable;
import cz.dat.oots.world.World;

public abstract class Entity extends Movable {

    public static final float STRENGTH_MULTIPLIER = 0.01f;

    protected AABB bb;
    protected World world;
    protected float lifes = 1;
    protected boolean alive;

    public Entity(World world, float x, float y, float z) {
        super(x, y, z);
        this.setWorld(world);
        this.alive = true;
    }

    public World getWorld() {
        return this.world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public AABB getAABB() {
        return this.bb;
    }

    public void setPosition(float x, float y, float z) {
        this.setPosX(x);
        this.setPosY(y);
        this.setPosZ(z);
    }

    public float getLifes() {
        return this.lifes;
    }

    public void setLifes(float lifes) {
        if (lifes > 1)
            this.lifes = 1;
        else if (lifes <= 0) {
            this.alive = false;
            this.lifes = 0;
        } else
            this.lifes = lifes;
    }

    public void regenerate(int strength) {
        this.setLifes(this.lifes + (strength * Entity.STRENGTH_MULTIPLIER));
    }

    public void hurt(int strength) {
        this.setLifes(this.lifes - (strength * Entity.STRENGTH_MULTIPLIER));
    }
}
