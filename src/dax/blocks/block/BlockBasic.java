package dax.blocks.block;

import org.lwjgl.opengl.GL11;

import dax.blocks.TextureManager;
import dax.blocks.world.World;

public class BlockBasic extends Block {

	protected float lightColorR = 1;
	protected float lightColorG = 1;
	protected float lightColorB = 1;
	
	public BlockBasic(int id) {
		super(id);
	}

	@Override
	public void renderFront(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp, World w) {
		GL11.glNormal3f(0, 0, 1);
		GL11.glTexCoord2f(TextureManager.getX2(sideTexture), TextureManager.getY1(sideTexture));
		addVertexWithAO(x+1, y+1, z+1, xp, zp, xpzp, lightColorR, lightColorG, lightColorB);
		//GL11.glVertex3f(x+1.0f, y+1f, z+1.0f); // Top Right Of The Quad (Front)
		GL11.glTexCoord2f(TextureManager.getX1(sideTexture), TextureManager.getY1(sideTexture));
		addVertexWithAO(x, y+1, z+1, xn, zp, xnzp, lightColorR, lightColorG, lightColorB);
		//GL11.glVertex3f(x+0f, y+1f, z+1.0f); // Top Left Of The Quad (Front)
		GL11.glTexCoord2f(TextureManager.getX1(sideTexture), TextureManager.getY2(sideTexture));
		addVertexWithAO(x, y, z+1, xn, zn, xnzn, lightColorR, lightColorG, lightColorB);
		//GL11.glVertex3f(x+0f, y, z+1.0f); // Bottom Left Of The Quad (Front)
		GL11.glTexCoord2f(TextureManager.getX2(sideTexture), TextureManager.getY2(sideTexture));
		addVertexWithAO(x+1, y, z+1, xp, zn, xpzn, lightColorR, lightColorG, lightColorB);
		//GL11.glVertex3f(x+1.0f, y, z+1.0f); // Bottom Right Of The Quad (Front)
	}

	@Override
	public void renderBack(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp, World w) {
		GL11.glNormal3f(0, 0, -1);
		GL11.glTexCoord2f(TextureManager.getX1(sideTexture), TextureManager.getY2(sideTexture));
		//GL11.glVertex3f(x+1.0f, y, z+0f); // Bottom Left Of The Quad (Back)
		addVertexWithAO(x+1, y, z, xp, zn, xpzn, lightColorR, lightColorG, lightColorB);
		GL11.glTexCoord2f(TextureManager.getX2(sideTexture), TextureManager.getY2(sideTexture));
		//GL11.glVertex3f(x+0f, y, z+0f); // Bottom Right Of The Quad (Back)
		addVertexWithAO(x, y, z, xn, zn, xnzn, lightColorR, lightColorG, lightColorB);
		GL11.glTexCoord2f(TextureManager.getX2(sideTexture), TextureManager.getY1(sideTexture));
		//GL11.glVertex3f(x+0f, y+1f, z+0f); // Top Right Of The Quad (Back)
		addVertexWithAO(x, y+1, z, xn, zp, xnzp, lightColorR, lightColorG, lightColorB);
		GL11.glTexCoord2f(TextureManager.getX1(sideTexture), TextureManager.getY1(sideTexture));
		//GL11.glVertex3f(x+1.0f, y+1f, z+0f); // Top Left Of The Quad (Back)
		addVertexWithAO(x+1, y+1, z, xp, zp, xpzp, lightColorR, lightColorG, lightColorB);
	}

	@Override
	public void renderRight(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp, World w) {
		GL11.glNormal3f(1, 0, 0);
		GL11.glTexCoord2f(TextureManager.getX2(sideTexture), TextureManager.getY1(sideTexture));
		//GL11.glVertex3f(x+1.0f, y+1f, z+0f); // Top Right Of The Quad (Right)
		addVertexWithAO(x+1, y+1, z, xn, zn, xnzn, lightColorR, lightColorG, lightColorB);
		GL11.glTexCoord2f(TextureManager.getX1(sideTexture), TextureManager.getY1(sideTexture));
		//GL11.glVertex3f(x+1.0f, y+1f, z+1.0f); // Top Left Of The Quad (Right)
		addVertexWithAO(x+1, y+1, z+1, xp, zn, xpzn, lightColorR, lightColorG, lightColorB);
		GL11.glTexCoord2f(TextureManager.getX1(sideTexture), TextureManager.getY2(sideTexture));
		//GL11.glVertex3f(x+1.0f, y, z+1.0f); // Bottom Left Of The Quad (Right)
		addVertexWithAO(x+1, y, z+1, xp, zp, xpzp, lightColorR, lightColorG, lightColorB);
		GL11.glTexCoord2f(TextureManager.getX2(sideTexture), TextureManager.getY2(sideTexture));
		//GL11.glVertex3f(x+1.0f, y, z+0f); // Bottom Right Of The Quad (Right)
		addVertexWithAO(x+1, y, z, xn, zp, xnzp, lightColorR, lightColorG, lightColorB);
	}

