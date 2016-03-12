package cz.dat.oots;

import cz.dat.oots.collisions.AABB;
import cz.dat.oots.render.ITickListener;
import cz.dat.oots.render.IWorldRenderer;
import cz.dat.oots.render.RenderEngine;
import cz.dat.oots.util.GameMath;
import cz.dat.oots.world.ParticleEngine;
import cz.dat.oots.world.World;

import java.util.Random;

public class Particle implements ITickListener, IWorldRenderer {

    public static final float BOUNCE_MAX = 0.5f;
    public static final float BOUNCE_MIN = 0.1f;
    public static final float FRICTION_AIR = 0.985f;
    public static final float FRICTION_GROUND = 0.75f;
    public static final float PARTICLE_SIZE = 0.5f;
    public static final float BOUNDING_BOX_SIZE = 0.01f;
    private static Random rand = new Random();
    public final float color;
    public AABB aabb;

    public float x;
    public float y;
    public float z;

    public float lastX;
    public float lastY;
    public float lastZ;

    public float velX;
    public float velY;
    public float velZ;

    public int lifetime;
    public int age;
    public boolean dead;
    public boolean ground;

    public World world;

    public Particle(World world, float x, float y, float z, float velX,
                    float velY, float velZ, int lifetime, float r, float g, float b) {
        this.world = world;
        this.x = this.lastX = x;
        this.y = this.lastY = y;
        this.z = this.lastZ = z;
        this.velX = velX;
        this.velY = velY;
        this.velZ = velZ;
        this.lifetime = lifetime;
        this.dead = false;
        this.ground = false;
        this.aabb = new AABB(
                this.x - Particle.BOUNDING_BOX_SIZE * 0.5f,
                this.y - Particle.BOUNDING_BOX_SIZE * 0.5f,
                this.z - Particle.BOUNDING_BOX_SIZE * 0.5f,
                this.x + Particle.BOUNDING_BOX_SIZE * 0.5f,
                this.y + Particle.BOUNDING_BOX_SIZE * 0.5f,
                this.z + Particle.BOUNDING_BOX_SIZE * 0.5f
        );

        this.color = ParticleEngine.packColor((int) (r * 255), (int) (g * 255), (int) (b * 255), 255);

        //this.r = r;
        //this.g = g;
        //this.b = b;
    }

    public float getPartialX(float ptt) {
        float delta = this.x - this.lastX;
        return this.lastX + delta * ptt;
    }

    public float getPartialY(float ptt) {
        float delta = this.y - this.lastY;
        return this.lastY + delta * ptt;
    }

    public float getPartialZ(float ptt) {
        float delta = this.z - this.lastZ;
        return this.lastZ + delta * ptt;
    }

    public void onTick() {
        this.lastX = this.x;
        this.lastY = this.y;
        this.lastZ = this.z;

        this.age++;

        if (this.age > this.lifetime) {
            this.dead = true;
        }

        this.velY -= World.GRAVITY;

        float maxVelX = this.velX;
        float maxVelY = this.velY;
        float maxVelZ = this.velZ;

        float[] clipped = this.world.clipMovement(this.aabb, maxVelX, maxVelY,
                maxVelZ);

        maxVelX = clipped[0];
        maxVelY = clipped[1];
        maxVelZ = clipped[2];

        boolean collidedX = false;
        boolean collidedY = false;
        boolean collidedZ = false;


        if (maxVelX != this.velX) {
            collidedX = true;
        }

        if (maxVelY != this.velY) {
            collidedY = true;
        }

        if (maxVelZ != this.velZ) {
            collidedZ = true;
        }

        this.ground = maxVelY != this.velY && this.velY < 0;

        if (GameMath.shouldCareAbout(this.velX)) {
            this.velX *= this.ground ? Particle.FRICTION_GROUND
                    : Particle.FRICTION_AIR;
            if (collidedX) {
                this.velX = (float) (-this.velX * (BOUNCE_MIN + rand
                        .nextFloat() * (BOUNCE_MAX - BOUNCE_MIN)));
            }
        }

        if (GameMath.shouldCareAbout(this.velY)) {
            this.velY *= this.ground ? Particle.FRICTION_GROUND
                    : Particle.FRICTION_AIR;
            if (collidedY) {
                this.velY = (float) (-this.velY * (BOUNCE_MIN + rand
                        .nextFloat() * (BOUNCE_MAX - BOUNCE_MIN)));
            }
        }

        if (GameMath.shouldCareAbout(this.velZ)) {
            this.velZ *= this.ground ? Particle.FRICTION_GROUND
                    : Particle.FRICTION_AIR;
            if (collidedZ) {
                this.velZ = (float) (-this.velZ * (BOUNCE_MIN + rand
                        .nextFloat() * (BOUNCE_MAX - BOUNCE_MIN)));
            }
        }


        this.x = this.aabb.x0 + Particle.PARTICLE_SIZE / 2;
        this.y = this.aabb.y0 + Particle.PARTICLE_SIZE / 2;
        this.z = this.aabb.z0 + Particle.PARTICLE_SIZE / 2;

    }

    @Override
    public void onRenderTick(float partialTickTime) {
    }

    @Override
    public void renderWorld(float partialTickTime, World world, RenderEngine e) {

    }
}
