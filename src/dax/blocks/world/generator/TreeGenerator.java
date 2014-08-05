package dax.blocks.world.generator;

import java.util.Random;
import dax.blocks.world.IDRegister;
import dax.blocks.world.World;

public class TreeGenerator {

	World world;
	
	public TreeGenerator(World world) {
		this.world = world;
	}
	
	public void generateTree(int x, int y, int z) {
		Random rand = new Random();
		
		int trunkHeight = 5 + rand.nextInt(3);
		int leavesStart = trunkHeight - 1 - rand.nextInt(2);
		int leavesEnd = trunkHeight + 2 + rand.nextInt(1);
		
		int leaves = IDRegister.leaves.getID();
		
		fill(x-2, y+leavesStart, z-2, x+2, y+leavesEnd-2, z+2, leaves);
		fill(x-2, y+leavesStart+2, z-1, x+2, y+leavesEnd-1, z+1, leaves);
		fill(x-1, y+leavesStart+2, z-2, x+1, y+leavesEnd-1, z+2, leaves);
		world.setBlock(x-1, y+leavesEnd, z, leaves, false, true);
		world.setBlock(x+1, y+leavesEnd, z, leaves, false, true);
		world.setBlock(x, y+leavesEnd, z-1, leaves, false, true);
		world.setBlock(x, y+leavesEnd, z+1, leaves, false, true);
		world.setBlock(x, y+leavesEnd, z, leaves, false, true);
		
		fill(x, y, z, x, y+trunkHeight, z, IDRegister.log.getID());
	}
	
	public void fill(int x0, int y0, int z0, int x1, int y1, int z1, int id) {
		for (int x = x0; x <= x1; x++) {
			for (int y = y0; y <= y1; y++) {
				for (int z = z0; z <= z1; z++) {
					world.setBlock(x, y, z, id, false, true);
				}
			}
		}
	}
	
}
