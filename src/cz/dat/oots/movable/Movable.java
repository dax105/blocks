package cz.dat.oots.movable;

import cz.dat.oots.render.ITickListener;


public abstract class Movable implements ITickListener {

	protected float posX;
	protected float posY;
	protected float posZ;
	
	protected float posXPartial;
	protected float posYPartial;
	protected float posZPartial;
	
	protected float velX;
	protected float velY;
	protected float velZ;
	
	protected float lastPosX;
	protected float lastPosY;
	protected float lastPosZ;

	public void onTick() {
		this.lastPosX = posX;
		this.lastPosY = posY;
		this.lastPosZ = posZ;
		
		this.updatePosition();
	}
	
	@Override
	public void onRenderTick(float partialTickTime) {
		this.updateRenderPosition(partialTickTime);
	}


	public Movable(float x, float y, float z) {
		this(x, y, z, 0, 0, 0);
	}

	public Movable(float x, float y, float z, float velX, float velY, float velZ) {
		this.setPosX(x);
		this.setPosY(y);
		this.setPosZ(z);
		this.setLastPosX(x);
		this.setLastPosY(y);
		this.setLastPosZ(z);
		this.setVelX(velX);
		this.setVelY(velY);
		this.setVelZ(velZ);
	}

	private void updateRenderPosition(float ptt) {
		float deltaX = this.posX - this.lastPosX;
		this.posXPartial = this.lastPosX + deltaX*ptt;
		
		float deltaY = this.posY - this.lastPosY;
		this.posYPartial = this.lastPosY + deltaY*ptt;
		
		float deltaZ = this.posZ - this.lastPosZ;
		this.posZPartial = this.lastPosZ + deltaZ*ptt;
	}

	public float getVelX() {
		return this.velX;
	}

	public void setVelX(float velX) {
		this.velX = velX;
	}

	public float getVelY() {
		return this.velY;
	}

	public void setVelY(float velY) {
		this.velY = velY;
	}

	public float getVelZ() {
		return this.velZ;
	}

	public void setVelZ(float velZ) {
		this.velZ = velZ;
	}

	public float getLastPosX() {
		return this.lastPosX;
	}

	public void setLastPosX(float lastPosX) {
		this.lastPosX = lastPosX;
	}

	public float getLastPosY() {
		return this.lastPosY;
	}

	public void setLastPosY(float lastPosY) {
		this.lastPosY = lastPosY;
	}

	public float getLastPosZ() {
		return this.lastPosZ;
	}

	public void setLastPosZ(float lastPosZ) {
		this.lastPosZ = lastPosZ;
	}

	public abstract void updatePosition();
	
	public float getPosX() {
		return this.posX;
	}
	
	public float getPosY() {
		return this.posY;
	}
	
	public float getPosZ() {
		return this.posZ;
	}
	
	public void setPosX(float posX) {
		this.posX = posX;
	}
	
	public void setPosY(float posY) {
		this.posY = posY;
	}
	
	public void setPosZ(float posZ) {
		this.posZ = posZ;
	}
	
	public float getPosXPartial() {
		return this.posXPartial;
	}
	
	public float getPosYPartial() {
		return this.posYPartial;
	}
	
	public float getPosZPartial() {
		return this.posZPartial;
	}
}
