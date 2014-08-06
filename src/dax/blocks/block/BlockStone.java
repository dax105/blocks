package dax.blocks.block;

import java.util.Random;

import dax.blocks.sound.SoundManager;
import dax.blocks.world.DataFlags;
import dax.blocks.world.World;

public class BlockStone extends BlockBasic {
	Random rnd;

	public BlockStone() {
		super(5);
		rnd = new Random();
		this.setAllTextures(0).setStepSound(SoundManager.footstep_stone).setFallSound("fall_hard");
	}

	public void setColor(int x, int y, int z, World w) {
		if(w.containsData(x, y, z, DataFlags.RECOLOR_R)) {
			this.colorR = w.getDataFloat(x, y, z, DataFlags.RECOLOR_R);
			this.colorG = w.getDataFloat(x, y, z, DataFlags.RECOLOR_G);
			this.colorB = w.getDataFloat(x, y, z, DataFlags.RECOLOR_B);
		}
	}

	private void recolor(int x, int y, int z, World w) {
		w.setData(x, y, z, DataFlags.RECOLOR_R, "" + rnd.nextFloat());
		w.setData(x, y, z, DataFlags.RECOLOR_G, "" + rnd.nextFloat());
		w.setData(x, y, z, DataFlags.RECOLOR_B, "" + rnd.nextFloat());
	}

	@Override
	public void onClicked(int button, int x, int y, int z, World world) {
		recolor(x, y, z, world);

		if(button != 10) {
			for(int x2 = (x - 1); x2 < (x + 2); x2++) {
				for(int y2 = (y - 1); y2 < (y + 2); y2++) {
					for(int z2 = (z - 1); z2 < (z + 2); z2++) {
						if(world.getBlock(x2, y2, z2) == this.getId()) {
							Block.stone.onClicked(10, x2, y2, z2, world);
						}
					}
				}
			}
		}

		world.setChunkDirty(x >> 4, y / 16, z >> 4);
	}
}
