package cz.dat.oots.overlay;

import cz.dat.oots.TextureManager;
import cz.dat.oots.movable.entity.Entity;
import cz.dat.oots.render.IOverlayRenderer;
import cz.dat.oots.util.GLHelper;

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
