package dax.blocks.movable.entity;

import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import dax.blocks.Game;
import dax.blocks.block.Block;
import dax.blocks.collisions.AABB;
import dax.blocks.gui.ingame.GuiManager;
import dax.blocks.inventory.BasicBlockStack;
import dax.blocks.inventory.BasicItemStack;
import dax.blocks.inventory.IObjectStack;
import dax.blocks.overlay.BasicLifesOverlay;
import dax.blocks.render.IOverlayRenderer;
import dax.blocks.settings.Keyconfig;
import dax.blocks.settings.Settings;
import dax.blocks.sound.SoundManager;
import dax.blocks.world.Explosion;
import dax.blocks.world.IDRegister;
import dax.blocks.world.World;

public class PlayerEntity extends Entity implements IOverlayRenderer {

	public static final float PLAYER_HEIGHT = 1.7f;
	public static final float EYES_HEIGHT = 1.6f;
	public static final float PLAYER_SIZE = 0.5f;
	public static final float STEP_TIMER_FULL = 2.25f;
	public static final float JUMP_STRENGTH = 0.4f;
	public static final float MAX_WALK_SPEED = 0.25f;
	public static final int REGENERATION_TICKS = 20;

	private IObjectStack inHand;
	private BasicLifesOverlay lifesOverlay;

	private int lookingAtX;
	private int lookingAtY;
	private int lookingAtZ;

	private int placesAtX;
	private int placesAtY;
	private int placesAtZ;

	private boolean hasSelected = false;

	private float heading = 140.0F;
	private float tilt = -60.0F;

	private boolean onGround = false;
	private boolean wasOnGround = false;

	private float speed = 0;
	private float speedStrafe = 0;

	private Random rand = new Random();

	private float spf;
	private float stepTimer = PlayerEntity.STEP_TIMER_FULL;
	private float fallVelocity;

	private Block standingOn = null;

	private int regenerationTimer = 0;

	public PlayerEntity(World world, float x, float y, float z) {
		super(world, x, y, z);
		this.setSelectedBlockID(1);
		this.bb = new AABB(this.posX - PlayerEntity.PLAYER_SIZE / 2, this.posY,
				this.posZ - PlayerEntity.PLAYER_SIZE / 2, this.posX
						+ PlayerEntity.PLAYER_SIZE / 2, this.posY
						+ PlayerEntity.PLAYER_HEIGHT, this.posZ
						+ PlayerEntity.PLAYER_SIZE / 2);
		this.updateOverlay();
	}

