package dax.blocks.block.renderer;

import dax.blocks.TextureManager;
import dax.blocks.block.Block;
import dax.blocks.render.IChunkRenderer;
import dax.blocks.world.World;

public class BlockRendererFluid implements IBlockRenderer {

	@Override
	public void render(IChunkRenderer renderer, World world, Block block, int x, int y, int z) {
		int blockID = block.getID();
		
		float height = 0.85f;
		
		int blockIdAbove = world.getBlock(x, y + 1, z);
		//Block blockAbove = world.getBlockObject(blockIdAbove);

		int blockIdBelow = world.getBlock(x, y - 1, z);
		Block blockBelow = world.getBlockObject(blockIdBelow);

		int blockIdInFront = world.getBlock(x, y, z + 1);
		Block blockInFront = world.getBlockObject(blockIdInFront);

		int blockIdBehind = world.getBlock(x, y, z - 1);
		Block blockBehind = world.getBlockObject(blockIdBehind);

		int blockIdOnRight = world.getBlock(x + 1, y, z);
		Block blockOnRight = world.getBlockObject(blockIdOnRight);

		int blockIdOnLeft = world.getBlock(x - 1, y, z);
		Block blockOnLeft = world.getBlockObject(blockIdOnLeft);
		
		boolean sameAbove = blockID == blockIdAbove;

		//TODO GL20.glVertexAttrib1f(attrib, blockID);
		
		if (blockIdAbove == 0 || !sameAbove) {
			boolean xnzn = world.isOccluder(x - 1, y + 1, z - 1);
			boolean zn = world.isOccluder(x, y + 1, z - 1);
			boolean xpzn = world.isOccluder(x + 1, y + 1, z - 1);
			boolean xn = world.isOccluder(x - 1, y + 1, z);
			boolean xp = world.isOccluder(x + 1, y + 1, z);
			boolean xnzp = world.isOccluder(x - 1, y + 1, z + 1);
			boolean zp = world.isOccluder(x, y + 1, z + 1);
			boolean xpzp = world.isOccluder(x + 1, y + 1, z + 1);
			
			renderer.normal(0, 1, 0);
			renderer.texCoord(TextureManager.getX2(block.getTopTexture()), TextureManager.getY1(block.getTopTexture()));
			renderer.vertexWithAO(x+1, y+height, z, xp, zn, xpzn);
			renderer.texCoord(TextureManager.getX2(block.getTopTexture()), TextureManager.getY2(block.getTopTexture()));
			renderer.vertexWithAO(x, y+height, z, xn, zn, xnzn);
			renderer.texCoord(TextureManager.getX1(block.getTopTexture()), TextureManager.getY2(block.getTopTexture()));
			renderer.vertexWithAO(x, y+height, z+1, xn, zp, xnzp);
			renderer.texCoord(TextureManager.getX1(block.getTopTexture()), TextureManager.getY1(block.getTopTexture()));
			renderer.vertexWithAO(x+1, y+height, z+1, xp, zp, xpzp);
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
			renderer.texCoord(TextureManager.getX1(block.getBottomTexture()), TextureManager.getY2(block.getBottomTexture()));
			renderer.vertexWithAO(x+1, y, z+1, xp, zp, xpzp);
			renderer.texCoord(TextureManager.getX1(block.getBottomTexture()), TextureManager.getY1(block.getBottomTexture()));
			renderer.vertexWithAO(x, y, z+1, xn, zp, xnzp);
			renderer.texCoord(TextureManager.getX2(block.getBottomTexture()), TextureManager.getY1(block.getBottomTexture()));
			renderer.vertexWithAO(x, y, z, xn, zn, xnzn);
			renderer.texCoord(TextureManager.getX2(block.getBottomTexture()), TextureManager.getY2(block.getBottomTexture()));
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
			
			if (sameAbove) {
				renderer.normal(0, 0, 1);
				renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
				renderer.vertexWithAO(x+1, y+1, z+1, xp, zp, xpzp);
				renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
				renderer.vertexWithAO(x, y+1, z+1, xn, zp, xnzp);
				renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
				renderer.vertexWithAO(x, y, z+1, xn, zn, xnzn);
				renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
				renderer.vertexWithAO(x+1, y, z+1, xp, zn, xpzn);
			} else {
				renderer.normal(0, 0, 1);
				renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
				renderer.vertexWithAO(x+1, y+height, z+1, xp, zp, xpzp);
				renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
				renderer.vertexWithAO(x, y+height, z+1, xn, zp, xnzp);
				renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
				renderer.vertexWithAO(x, y, z+1, xn, zn, xnzn);
				renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
				renderer.vertexWithAO(x+1, y, z+1, xp, zn, xpzn);
			}
		} else if (blockID == blockIdInFront && sameAbove && world.getBlock(x, y + 1, z + 1) != blockID) {
			renderer.normal(0, 0, 1);
			renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
			renderer.vertex(x+1, y+1, z+1);
			renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
			renderer.vertex(x, y+1, z+1);
			renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
			renderer.vertex(x, y+height, z+1);
			renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
			renderer.vertex(x+1, y+height, z+1);
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
			
			if (sameAbove) {
			renderer.normal(0, 0, -1);
			renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
			renderer.vertexWithAO(x+1, y, z, xp, zn, xpzn);
			renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
			renderer.vertexWithAO(x, y, z, xn, zn, xnzn);
			renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
			renderer.vertexWithAO(x, y+1, z, xn, zp, xnzp);
			renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
			renderer.vertexWithAO(x+1, y+1, z, xp, zp, xpzp);
			} else {
				renderer.normal(0, 0, -1);
				renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
				renderer.vertexWithAO(x+1, y, z, xp, zn, xpzn);
				renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
				renderer.vertexWithAO(x, y, z, xn, zn, xnzn);
				renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
				renderer.vertexWithAO(x, y+height, z, xn, zp, xnzp);
				renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
				renderer.vertexWithAO(x+1, y+height, z, xp, zp, xpzp);
			}
		} else if (blockID == blockIdBehind && sameAbove && world.getBlock(x, y + 1, z - 1) != blockID) {
			renderer.normal(0, 0, -1);
			renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
			renderer.vertex(x+1, y + height, z);
			renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
			renderer.vertex(x, y + height, z);
			renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
			renderer.vertex(x, y+1, z);
			renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
			renderer.vertex(x+1, y+1, z);
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
			
			if (sameAbove) {
			renderer.normal(1, 0, 0);
			renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
			renderer.vertexWithAO(x+1, y+1, z, xn, zn, xnzn);
			renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
			renderer.vertexWithAO(x+1, y+1, z+1, xp, zn, xpzn);
			renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
			renderer.vertexWithAO(x+1, y, z+1, xp, zp, xpzp);
			renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
			renderer.vertexWithAO(x+1, y, z, xn, zp, xnzp);
			} else {
				renderer.normal(1, 0, 0);
				renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
				renderer.vertexWithAO(x+1, y+height, z, xn, zn, xnzn);
				renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
				renderer.vertexWithAO(x+1, y+height, z+1, xp, zn, xpzn);
				renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
				renderer.vertexWithAO(x+1, y, z+1, xp, zp, xpzp);
				renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
				renderer.vertexWithAO(x+1, y, z, xn, zp, xnzp);
			}
		} else if (blockID == blockIdOnRight && sameAbove && world.getBlock(x + 1, y + 1, z) != blockID) {
			renderer.normal(1, 0, 0);
			renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
			renderer.vertex(x+1, y+1, z);
			renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
			renderer.vertex(x+1, y+1, z+1);
			renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
			renderer.vertex(x+1, y+height, z+1);
			renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
			renderer.vertex(x+1, y+height, z);
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

			if (sameAbove) {
			renderer.normal(-1, 0, 0);
			renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
			renderer.vertexWithAO(x, y+1, z+1, xp, zn, xpzn);
			renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
			renderer.vertexWithAO(x, y+1, z, xn, zn, xnzn);
			renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
			renderer.vertexWithAO(x, y, z, xn, zp, xnzp);
			renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
			renderer.vertexWithAO(x, y, z+1, xp, zp, xpzp);
			} else {
				renderer.normal(-1, 0, 0);
				renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
				renderer.vertexWithAO(x, y+height, z+1, xp, zn, xpzn);
				renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
				renderer.vertexWithAO(x, y+height, z, xn, zn, xnzn);
				renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
				renderer.vertexWithAO(x, y, z, xn, zp, xnzp);
				renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
				renderer.vertexWithAO(x, y, z+1, xp, zp, xpzp);
			} 
		} else if (blockID == blockIdOnLeft && sameAbove && world.getBlock(x - 1, y + 1, z) != blockID) {
			renderer.normal(-1, 0, 0);
			renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
			renderer.vertex(x, y+1, z+1);
			renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY1(block.getSideTexture()));
			renderer.vertex(x, y+1, z);
			renderer.texCoord(TextureManager.getX1(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
			renderer.vertex(x, y + height, z);
			renderer.texCoord(TextureManager.getX2(block.getSideTexture()), TextureManager.getY2(block.getSideTexture()));
			renderer.vertex(x, y + height, z+1);
		}
		
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
