package cz.dat.oots.block;

import cz.dat.oots.sound.SoundManager;
import cz.dat.oots.world.IDRegister;
import cz.dat.oots.world.UpdateType;
import cz.dat.oots.world.World;

public class BlockSand extends BlockBasic {

	public BlockSand(IDRegister r) {
		super("oots/blockSand", r);
		this.setAllTextures(6).setFootStepSound(SoundManager.footstep_dirt)
				.setFallSound("fall_soft");
	}

	@Override
	public void onNeighbourUpdate(int x, int y, int z, World world) {
		world.scheduleUpdate(x, y, z, 1, UpdateType.SAND_FALL);
	}

	@Override
	public void onUpdate(int x, int y, int z, int type, World world) {
		if(type == UpdateType.SAND_FALL) {
			int below = world.getBlock(x, y - 1, z);
			if(below == 0 || !world.getBlockObject(below).isCollidable()) {
				world.setBlock(x, y - 1, z, 7, true, true);
				world.setBlock(x, y, z, 0, true, true);
			}
		}
	}

}
