package dax.blocks.block.renderer;

import dax.blocks.block.Block;
import dax.blocks.render.IChunkRenderer;
import dax.blocks.world.World;

public interface IBlockRenderer {

	public void preRender(World world, Block block, int x, int y, int z);
	public void render(IChunkRenderer chunkRenderer, World world, Block block, 
			int x, int y, int z);
	public void postRender(World world, Block block, int x, int y, int z);
	
}
