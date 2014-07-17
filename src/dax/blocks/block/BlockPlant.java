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
		setRenderPass(RenderPass.TRANSPARENT);
		setCollidable(false);
	}
	

	@Override
	public void renderIndependent(int x, int y, int z, World w) {

		GL11.glNormal3f(0, 0, 0);
		GL11.glColor3f(1, 1, 1);
		
		int lastTexture = this.sideTexture;
		if(w.getDataBoolean(x, y, z, "spec_tex"))
			this.setSideTexture(2);
		
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
		
		this.setSideTexture(lastTexture);
	}

	@Override
	public void onTick(int x, int y, int z, World world) {
		if (world.getBlock(x, y-1, z) == 0) {
			world.setBlock(x, y, z, 0, true, true);
		}
	}


	@Override
	public void onRenderTick(float partialTickTime, int x, int y, int z, World world) {
	}

	@Override
	public void onClicked(int button, int x, int y, int z, World world) {
		world.setData(x, y, z, "spec_tex", "true");
		world.setChunkDirty(x >> 4, y/16, z >> 4);
	}


	@Override
	public void renderFront(int x, int y, int z, boolean xnzn, boolean zn,
			boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp,
			boolean xpzp, World world) {
		
		
	}


	@Override
	public void renderBack(int x, int y, int z, boolean xnzn, boolean zn,
			boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp,
			boolean xpzp, World world) {
		
		
	}


	@Override
	public void renderRight(int x, int y, int z, boolean xnzn, boolean zn,
			boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp,
			boolean xpzp, World world) {
		
		
	}


	@Override
	public void renderLeft(int x, int y, int z, boolean xnzn, boolean zn,
			boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp,
			boolean xpzp, World world) {
		
		
	}


	@Override
	public void renderTop(int x, int y, int z, boolean xnzn, boolean zn,
			boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp,
			boolean xpzp, World world) {
		
		
	}


	@Override
	public void renderBottom(int x, int y, int z, boolean xnzn, boolean zn,
			boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp,
			boolean xpzp, World world) {
		
		
	}

}
