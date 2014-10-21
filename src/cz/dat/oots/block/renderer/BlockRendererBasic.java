package cz.dat.oots.block.renderer;

import cz.dat.oots.TextureManager;
import cz.dat.oots.block.Block;
import cz.dat.oots.render.IChunkRenderer;
import cz.dat.oots.world.World;

public class BlockRendererBasic implements IBlockRenderer {

	@Override
	public void render(IChunkRenderer renderer, World world, Block block,
			int x, int y, int z) {
		int blockID = block.getID();

		int blockIdAbove = world.getBlock(x, y + 1, z);
		Block blockAbove = world.getBlockObject(blockIdAbove);

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

		// TODO GL20.glVertexAttrib1f(attrib, blockID);

		if(blockIdAbove == 0 || !blockAbove.isOpaque()
				&& !(blockID == blockIdAbove && blockAbove.shouldCullSame())) {
			boolean xnzn = world.isOccluder(x - 1, y + 1, z - 1);
			boolean zn = world.isOccluder(x, y + 1, z - 1);
			boolean xpzn = world.isOccluder(x + 1, y + 1, z - 1);
			boolean xn = world.isOccluder(x - 1, y + 1, z);
			boolean xp = world.isOccluder(x + 1, y + 1, z);
			boolean xnzp = world.isOccluder(x - 1, y + 1, z + 1);
			boolean zp = world.isOccluder(x, y + 1, z + 1);
			boolean xpzp = world.isOccluder(x + 1, y + 1, z + 1);

			renderer.normal(0, 1, 0);
			renderer.texCoord(TextureManager.getX2(block.getTopTexture()),
					TextureManager.getY1(block.getTopTexture()));
			renderer.vertexWithColoredAO(x + 1, y + 1, z, block.getColorR(),
					block.getColorG(), block.getColorB(), xp, zn, xpzn);
			renderer.texCoord(TextureManager.getX2(block.getTopTexture()),
					TextureManager.getY2(block.getTopTexture()));
			renderer.vertexWithColoredAO(x, y + 1, z, block.getColorR(),
					block.getColorG(), block.getColorB(), xn, zn, xnzn);
			renderer.texCoord(TextureManager.getX1(block.getTopTexture()),
					TextureManager.getY2(block.getTopTexture()));
			renderer.vertexWithColoredAO(x, y + 1, z + 1, block.getColorR(),
					block.getColorG(), block.getColorB(), xn, zp, xnzp);
			renderer.texCoord(TextureManager.getX1(block.getTopTexture()),
					TextureManager.getY1(block.getTopTexture()));
			renderer.vertexWithColoredAO(x + 1, y + 1, z + 1,
					block.getColorR(), block.getColorG(), block.getColorB(),
					xp, zp, xpzp);
		}

		if(blockIdBelow == 0 || !blockBelow.isOpaque()
				&& !(blockID == blockIdBelow && blockBelow.shouldCullSame())) {
			boolean xnzn = world.isOccluder(x - 1, y - 1, z - 1);
			boolean zn = world.isOccluder(x, y - 1, z - 1);
			boolean xpzn = world.isOccluder(x + 1, y - 1, z - 1);
			boolean xn = world.isOccluder(x - 1, y - 1, z);
			boolean xp = world.isOccluder(x + 1, y - 1, z);
			boolean xnzp = world.isOccluder(x - 1, y - 1, z + 1);
			boolean zp = world.isOccluder(x, y - 1, z + 1);
			boolean xpzp = world.isOccluder(x + 1, y - 1, z + 1);

			renderer.normal(0, -1, 0);
			renderer.texCoord(TextureManager.getX1(block.getBottomTexture()),
					TextureManager.getY2(block.getBottomTexture()));
			renderer.vertexWithColoredAO(x + 1, y, z + 1, block.getColorR(),
					block.getColorG(), block.getColorB(), xp, zp, xpzp);
			renderer.texCoord(TextureManager.getX1(block.getBottomTexture()),
					TextureManager.getY1(block.getBottomTexture()));
			renderer.vertexWithColoredAO(x, y, z + 1, block.getColorR(),
					block.getColorG(), block.getColorB(), xn, zp, xnzp);
			renderer.texCoord(TextureManager.getX2(block.getBottomTexture()),
					TextureManager.getY1(block.getBottomTexture()));
			renderer.vertexWithColoredAO(x, y, z, block.getColorR(),
					block.getColorG(), block.getColorB(), xn, zn, xnzn);
			renderer.texCoord(TextureManager.getX2(block.getBottomTexture()),
					TextureManager.getY2(block.getBottomTexture()));
			renderer.vertexWithColoredAO(x + 1, y, z, block.getColorR(),
					block.getColorG(), block.getColorB(), xp, zn, xpzn);
		}

		if(blockIdInFront == 0
				|| !blockInFront.isOpaque()
				&& !(blockID == blockIdInFront && blockInFront.shouldCullSame())) {
			boolean xnzn = world.isOccluder(x - 1, y - 1, z + 1);
			boolean zn = world.isOccluder(x, y - 1, z + 1);
			boolean xpzn = world.isOccluder(x + 1, y - 1, z + 1);
			boolean xn = world.isOccluder(x - 1, y, z + 1);
			boolean xp = world.isOccluder(x + 1, y, z + 1);
			boolean xnzp = world.isOccluder(x - 1, y + 1, z + 1);
			boolean zp = world.isOccluder(x, y + 1, z + 1);
			boolean xpzp = world.isOccluder(x + 1, y + 1, z + 1);

			renderer.normal(0, 0, 1);
			renderer.texCoord(TextureManager.getX2(block.getSideTexture()),
					TextureManager.getY1(block.getSideTexture()));
			renderer.vertexWithColoredAO(x + 1, y + 1, z + 1,
					block.getColorR(), block.getColorG(), block.getColorB(),
					xp, zp, xpzp);
			renderer.texCoord(TextureManager.getX1(block.getSideTexture()),
					TextureManager.getY1(block.getSideTexture()));
			renderer.vertexWithColoredAO(x, y + 1, z + 1, block.getColorR(),
					block.getColorG(), block.getColorB(), xn, zp, xnzp);
			renderer.texCoord(TextureManager.getX1(block.getSideTexture()),
					TextureManager.getY2(block.getSideTexture()));
			renderer.vertexWithColoredAO(x, y, z + 1, block.getColorR(),
					block.getColorG(), block.getColorB(), xn, zn, xnzn);
			renderer.texCoord(TextureManager.getX2(block.getSideTexture()),
					TextureManager.getY2(block.getSideTexture()));
			renderer.vertexWithColoredAO(x + 1, y, z + 1, block.getColorR(),
					block.getColorG(), block.getColorB(), xp, zn, xpzn);
		}

		if(blockIdBehind == 0 || !blockBehind.isOpaque()
				&& !(blockID == blockIdBehind && blockBehind.shouldCullSame())) {
			boolean xnzn = world.isOccluder(x - 1, y - 1, z - 1);
			boolean zn = world.isOccluder(x, y - 1, z - 1);
			boolean xpzn = world.isOccluder(x + 1, y - 1, z - 1);
			boolean xn = world.isOccluder(x - 1, y, z - 1);
			boolean xp = world.isOccluder(x + 1, y, z - 1);
			boolean xnzp = world.isOccluder(x - 1, y + 1, z - 1);
			boolean zp = world.isOccluder(x, y + 1, z - 1);
			boolean xpzp = world.isOccluder(x + 1, y + 1, z - 1);

			renderer.normal(0, 0, -1);
			renderer.texCoord(TextureManager.getX1(block.getSideTexture()),
					TextureManager.getY2(block.getSideTexture()));
			renderer.vertexWithColoredAO(x + 1, y, z, block.getColorR(),
					block.getColorG(), block.getColorB(), xp, zn, xpzn);
			renderer.texCoord(TextureManager.getX2(block.getSideTexture()),
					TextureManager.getY2(block.getSideTexture()));
			renderer.vertexWithColoredAO(x, y, z, block.getColorR(),
					block.getColorG(), block.getColorB(), xn, zn, xnzn);
			renderer.texCoord(TextureManager.getX2(block.getSideTexture()),
					TextureManager.getY1(block.getSideTexture()));
			renderer.vertexWithColoredAO(x, y + 1, z, block.getColorR(),
					block.getColorG(), block.getColorB(), xn, zp, xnzp);
			renderer.texCoord(TextureManager.getX1(block.getSideTexture()),
					TextureManager.getY1(block.getSideTexture()));
			renderer.vertexWithColoredAO(x + 1, y + 1, z, block.getColorR(),
					block.getColorG(), block.getColorB(), xp, zp, xpzp);
		}

		if(blockIdOnRight == 0
				|| !blockOnRight.isOpaque()
				&& !(blockID == blockIdOnRight && blockOnRight.shouldCullSame())) {
			boolean xnzn = world.isOccluder(x + 1, y + 1, z - 1);
			boolean zn = world.isOccluder(x + 1, y + 1, z);
			boolean xpzn = world.isOccluder(x + 1, y + 1, z + 1);
			boolean xn = world.isOccluder(x + 1, y, z - 1);
			boolean xp = world.isOccluder(x + 1, y, z + 1);
			boolean xnzp = world.isOccluder(x + 1, y - 1, z - 1);
			boolean zp = world.isOccluder(x + 1, y - 1, z);
			boolean xpzp = world.isOccluder(x + 1, y - 1, z + 1);

			renderer.normal(1, 0, 0);
			renderer.texCoord(TextureManager.getX2(block.getSideTexture()),
					TextureManager.getY1(block.getSideTexture()));
			renderer.vertexWithColoredAO(x + 1, y + 1, z, block.getColorR(),
					block.getColorG(), block.getColorB(), xn, zn, xnzn);
			renderer.texCoord(TextureManager.getX1(block.getSideTexture()),
					TextureManager.getY1(block.getSideTexture()));
			renderer.vertexWithColoredAO(x + 1, y + 1, z + 1,
					block.getColorR(), block.getColorG(), block.getColorB(),
					xp, zn, xpzn);
			renderer.texCoord(TextureManager.getX1(block.getSideTexture()),
					TextureManager.getY2(block.getSideTexture()));
			renderer.vertexWithColoredAO(x + 1, y, z + 1, block.getColorR(),
					block.getColorG(), block.getColorB(), xp, zp, xpzp);
			renderer.texCoord(TextureManager.getX2(block.getSideTexture()),
					TextureManager.getY2(block.getSideTexture()));
			renderer.vertexWithColoredAO(x + 1, y, z, block.getColorR(),
					block.getColorG(), block.getColorB(), xn, zp, xnzp);
		}

		if(blockIdOnLeft == 0 || !blockOnLeft.isOpaque()
				&& !(blockID == blockIdOnLeft && blockOnLeft.shouldCullSame())) {
			boolean xnzn = world.isOccluder(x - 1, y + 1, z - 1);
			boolean zn = world.isOccluder(x - 1, y + 1, z);
			boolean xpzn = world.isOccluder(x - 1, y + 1, z + 1);
			boolean xn = world.isOccluder(x - 1, y, z - 1);
			boolean xp = world.isOccluder(x - 1, y, z + 1);
			boolean xnzp = world.isOccluder(x - 1, y - 1, z - 1);
			boolean zp = world.isOccluder(x - 1, y - 1, z);
			boolean xpzp = world.isOccluder(x - 1, y - 1, z + 1);

			renderer.normal(-1, 0, 0);
			renderer.texCoord(TextureManager.getX2(block.getSideTexture()),
					TextureManager.getY1(block.getSideTexture()));
			renderer.vertexWithColoredAO(x, y + 1, z + 1, block.getColorR(),
					block.getColorG(), block.getColorB(), xp, zn, xpzn);
			renderer.texCoord(TextureManager.getX1(block.getSideTexture()),
					TextureManager.getY1(block.getSideTexture()));
			renderer.vertexWithColoredAO(x, y + 1, z, block.getColorR(),
					block.getColorG(), block.getColorB(), xn, zn, xnzn);
			renderer.texCoord(TextureManager.getX1(block.getSideTexture()),
					TextureManager.getY2(block.getSideTexture()));
			renderer.vertexWithColoredAO(x, y, z, block.getColorR(),
					block.getColorG(), block.getColorB(), xn, zp, xnzp);
			renderer.texCoord(TextureManager.getX2(block.getSideTexture()),
					TextureManager.getY2(block.getSideTexture()));
			renderer.vertexWithColoredAO(x, y, z + 1, block.getColorR(),
					block.getColorG(), block.getColorB(), xp, zp, xpzp);
		}

	}

	@Override
	public void preRender(World world, Block block, int x, int y, int z) {
		block.updateColor(x, y, z, world);
	}

	@Override
	public void postRender(World world, Block block, int x, int y, int z) {
		block.restoreColor();
	}

}
