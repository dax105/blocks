package dax.blocks.render;

import org.lwjgl.BufferUtils;

import dax.blocks.block.Block;
import dax.blocks.world.chunk.Chunk;

public class ChunkMeshBuilder {
	
	/**
	 * Generates geometry for a specified chunk
	 * 
	 * @param chunk
	 */
	public ChunkMesh generateMesh(Chunk chunk) {

		ChunkMesh cm = new ChunkMesh();
		
		int opaqueCount = 0;
		int transparentCount = 0;

		for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
			for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
				for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {

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

		cm.vBufferOpaque = BufferUtils.createFloatBuffer(opaqueCount * 12);
		cm.tBufferOpaque = BufferUtils.createFloatBuffer(opaqueCount * 8);
		cm.nBufferOpaque = BufferUtils.createFloatBuffer(opaqueCount * 12);

		cm.vBufferTransparent = BufferUtils.createFloatBuffer(transparentCount * 12);
		cm.tBufferTransparent = BufferUtils.createFloatBuffer(transparentCount * 8);
		cm.nBufferTransparent = BufferUtils.createFloatBuffer(transparentCount * 12);

		for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
			for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
				for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
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
								cm.vBufferOpaque.put(vT);
								cm.tBufferOpaque.put(tT);
								cm.nBufferOpaque.put(nT);
							} else {
								cm.vBufferTransparent.put(vT);
								cm.tBufferTransparent.put(tT);
								cm.nBufferTransparent.put(nT);
							}
						}

						if (!(chunk.world.getBlock(wx, y - 1, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx, y - 1, wz)).isOpaque()) && (!cullSame || chunk.world.getBlock(wx, y - 1, wz) != blockID)) {
							float[] vT = block.getVBottom(x, y, z);
							float[] tT = block.getTBottom();
							float[] nT = block.getNBottom();

							if (block.isOpaque()) {
								cm.vBufferOpaque.put(vT);
								cm.tBufferOpaque.put(tT);
								cm.nBufferOpaque.put(nT);
							} else {
								cm.vBufferTransparent.put(vT);
								cm.tBufferTransparent.put(tT);
								cm.nBufferTransparent.put(nT);
							}
						}

						if (!(chunk.world.getBlock(wx - 1, y, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx - 1, y, wz)).isOpaque()) && (!cullSame || chunk.world.getBlock(wx - 1, y, wz) != blockID)) {
							float[] vT = block.getVLeft(x, y, z);
							float[] tT = block.getTLeft();
							float[] nT = block.getNLeft();

							if (block.isOpaque()) {
								cm.vBufferOpaque.put(vT);
								cm.tBufferOpaque.put(tT);
								cm.nBufferOpaque.put(nT);
							} else {
								cm.vBufferTransparent.put(vT);
								cm.tBufferTransparent.put(tT);
								cm.nBufferTransparent.put(nT);
							}
						}

						if (!(chunk.world.getBlock(wx + 1, y, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx + 1, y, wz)).isOpaque()) && (!cullSame || chunk.world.getBlock(wx + 1, y, wz) != blockID)) {
							float[] vT = block.getVRight(x, y, z);
							float[] tT = block.getTRight();
							float[] nT = block.getNRight();

							if (block.isOpaque()) {
								cm.vBufferOpaque.put(vT);
								cm.tBufferOpaque.put(tT);
								cm.nBufferOpaque.put(nT);
							} else {
								cm.vBufferTransparent.put(vT);
								cm.tBufferTransparent.put(tT);
								cm.nBufferTransparent.put(nT);
							}
						}

						if (!(chunk.world.getBlock(wx, y, wz + 1) != 0 && Block.getBlock(chunk.world.getBlock(wx, y, wz + 1)).isOpaque()) && (!cullSame || chunk.world.getBlock(wx, y, wz + 1) != blockID)) {
							float[] vT = block.getVFront(x, y, z);
							float[] tT = block.getTFront();
							float[] nT = block.getNFront();

							if (block.isOpaque()) {
								cm.vBufferOpaque.put(vT);
								cm.tBufferOpaque.put(tT);
								cm.nBufferOpaque.put(nT);
							} else {
								cm.vBufferTransparent.put(vT);
								cm.tBufferTransparent.put(tT);
								cm.nBufferTransparent.put(nT);
							}
						}

						if (!(chunk.world.getBlock(wx, y, wz - 1) != 0 && Block.getBlock(chunk.world.getBlock(wx, y, wz - 1)).isOpaque()) && (!cullSame || chunk.world.getBlock(wx, y, wz - 1) != blockID)) {
							float[] vT = block.getVBack(x, y, z);
							float[] tT = block.getTBack();
							float[] nT = block.getNBack();

							if (block.isOpaque()) {
								cm.vBufferOpaque.put(vT);
								cm.tBufferOpaque.put(tT);
								cm.nBufferOpaque.put(nT);
							} else {
								cm.vBufferTransparent.put(vT);
								cm.tBufferTransparent.put(tT);
								cm.nBufferTransparent.put(nT);
							}
						}
					}
				}
			}
		}
		cm.vBufferOpaque.flip();
		cm.tBufferOpaque.flip();
		cm.nBufferOpaque.flip();

		cm.vBufferTransparent.flip();
		cm.tBufferTransparent.flip();
		cm.nBufferTransparent.flip();
		
		if (transparentCount > 0) {
			cm.hasTransparent = true;
		} else {
			cm.hasTransparent = false;
		}
		
		cm.isDirty = false;
		cm.vertices = (opaqueCount + transparentCount)*4;
		
		return cm;
	}
}
