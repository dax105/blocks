package dax.blocks.world.chunk;

import dax.blocks.world.World;

public class Chunk {
	public static final int CHUNK_SIZE = 16;
	public static final int CHUNK_HEIGHT = 128;

	public int x;
	public int z;

	public World world;

	public ChunkMesh cm = new ChunkMesh();
	public byte[][][] blocks;

	public void setBlock(int x, int y, int z, byte id, boolean rebuild) {
		blocks[x][z][y] = id;
		if (rebuild) {
			long start = System.nanoTime();
			rebuild();

			if (x == CHUNK_SIZE - 1) {
				if (world.getBlock(this.x * CHUNK_SIZE + x + 1, y, this.z * CHUNK_SIZE + z) > 0) {
					world.updateChunk(this.x + 1, this.z);
				}
			}

			if (x == 0) {
				if (world.getBlock(this.x * CHUNK_SIZE + x - 1, y, this.z * CHUNK_SIZE + z) > 0) {
					world.updateChunk(this.x - 1, this.z);
				}
			}

			if (z == CHUNK_SIZE - 1) {
				if (world.getBlock(this.x * CHUNK_SIZE + x, y, this.z * CHUNK_SIZE + z + 1) > 0) {
					world.updateChunk(this.x, this.z + 1);
				}
			}

			if (z == 0) {
				if (world.getBlock(this.x * CHUNK_SIZE + x, y, this.z * CHUNK_SIZE + z - 1) > 0) {
					world.updateChunk(this.x, this.z - 1);
				}
			}
			System.out.println("Geometry rebuilt in " + (System.nanoTime() - start) / 1000000 + "ms");
		}
	}

	public byte getBlock(int x, int y, int z) {
		return blocks[x][z][y];
	}

	public void rebuild() {
		cm.generateMesh(this);
	}

	public Chunk(byte[][][] blocks) {
		this.blocks = blocks;

	}

	public Chunk(int cX, int cZ, World world) {
		blocks = new byte[CHUNK_SIZE][CHUNK_SIZE][CHUNK_HEIGHT];
		this.world = world;
		x = cX;
		z = cZ;
	}
}