	@Override
	public void onTick() {
		super.onTick();

		this.updateStandingOn();

		this.regenerationTimer++;

		if(this.regenerationTimer >= PlayerEntity.REGENERATION_TICKS) {
			this.regenerationTimer = 0;
			this.regenerate(1);
		}

		if(Keyboard.isKeyDown(Keyconfig.explosion)) {
			if(this.hasSelected) {
				Explosion.explode(this.world, this.lookingAtX, this.lookingAtY,
						this.lookingAtZ);
			}
		}

		if(Keyboard.isKeyDown(Keyconfig.particleFirework)) {
			if(this.hasSelected) {
				for(int i = 0; i < 50; i++) {
					this.world.spawnParticleWithRandomDirectionFast(
							this.lookingAtX, this.lookingAtY + 1,
							this.lookingAtZ, this.rand.nextInt(10), 0.3f);
				}
			}
		}
		
		while(Mouse.next()) {

			if(Mouse.isGrabbed()) {

				if(Mouse.getEventButtonState()) {

					if(Keyconfig.isDown(Keyconfig.crouch)) {
						
						if(hasSelected) {
							this.world.getBlockObject(this.lookingAtX,
									this.lookingAtY, this.lookingAtZ).onClick(
									Mouse.getEventButton(), this.lookingAtX, this.lookingAtY,
									this.lookingAtZ, world);

						} else {
							this.inHand.useItem(Mouse.getEventButton(), this.lookingAtX,
									this.lookingAtY, this.lookingAtZ, 0,
									this.world);
						}
						
					} else if(Mouse.getEventButton() == 0) {
						if(this.hasSelected) {
							this.world.setBlock(this.lookingAtX,
									this.lookingAtY, this.lookingAtZ, 0, true,
									true);
						}
					} else if(Mouse.getEventButton() == 1) {
						if(this.hasSelected
								&& (this.lookingAtX != this.placesAtX
										|| this.lookingAtY != this.placesAtY || this.lookingAtZ != this.placesAtZ)) {
							this.inHand.useItem(1, this.placesAtX,
									this.placesAtY, this.placesAtZ, 0,
									this.world);
						}
					}

				}

				this.updateBlock();

			} else {
				if(Mouse.getEventButtonState() && Mouse.getEventButton() == 0) {
					if(GuiManager.getInstance().isOpened())
						GuiManager.getInstance().checkMouseClosing();
				}

			}

		}

		if(this.inHand.shouldRecycle()) {
			this.inHand = new BasicBlockStack(world.getBlockObject(this.inHand
					.getItemID()), 32);
		}

		if(!this.wasOnGround && this.onGround) {

			Block block = this.standingOn;

			if(block != null) {
				SoundManager.getInstance().playSound(block.getFallSound(),
						0.7f + this.rand.nextFloat() * 0.25f);

				if(this.fallVelocity > 0.7f) {
					int h = block.getFallHurt() * (int) (this.fallVelocity * 3);
					this.hurt(h);
				}
			}
		}

		if(this.onGround) {
			this.stepTimer -= this.spf;
		} else {
			this.stepTimer = 0.0f;
		}

		if(this.stepTimer <= 0 && this.onGround) {
			Block block = this.standingOn;
			if(block != null) {
				SoundManager.getInstance().playSound(block.getFootStepSound(),
						1.0f - (this.rand.nextFloat() * 0.2f));
			}

			this.stepTimer += PlayerEntity.STEP_TIMER_FULL;
		}

		if(!this.alive && !Settings.getInstance().peacefulMode.getValue()) {
			Game.getInstance().getWorldsManager().exitWorld();
		}

		this.updateOverlay();
	}

	private void updateStandingOn() {

		int blockX = (int) Math.floor(this.posX);
		int blockY = (int) Math.floor(this.posY - 1);
		int blockZ = (int) Math.floor(this.posZ);

		int b = this.world.getBlock(blockX, blockY, blockZ);

		if(b == 0) {
			float[][] blocksAround = new float[3][3];
			for(int x = 0; x < 3; x++) {
				for(int z = 0; z < 3; z++) {
					if(this.world.getBlock(blockX + x - 1, blockY, blockZ + z
							- 1) == 0) {
						blocksAround[x][z] = -1;
					} else {
						float xDist = x - 0.5f;
						float zDist = z - 0.5f;

						blocksAround[x][z] = (float) Math.sqrt(xDist * xDist
								+ zDist * zDist);
					}
				}
			}

			boolean foundBlock = false;

			float minDist = 999999;

			int closestX = 0;
			int closestZ = 0;

			for(int x = 0; x < 3; x++) {
				for(int z = 0; z < 3; z++) {
					if(blocksAround[x][z] >= 0) {
						if(blocksAround[x][z] < minDist) {
							foundBlock = true;
							minDist = blocksAround[x][z];
							closestX = x;
							closestZ = z;
						}
					}
				}
			}

			b = foundBlock ? this.world.getBlock(closestX + blockX - 1, blockY,
					closestZ + blockZ - 1) : 0;

		}

		this.standingOn = world.getBlockObject(b);

	}

	public void onRenderTick(float ptt) {
		super.onRenderTick(ptt);

		if(Mouse.isGrabbed()) {
			float mouseDX = Mouse.getDX() * 0.8f * 0.16f;
			float mouseDY = Mouse.getDY() * 0.8f * 0.16f;
			this.heading += mouseDX;
			this.tilt += mouseDY;
		}

		while(this.heading <= -180) {
			this.heading += 360;
		}
		while(this.heading > 180) {
			this.heading -= 360;
		}

		if(this.tilt < -90) {
			this.tilt = -90;
		}

		if(this.tilt > 90) {
			this.tilt = 90;
		}

		this.updateLookingAt();
	}

