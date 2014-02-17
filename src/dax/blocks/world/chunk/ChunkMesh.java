package dax.blocks.world.chunk;

import dax.blocks.block.Block;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

public class ChunkMesh {

	public FloatBuffer tBufferOpaque;
	public FloatBuffer nBufferOpaque;
	public FloatBuffer vBufferOpaque;

	public FloatBuffer tBufferTransparent;
	public FloatBuffer nBufferTransparent;
	public FloatBuffer vBufferTransparent;

	/**
	 * Generates geometry for specified chunk
	 * 
	 * @param chunk
	 */
	public void generateMesh(Chunk chunk) {

		int opaqueCount = 0;
		int transparentCount = 0;

		for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
			for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
				for (int y = 0; y < Chunk.CHUNK_HEIGHT; y++) {

					byte blockID = chunk.getBlock(x, y, z);
					Block block = Block.getBlock(blockID);

					int wx = x + chunk.x * Chunk.CHUNK_SIZE;
					int wz = z + chunk.z * Chunk.CHUNK_SIZE;

					if (blockID > 0) {
						if (!(chunk.world.getBlock(wx, y + 1, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx, y + 1, wz)).isOpaque())) {
							if (block.isOpaque()) {
								opaqueCount++;
							} else {
								transparentCount++;
							}
						}

						if (!(chunk.world.getBlock(wx, y - 1, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx, y - 1, wz)).isOpaque())) {
							if (block.isOpaque()) {
								opaqueCount++;
							} else {
								transparentCount++;
							}
						}

						if (!(chunk.world.getBlock(wx - 1, y, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx - 1, y, wz)).isOpaque())) {
							if (block.isOpaque()) {
								opaqueCount++;
							} else {
								transparentCount++;
							}
						}

						if (!(chunk.world.getBlock(wx + 1, y, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx + 1, y, wz)).isOpaque())) {
							if (block.isOpaque()) {
								opaqueCount++;
							} else {
								transparentCount++;
							}
						}

						if (!(chunk.world.getBlock(wx, y, wz + 1) != 0 && Block.getBlock(chunk.world.getBlock(wx, y, wz + 1)).isOpaque())) {
							if (block.isOpaque()) {
								opaqueCount++;
							} else {
								transparentCount++;
							}
						}

						if (!(chunk.world.getBlock(wx, y, wz - 1) != 0 && Block.getBlock(chunk.world.getBlock(wx, y, wz - 1)).isOpaque())) {
							if (block.isOpaque()) {
								opaqueCount++;
							} else {
								transparentCount++;
							}
						}

					}

				}

			}
		}

		vBufferOpaque = BufferUtils.createFloatBuffer(opaqueCount * 12);
		tBufferOpaque = BufferUtils.createFloatBuffer(opaqueCount * 8);
		nBufferOpaque = BufferUtils.createFloatBuffer(opaqueCount * 12);

		vBufferTransparent = BufferUtils.createFloatBuffer(transparentCount * 12);
		tBufferTransparent = BufferUtils.createFloatBuffer(transparentCount * 8);
		nBufferTransparent = BufferUtils.createFloatBuffer(transparentCount * 12);

		for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
			for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
				for (int y = 0; y < Chunk.CHUNK_HEIGHT; y++) {
					byte blockID = chunk.getBlock(x, y, z);
					Block block = Block.getBlock(blockID);

					int wx = x + chunk.x * Chunk.CHUNK_SIZE;
					int wz = z + chunk.z * Chunk.CHUNK_SIZE;

					if (blockID > 0) {
						if (!(chunk.world.getBlock(wx, y + 1, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx, y + 1, wz)).isOpaque())) {
							float[] vT = block.getVTop(x, y, z);
							float[] tT = block.getTTop();
							float[] nT = block.getNTop();

							if (block.isOpaque()) {
								vBufferOpaque.put(vT);
								tBufferOpaque.put(tT);
								nBufferOpaque.put(nT);
							} else {
								vBufferTransparent.put(vT);
								tBufferTransparent.put(tT);
								nBufferTransparent.put(nT);
							}
						}

						if (!(chunk.world.getBlock(wx, y - 1, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx, y - 1, wz)).isOpaque())) {
							float[] vT = block.getVBottom(x, y, z);
							float[] tT = block.getTBottom();
							float[] nT = block.getNBottom();

							if (block.isOpaque()) {
								vBufferOpaque.put(vT);
								tBufferOpaque.put(tT);
								nBufferOpaque.put(nT);
							} else {
								vBufferTransparent.put(vT);
								tBufferTransparent.put(tT);
								nBufferTransparent.put(nT);
							}
						}

						if (!(chunk.world.getBlock(wx - 1, y, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx - 1, y, wz)).isOpaque())) {
							float[] vT = block.getVLeft(x, y, z);
							float[] tT = block.getTLeft();
							float[] nT = block.getNLeft();

							if (block.isOpaque()) {
								vBufferOpaque.put(vT);
								tBufferOpaque.put(tT);
								nBufferOpaque.put(nT);
							} else {
								vBufferTransparent.put(vT);
								tBufferTransparent.put(tT);
								nBufferTransparent.put(nT);
							}
						}

						if (!(chunk.world.getBlock(wx + 1, y, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx + 1, y, wz)).isOpaque())) {
							float[] vT = block.getVRight(x, y, z);
							float[] tT = block.getTRight();
							float[] nT = block.getNRight();

							if (block.isOpaque()) {
								vBufferOpaque.put(vT);
								tBufferOpaque.put(tT);
								nBufferOpaque.put(nT);
							} else {
								vBufferTransparent.put(vT);
								tBufferTransparent.put(tT);
								nBufferTransparent.put(nT);
							}
						}

						if (!(chunk.world.getBlock(wx, y, wz + 1) != 0 && Block.getBlock(chunk.world.getBlock(wx, y, wz + 1)).isOpaque())) {
							float[] vT = block.getVFront(x, y, z);
							float[] tT = block.getTFront();
							float[] nT = block.getNFront();

							if (block.isOpaque()) {
								vBufferOpaque.put(vT);
								tBufferOpaque.put(tT);
								nBufferOpaque.put(nT);
							} else {
								vBufferTransparent.put(vT);
								tBufferTransparent.put(tT);
								nBufferTransparent.put(nT);
							}
						}

						if (!(chunk.world.getBlock(wx, y, wz - 1) != 0 && Block.getBlock(chunk.world.getBlock(wx, y, wz - 1)).isOpaque())) {
							float[] vT = block.getVBack(x, y, z);
							float[] tT = block.getTBack();
							float[] nT = block.getNBack();

							if (block.isOpaque()) {
								vBufferOpaque.put(vT);
								tBufferOpaque.put(tT);
								nBufferOpaque.put(nT);
							} else {
								vBufferTransparent.put(vT);
								tBufferTransparent.put(tT);
								nBufferTransparent.put(nT);
							}
						}
					}
				}
			}
		}
		vBufferOpaque.flip();
		tBufferOpaque.flip();
		nBufferOpaque.flip();

		vBufferTransparent.flip();
		tBufferTransparent.flip();
		nBufferTransparent.flip();
	}
}
