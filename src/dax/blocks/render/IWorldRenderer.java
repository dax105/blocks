package dax.blocks.render;

import dax.blocks.world.World;

public interface IWorldRenderer {
	public void renderWorld(float partialTickTime, World world, RenderEngine engine);
}