	@Override
	public void renderLeft(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp, World w) {
		GL11.glNormal3f(-1, 0, 0);
		GL11.glTexCoord2f(TextureManager.getX2(sideTexture), TextureManager.getY1(sideTexture));
		//GL11.glVertex3f(x+0f, y+1f, z+1.0f); // Top Right Of The Quad (Left)
		addVertexWithAO(x, y+1, z+1, xp, zn, xpzn, lightColorR, lightColorG, lightColorB);
		GL11.glTexCoord2f(TextureManager.getX1(sideTexture), TextureManager.getY1(sideTexture));
		//GL11.glVertex3f(x+0f, y+1f, z+0f); // Top Left Of The Quad (Left)
		addVertexWithAO(x, y+1, z, xn, zn, xnzn, lightColorR, lightColorG, lightColorB);
		GL11.glTexCoord2f(TextureManager.getX1(sideTexture), TextureManager.getY2(sideTexture));
		//GL11.glVertex3f(x+0f, y, z+0f); // Bottom Left Of The Quad (Left)
		addVertexWithAO(x, y, z, xn, zp, xnzp, lightColorR, lightColorG, lightColorB);
		GL11.glTexCoord2f(TextureManager.getX2(sideTexture), TextureManager.getY2(sideTexture));
		//GL11.glVertex3f(x+0f, y, z+1.0f); // Bottom Right Of The Quad (Left)
		addVertexWithAO(x, y, z+1, xp, zp, xpzp, lightColorR, lightColorG, lightColorB);	
	}

	@Override
	public void renderTop(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp, World w) {
		GL11.glNormal3f(0, 1, 0);
		GL11.glTexCoord2f(TextureManager.getX2(topTexture), TextureManager.getY1(topTexture));
		//GL11.glVertex3f(x+1.0f, y+1f, z+0f); // Top Right Of The Quad (Top)
		addVertexWithAO(x+1, y+1, z, xp, zn, xpzn, lightColorR, lightColorG, lightColorB);
		GL11.glTexCoord2f(TextureManager.getX2(topTexture), TextureManager.getY2(topTexture));
		//GL11.glVertex3f(x+0f, y+1f, z+0f); // Top Left Of The Quad (Top)
		addVertexWithAO(x, y+1, z, xn, zn, xnzn, lightColorR, lightColorG, lightColorB);
		GL11.glTexCoord2f(TextureManager.getX1(topTexture), TextureManager.getY2(topTexture));
		//GL11.glVertex3f(x+0f, y+1f, z+1.0f); // Bottom Left Of The Quad (Top)
		addVertexWithAO(x, y+1, z+1, xn, zp, xnzp, lightColorR, lightColorG, lightColorB);
		GL11.glTexCoord2f(TextureManager.getX1(topTexture), TextureManager.getY1(topTexture));
		//GL11.glVertex3f(x+1.0f, y+1f, z+1.0f); // Bottom Right Of The Quad (Top)
		addVertexWithAO(x+1, y+1, z+1, xp, zp, xpzp, lightColorR, lightColorG, lightColorB);
	}

	@Override
	public void renderBottom(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp, World w) {
		GL11.glNormal3f(0, -1, 0);
		GL11.glTexCoord2f(TextureManager.getX1(bottomTexture), TextureManager.getY2(bottomTexture));
		addVertexWithAO(x+1, y, z+1, xp, zp, xpzp, lightColorR, lightColorG, lightColorB);
		//GL11.glVertex3f(x+1.0f, y, z+1.0f); // Top Right Of The Quad (Bottom)
		GL11.glTexCoord2f(TextureManager.getX1(bottomTexture), TextureManager.getY1(bottomTexture));
		addVertexWithAO(x, y, z+1, xn, zp, xnzp, lightColorR, lightColorG, lightColorB);
		//GL11.glVertex3f(x+0f, y, z+1.0f); // Top Left Of The Quad (Bottom)
		GL11.glTexCoord2f(TextureManager.getX2(bottomTexture), TextureManager.getY1(bottomTexture));
		addVertexWithAO(x, y, z, xn, zn, xnzn, lightColorR, lightColorG, lightColorB);
		//GL11.glVertex3f(x+0f, y, z+0f); // Bottom Left Of The Quad (Bottom)
		GL11.glTexCoord2f(TextureManager.getX2(bottomTexture), TextureManager.getY2(bottomTexture));
		addVertexWithAO(x+1, y, z, xp, zn, xpzn, lightColorR, lightColorG, lightColorB);
		//GL11.glVertex3f(x+1.0f, y, z+0f); // Bottom Right Of The Quad (Bottom)	
	}

	@Override
	public void renderIndependent(int x, int y, int z, World w) {
		
	}


	@Override
	public void onRenderTick(float partialTickTime, int x, int y, int z, World world) {
	}


	@Override
	public void onTick(int x, int y, int z, World world) {
	}

	@Override
	public void onClicked(int button, int x, int y, int z, World world) {
	}

}
