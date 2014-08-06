package dax.blocks;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import dax.blocks.collisions.AABB;
import dax.blocks.render.IRenderable;
import dax.blocks.render.ITickListener;
import dax.blocks.render.IWorldRenderer;
import dax.blocks.render.RenderEngine;
import dax.blocks.world.World;

public class Particle implements ITickListener, IWorldRenderer {

	public static final float BOUNCE_MAX = 0.5f;
	public static final float BOUNCE_MIN = 0.1f;
	public static final float FRICTION_AIR = 0.985f;
	public static final float FRICTION_GROUND = 0.75f;
	public static final float PARTICLE_SIZE = 0.075f;
	
	public final float r;
	public final float g;
	public final float b;
	
	private static Random rand = new Random();
	
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
	
	public Particle(World world, float x, float y, float z, float velX, float velY, float velZ, int lifetime, float r, float g, float b) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.velX = velX;
		this.velY = velY;
		this.velZ = velZ;
		this.lifetime = lifetime;
		this.dead = false;
		this.ground = false;
		this.aabb = new AABB(x-PARTICLE_SIZE*0.5f, y-PARTICLE_SIZE*0.5f, z-PARTICLE_SIZE*0.5f, x+PARTICLE_SIZE*0.5f, y+PARTICLE_SIZE*0.5f, z+PARTICLE_SIZE*0.5f);
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public float getPartialX(float ptt) {
		float delta = x - lastX;
		return lastX + delta*ptt;
	}
	
	public float getPartialY(float ptt) {
		float delta = y - lastY;
		return lastY + delta*ptt;
	}
	
	public float getPartialZ(float ptt) {
		float delta = z - lastZ;
		return lastZ + delta*ptt;
	}
	
	public void onTick() {
		lastX = x;
		lastY = y;
		lastZ = z;
		
		age++;
		
		if (age > lifetime) {
			this.dead = true;
			this.world.registerNewRenderableRemoval(this);
			return;
		}
		
		this.velY -= World.GRAVITY;
		
		float maxVelX = velX;
		float maxVelY = velY;
		float maxVelZ = velZ;
		
		float[] clipped = world.clipMovement(this.aabb, maxVelX, maxVelY, maxVelZ);
		
		maxVelX = clipped[0];
		maxVelY = clipped[1];
		maxVelZ = clipped[2];
		
		boolean collidedX = false;
		boolean collidedY = false;
		boolean collidedZ = false;
		
		x = aabb.x0+PARTICLE_SIZE/2;
		y = aabb.y0+PARTICLE_SIZE/2;
		z = aabb.z0+PARTICLE_SIZE/2;
		
		if (maxVelX != velX) {
			collidedX = true;
		}
		
		if (maxVelY != velY) {
			collidedY = true;
		}
		
		if (maxVelZ != velZ) {
			collidedZ = true;
		}
		
		this.ground = maxVelY != velY && velY < 0.0F;
		
		this.velX *= ground ? FRICTION_GROUND : FRICTION_AIR;
		this.velY *= ground ? FRICTION_GROUND : FRICTION_AIR;
		this.velZ *= ground ? FRICTION_GROUND : FRICTION_AIR;
		
		if (collidedX) {
			this.velX = (float) (-velX*(BOUNCE_MIN+rand.nextDouble()*(BOUNCE_MAX-BOUNCE_MIN)));
		}
		
		if (collidedY) {
			this.velY = (float) (-velY*(BOUNCE_MIN+rand.nextDouble()*(BOUNCE_MAX-BOUNCE_MIN)));
		}
		
		if (collidedZ) {
			this.velZ = (float) (-velZ*(BOUNCE_MIN+rand.nextDouble()*(BOUNCE_MAX-BOUNCE_MIN)));
		}
		
	}
	

	@Override
	public void onRenderTick(float partialTickTime) {
	}
	
	@Override
	public void renderWorld(float partialTickTime, World world, RenderEngine e) {
		GL11.glBegin(GL11.GL_QUADS);
		e.renderParticle(this, partialTickTime);
		GL11.glEnd();
	}
}
