package dax.blocks.block;

import org.lwjgl.opengl.GL11;
import dax.blocks.TextureManager;
import dax.blocks.render.RenderPass;
import dax.blocks.world.World;

public class BlockPlant extends Block {

	public BlockPlant(int id) {
		super(id);
		setOpaque(false);
		setOccluder(false);
		setRenderPass(RenderPass.PASS_TRANSPARENT);
	}
	
	@Override
	public void renderBack(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp) {
		
	}
	
	@Override
	public void renderFront(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp) {
		
	}
	
	@Override
	public void renderTop(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp) {
		
	}
	
	@Override
	public void renderBottom(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp) {
		
	}
	
	@Override
	public void renderRight(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp) {
		
	}
	
	@Override
	public void renderLeft(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp) {
		
	}

	@Override
	public void renderIndependent(int x, int y, int z) {

		GL11.glNormal3f(0, 0, 0);
		GL11.glColor3f(1, 1, 1);
		
		GL11.glTexCoord2f(TextureManager.getX1(this.sideTexture), TextureManager.getY1(this.sideTexture));
		GL11.glVertex3f(x+0, y+1, z+0);
		GL11.glTexCoord2f(TextureManager.getX2(this.sideTexture), TextureManager.getY1(this.sideTexture));
		GL11.glVertex3f(x+1, y+1, z+1);
		GL11.glTexCoord2f(TextureManager.getX2(this.sideTexture), TextureManager.getY2(this.sideTexture));
		GL11.glVertex3f(x+1, y+0, z+1);
		GL11.glTexCoord2f(TextureManager.getX1(this.sideTexture), TextureManager.getY2(this.sideTexture));
		GL11.glVertex3f(x+0, y+0, z+0);
		
		GL11.glTexCoord2f(TextureManager.getX2(this.sideTexture), TextureManager.getY1(this.sideTexture));
		GL11.glVertex3f(x+0, y+1, z+0);
		GL11.glTexCoord2f(TextureManager.getX2(this.sideTexture), TextureManager.getY2(this.sideTexture));
		GL11.glVertex3f(x+0, y+0, z+0);
		GL11.glTexCoord2f(TextureManager.getX1(this.sideTexture), TextureManager.getY2(this.sideTexture));
		GL11.glVertex3f(x+1, y+0, z+1);
		GL11.glTexCoord2f(TextureManager.getX1(this.sideTexture), TextureManager.getY1(this.sideTexture));
		GL11.glVertex3f(x+1, y+1, z+1);
		
		GL11.glTexCoord2f(TextureManager.getX1(this.sideTexture), TextureManager.getY1(this.sideTexture));
		GL11.glVertex3f(x+0, y+1, z+1);
		GL11.glTexCoord2f(TextureManager.getX2(this.sideTexture), TextureManager.getY1(this.sideTexture));
		GL11.glVertex3f(x+1, y+1, z+0);
		GL11.glTexCoord2f(TextureManager.getX2(this.sideTexture), TextureManager.getY2(this.sideTexture));
		GL11.glVertex3f(x+1, y+0, z+0);
		GL11.glTexCoord2f(TextureManager.getX1(this.sideTexture), TextureManager.getY2(this.sideTexture));
		GL11.glVertex3f(x+0, y+0, z+1);
		
		GL11.glTexCoord2f(TextureManager.getX2(this.sideTexture), TextureManager.getY1(this.sideTexture));
		GL11.glVertex3f(x+0, y+1, z+1);
		GL11.glTexCoord2f(TextureManager.getX2(this.sideTexture), TextureManager.getY2(this.sideTexture));
		GL11.glVertex3f(x+0, y+0, z+1);
		GL11.glTexCoord2f(TextureManager.getX1(this.sideTexture), TextureManager.getY2(this.sideTexture));
		GL11.glVertex3f(x+1, y+0, z+0);
		GL11.glTexCoord2f(TextureManager.getX1(this.sideTexture), TextureManager.getY1(this.sideTexture));
		GL11.glVertex3f(x+1, y+1, z+0);
		
	}

	@Override
	public void onTick(int x, int y, int z, World world) {
		if (world.getBlock(x, y-1, z) == 0) {
			world.setBlock(x, y, z, 0, true);
		}
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
