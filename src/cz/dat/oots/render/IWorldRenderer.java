package cz.dat.oots.render;

import cz.dat.oots.world.World;

public interface IWorldRenderer {
	public void renderWorld(float partialTickTime, World world, RenderEngine engine);
}
