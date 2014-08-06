package dax.blocks.block.renderer;

import dax.blocks.TextureManager;
import dax.blocks.block.Block;
import dax.blocks.data.DataFlags;
import dax.blocks.render.IChunkRenderer;
import dax.blocks.world.World;

public class BlockRendererPlant implements IBlockRenderer {

	@Override
	public void render(IChunkRenderer renderer, World world, Block block, int x, int y, int z) {
		renderer.normal(0, 0, 0);
		renderer.color(1, 1, 1);
		
		int lastTexture = block.getSideTexture();
		if(world.getDataBoolean(x, y, z, DataFlags.SPECIAL_TEXTURE)) {
			block.setSideTexture(2);
		}	
		
		renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
		renderer.vertex(x+0, y+1, z+0);
		renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
		renderer.vertex(x+1, y+1, z+1);
		renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
		renderer.vertex(x+1, y+0, z+1);
		renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
		renderer.vertex(x+0, y+0, z+0);
		
		renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
		renderer.vertex(x+0, y+1, z+0);
		renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
		renderer.vertex(x+0, y+0, z+0);
		renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
		renderer.vertex(x+1, y+0, z+1);
		renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
		renderer.vertex(x+1, y+1, z+1);
		
		renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
		renderer.vertex(x+0, y+1, z+1);
		renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
		renderer.vertex(x+1, y+1, z+0);
		renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
		renderer.vertex(x+1, y+0, z+0);
		renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
		renderer.vertex(x+0, y+0, z+1);
		
		renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
		renderer.vertex(x+0, y+1, z+1);
		renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
		renderer.vertex(x+0, y+0, z+1);
		renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
		renderer.vertex(x+1, y+0, z+0);
		renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
		renderer.vertex(x+1, y+1, z+0);
		
		block.setSideTexture(lastTexture);
	}

	@Override
	public void preRender(World world, Block block, int x, int y, int z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postRender(World world, Block block, int x, int y, int z) {
		// TODO Auto-generated method stub
		
	}

}
