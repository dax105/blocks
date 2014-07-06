package dax.blocks.movable.entity;

import dax.blocks.block.Block;
import dax.blocks.world.World;

public class PlayerEntity extends Entity {

	public static final float PLAYER_HEIGHT = 1.7f;
	public static final float EYES_HEIGHT = 1.6f;
	public static final float PLAYER_SIZE = 0.5f;

	byte selectedBlockID = Block.stoneMossy.getId();
	
	public int lookingAtX;
	public int lookingAtY;
	public int lookingAtZ;

	int placesAtX;
	int placesAtY;
	int placesAtZ;

	public boolean hasSelected = false;

	public float heading = 140.0F;
	public float tilt = -60.0F;
	
	public PlayerEntity(World world, float x, float y, float z) {
		super(world, x, y, z);
	}

	@Override
	public void onTick() {
		super.onTick();
	}
	
	@Override
	public void onRenderTick(float ptt) {
		super.onRenderTick(ptt);
		updateLookingAt();
	}
	
	@Override
	public void updatePosition() {
		
	}
	
	private void updateLookingAt() {
		float reach = 16.0F;

		float xn = (float) getPosXPartial();
		float yn = (float) getPosYPartial() + PLAYER_HEIGHT;
		float zn = (float) getPosZPartial();

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

			xn = (float) (getPosX() + f * xChange);
			yn = (float) (getPosY() + EYES_HEIGHT + f * yChange);
			zn = (float) (getPosZ() + f * zChange);

			if (getWorld().getBlock((int) Math.floor(xn), (int) Math.floor(yn), (int) Math.floor(zn)) > 0) {
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
