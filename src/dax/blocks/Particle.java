package dax.blocks;

import java.util.List;
import java.util.Random;

import dax.blocks.collisions.AABB;

public class Particle {

	public static final float GRAVITY = -0.02f;
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
	
	public Particle(float x, float y, float z, float velX, float velY, float velZ, int lifetime, float r, float g, float b) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.velX = velX;
		this.velY = velY;
		this.velZ = velZ;
		this.lifetime = lifetime;
		this.dead = false;
		this.ground = false;
		this.aabb = new AABB(x-PARTICLE_SIZE/2, y-PARTICLE_SIZE/2, z-PARTICLE_SIZE/2, x+PARTICLE_SIZE/2, y+PARTICLE_SIZE/2, z+PARTICLE_SIZE/2);
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
	
	public void update(List<AABB> obstacles) {
		lastX = x;
		lastY = y;
		lastZ = z;
		
		age++;
		
		if (age > lifetime) {
			this.dead = true;
			return;
		}
		
		this.velY += GRAVITY;
		
		float maxVelX = velX;
		float maxVelY = velY;
		float maxVelZ = velZ;
		
		for (AABB o : obstacles) {
			maxVelX = o.clipXCollide(this.aabb, maxVelX);
		}
		
		this.aabb.move(maxVelX, 0, 0);
		
		for (AABB o : obstacles) {
			maxVelY = o.clipYCollide(this.aabb, maxVelY);
		}
		
		this.aabb.move(0, maxVelY, 0);
		
		for (AABB o : obstacles) {
			maxVelZ = o.clipZCollide(this.aabb, maxVelZ);
		}
		
		this.aabb.move(0, 0, maxVelZ);
		
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
	
}
