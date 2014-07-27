package dax.blocks.block.renderer;

import dax.blocks.TextureManager;
import dax.blocks.block.Block;
import dax.blocks.render.IChunkRenderer;
import dax.blocks.world.World;

public class BlockRendererBasic implements IBlockRenderer {

	@Override
	public void render(IChunkRenderer renderer, World world, Block block, int x, int y, int z) {
		int blockID = block.getId();
		
		int blockIdAbove = world.getBlock(x, y + 1, z);
		Block blockAbove = Block.getBlock(blockIdAbove);

		int blockIdBelow = world.getBlock(x, y - 1, z);
		Block blockBelow = Block.getBlock(blockIdBelow);

		int blockIdInFront = world.getBlock(x, y, z + 1);
		Block blockInFront = Block.getBlock(blockIdInFront);

		int blockIdBehind = world.getBlock(x, y, z - 1);
		Block blockBehind = Block.getBlock(blockIdBehind);

		int blockIdOnRight = world.getBlock(x + 1, y, z);
		Block blockOnRight = Block.getBlock(blockIdOnRight);

		int blockIdOnLeft = world.getBlock(x - 1, y, z);
		Block blockOnLeft = Block.getBlock(blockIdOnLeft);

		//TODO GL20.glVertexAttrib1f(attrib, blockID);
		
		if (blockIdAbove == 0 || !blockAbove.isOpaque() && !(blockID == blockIdAbove && blockAbove.shouldCullSame())) {
			boolean xnzn = world.isOccluder(x - 1, y + 1, z - 1);
			boolean zn = world.isOccluder(x, y + 1, z - 1);
			boolean xpzn = world.isOccluder(x + 1, y + 1, z - 1);
			boolean xn = world.isOccluder(x - 1, y + 1, z);
			boolean xp = world.isOccluder(x + 1, y + 1, z);
			boolean xnzp = world.isOccluder(x - 1, y + 1, z + 1);
			boolean zp = world.isOccluder(x, y + 1, z + 1);
			boolean xpzp = world.isOccluder(x + 1, y + 1, z + 1);
			
			renderer.normal(0, 1, 0);
			renderer.texCoord(TextureManager.getX2(block.topTexture), TextureManager.getY1(block.topTexture));
			renderer.vertexWithAO(x+1, y+1, z, xp, zn, xpzn);
			renderer.texCoord(TextureManager.getX2(block.topTexture), TextureManager.getY2(block.topTexture));
			renderer.vertexWithAO(x, y+1, z, xn, zn, xnzn);
			renderer.texCoord(TextureManager.getX1(block.topTexture), TextureManager.getY2(block.topTexture));
			renderer.vertexWithAO(x, y+1, z+1, xn, zp, xnzp);
			renderer.texCoord(TextureManager.getX1(block.topTexture), TextureManager.getY1(block.topTexture));
			renderer.vertexWithAO(x+1, y+1, z+1, xp, zp, xpzp);
		}

		if (blockIdBelow == 0 || !blockBelow.isOpaque() && !(blockID == blockIdBelow && blockBelow.shouldCullSame())) {
			boolean xnzn = world.isOccluder(x - 1, y - 1, z - 1);
			boolean zn = world.isOccluder(x, y - 1, z - 1);
			boolean xpzn = world.isOccluder(x + 1, y - 1, z - 1);
			boolean xn = world.isOccluder(x - 1, y - 1, z);
			boolean xp = world.isOccluder(x + 1, y - 1, z);
			boolean xnzp = world.isOccluder(x - 1, y - 1, z + 1);
			boolean zp = world.isOccluder(x, y - 1, z + 1);
			boolean xpzp = world.isOccluder(x + 1, y - 1, z + 1);
			
			renderer.normal(0, -1, 0);
			renderer.texCoord(TextureManager.getX1(block.bottomTexture), TextureManager.getY2(block.bottomTexture));
			renderer.vertexWithAO(x+1, y, z+1, xp, zp, xpzp);
			renderer.texCoord(TextureManager.getX1(block.bottomTexture), TextureManager.getY1(block.bottomTexture));
			renderer.vertexWithAO(x, y, z+1, xn, zp, xnzp);
			renderer.texCoord(TextureManager.getX2(block.bottomTexture), TextureManager.getY1(block.bottomTexture));
			renderer.vertexWithAO(x, y, z, xn, zn, xnzn);
			renderer.texCoord(TextureManager.getX2(block.bottomTexture), TextureManager.getY2(block.bottomTexture));
			renderer.vertexWithAO(x+1, y, z, xp, zn, xpzn);
		}

		if (blockIdInFront == 0 || !blockInFront.isOpaque() && !(blockID == blockIdInFront && blockInFront.shouldCullSame())) {
			boolean xnzn = world.isOccluder(x - 1, y - 1, z + 1);
			boolean zn = world.isOccluder(x, y - 1, z + 1);
			boolean xpzn = world.isOccluder(x + 1, y - 1, z + 1);
			boolean xn = world.isOccluder(x - 1, y, z + 1);
			boolean xp = world.isOccluder(x + 1, y, z + 1);
			boolean xnzp = world.isOccluder(x - 1, y + 1, z + 1);
			boolean zp = world.isOccluder(x, y + 1, z + 1);
			boolean xpzp = world.isOccluder(x + 1, y + 1, z + 1);
			
			renderer.normal(0, 0, 1);
			renderer.texCoord(TextureManager.getX2(block.sideTexture), TextureManager.getY1(block.sideTexture));
			renderer.vertexWithAO(x+1, y+1, z+1, xp, zp, xpzp);
			renderer.texCoord(TextureManager.getX1(block.sideTexture), TextureManager.getY1(block.sideTexture));
			renderer.vertexWithAO(x, y+1, z+1, xn, zp, xnzp);
			renderer.texCoord(TextureManager.getX1(block.sideTexture), TextureManager.getY2(block.sideTexture));
			renderer.vertexWithAO(x, y, z+1, xn, zn, xnzn);
			renderer.texCoord(TextureManager.getX2(block.sideTexture), TextureManager.getY2(block.sideTexture));
			renderer.vertexWithAO(x+1, y, z+1, xp, zn, xpzn);
		}

		if (blockIdBehind == 0 || !blockBehind.isOpaque() && !(blockID == blockIdBehind && blockBehind.shouldCullSame())) {
			boolean xnzn = world.isOccluder(x - 1, y - 1, z - 1);
			boolean zn = world.isOccluder(x, y - 1, z - 1);
			boolean xpzn = world.isOccluder(x + 1, y - 1, z - 1);
			boolean xn = world.isOccluder(x - 1, y, z - 1);
			boolean xp = world.isOccluder(x + 1, y, z - 1);
			boolean xnzp = world.isOccluder(x - 1, y + 1, z - 1);
			boolean zp = world.isOccluder(x, y + 1, z - 1);
			boolean xpzp = world.isOccluder(x + 1, y + 1, z - 1);
			
			renderer.normal(0, 0, -1);
			renderer.texCoord(TextureManager.getX1(block.sideTexture), TextureManager.getY2(block.sideTexture));
			renderer.vertexWithAO(x+1, y, z, xp, zn, xpzn);
			renderer.texCoord(TextureManager.getX2(block.sideTexture), TextureManager.getY2(block.sideTexture));
			renderer.vertexWithAO(x, y, z, xn, zn, xnzn);
			renderer.texCoord(TextureManager.getX2(block.sideTexture), TextureManager.getY1(block.sideTexture));
			renderer.vertexWithAO(x, y+1, z, xn, zp, xnzp);
			renderer.texCoord(TextureManager.getX1(block.sideTexture), TextureManager.getY1(block.sideTexture));
			renderer.vertexWithAO(x+1, y+1, z, xp, zp, xpzp);
		}

		if (blockIdOnRight == 0 || !blockOnRight.isOpaque() && !(blockID == blockIdOnRight && blockOnRight.shouldCullSame())) {
			boolean xnzn = world.isOccluder(x + 1, y + 1, z - 1);
			boolean zn = world.isOccluder(x + 1, y + 1, z);
			boolean xpzn = world.isOccluder(x + 1, y + 1, z + 1);
			boolean xn = world.isOccluder(x + 1, y, z - 1);
			boolean xp = world.isOccluder(x + 1, y, z + 1);
			boolean xnzp = world.isOccluder(x + 1, y - 1, z - 1);
			boolean zp = world.isOccluder(x + 1, y - 1, z);
			boolean xpzp = world.isOccluder(x + 1, y - 1, z + 1);
			
			renderer.normal(1, 0, 0);
			renderer.texCoord(TextureManager.getX2(block.sideTexture), TextureManager.getY1(block.sideTexture));
			renderer.vertexWithAO(x+1, y+1, z, xn, zn, xnzn);
			renderer.texCoord(TextureManager.getX1(block.sideTexture), TextureManager.getY1(block.sideTexture));
			renderer.vertexWithAO(x+1, y+1, z+1, xp, zn, xpzn);
			renderer.texCoord(TextureManager.getX1(block.sideTexture), TextureManager.getY2(block.sideTexture));
			renderer.vertexWithAO(x+1, y, z+1, xp, zp, xpzp);
			renderer.texCoord(TextureManager.getX2(block.sideTexture), TextureManager.getY2(block.sideTexture));
			renderer.vertexWithAO(x+1, y, z, xn, zp, xnzp);
		}

		if (blockIdOnLeft == 0 || !blockOnLeft.isOpaque() && !(blockID == blockIdOnLeft && blockOnLeft.shouldCullSame())) {
			boolean xnzn = world.isOccluder(x - 1, y + 1, z - 1);
			boolean zn = world.isOccluder(x - 1, y + 1, z);
			boolean xpzn = world.isOccluder(x - 1, y + 1, z + 1);
			boolean xn = world.isOccluder(x - 1, y, z - 1);
			boolean xp = world.isOccluder(x - 1, y, z + 1);
			boolean xnzp = world.isOccluder(x - 1, y - 1, z - 1);
			boolean zp = world.isOccluder(x - 1, y - 1, z);
			boolean xpzp = world.isOccluder(x - 1, y - 1, z + 1);

			renderer.normal(-1, 0, 0);
			renderer.texCoord(TextureManager.getX2(block.sideTexture), TextureManager.getY1(block.sideTexture));
			renderer.vertexWithAO(x, y+1, z+1, xp, zn, xpzn);
			renderer.texCoord(TextureManager.getX1(block.sideTexture), TextureManager.getY1(block.sideTexture));
			renderer.vertexWithAO(x, y+1, z, xn, zn, xnzn);
			renderer.texCoord(TextureManager.getX1(block.sideTexture), TextureManager.getY2(block.sideTexture));
			renderer.vertexWithAO(x, y, z, xn, zp, xnzp);
			renderer.texCoord(TextureManager.getX2(block.sideTexture), TextureManager.getY2(block.sideTexture));
			renderer.vertexWithAO(x, y, z+1, xp, zp, xpzp);
		}
		
	}

}
