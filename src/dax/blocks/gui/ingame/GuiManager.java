package dax.blocks.gui.ingame;

import dax.blocks.render.IRenderable;
import dax.blocks.world.World;

public class GuiManager implements IRenderable {
	World world;
	GuiScreen currentGuiScreen;
	
	public GuiManager(World w) {
		this.world = w;
	}
	
	
	@Override
	public void onTick() {
	}

	@Override
	public void onRenderTick(float partialTickTime) {
	}

	@Override
	public void renderWorld(float partialTickTime) {
	}

	@Override
	public void renderGui(float partialTickTime) {
	}

}
