package dax.blocks.overlay;

import dax.blocks.TextureManager;
import dax.blocks.movable.entity.Entity;
import dax.blocks.render.IOverlayRenderer;
import dax.blocks.util.GLHelper;

public class BasicLifesOverlay implements IOverlayRenderer {

	private Entity entity;
	private int x;
	private int y;
	
	public BasicLifesOverlay(Entity entity, int x, int y) {
		this.entity = entity;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void renderOverlay(float partialTickTime) {
		GLHelper.drawTexture(TextureManager.life_zero, x, y);
		GLHelper.drawTextureCropped(TextureManager.life_full, 
				x, y, this.entity.getLifes(), 1);
	}

}
