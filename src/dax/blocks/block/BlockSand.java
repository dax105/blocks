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
		if(world.getBlock(x, y - 1, z) == 0) {
			world.setBlock(x, y - 1, z, 7, false);
			world.setBlock(x, y, z, 0, true);
		}
	}

}
