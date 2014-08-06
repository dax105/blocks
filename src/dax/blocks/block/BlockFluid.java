package dax.blocks.block;

import dax.blocks.block.renderer.BlockRendererFluid;
import dax.blocks.render.RenderPass;
import dax.blocks.world.World;

public class BlockFluid extends BlockBasic {

	public BlockFluid(int id) {
		super(id);
		setCullSame(true);
		setOpaque(false);
		setOccluder(false);
		setRenderPass(RenderPass.TRANSLUCENT);
		setCollidable(false);
		setRenderer(new BlockRendererFluid());
	}
	
	@Override
	public void onTick(int x, int y, int z, World world) {
		if(world.getBlock(x, y-1, z) == Block.water.getId()) {
			return;
		}
		
		Block b = Block.getBlock(world.getBlock(x, y - 1, z));
		boolean isPlant = (b != null && b instanceof BlockPlant);
		
		if(world.getBlock(x, y-1, z) == 0 || isPlant) {
			world.setBlock(x, y-1, z, Block.water.getId(), true, true);
			return;
		}
		if(world.getBlock(x+1, y, z) == 0 || isPlant) {
			world.setBlock(x+1, y, z, Block.water.getId(), true, true);
		}
		if(world.getBlock(x-1, y, z) == 0 || isPlant) {
			world.setBlock(x-1, y, z, Block.water.getId(), true, true);
		}
		if(world.getBlock(x, y, z+1) == 0 || isPlant) {
			world.setBlock(x, y, z+1, Block.water.getId(), true, true);
		}
		if(world.getBlock(x, y, z-1) == 0 || isPlant) {
			world.setBlock(x, y, z-1, Block.water.getId(), true, true);
		}
	}

}
