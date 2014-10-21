package cz.dat.oots.block.renderer;

import cz.dat.oots.block.Block;
import cz.dat.oots.render.IChunkRenderer;
import cz.dat.oots.world.World;

public interface IBlockRenderer {

	public void preRender(World world, Block block, int x, int y, int z);

	public void render(IChunkRenderer chunkRenderer, World world, Block block,
			int x, int y, int z);

	public void postRender(World world, Block block, int x, int y, int z);

}
