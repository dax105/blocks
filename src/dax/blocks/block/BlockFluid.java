package dax.blocks.block;

import dax.blocks.block.renderer.BlockRendererFluid;
import dax.blocks.render.RenderPass;
import dax.blocks.world.IDRegister;
import dax.blocks.world.World;

public class BlockFluid extends BlockBasic {

	public BlockFluid(String name, IDRegister r) {
		super(name, r);
		setCullSame(true);
		setOpaque(false);
		setOccluder(false);
		setRenderPass(RenderPass.TRANSLUCENT);
		setCollidable(false);
		setRenderer(new BlockRendererFluid());
	}
	
	@Override
	public void onTick(int x, int y, int z, World world) {
		if (world.getBlockObject(x, y-1, z) == IDRegister.water) {
			return;
		}
		
		Block b =world.getBlockObject(x, y - 1, z);
		boolean isPlant = (b != null && b instanceof BlockPlant);
		
		if (world.getBlock(x, y-1, z) == 0 || isPlant) {
			world.setBlock(x, y-1, z, IDRegister.water.getID(), true, true);
			return;
		}
		if (world.getBlock(x+1, y, z) == 0 || isPlant) {
			world.setBlock(x+1, y, z, IDRegister.water.getID(), true, true);
		}
		if (world.getBlock(x-1, y, z) == 0 || isPlant) {
			world.setBlock(x-1, y, z, IDRegister.water.getID(), true, true);
		}
		if (world.getBlock(x, y, z+1) == 0 || isPlant) {
			world.setBlock(x, y, z+1, IDRegister.water.getID(), true, true);
		}
		if (world.getBlock(x, y, z-1) == 0 || isPlant) {
			world.setBlock(x, y, z-1, IDRegister.water.getID(), true, true);
		}
	}

}
