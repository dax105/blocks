package dax.blocks;

import dax.blocks.block.Block;
import dax.blocks.collisions.AABB;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import dax.blocks.world.Explosion;
import dax.blocks.world.World;

public class Player {

	Random rand = new Random();
	
	World world;

	public static final float PLAYER_HEIGHT = 1.7f;
	public static final float EYES_HEIGHT = 1.6f;
	public static final float PLAYER_SIZE = 0.5f;
	
	public static final float STEP_TIMER_FULL = 2.25f;

	public static final float JUMP_STRENGTH = 0.4f;
	public static final float MAX_WALK_SPEED = 0.25f;

	byte selectedBlockID = 3;

	public float xBob = 0;
	public float yBob = 0;
	
	public float posX = 0.0F;
	public float posZ = 0.0F;
	public float posY = 128F;
	
	public float lastPosX = 0.0F;
	public float lastPosZ = 0.0F;
	public float lastPosY = 128F;

	public int lookingAtX = -1;
	public int lookingAtY = -1;
	public int lookingAtZ = -1;

	int placesAtX = -1;
	int placesAtY = -1;
	int placesAtZ = -1;

	public boolean hasSelected = false;

	public float heading = 140.0F;
	public float tilt = -60.0F;

	boolean reload = false;
	
	AABB bb;
	
	public float speed = 0;
	public float speedStrafe = 0;
	
	float bob = 0;
	
	public float xv = 0.0f;
	public float yv = 0.0f;
	public float zv = 0.0f;
	
	private boolean onGround = false;
	private boolean wasOnGround = false;
	
	public float stepTimer = STEP_TIMER_FULL;

	public Player(World world) {
		this.world = world;
		this.bb = new AABB(posX-PLAYER_SIZE/2, posY, posZ-PLAYER_SIZE/2, posX+PLAYER_SIZE/2, posY+PLAYER_HEIGHT, posZ+PLAYER_SIZE/2);
	}

	public float getPartialX(float ptt) {
		float delta = posX - lastPosX;
		return lastPosX + delta*ptt;
	}
	
	public float getPartialY(float ptt) {
		float delta = posY - lastPosY;
		return lastPosY + delta*ptt;
	}
	
	public float getPartialZ(float ptt) {
		float delta = posZ - lastPosZ;
		return lastPosZ + delta*ptt;
	}
	
	public void setPos(float x, float y, float z) {
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.bb = new AABB(posX-PLAYER_SIZE/2, posY, posZ-PLAYER_SIZE/2, posX+PLAYER_SIZE/2, posY+PLAYER_HEIGHT, posZ+PLAYER_SIZE/2);
	}
	
	private void updateLookingAt() {
		float reach = Game.settings.reach.getValue();

		if (!Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			
		float xn = (float) posX;
		float yn = (float) posY + PLAYER_HEIGHT;
		float zn = (float) posZ;

		float xl;
		float yl;
		float zl;

		float yChange = (float) Math.cos((-tilt + 90) / 180 * Math.PI);
		float ymult = (float) Math.sin((-tilt + 90) / 180 * Math.PI);

		float xChange = (float) (Math.cos((-heading + 90) / 180 * Math.PI) * ymult);
		float zChange = (float) (-Math.sin((-heading + 90) / 180 * Math.PI) * ymult);

		for (float f = 0; f <= reach; f += 0.01f) {
			xl = xn;
			yl = yn;
			zl = zn;

			xn = (float) (posX + f * xChange);
			yn = (float) (posY + EYES_HEIGHT + f * yChange);
			zn = (float) (posZ + f * zChange);

			if (world.getBlock((int) Math.floor(xn), (int) Math.floor(yn), (int) Math.floor(zn)) > 0) {
				lookingAtX = (int) Math.floor(xn);
				lookingAtY = (int) Math.floor(yn);
				lookingAtZ = (int) Math.floor(zn);

				placesAtX = (int) Math.floor(xl);
				placesAtY = (int) Math.floor(yl);
				placesAtZ = (int) Math.floor(zl);
				hasSelected = true;
				return;
			}

			hasSelected = false;

		}
		
		}
	}

	public void setSelectedBlock(int id) {
		selectedBlockID = (byte) id;
	}
	
