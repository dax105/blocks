package dax.blocks.block;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import dax.blocks.Game;
import dax.blocks.TextureManager;
import dax.blocks.render.ChunkDisplayList;

public class BlockFluid extends BlockBasic {

	public BlockFluid(int id, int texture) {
		super(id, texture, false, true, ChunkDisplayList.PASS_TRANSLUCENT);
	}
	
	@Override
	public void renderTop(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp) {
		
	}
	
	@Override
	public void renderIndependent(int x, int y, int z) {
		GL11.glNormal3f(0, 1, 0);


		GL11.glTexCoord2f(TextureManager.getX2(topTexture), TextureManager.getY1(topTexture));
		//GL11.glVertex3f(x+1.0f, y+1f, z+0f); // Top Right Of The Quad (Top)
		addVertexWithAO(x+1, y+0.85f, z, false, false, false);
		GL11.glTexCoord2f(TextureManager.getX2(topTexture), TextureManager.getY2(topTexture));
		//GL11.glVertex3f(x+0f, y+1f, z+0f); // Top Left Of The Quad (Top)
		addVertexWithAO(x, y+0.85f, z, false, false, false);
		GL11.glTexCoord2f(TextureManager.getX1(topTexture), TextureManager.getY2(topTexture));
		//GL11.glVertex3f(x+0f, y+1f, z+1.0f); // Bottom Left Of The Quad (Top)
		addVertexWithAO(x, y+0.85f, z+1, false, false, false);
		GL11.glTexCoord2f(TextureManager.getX1(topTexture), TextureManager.getY1(topTexture));
		//GL11.glVertex3f(x+1.0f, y+1f, z+1.0f); // Bottom Right Of The Quad (Top)
		addVertexWithAO(x+1, y+0.85f, z+1, false, false, false);
	}
	
	@Override
	public float[] getVTop(int x, int y, int z) {
		float[] v = new float[12];

		v[0] = x + 1;
		v[1] = y + 0.85f;
		v[2] = z + 1;

		v[3] = x + 1;
		v[4] = y + 0.85f;
		v[5] = z;

		v[6] = x;
		v[7] = y + 0.85f;
		v[8] = z;

		v[9] = x;
		v[10] = y + 0.85f;
		v[11] = z + 1;

		return v;
	}

}
