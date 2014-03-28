package dax.blocks.render;

import dax.blocks.block.Block;
import dax.blocks.world.chunk.Chunk;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class ChunkMesh {

	public int vertices;
	public boolean hasTransparent;
	
	public boolean hasVBO = false;
	public boolean isDirty = true;
	public boolean needsNewVBO = true;
	
	public FloatBuffer tBufferOpaque;
	public FloatBuffer nBufferOpaque;
	public FloatBuffer vBufferOpaque;

	public FloatBuffer tBufferTransparent;
	public FloatBuffer nBufferTransparent;
	public FloatBuffer vBufferTransparent;

	public int vHandleOpaque;
	public int tHandleOpaque;
	public int nHandleOpaque;

	public int vHandleTransparent;
	public int tHandleTransparent;
	public int nHandleTransparent;

	/**
	 * Generates geometry for specified chunk
	 * 
	 * @param chunk
	 */
	public void generateMesh(Chunk chunk, int cy) {

		int opaqueCount = 0;
		int transparentCount = 0;
		
		int yStart = cy*Chunk.CHUNK_SIZE;

		for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
			for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
				for (int y = yStart; y < yStart + Chunk.CHUNK_SIZE; y++) {

					byte blockID = chunk.getBlock(x, y, z);
					Block block = Block.getBlock(blockID);

					int wx = x + chunk.x * Chunk.CHUNK_SIZE;
					int wz = z + chunk.z * Chunk.CHUNK_SIZE;

					if (blockID > 0) {
						boolean cullSame = block.shouldCullSame();
						
						if (!(chunk.world.getBlock(wx, y + 1, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx, y + 1, wz)).isOpaque()) && (!cullSame || chunk.world.getBlock(wx, y + 1, wz) != blockID) ) {
							if (block.isOpaque()) {
								opaqueCount++;
							} else {
								transparentCount++;
							}
						}

						if (!(chunk.world.getBlock(wx, y - 1, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx, y - 1, wz)).isOpaque()) && (!cullSame || chunk.world.getBlock(wx, y - 1, wz) != blockID) ) {
							if (block.isOpaque()) {
								opaqueCount++;
							} else {
								transparentCount++;
							}
						}

						if (!(chunk.world.getBlock(wx - 1, y, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx - 1, y, wz)).isOpaque()) && (!cullSame || chunk.world.getBlock(wx - 1, y, wz) != blockID)) {
							if (block.isOpaque()) {
								opaqueCount++;
							} else {
								transparentCount++;
							}
						}

						if (!(chunk.world.getBlock(wx + 1, y, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx + 1, y, wz)).isOpaque()) && (!cullSame || chunk.world.getBlock(wx + 1, y, wz) != blockID)) {
							if (block.isOpaque()) {
								opaqueCount++;
							} else {
								transparentCount++;
							}
						}

						if (!(chunk.world.getBlock(wx, y, wz + 1) != 0 && Block.getBlock(chunk.world.getBlock(wx, y, wz + 1)).isOpaque()) && (!cullSame || chunk.world.getBlock(wx, y, wz + 1) != blockID)) {
							if (block.isOpaque()) {
								opaqueCount++;
							} else {
								transparentCount++;
							}
						}

						if (!(chunk.world.getBlock(wx, y, wz - 1) != 0 && Block.getBlock(chunk.world.getBlock(wx, y, wz - 1)).isOpaque()) && (!cullSame || chunk.world.getBlock(wx, y, wz - 1) != blockID)) {
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
				for (int y = yStart; y < yStart + Chunk.CHUNK_SIZE; y++) {
					byte blockID = chunk.getBlock(x, y, z);
					Block block = Block.getBlock(blockID);

					int wx = x + chunk.x * Chunk.CHUNK_SIZE;
					int wz = z + chunk.z * Chunk.CHUNK_SIZE;

					if (blockID > 0) {
						boolean cullSame = block.shouldCullSame();
						if (!(chunk.world.getBlock(wx, y + 1, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx, y + 1, wz)).isOpaque()) && (!cullSame || chunk.world.getBlock(wx, y + 1, wz) != blockID) ) {
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

						if (!(chunk.world.getBlock(wx, y - 1, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx, y - 1, wz)).isOpaque()) && (!cullSame || chunk.world.getBlock(wx, y - 1, wz) != blockID)) {
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

						if (!(chunk.world.getBlock(wx - 1, y, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx - 1, y, wz)).isOpaque()) && (!cullSame || chunk.world.getBlock(wx - 1, y, wz) != blockID)) {
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

						if (!(chunk.world.getBlock(wx + 1, y, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx + 1, y, wz)).isOpaque()) && (!cullSame || chunk.world.getBlock(wx + 1, y, wz) != blockID)) {
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

						if (!(chunk.world.getBlock(wx, y, wz + 1) != 0 && Block.getBlock(chunk.world.getBlock(wx, y, wz + 1)).isOpaque()) && (!cullSame || chunk.world.getBlock(wx, y, wz + 1) != blockID)) {
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

						if (!(chunk.world.getBlock(wx, y, wz - 1) != 0 && Block.getBlock(chunk.world.getBlock(wx, y, wz - 1)).isOpaque()) && (!cullSame || chunk.world.getBlock(wx, y, wz - 1) != blockID)) {
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
		
		if (transparentCount > 0) {
			hasTransparent = true;
		} else {
			hasTransparent = false;
		}
		
		this.isDirty = false;
		this.vertices = (opaqueCount + transparentCount)*4;
	}
	
}