	public void update() {
		wasOnGround = onGround;
		
		float speedC = 0;
		float speedStrafeC = 0;
		
		int multi = 1;

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			multi = 15;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			speedC -= onGround ? 0.25 * multi : 0.03 * multi;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			speedC += onGround ? 0.25 * multi : 0.03 * multi;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			speedStrafeC -= onGround ? 0.25 * multi : 0.03 * multi;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			speedStrafeC += onGround ? 0.25 * multi : 0.03 * multi;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && onGround) {
			//moveY += 0.005D * multi;
			
			if (multi == 1) {
				yv += JUMP_STRENGTH * multi;
			} else {
				yv += JUMP_STRENGTH *5;
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			//moveY -= 0.005D * multi;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
			if (hasSelected) {
				Explosion.explode(world, lookingAtX, lookingAtY, lookingAtZ);
			}
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_1) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD1)) {
			setSelectedBlock(1);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_2) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)) {
			setSelectedBlock(2);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_3) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD3)) {
			setSelectedBlock(13);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_4) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4)) {
			setSelectedBlock(4);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_5) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD5)) {
			setSelectedBlock(5);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_6) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6)) {
			setSelectedBlock(6);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_7) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD7)) {
			setSelectedBlock(7);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_8) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8)) {
			setSelectedBlock(14);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_9) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD9)) {
			setSelectedBlock(9);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_0) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD0)) {
			setSelectedBlock(10);
		}
		
		float xsq = Math.abs(speedC)*Math.abs(speedC);
		float ysq = Math.abs(speedStrafeC)*Math.abs(speedStrafeC);
		
		float sp = (float) Math.sqrt(xsq + ysq);
		
		//Game.console.out("SPD A: " + sp);
		
		if (sp  > MAX_WALK_SPEED*multi) {
			float mult = (float) (MAX_WALK_SPEED*multi / sp);
			speedC *= mult;
			speedStrafeC *= mult;
		}
		
		speed += speedC;
		speedStrafe += speedStrafeC;
		
		speed *= onGround ? 0.5f : 0.9f;
		speedStrafe *= onGround ? 0.5f : 0.9f;
		
		float spf = (float) Math.sqrt(speed*speed+speedStrafe*speedStrafe);
		
		//Game.console.out("SPD B: " + spf);
		
		yv -= world.GRAVITY;
		
			double toMoveZ = (posZ + Math.cos(-heading / 180 * Math.PI) * speed) + (Math.cos((-heading + 90) / 180 * Math.PI) * speedStrafe);
			double toMoveX = (posX + Math.sin(-heading / 180 * Math.PI) * speed) + (Math.sin((-heading + 90) / 180 * Math.PI) * speedStrafe);
			double toMoveY = (posY + (yv));

		    float xa = (float) -(posX - toMoveX);
		    float ya = (float) -(posY - toMoveY);
		    float za = (float) -(posZ - toMoveZ);
		    
		    xv = xa;
		    zv = za;
		    
		    float yab = ya;
			
		      ArrayList<AABB> aABBs = this.world.getBBs(this.bb.expand(xa, ya, za));
		      
		      for(int i = 0; i < aABBs.size(); ++i) {
		         ya = ((AABB)aABBs.get(i)).clipYCollide(this.bb, ya);
		      }

		      this.bb.move(0.0F, ya, 0.0F);

		      for(int i = 0; i < aABBs.size(); ++i) {
		         xa = ((AABB)aABBs.get(i)).clipXCollide(this.bb, xa);
		      }

		      this.bb.move(xa, 0.0F, 0.0F);

		      for(int i = 0; i < aABBs.size(); ++i) {
		          za = ((AABB)aABBs.get(i)).clipZCollide(this.bb, za);
		       }

		       this.bb.move(0.0F, 0.0F, za);
		       
		       this.onGround = yab != ya && yab < 0.0F;
		       
		       if (this.onGround) {
		    	   yv = 0;
		       }
		       
		       if(yab != ya) {
			          this.yv = 0.0F;
		       }
		       
		       /*if(xaOrg != xa) {
		          this.xd = 0.0F;
		       }



		       if(zaOrg != za) {
		          this.zd = 0.0F;
		       }*/

		       bob += za + xa;
		       
		       lastPosX = posX;
		       lastPosY = posY;
		       lastPosZ = posZ;
		       
		       this.posX = (this.bb.x0 + this.bb.x1) / 2.0F;
		       this.posY = this.bb.y0;
		       this.posZ = (this.bb.z0 + this.bb.z1) / 2.0F;
		  

		while (Mouse.next()) {
			if (Mouse.getEventButtonState()) {
				if (Mouse.getEventButton() == 0) {
					if (hasSelected) {
						world.setBlock(lookingAtX, lookingAtY, lookingAtZ, (byte) 0, true);
					}
				}
				if (Mouse.getEventButton() == 1) {
					if (hasSelected && (lookingAtX != placesAtX || lookingAtY != placesAtY || lookingAtZ != placesAtZ)) {
						world.setBlock(placesAtX, placesAtY, placesAtZ, selectedBlockID, true);
					}
				}
			}
		}
		
		if (!wasOnGround && onGround) {
			int b = world.getBlock((int)Math.floor(this.posX), (int)Math.floor(this.posY-1.0f), (int)Math.floor(this.posZ));
			Block block = Block.getBlock((byte)b);
			if (block != null) {
				SoundManager.play(block.getFallSound(), 0.7f+rand.nextFloat()*0.25f, 0.5f);
			}
			
			//SoundManager.fall_soft.playAsSoundEffect(0.7f+rand.nextFloat()*0.25f, 0.5f, false);
		}
		
		if (onGround) {
			stepTimer -= spf;
		} else {
			stepTimer = 0.0f;
		}

		if (stepTimer <= 0 && onGround) {
			//SoundManager.footstep.playAsSoundEffect(1.0f-rand.nextFloat()*0.3f, 1.0f, false);
			
			int b = world.getBlock((int)Math.floor(this.posX), (int)Math.floor(this.posY-1.0f), (int)Math.floor(this.posZ));
			Block block = Block.getBlock((byte)b);
			if (block != null) {
				SoundManager.play(block.getStepSound(), 1.0f-(rand.nextFloat()*0.2f-0.1f), 1f);
			}
			
			//SoundManager.play(SoundManager.footstep_grass, 1.0f-(rand.nextFloat()*0.2f-0.1f), 1f);
			stepTimer += STEP_TIMER_FULL;
		}
		
	}

	public void onRender() {
		
		if (Mouse.isGrabbed()) {
			float mouseDX = Mouse.getDX() * 0.8f * 0.16f;
			float mouseDY = Mouse.getDY() * 0.8f * 0.16f;
			heading += mouseDX;
			tilt += mouseDY;
		}

		while (heading <= -180) {
			heading += 360;
		}
		while (heading > 180) {
			heading -= 360;
		}

		if (tilt < -90) {
			tilt = -90;
		}

		if (tilt > 90) {
			tilt = 90;
		}
		
		xBob = (float) Math.sin(System.nanoTime()/100000000.0d)*0.5f*speed;
		yBob = (float) Math.cos(System.nanoTime()/100000000.0d)*0.5f*speed;
		
		updateLookingAt();
		
	}

}