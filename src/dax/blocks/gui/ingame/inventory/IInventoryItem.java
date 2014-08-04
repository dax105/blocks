package dax.blocks.gui.ingame.inventory;

import org.newdawn.slick.opengl.Texture;

import dax.blocks.render.IRenderable;

public interface IInventoryItem extends IRenderable {
	public Texture[] getTextures();
	public int getItemID();
}
