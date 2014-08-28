package dax.blocks.world.chunk;

import java.nio.ShortBuffer;

import dax.blocks.render.ChunkMeshBuilder;
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
		if(y >= 0 && y < 8) {
			this.renderChunks[y].setDirty(true);
		}
	}
	
	public void setAllDirty() {
		for(RenderChunk r : this.renderChunks) {
			r.setDirty(true);
		}
	}

	public float getDistanceToPlayer() {
		float pX = this.world.getPlayer().getPosX();
		float pZ = this.world.getPlayer().getPosZ();

		float dX = Math.abs(pX - this.x * Chunk.CHUNK_SIZE - Chunk.CHUNK_SIZE / 2);
		float dZ = Math.abs(pZ - this.z * Chunk.CHUNK_SIZE - Chunk.CHUNK_SIZE / 2);

		return (float) Math.sqrt(dX * dX + dZ * dZ);
	}

	public void setBlock(int x, int y, int z, int id, boolean rebuild) {
		if(y < Chunk.CHUNK_HEIGHT && y >= 0) {
			blocksBuffer.put(x + Chunk.CHUNK_SIZE * (y + Chunk.CHUNK_HEIGHT * z), (short) id);
			int meshY = y / Chunk.CHUNK_SIZE;
			if(rebuild) {
				this.setDirty(meshY);

				if(y % Chunk.CHUNK_SIZE == 0) {
					this.setDirty(meshY - 1);
				}

				if(y % 16 == Chunk.CHUNK_SIZE - 1) {
					this.setDirty(meshY + 1);
				}

				if(x == Chunk.CHUNK_SIZE - 1) {
					if(this.world.getBlock(this.x * Chunk.CHUNK_SIZE + x + 1, y, this.z * Chunk.CHUNK_SIZE + z) > 0) {
						this.world.setChunkDirty(this.x + 1, meshY, this.z);
					}
				}

				if(x == 0) {
					if(this.world.getBlock(this.x * Chunk.CHUNK_SIZE + x - 1, y, this.z * Chunk.CHUNK_SIZE + z) > 0) {
						this.world.setChunkDirty(this.x - 1, meshY, this.z);
					}
				}

				if(z == Chunk.CHUNK_SIZE - 1) {
					if(this.world.getBlock(this.x * Chunk.CHUNK_SIZE + x, y, this.z * Chunk.CHUNK_SIZE + z + 1) > 0) {
						this.world.setChunkDirty(this.x, meshY, this.z + 1);
					}
				}

				if(z == 0) {
					if(this.world.getBlock(this.x * Chunk.CHUNK_SIZE + x, y, this.z * Chunk.CHUNK_SIZE + z - 1) > 0) {
						this.world.setChunkDirty(this.x, meshY, this.z - 1);
					}
				}
			}
		}
	}

	public int getBlock(int x, int y, int z) {
		if(y < 0 || y >= 128) {
			return 0;
		}

		return this.blocksBuffer.get(x + Chunk.CHUNK_SIZE * (y + Chunk.CHUNK_HEIGHT * z));
	}

	public void deleteAllRenderChunks() {
		for(int y = 0; y < this.renderChunks.length; y++) {
			this.deleteRenderChunk(y);
		}
	}

	public void deleteRenderChunk(int y) {
		if(this.renderChunks[y].isBuilt()) { 
			this.renderChunks[y].clear();
		}
	}

	public void rebuild(int y) {
		if(y > 0 || y < this.renderChunks.length) {
			
			if(this.renderChunks[y].isBuilt()) {
				this.deleteRenderChunk(y);
			}		
			this.renderChunks[y].setCm(ChunkMeshBuilder.generateMesh(world.getGame(), this, y));
		}
	}

	public void rebuildEntireChunk() {
		for(int y = 0; y < 8; y++) {
			this.rebuild(y);
		}
	}

	public Chunk(int cX, int cZ, World world) {
		this.blocksBuffer = ShortBuffer.allocate(Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE * Chunk.CHUNK_HEIGHT);
		this.world = world;
		this.x = cX;
		this.z = cZ;
		
		for(int y = 0; y < 8; y++) {
			this.renderChunks[y] = new RenderChunk(cX, y, cZ);
		}
		
	}
}
