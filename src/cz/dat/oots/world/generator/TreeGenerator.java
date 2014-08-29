package cz.dat.oots.world.generator;

import java.util.Random;

import cz.dat.oots.world.IDRegister;
import cz.dat.oots.world.World;

public class TreeGenerator {

	private World world;
	
	public TreeGenerator(World world) {
		this.world = world;
	}
	
	public void generateTree(int x, int y, int z) {
		Random rand = new Random();
		
		int trunkHeight = 5 + rand.nextInt(3);
		int leavesStart = trunkHeight - 1 - rand.nextInt(2);
		int leavesEnd = trunkHeight + 2 + rand.nextInt(1);
		
		int leaves = IDRegister.leaves.getID();
		this.fill(x-2, y+leavesStart, z-2, x+2, y+leavesEnd-2, z+2, leaves);
		this.fill(x-2, y+leavesStart+2, z-1, x+2, y+leavesEnd-1, z+1, leaves);
		this.fill(x-1, y+leavesStart+2, z-2, x+1, y+leavesEnd-1, z+2, leaves);
		this.world.setBlock(x-1, y+leavesEnd, z, leaves, false, true);
		this.world.setBlock(x+1, y+leavesEnd, z, leaves, false, true);
		this.world.setBlock(x, y+leavesEnd, z-1, leaves, false, true);
		this.world.setBlock(x, y+leavesEnd, z+1, leaves, false, true);
		this.world.setBlock(x, y+leavesEnd, z, leaves, false, true);
		
		this.fill(x, y, z, x, y+trunkHeight, z, IDRegister.log.getID());
	}
	
	public void fill(int x0, int y0, int z0, int x1, int y1, int z1, int id) {
		for(int x = x0; x <= x1; x++) {
			for(int y = y0; y <= y1; y++) {
				for(int z = z0; z <= z1; z++) {
					this.world.setBlock(x, y, z, id, false, true);
				}
			}
		}
	}
	
}
