package dax.blocks.block.renderer;

import dax.blocks.render.IChunkRenderer;
import dax.blocks.world.World;

public interface IBlockRenderer {

	public void render(IChunkRenderer chunkRenderer, World world, int x, int y, int z);
	
}