	public void updateOverlay() {
		int heartsX = 80;
		int heartsY = Settings.getInstance().windowHeight.getValue() - 43;

		if(this.lifesOverlay == null) {
			this.lifesOverlay = new BasicLifesOverlay(this, heartsX, heartsY);
			Game.getInstance().getOverlayManager()
					.addOverlay(this.lifesOverlay);
		} else {
			this.lifesOverlay.setPosition(heartsX, heartsY);
		}
	}

	@Override
	public void renderOverlay(float ptt) {
		this.inHand.renderGUITexture(25,
				Settings.getInstance().windowHeight.getValue() - 75, 50, 50);
	}

	@Override
	public void updatePosition() {
		if(Settings.getInstance().noclip.getValue()) {
			
			this.wasOnGround = this.onGround;

			float frictionMultipler = 0.7f;

			float speedC = 0;
			float speedStrafeC = 0;

			float multi = 1;

			if(!GuiManager.getInstance().isOpened()) {
				if(Keyconfig.isDown(Keyconfig.boost)) {
					multi = 8;
				}

				if(Keyconfig.isDown(Keyconfig.ahead)) {
					speedC -= 0.4f * multi;
				}

				if(Keyconfig.isDown(Keyconfig.back)) {
					speedC += 0.4f * multi;
				}

				if(Keyconfig.isDown(Keyconfig.left)) {
					speedStrafeC -= 0.4f * multi;
				}

				if(Keyconfig.isDown(Keyconfig.right)) {
					speedStrafeC += 0.4f * multi;
				}

				if(Keyconfig.isDown(Keyconfig.jump)) {
					this.velY += 0.4f * multi;
				}
				
				if(Keyconfig.isDown(Keyconfig.crouch)) {
					this.velY -= 0.4f * multi;
				}
			}


			
			this.speed += speedC;
			this.speedStrafe += speedStrafeC;

			this.speed *= frictionMultipler;
			this.speedStrafe *= frictionMultipler;

			spf = (float) Math.sqrt(this.speed * this.speed + this.speedStrafe
					* this.speedStrafe);

			this.velY *= frictionMultipler;

			double toMoveZ = (this.posZ + Math.cos(-this.heading / 180 * Math.PI)
					* this.speed)
					+ (Math.cos((-this.heading + 90) / 180 * Math.PI) * this.speedStrafe);
			double toMoveX = (this.posX + Math.sin(-this.heading / 180 * Math.PI)
					* this.speed)
					+ (Math.sin((-this.heading + 90) / 180 * Math.PI) * this.speedStrafe);
			double toMoveY = (this.posY + (this.velY));

			float xa = (float) -(this.posX - toMoveX);
			float ya = (float) -(this.posY - toMoveY);
			float za = (float) -(this.posZ - toMoveZ);

			this.velX = xa;
			this.velZ = za;

			this.bb.move(xa, ya, za);

			this.onGround = false;

			this.posX = (this.bb.x0 + this.bb.x1) / 2.0F;
			this.posY = this.bb.y0;
			this.posZ = (this.bb.z0 + this.bb.z1) / 2.0F;
			
			return;
		}
		this.wasOnGround = this.onGround;

		int blockPosX = (int) Math.floor(this.posX);
		int blockPosY = (int) Math.floor(this.posY);
		int blockPosZ = (int) Math.floor(this.posZ);

		boolean inWater = ((this.world.getBlockObject(blockPosX, blockPosY,
				blockPosZ) == IDRegister.water) || (this.world.getBlockObject(
				blockPosX, blockPosY + 1, blockPosZ) == IDRegister.water));

		float d0 = this.world.getBlockObject(blockPosX, blockPosY, blockPosZ) != null ? this.world
				.getBlockObject(blockPosX, blockPosY, blockPosZ).getDensity()
				: 1;

		float d1 = this.world.getBlockObject(blockPosX, blockPosY + 1,
				blockPosZ) != null ? this.world.getBlockObject(blockPosX,
				blockPosY + 1, blockPosZ).getDensity() : 1;

		float density = (d0 + d1) / 2f;
		float frictionMultipler = 1f / density;

		float speedC = 0;
		float speedStrafeC = 0;

		float multi = 1;

		if(!GuiManager.getInstance().isOpened()) {
			if(Keyconfig.isDown(Keyconfig.boost)) {
				multi = 15;
			}

			if(Keyconfig.isDown(Keyconfig.ahead)) {
				speedC -= this.onGround ? 0.25 * multi : 0.03 * multi;
			}

			if(Keyconfig.isDown(Keyconfig.back)) {
				speedC += this.onGround ? 0.25 * multi : 0.03 * multi;
			}

			if(Keyconfig.isDown(Keyconfig.left)) {
				speedStrafeC -= this.onGround ? 0.25 * multi : 0.03 * multi;
			}

			if(Keyconfig.isDown(Keyconfig.right)) {
				speedStrafeC += this.onGround ? 0.25 * multi : 0.03 * multi;
			}

			if(Keyconfig.isDown(Keyconfig.jump)) {
				if(this.onGround) {
					if(multi == 1) {
						this.velY += PlayerEntity.JUMP_STRENGTH;
					} else if(multi > 1) {
						this.velY += PlayerEntity.JUMP_STRENGTH * 4;
					} else {
						this.velY += PlayerEntity.JUMP_STRENGTH * 0.95f;
					}
				} else if(!this.onGround && inWater) {
					this.velY += PlayerEntity.JUMP_STRENGTH / 4;
				}
			}
		}

		float xsq = Math.abs(speedC) * Math.abs(speedC);
		float ysq = Math.abs(speedStrafeC) * Math.abs(speedStrafeC);
		float sp = (float) Math.sqrt(xsq + ysq);
		if(sp > PlayerEntity.MAX_WALK_SPEED * multi) {
			float mult = PlayerEntity.MAX_WALK_SPEED * multi / sp;
			speedC *= mult;
			speedStrafeC *= mult;
		}

		this.speed += speedC;
		this.speedStrafe += speedStrafeC;

		this.speed *= this.onGround ? 0.5f : 0.9f;
		this.speed *= frictionMultipler;
		this.speedStrafe *= this.onGround ? 0.5f : 0.9f;
		this.speedStrafe *= frictionMultipler;

		spf = (float) Math.sqrt(this.speed * this.speed + this.speedStrafe
				* this.speedStrafe);

		this.velY -= World.GRAVITY;
		this.velY *= frictionMultipler;

		double toMoveZ = (this.posZ + Math.cos(-this.heading / 180 * Math.PI)
				* this.speed)
				+ (Math.cos((-this.heading + 90) / 180 * Math.PI) * this.speedStrafe);
		double toMoveX = (this.posX + Math.sin(-this.heading / 180 * Math.PI)
				* this.speed)
				+ (Math.sin((-this.heading + 90) / 180 * Math.PI) * this.speedStrafe);
		double toMoveY = (this.posY + (this.velY));

		float xa = (float) -(this.posX - toMoveX);
		float ya = (float) -(this.posY - toMoveY);
		float za = (float) -(this.posZ - toMoveZ);

		this.velX = xa;
		this.velZ = za;

		float yab = ya;

		float[] clipped = this.world.clipMovement(this.bb, xa, ya, za);
		ya = clipped[1];

		this.onGround = yab != ya && yab < 0.0F;

		if(this.onGround) {
			this.fallVelocity = -this.velY;
			this.velY = 0;
		}

		if(yab != ya) {
			this.velY = 0;
		}

		this.posX = (this.bb.x0 + this.bb.x1) / 2.0F;
		this.posY = this.bb.y0;
		this.posZ = (this.bb.z0 + this.bb.z1) / 2.0F;

	}

