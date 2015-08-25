package cz.dat.oots.block.renderer;

import cz.dat.oots.TextureManager;
import cz.dat.oots.block.Block;
import cz.dat.oots.data.block.PlantDataObject;
import cz.dat.oots.render.IChunkRenderer;
import cz.dat.oots.world.World;

public class BlockRendererPlant implements IBlockRenderer {

	@Override
	public void render(IChunkRenderer renderer, World world, Block block,
			int x, int y, int z) {
		renderer.normal(0, 0, 0);
		renderer.color(1, 1, 1);

		int lastTexture = block.getSideTexture();
		if(world.hasData(x, y, z)
				&& ((PlantDataObject) world.getData(x, y, z)).isTextured()) {
			block.setSideTexture(2);
		}

		renderer.texCoord(TextureManager.getX1(block.getSideTexture()),
				TextureManager.getY1(block.getSideTexture()));
		renderer.vertex(x + 0, y + 1, z + 0);
		renderer.texCoord(TextureManager.getX2(block.getSideTexture()),
				TextureManager.getY1(block.getSideTexture()));
		renderer.vertex(x + 1, y + 1, z + 1);
		renderer.texCoord(TextureManager.getX2(block.getSideTexture()),
				TextureManager.getY2(block.getSideTexture()));
		renderer.vertex(x + 1, y + 0, z + 1);
		renderer.texCoord(TextureManager.getX1(block.getSideTexture()),
				TextureManager.getY2(block.getSideTexture()));
		renderer.vertex(x + 0, y + 0, z + 0);

		renderer.texCoord(TextureManager.getX2(block.getSideTexture()),
				TextureManager.getY1(block.getSideTexture()));
		renderer.vertex(x + 0, y + 1, z + 0);
		renderer.texCoord(TextureManager.getX2(block.getSideTexture()),
				TextureManager.getY2(block.getSideTexture()));
		renderer.vertex(x + 0, y + 0, z + 0);
		renderer.texCoord(TextureManager.getX1(block.getSideTexture()),
				TextureManager.getY2(block.getSideTexture()));
		renderer.vertex(x + 1, y + 0, z + 1);
		renderer.texCoord(TextureManager.getX1(block.getSideTexture()),
				TextureManager.getY1(block.getSideTexture()));
		renderer.vertex(x + 1, y + 1, z + 1);

		renderer.texCoord(TextureManager.getX1(block.getSideTexture()),
				TextureManager.getY1(block.getSideTexture()));
		renderer.vertex(x + 0, y + 1, z + 1);
		renderer.texCoord(TextureManager.getX2(block.getSideTexture()),
				TextureManager.getY1(block.getSideTexture()));
		renderer.vertex(x + 1, y + 1, z + 0);
		renderer.texCoord(TextureManager.getX2(block.getSideTexture()),
				TextureManager.getY2(block.getSideTexture()));
		renderer.vertex(x + 1, y + 0, z + 0);
		renderer.texCoord(TextureManager.getX1(block.getSideTexture()),
				TextureManager.getY2(block.getSideTexture()));
		renderer.vertex(x + 0, y + 0, z + 1);

		renderer.texCoord(TextureManager.getX2(block.getSideTexture()),
				TextureManager.getY1(block.getSideTexture()));
		renderer.vertex(x + 0, y + 1, z + 1);
		renderer.texCoord(TextureManager.getX2(block.getSideTexture()),
				TextureManager.getY2(block.getSideTexture()));
		renderer.vertex(x + 0, y + 0, z + 1);
		renderer.texCoord(TextureManager.getX1(block.getSideTexture()),
				TextureManager.getY2(block.getSideTexture()));
		renderer.vertex(x + 1, y + 0, z + 0);
		renderer.texCoord(TextureManager.getX1(block.getSideTexture()),
				TextureManager.getY1(block.getSideTexture()));
		renderer.vertex(x + 1, y + 1, z + 0);

		block.setSideTexture(lastTexture);
	}

	@Override
	public void preRender(World world, Block block, int x, int y, int z) {
	}

	@Override
	public void postRender(World world, Block block, int x, int y, int z) {
	}

}
