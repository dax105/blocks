package dax.blocks.render;

import dax.blocks.world.World;

public interface IRenderableBlock extends IRenderable {
	public void onTick(int x, int y, int z, World world);
}
