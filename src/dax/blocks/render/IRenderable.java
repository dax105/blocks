package dax.blocks.render;

public interface IRenderable extends ITickListener {
	public void renderWorld(float partialTickTime);
	public void renderGui(float partialTickTime);
}