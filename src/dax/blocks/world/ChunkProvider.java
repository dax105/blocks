package dax.blocks.world;

import java.util.Random;

import dax.blocks.block.Block;
import dax.blocks.world.chunk.Chunk;
import dax.blocks.world.generator.SimplexNoise;

public class ChunkProvider {

	SimplexNoise simplex2D;
	SimplexNoise simplex3D;

	World world;
	int seed;

	public ChunkProvider(World world) {
		this(new Random().nextInt(), world);
	}

	public ChunkProvider(int seed, World world) {
		this.seed = seed;
		this.world = world;
		this.simplex2D = new SimplexNoise(256, 0.3, seed);
		this.simplex3D = new SimplexNoise(32, 0.1, seed);
	}

	public static float lerp(float x, float x1, float x2, float q00, float q01) {
		return ((x2 - x) / (x2 - x1)) * q00 + ((x - x1) / (x2 - x1)) * q01;
	}

	public Chunk getChunk(int xc, int zc) {
		int xStart = xc * Chunk.CHUNK_SIZE;
		int zStart = zc * Chunk.CHUNK_SIZE;
		int xEnd = xc * Chunk.CHUNK_SIZE + Chunk.CHUNK_SIZE;
		int zEnd = zc * Chunk.CHUNK_SIZE + Chunk.CHUNK_SIZE;

		int[][] heightMap = new int[Chunk.CHUNK_SIZE][Chunk.CHUNK_SIZE];

		for (int x = xStart; x < xEnd; x++) {
			for (int z = zStart; z < zEnd; z++) {
				int precalc = (int) (Math.round((40.0f + simplex2D.getNoise(x, z) * world.multipler)));
				if (precalc > Chunk.CHUNK_HEIGHT - 1) {
					precalc = Chunk.CHUNK_HEIGHT - 1;
				}

				if (precalc < 0) {
					precalc = 0;
				}

				heightMap[x % Chunk.CHUNK_SIZE][z % Chunk.CHUNK_SIZE] = precalc;
			}
		}
		Chunk chunk = new Chunk(xc, zc, world);

		for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
			for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
				int h = heightMap[x][z];
				for (int y = 0; y < Chunk.CHUNK_HEIGHT; y++) {
					if ((y <= h || y == 0) && !(simplex3D.getNoise(x + (xc * Chunk.CHUNK_SIZE), y, z + (zc * Chunk.CHUNK_SIZE)) < -0.2D * (y / 80.0D))) {
						if (y == h) {
							chunk.setBlock(x, y, z, Block.grass.getId(), false);
						} else {
							if (y < h - 4) {
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
