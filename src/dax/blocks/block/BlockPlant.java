package dax.blocks.block;

import dax.blocks.block.renderer.BlockRendererPlant;
import dax.blocks.render.RenderPass;
import dax.blocks.world.World;

public class BlockPlant extends Block {

	public BlockPlant(int id) {
		super(id);
		setOpaque(false);
		setOccluder(false);
		setRenderPass(RenderPass.TRANSPARENT);
		setCollidable(false);
		setRenderer(new BlockRendererPlant());
	}

	@Override
	public void onTick(int x, int y, int z, World world) {
		if (world.getBlock(x, y-1, z) == 0) {
			world.setBlock(x, y, z, 0, true, true);
		}
	}

	@Override
	public void onRenderTick(float partialTickTime, int x, int y, int z, World world) {
	}

	@Override
	public void onClicked(int button, int x, int y, int z, World world) {
		world.setData(x, y, z, "spec_tex", "true");
		world.setChunkDirty(x >> 4, y/16, z >> 4);
	}

}