	private void updateBlock() {
		int wh = Mouse.getEventDWheel();

		if(wh > 0) {
			int newSelectedBlock = this.inHand.getItemID() + 1;

			if(newSelectedBlock == 18) {
				this.inHand = new BasicItemStack(
						IDRegister.itemImaginaryChocolate, 1);
				return;
			}

			if(newSelectedBlock > (world.getRegister().getBlockCount())) {
				newSelectedBlock = 1;
			}

			this.setSelectedBlockID(newSelectedBlock);
		}

		if(wh < 0) {
			int newSelectedBlock = this.inHand.getItemID() - 1;
			if(newSelectedBlock < 1) {
				newSelectedBlock = world.getRegister().getBlockCount();
			}

			this.setSelectedBlockID(newSelectedBlock);
		}
	}

	private void updateLookingAt() {
		float reach = Settings.getInstance().reach.getValue();

		float xn = (float) this.getPosXPartial();
		float yn = (float) this.getPosYPartial() + PlayerEntity.PLAYER_HEIGHT;
		float zn = (float) this.getPosZPartial();

		float xl;
		float yl;
		float zl;

		float yChange = (float) Math.cos((-this.tilt + 90) / 180 * Math.PI);
		float ymult = (float) Math.sin((-this.tilt + 90) / 180 * Math.PI);

		float xChange = (float) (Math.cos((-this.heading + 90) / 180 * Math.PI) * ymult);
		float zChange = (float) (-Math
				.sin((-this.heading + 90) / 180 * Math.PI) * ymult);

		for(float f = 0; f <= reach; f += 0.01f) {
			xl = xn;
			yl = yn;
			zl = zn;

			xn = (float) (this.getPosX() + f * xChange);
			yn = (float) (this.getPosY() + PlayerEntity.EYES_HEIGHT + f
					* yChange);
			zn = (float) (this.getPosZ() + f * zChange);

			if(this.getWorld().getBlock((int) Math.floor(xn),
					(int) Math.floor(yn), (int) Math.floor(zn)) > 0) {
				this.lookingAtX = (int) Math.floor(xn);
				this.lookingAtY = (int) Math.floor(yn);
				this.lookingAtZ = (int) Math.floor(zn);

				this.placesAtX = (int) Math.floor(xl);
				this.placesAtY = (int) Math.floor(yl);
				this.placesAtZ = (int) Math.floor(zl);
				this.hasSelected = true;
				return;
			}

			this.hasSelected = false;

		}
	}

