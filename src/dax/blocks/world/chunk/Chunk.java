package dax.blocks.world.chunk;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import dax.blocks.world.World;

public class Chunk {
	public static final int CHUNK_SIZE = 16;
	public static final int CHUNK_HEIGHT = 128;

	public int x;
	public int z;

	public World world;

	public ChunkMesh[] meshes;
	
	public byte[][][] blocks;

	public int getDisplayListID() {
		return x * world.size + z + 1;
	}

	public void setBlock(int x, int y, int z, byte id, boolean rebuild) {
		blocks[x][z][y] = id;
		int meshY = y / CHUNK_SIZE;
		if (rebuild) {
			long start = System.nanoTime();
			rebuild(meshY);

			if (y % CHUNK_SIZE == 0) {
				rebuild(meshY-1);
			}
			
			if (y == CHUNK_SIZE-1) {
				rebuild(meshY+1);
			}
			
			if (x == CHUNK_SIZE - 1) {
				if (world.getBlock(this.x * CHUNK_SIZE + x + 1, y, this.z * CHUNK_SIZE + z) > 0) {
					world.rebuild(this.x + 1, meshY, this.z);
				}
			}

			if (x == 0) {
				if (world.getBlock(this.x * CHUNK_SIZE + x - 1, y, this.z * CHUNK_SIZE + z) > 0) {
					world.rebuild(this.x - 1, meshY, this.z);
				}
			}

			if (z == CHUNK_SIZE - 1) {
				if (world.getBlock(this.x * CHUNK_SIZE + x, y, this.z * CHUNK_SIZE + z + 1) > 0) {
					world.rebuild(this.x, meshY, this.z + 1);
				}
			}

			if (z == 0) {
				if (world.getBlock(this.x * CHUNK_SIZE + x, y, this.z * CHUNK_SIZE + z - 1) > 0) {
					world.rebuild(this.x, meshY, this.z - 1);
				}
			}
			System.out.println("Geometry rebuilt in " + (System.nanoTime() - start) / 1000000 + "ms");
		}
	}

	public byte getBlock(int x, int y, int z) {
		return blocks[x][z][y];
	}

	public void rebuildEntireChunk() {
		for (int i = 0; i < meshes.length; i++) {
			meshes[i].hasVBO = false;
			IntBuffer ib = BufferUtils.createIntBuffer(6);
			ib.put(meshes[i].vHandleOpaque).put(meshes[i].tHandleOpaque).put(meshes[i].nHandleOpaque).put(meshes[i].vHandleTransparent).put(meshes[i].tHandleTransparent).put(meshes[i].nHandleTransparent);
			GL15.glDeleteBuffers(ib);
			meshes[i].generateMesh(this, i);
		}
	}
	
	public void rebuild(int y) {
		if (y < 0 || y >= meshes.length) {
			return;
		}
		
		meshes[y].hasVBO = false;
		IntBuffer ib = BufferUtils.createIntBuffer(6);
		ib.put(meshes[y].vHandleOpaque).put(meshes[y].tHandleOpaque).put(meshes[y].nHandleOpaque).put(meshes[y].vHandleTransparent).put(meshes[y].tHandleTransparent).put(meshes[y].nHandleTransparent);
		GL15.glDeleteBuffers(ib);
		meshes[y].generateMesh(this, y);
	}

	public Chunk(int cX, int cZ, World world) {
		blocks = new byte[CHUNK_SIZE][CHUNK_SIZE][CHUNK_HEIGHT];
		this.world = world;
		x = cX;
		z = cZ;
		meshes = new ChunkMesh[Chunk.CHUNK_HEIGHT/CHUNK_SIZE];
		for (int i = 0; i < meshes.length; i++){
			meshes[i] = new ChunkMesh();
		}
	}
}
