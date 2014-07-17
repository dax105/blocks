package dax.blocks.block;

import dax.blocks.sound.SoundManager;
import dax.blocks.world.World;

public class BlockSand extends BlockBasic {

	public BlockSand() {
		super(7);
		this.setAllTextures(6).setStepSound(SoundManager.footstep_dirt).setFallSound("fall_soft");
	}

	@Override
	public void onTick(int x, int y, int z, World world) {
		super.onTick(x, y, z, world);
		int below = world.getBlock(x, y - 1, z);
		if(below == 0 || !Block.getBlock(below).isCollidable()) {
			world.setBlock(x, y - 1, z, 7, true, true);
			world.setBlock(x, y, z, 0, true, true);
		}
	}

}
