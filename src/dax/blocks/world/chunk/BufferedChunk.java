package dax.blocks.world.chunk;

import dax.blocks.block.Block;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

public class BufferedChunk {
	public FloatBuffer tBufferOpaque;
	public FloatBuffer nBufferOpaque;
	public FloatBuffer vBufferOpaque;

	public FloatBuffer tBufferTransparent;
	public FloatBuffer nBufferTransparent;
	public FloatBuffer vBufferTransparent;

	public void bufferChunk(Chunk chunk) {
		//System.out.println("-BUILDING CHUNK GEOMETRY FOR X: " + chunk.x + " Z: " + chunk.z);
		//long startTime = System.nanoTime();

		int opaqueCount = 0;
		int transparentCount = 0;

		//System.out.print("--Counting faces...");
		for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
			for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
				for (int y = 0; y < Chunk.CHUNK_HEIGHT; y++) {
					byte blockID = chunk.getBlock(x, y, z);
					Block block = Block.getBlock(blockID);

					int wx = x + chunk.x * Chunk.CHUNK_SIZE;
					int wz = z + chunk.z * Chunk.CHUNK_SIZE;

					if (blockID > 0) {

						boolean top = true;
						boolean bottom = true;
						boolean left = true;
						boolean right = true;
						boolean front = true;
						boolean back = true;

						if (chunk.world.getBlock(wx, y + 1, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx, y + 1, wz)).isOpaque()) {
							top = false;
						}
						if (chunk.world.getBlock(wx, y - 1, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx, y - 1, wz)).isOpaque()) {
							bottom = false;
						}
						if (chunk.world.getBlock(wx - 1, y, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx - 1, y, wz)).isOpaque()) {
							left = false;
						}
						if (chunk.world.getBlock(wx + 1, y, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx + 1, y, wz)).isOpaque()) {
							right = false;
						}
						if (chunk.world.getBlock(wx, y, wz + 1) != 0 && Block.getBlock(chunk.world.getBlock(wx, y, wz + 1)).isOpaque()) {
							front = false;
						}
						if (chunk.world.getBlock(wx, y, wz - 1) != 0 && Block.getBlock(chunk.world.getBlock(wx, y, wz - 1)).isOpaque()) {
							back = false;
						}

						if (top) {
							if (block.isOpaque()) {
								opaqueCount++;
							} else {
								transparentCount++;
							}
						}

						if (bottom) {
							if (block.isOpaque()) {
								opaqueCount++;
							} else {
								transparentCount++;
							}
						}

						if (left) {
							if (block.isOpaque()) {
								opaqueCount++;
							} else {
								transparentCount++;
							}
						}

						if (right) {
							if (block.isOpaque()) {
								opaqueCount++;
							} else {
								transparentCount++;
							}
						}

						if (front) {
							if (block.isOpaque()) {
								opaqueCount++;
							} else {
								transparentCount++;
							}
						}

						if (back) {
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
		
		//System.out.println("Done in " + (System.nanoTime() - startTime) + "ns");
		//startTime = System.nanoTime();
		//System.out.print("--Creating FloatBuffers...");

		vBufferOpaque = BufferUtils.createFloatBuffer(opaqueCount * 12);
		tBufferOpaque = BufferUtils.createFloatBuffer(opaqueCount * 8);
		nBufferOpaque = BufferUtils.createFloatBuffer(opaqueCount * 12);

		vBufferTransparent = BufferUtils.createFloatBuffer(transparentCount * 12);
		tBufferTransparent = BufferUtils.createFloatBuffer(transparentCount * 8);
		nBufferTransparent = BufferUtils.createFloatBuffer(transparentCount * 12);
		//System.out.println("Done in " + (System.nanoTime() - startTime) + "ns");
		//startTime = System.nanoTime();
		//System.out.print("--Getting vertexes, normals and texcoords...");

		for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
			for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
				for (int y = 0; y < Chunk.CHUNK_HEIGHT; y++) {
					byte blockID = chunk.getBlock(x, y, z);
					Block block = Block.getBlock(blockID);

					int wx = x + chunk.x * Chunk.CHUNK_SIZE;
					int wz = z + chunk.z * Chunk.CHUNK_SIZE;

					if (blockID > 0) {

						boolean top = true;
						boolean bottom = true;
						boolean left = true;
						boolean right = true;
						boolean front = true;
						boolean back = true;

						if (chunk.world.getBlock(wx, y + 1, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx, y + 1, wz)).isOpaque()) {
							top = false;
						}
						if (chunk.world.getBlock(wx, y - 1, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx, y - 1, wz)).isOpaque()) {
							bottom = false;
						}
						if (chunk.world.getBlock(wx - 1, y, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx - 1, y, wz)).isOpaque()) {
							left = false;
						}
						if (chunk.world.getBlock(wx + 1, y, wz) != 0 && Block.getBlock(chunk.world.getBlock(wx + 1, y, wz)).isOpaque()) {
							right = false;
						}
						if (chunk.world.getBlock(wx, y, wz + 1) != 0 && Block.getBlock(chunk.world.getBlock(wx, y, wz + 1)).isOpaque()) {
							front = false;
						}
						if (chunk.world.getBlock(wx, y, wz - 1) != 0 && Block.getBlock(chunk.world.getBlock(wx, y, wz - 1)).isOpaque()) {
							back = false;
						}

						if (top) {
							if (block.isOpaque()) {
								float[] vT = block.getVTop(x, y, z);
								vBufferOpaque.put(vT);

								float[] tT = block.getTTop();
								tBufferOpaque.put(tT);

								float[] nT = block.getNTop();
								nBufferOpaque.put(nT);
							} else {
								float[] vT = block.getVTop(x, y, z);
								vBufferTransparent.put(vT);

								float[] tT = block.getTTop();
								tBufferTransparent.put(tT);

								float[] nT = block.getNTop();
								nBufferTransparent.put(nT);
							}
						}

						if (bottom) {
							if (block.isOpaque()) {
								float[] vT = block.getVBottom(x, y, z);
								vBufferOpaque.put(vT);

								float[] tT = block.getTBottom();
								tBufferOpaque.put(tT);

								float[] nT = block.getNBottom();
								nBufferOpaque.put(nT);
							} else {
								float[] vT = block.getVBottom(x, y, z);
								vBufferTransparent.put(vT);

								float[] tT = block.getTBottom();
								tBufferTransparent.put(tT);

								float[] nT = block.getNBottom();
								nBufferTransparent.put(nT);
							}
						}

						if (left) {
							if (block.isOpaque()) {
								float[] vT = block.getVLeft(x, y, z);
								vBufferOpaque.put(vT);

								float[] tT = block.getTLeft();
								tBufferOpaque.put(tT);

								float[] nT = block.getNLeft();
								nBufferOpaque.put(nT);
							} else {
								float[] vT = block.getVLeft(x, y, z);
								vBufferTransparent.put(vT);

								float[] tT = block.getTLeft();
								tBufferTransparent.put(tT);

								float[] nT = block.getNLeft();
								nBufferTransparent.put(nT);
							}
						}

						if (right) {
							if (block.isOpaque()) {
								float[] vT = block.getVRight(x, y, z);
								vBufferOpaque.put(vT);

								float[] tT = block.getTRight();
								tBufferOpaque.put(tT);

								float[] nT = block.getNRight();
								nBufferOpaque.put(nT);
							} else {
								float[] vT = block.getVRight(x, y, z);
								vBufferTransparent.put(vT);

								float[] tT = block.getTRight();
								tBufferTransparent.put(tT);

								float[] nT = block.getNRight();
								nBufferTransparent.put(nT);
							}
						}

						if (front) {
							if (block.isOpaque()) {
								float[] vT = block.getVFront(x, y, z);
								vBufferOpaque.put(vT);

								float[] tT = block.getTFront();
								tBufferOpaque.put(tT);

								float[] nT = block.getNFront();
								nBufferOpaque.put(nT);
							} else {
								float[] vT = block.getVFront(x, y, z);
								vBufferTransparent.put(vT);

								float[] tT = block.getTFront();
								tBufferTransparent.put(tT);

								float[] nT = block.getNFront();
								nBufferTransparent.put(nT);
							}
						}

						if (back) {
							if (block.isOpaque()) {
								float[] vT = block.getVBack(x, y, z);
								vBufferOpaque.put(vT);

								float[] tT = block.getTBack();
								tBufferOpaque.put(tT);

								float[] nT = block.getNBack();
								nBufferOpaque.put(nT);
							} else {
								float[] vT = block.getVBack(x, y, z);
								vBufferTransparent.put(vT);

								float[] tT = block.getTBack();
								tBufferTransparent.put(tT);

								float[] nT = block.getNBack();
								nBufferTransparent.put(nT);
							}
						}
					}
				}
			}
		}
		
		//System.out.println("Done in " + (System.nanoTime() - startTime) + "ns");
		//startTime = System.nanoTime();
		//System.out.print("--Flipping FloatBuffers...");

		vBufferOpaque.flip();
		tBufferOpaque.flip();
		nBufferOpaque.flip();

		vBufferTransparent.flip();
		tBufferTransparent.flip();
		nBufferTransparent.flip();
		
		//System.out.println("Done in " + (System.nanoTime() - startTime) + "ns");
	}
}