	@Override
	public void setPosition(float x, float y, float z) {
		super.setPosition(x, y, z);
		this.bb = new AABB(this.posX - PlayerEntity.PLAYER_SIZE / 2, this.posY,
				this.posZ - PlayerEntity.PLAYER_SIZE / 2, this.posX
						+ PlayerEntity.PLAYER_SIZE / 2, this.posY
						+ PlayerEntity.PLAYER_HEIGHT, this.posZ
						+ PlayerEntity.PLAYER_SIZE / 2);
	}

	public int getSelectedBlockID() {
		return this.inHand.getItemID();
	}

	public void setSelectedBlockID(int selectedBlockID) {
		this.inHand = new BasicBlockStack(
				world.getBlockObject(selectedBlockID), 32);
	}

	public float getHeading() {
		return this.heading;
	}

	public void setHeading(float heading) {
		this.heading = heading;
	}

	public float getTilt() {
		return this.tilt;
	}

	public void setTilt(float tilt) {
		this.tilt = tilt;
	}

	public int getLookingAtX() {
		return this.lookingAtX;
	}

	public int getLookingAtY() {
		return this.lookingAtY;
	}

	public int getLookingAtZ() {
		return this.lookingAtZ;
	}

	public int getPlacesAtX() {
		return this.placesAtX;
	}

	public int getPlacesAtY() {
		return this.placesAtY;
	}

	public int getPlacesAtZ() {
		return this.placesAtZ;
	}

	public boolean hasSelectedBlock() {
		return this.hasSelected;
	}

	public float getSpeed() {
		return this.speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getSpeedStrafe() {
		return this.speedStrafe;
	}

	public void setSpeedStrafe(float speedStrafe) {
		this.speedStrafe = speedStrafe;
	}
}
