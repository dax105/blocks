package dax.blocks.world;

import java.util.Random;

import dax.blocks.block.Block;
import dax.blocks.world.chunk.Chunk;
import dax.blocks.world.generator.SimplexNoise;

public class ChunkProvider {

	SimplexNoise simplex;
	
	World world;
	int seed;
	
	public ChunkProvider(World world) {
		this(new Random().nextInt(), world);
	}
	
	public ChunkProvider(int seed, World world) {
		this.seed = seed;
		this.world = world;
		this.simplex = new SimplexNoise(1024, 0.5, seed);
	}
	
	public Chunk getChunk(int xc, int zc) {
		int xStart = xc*Chunk.CHUNK_SIZE;
		int zStart = zc*Chunk.CHUNK_SIZE;
		int xEnd = xc*Chunk.CHUNK_SIZE+Chunk.CHUNK_SIZE;
		int zEnd = zc*Chunk.CHUNK_SIZE+Chunk.CHUNK_SIZE;
		
		int[][] heightMap = new int[Chunk.CHUNK_SIZE][Chunk.CHUNK_SIZE];
		
		for (int x = xStart; x < xEnd; x++) {
			for (int z = zStart; z < zEnd; z++) {
				int precalc = (int) (Math.round((40.0f + simplex.getNoise(x, z)*world.multipler)));
				if (precalc > Chunk.CHUNK_HEIGHT - 1) {
					precalc = Chunk.CHUNK_HEIGHT - 1;
				}

				if (precalc < 0) {
					precalc = 0;
				}

				heightMap[x%Chunk.CHUNK_SIZE][z%Chunk.CHUNK_SIZE] = precalc;
			}
		}	
		Chunk chunk = new Chunk(xc, zc, world);
		
		for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
			for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
				int h = heightMap[x][z];
				for (int y = 0; y < Chunk.CHUNK_HEIGHT; y++) {
					if (y <= h || y == 0) {
						if (y == h) {
							chunk.setBlock(x, y, z, Block.grass.getId(), false);
						} else {
							if (y < h-4) {
								chunk.setBlock(x, y, z, Block.stone.getId(), false);
							} else {
								chunk.setBlock(x, y, z, Block.dirt.getId(), false);
							}
						}
					}
				}
			}
		}
		
		return chunk;
		
	}
}
