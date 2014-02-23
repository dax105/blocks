package dax.blocks.world.generator;

import java.util.Random;

import dax.blocks.block.Block;
import dax.blocks.world.World;

public class TreeGenerator {

	World world;
	
	public TreeGenerator(World world) {
		this.world = world;
	}
	
	public void generateTree(int x, int y, int z) {
		Random rand = new Random();
		
		y += 1;
		
		int stemHeight = 5 + rand.nextInt(4);
		int leavesStart = stemHeight - 2 - rand.nextInt(3);
		int leavesEnd = stemHeight + 1 + rand.nextInt(2);
		
		fill(x-1, y+leavesStart, z-1, x+1, y+leavesEnd, z+1, Block.leaves.getId());
		fill(x, y, z, x, y+stemHeight, z, Block.log.getId());
	}
	
	public void fill(int x0, int y0, int z0, int x1, int y1, int z1, byte id) {
		for (int x = x0; x <= x1; x++) {
			for (int y = y0; y <= y1; y++) {
				for (int z = z0; z <= z1; z++) {
					world.setBlockNoRebuild(x, y, z, id);
				}
			}
		}
	}
	
}
