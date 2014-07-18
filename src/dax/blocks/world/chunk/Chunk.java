package dax.blocks.world.chunk;

import java.nio.ShortBuffer;

import dax.blocks.render.ChunkMeshGenerator;
import dax.blocks.render.RenderChunk;
import dax.blocks.world.World;

public class Chunk {
	public boolean changed = false;
	public boolean populated = false;
	
	public static final int CHUNK_SIZE = 16;
	public static final int CHUNK_HEIGHT = 128;

	public int x;
	public int z;

	public World world;
	
	public RenderChunk[] renderChunks = new RenderChunk[8];

	public ShortBuffer blocksBuffer;

	public void setDirty(int y) {
		if (y >= 0 && y < 8) {
			renderChunks[y].setDirty(true);
		}
	}
	
	public void setAllDirty() {
		for (RenderChunk r : renderChunks) {
			r.setDirty(true);
		}
	}

	public float getDistanceToPlayer() {
		float pX = world.player.getPosX();
		float pZ = world.player.getPosZ();

		float dX = Math.abs(pX - x * CHUNK_SIZE - CHUNK_SIZE / 2);
		float dZ = Math.abs(pZ - z * CHUNK_SIZE - CHUNK_SIZE / 2);

		return (float) Math.sqrt(dX * dX + dZ * dZ);
	}

	public void setBlock(int x, int y, int z, int id, boolean rebuild) {
		if (y < CHUNK_HEIGHT && y >= 0) {
			blocksBuffer.put(x + Chunk.CHUNK_SIZE * (y + Chunk.CHUNK_HEIGHT * z), (short) id);
			int meshY = y / CHUNK_SIZE;
			if (rebuild) {
				setDirty(meshY);

				if (y % CHUNK_SIZE == 0) {
					setDirty(meshY - 1);
				}

				if (y % 16 == CHUNK_SIZE - 1) {
					setDirty(meshY + 1);
				}

				if (x == CHUNK_SIZE - 1) {
					if (world.getBlock(this.x * CHUNK_SIZE + x + 1, y, this.z * CHUNK_SIZE + z) > 0) {
						world.setChunkDirty(this.x + 1, meshY, this.z);
					}
				}

				if (x == 0) {
					if (world.getBlock(this.x * CHUNK_SIZE + x - 1, y, this.z * CHUNK_SIZE + z) > 0) {
						world.setChunkDirty(this.x - 1, meshY, this.z);
					}
				}

				if (z == CHUNK_SIZE - 1) {
					if (world.getBlock(this.x * CHUNK_SIZE + x, y, this.z * CHUNK_SIZE + z + 1) > 0) {
						world.setChunkDirty(this.x, meshY, this.z + 1);
					}
				}

				if (z == 0) {
					if (world.getBlock(this.x * CHUNK_SIZE + x, y, this.z * CHUNK_SIZE + z - 1) > 0) {
						world.setChunkDirty(this.x, meshY, this.z - 1);
					}
				}
			}
		}
	}

	public int getBlock(int x, int y, int z) {
		if (y < 0 || y >= 128) {
			return 0;
		}

		return blocksBuffer.get(x + Chunk.CHUNK_SIZE * (y + Chunk.CHUNK_HEIGHT * z));
	}

	public void deleteAllRenderChunks() {
		for (int y = 0; y < renderChunks.length; y++) {
			deleteRenderChunk(y);
		}
	}

	public void deleteRenderChunk(int y) {
		if (renderChunks[y].isGenerated()) { 
			renderChunks[y].clear();
		}
	}

	public void rebuild(int y) {
		if (y > 0 || y < renderChunks.length) {
			
			if (renderChunks[y].isGenerated()) {
				deleteRenderChunk(y);
			}		
			renderChunks[y].setCdl(ChunkMeshGenerator.genDisplayList(this, y));
		}
	}

	public void rebuildEntireChunk() {
		for (int y = 0; y < 8; y++) {
			rebuild(y);
		}
	}

	public Chunk(int cX, int cZ, World world) {
		this.blocksBuffer = ShortBuffer.allocate(Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE * Chunk.CHUNK_HEIGHT);
		this.world = world;
		this.x = cX;
		this.z = cZ;
		
		for (int i = 0; i < 8; i++) {
			renderChunks[i] = new RenderChunk();
		}
		
	}
}
