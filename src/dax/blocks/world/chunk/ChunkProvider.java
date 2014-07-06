package dax.blocks.world.chunk;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;

import org.xerial.snappy.Snappy;

import dax.blocks.Coord2D;
import dax.blocks.Game;
import dax.blocks.GameMath;
import dax.blocks.block.Block;
import dax.blocks.block.BlockPlant;
import dax.blocks.render.ChunkDistanceComparator;
import dax.blocks.world.CoordDistanceComparator;
import dax.blocks.world.World;
import dax.blocks.world.generator.SimplexNoise;

public class ChunkProvider {

	private static final int SAMPLE_RATE_HORIZONTAL = 8;
	private static final int SAMPLE_RATE_VERTICAL = 4;



	public boolean loading = false;
	//Shall I load the world from files?
	public boolean loadingWorld = true;
	Map<Coord2D, Chunk> loadedChunks;
	
	public double[] densityOffsets;

	SimplexNoise simplex3D_1;
	SimplexNoise simplex3D_2;
	SimplexNoise simplex3D_caves;

	World world;
	public ChunkSaveManager loader;

	int seed;

	public boolean isChunkLoaded(Coord2D coord) {
		return loadedChunks.containsKey(coord);
	}

	public Chunk getChunk(Coord2D coord) {
		return loadedChunks.get(coord);
	}

	@SuppressWarnings("rawtypes")
	public void updateLoadedChunksInRadius(int x, int y, int r) {
		int loaded = 0;

		List<Coord2D> sortedCoords = new ArrayList<Coord2D>();

		// (Math.sqrt(cdistX * cdistX + cdistZ * cdistZ) < r)
		for (int ix = x - r; ix <= x + r; ix++) {
			for (int iy = y - r; iy <= y + r; iy++) {
				Coord2D coord = new Coord2D(ix, iy);
				if (!loadedChunks.containsKey(coord)) {
					sortedCoords.add(coord);
				}
			}
		}

		Collections.sort(sortedCoords, new CoordDistanceComparator(x, y));

		for (Coord2D c : sortedCoords) {
			if (loaded >= Game.settings.loads_pt.getValue()) {
				loading = true;
				break;
			} 
			
			loadChunk(c);
			loaded++;
			loading = false;
		}

		List<Chunk> unpopulated = new LinkedList<Chunk>();
		
		Iterator it = loadedChunks.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();

			Chunk c = (Chunk) pairs.getValue();
			Coord2D coord = (Coord2D) pairs.getKey();

			int xdist = Math.abs(x - c.x);
			int ydist = Math.abs(y - c.z);

			if ((xdist > r || ydist > r) && loadedChunks.containsKey(coord)) {
				loader.saveChunk(c);
				c.deleteAllRenderChunks();
				it.remove();
			} 
			
			if (!c.populated && loadedChunks.containsKey(coord)){
				
				unpopulated.add(c);
				
			}

		}
		
		int populated = 0;
		
		ChunkDistanceComparator cdc = new ChunkDistanceComparator();
		cdc.setFrontToBack();
		
		Collections.sort(unpopulated, cdc);
		
		for (Chunk c : unpopulated) {
			
			if (populated >= Game.settings.decorations_pt.getValue()) {
				break;
			}
			
			Coord2D q00 = new Coord2D(c.x-1, c.z-1);
			Coord2D q10 = new Coord2D(c.x, c.z-1);
			Coord2D q20 = new Coord2D(c.x+1, c.z-1);
			Coord2D q01 = new Coord2D(c.x-1, c.z);
			Coord2D q21 = new Coord2D(c.x+1, c.z);
			Coord2D q02 = new Coord2D(c.x-1, c.z+1);
			Coord2D q12 = new Coord2D(c.x, c.z+1);
			Coord2D q22 = new Coord2D(c.x+1, c.z+1);
			
			if (isChunkLoaded(q00) && isChunkLoaded(q10) && isChunkLoaded(q20) && isChunkLoaded(q01) && isChunkLoaded(q21) && isChunkLoaded(q02) && isChunkLoaded(q12) && isChunkLoaded(q22)) {
				c.populated = true;
				populated++;
				Random rand = new Random((c.x*31+c.z)+seed);
				
				int tries = rand.nextInt(3);
				
				for (int i = 0; i < tries; i++) {
					int tx = c.x*16+rand.nextInt(16);
					int tz = c.z*16+rand.nextInt(16);

					for (int h = 127; h >= 0; h--) {
						int block = world.getBlock(tx, h, tz);
						if (block == Block.grass.getId()) {
							
							if(Game.settings.tree_generation.getValue())
								world.treeGen.generateTree(tx, h+1, tz);
							
						} else if (block != 0 && !(Block.getBlock((byte) block) instanceof BlockPlant)) {
							break;
						}
					}
				}
				
			}
		}
	}

	public void unloadChunk(Coord2D coord) {
		Chunk c = loadedChunks.get(coord);
		c.deleteAllRenderChunks();
		loader.saveChunk(c);
		loadedChunks.remove(coord);
	}

	public ChunkProvider(World world, boolean shouldLoad) {
		this(new Random().nextInt(), world, shouldLoad);
	}

	public LinkedList<Chunk> getAllLoadedChunks() {
		return new LinkedList<Chunk>(loadedChunks.values());
	}

	public List<Chunk> getChunksInRadius(int x, int y, int r) {
		List<Chunk> chunks = new LinkedList<Chunk>();

		for (int ix = x - r; ix <= x + r; ix++) {
			for (int iy = y - r; iy <= y + r; iy++) {
				Coord2D coord = world.getCoord2D(ix, iy);
				chunks.add(getChunk(coord));
			}
		}

		return chunks;
	}

	public ChunkProvider(int seed, World world, boolean shouldLoad) {
		this.loadedChunks = new HashMap<Coord2D, Chunk>();
		this.seed = seed;
		this.world = world;
		this.loader = new ChunkSaveManager(this, world.name);
		
		this.loadingWorld = shouldLoad;
			loader.tryToLoadWorld();
		

		this.simplex3D_1 = new SimplexNoise(512, 0.425, this.seed);
		this.simplex3D_2 = new SimplexNoise(512, 0.525, this.seed*2);
		this.simplex3D_caves = new SimplexNoise(64, 0.55, this.seed*3);
		
		double[] offsets = new double[128];
		Arrays.fill(offsets, -9999);
		
		offsets[0] = 1.2;
		offsets[38] = 0.5;
		offsets[40] = 0.3;
		offsets[40] = 0.3;
		offsets[52] = 0.0;
		offsets[56] = -0.1;
		offsets[60] = -0.14;
		offsets[68] = -0.175;
		offsets[75] = -0.2;
		offsets[80] = -0.30;
		offsets[82] = -0.4;
		offsets[85] = -0.5;
		offsets[127] = -1.2;
		
		densityOffsets = new double[128];	
		
		for (int i = 0; i < 128; i++) {
			if (offsets[i] != -9999) {
				densityOffsets[i] = offsets[i];
			} else {
				int lp = 0;
				int hp = 0;
				
				for (int x = i; x >= 0; x--) {
					if (offsets[x] != -9999) {
						lp = x;
						break;
					}
				}
				
				for (int x = i; x < 128; x++) {
					if (offsets[x] != -9999) {
						hp = x;
						break;
					}
				}
				
				densityOffsets[i] = GameMath.lerp(i, lp, hp, offsets[lp], offsets[hp]);
				
			}
		}
		
	}

	public void loadChunk(Coord2D coord) {
		loadedChunks.put(coord, getChunk(coord.x, coord.y, loadingWorld));
	}

	public double[][][] getChunkDensityMap(int cx, int cz) {
		double[][][] densityMap = new double[16+1][128+1][16+1];

		for (int x = 0; x <= 16; x += SAMPLE_RATE_HORIZONTAL) {
			for (int z = 0; z <= 16; z += SAMPLE_RATE_HORIZONTAL) {
				for (int y = 0; y <= 128; y += SAMPLE_RATE_VERTICAL) {
					densityMap[x][y][z] = (float) Math.max(simplex3D_1.getNoise(cx*16+x, y, cz*16+z), simplex3D_2.getNoise(cx*16+x, y, cz*16+z));				
				}
			}
		}

		triLerpDensityMap(densityMap);

		return densityMap;
	}
	
	public double[][][] getChunkCaveMap(int cx, int cz) {
		double[][][] caveMap = new double[16+1][128+1][16+1];

		for (int x = 0; x <= 16; x += SAMPLE_RATE_HORIZONTAL) {
			for (int z = 0; z <= 16; z += SAMPLE_RATE_HORIZONTAL) {
				for (int y = 0; y <= 128; y += SAMPLE_RATE_VERTICAL) {
					caveMap[x][y][z] = simplex3D_caves.getNoise(cx*16+x, y, cz*16+z);				
				}
			}
		}

		triLerpCaveMap(caveMap);

		return caveMap;
	}

	private void triLerpDensityMap(double[][][] densityMap) {
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 128; y++) {
				for (int z = 0; z < 16; z++) {
					if (!(x % SAMPLE_RATE_HORIZONTAL == 0 && y % SAMPLE_RATE_VERTICAL == 0 && z % SAMPLE_RATE_HORIZONTAL == 0)) {
						int offsetX = (x / SAMPLE_RATE_HORIZONTAL) * SAMPLE_RATE_HORIZONTAL;
						int offsetY = (y / SAMPLE_RATE_VERTICAL) * SAMPLE_RATE_VERTICAL;
						int offsetZ = (z / SAMPLE_RATE_HORIZONTAL) * SAMPLE_RATE_HORIZONTAL;
						densityMap[x][y][z] = GameMath.triLerp(x, y, z,
								densityMap[offsetX][offsetY][offsetZ],
								densityMap[offsetX][SAMPLE_RATE_VERTICAL + offsetY][offsetZ],
								densityMap[offsetX][offsetY][offsetZ + SAMPLE_RATE_HORIZONTAL],
								densityMap[offsetX][offsetY + SAMPLE_RATE_VERTICAL][offsetZ + SAMPLE_RATE_HORIZONTAL],
								densityMap[SAMPLE_RATE_HORIZONTAL + offsetX][offsetY][offsetZ],
								densityMap[SAMPLE_RATE_HORIZONTAL + offsetX][offsetY + SAMPLE_RATE_VERTICAL][offsetZ],
								densityMap[SAMPLE_RATE_HORIZONTAL + offsetX][offsetY][offsetZ + SAMPLE_RATE_HORIZONTAL],
								densityMap[SAMPLE_RATE_HORIZONTAL + offsetX][offsetY + SAMPLE_RATE_VERTICAL][offsetZ + SAMPLE_RATE_HORIZONTAL],
								offsetX, SAMPLE_RATE_HORIZONTAL + offsetX, offsetY,
								SAMPLE_RATE_VERTICAL + offsetY, offsetZ, offsetZ + SAMPLE_RATE_HORIZONTAL);
					}
				}
			}
		}
	}
	
	private void triLerpCaveMap(double[][][] caveMap) {
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 128; y++) {
				for (int z = 0; z < 16; z++) {
					if (!(x % SAMPLE_RATE_HORIZONTAL == 0 && y % SAMPLE_RATE_VERTICAL == 0 && z % SAMPLE_RATE_HORIZONTAL == 0)) {
						int offsetX = (x / SAMPLE_RATE_HORIZONTAL) * SAMPLE_RATE_HORIZONTAL;
						int offsetY = (y / SAMPLE_RATE_VERTICAL) * SAMPLE_RATE_VERTICAL;
						int offsetZ = (z / SAMPLE_RATE_HORIZONTAL) * SAMPLE_RATE_HORIZONTAL;
						caveMap[x][y][z] = GameMath.triLerp(x, y, z,
								caveMap[offsetX][offsetY][offsetZ],
								caveMap[offsetX][SAMPLE_RATE_VERTICAL + offsetY][offsetZ],
								caveMap[offsetX][offsetY][offsetZ + SAMPLE_RATE_HORIZONTAL],
								caveMap[offsetX][offsetY + SAMPLE_RATE_VERTICAL][offsetZ + SAMPLE_RATE_HORIZONTAL],
								caveMap[SAMPLE_RATE_HORIZONTAL + offsetX][offsetY][offsetZ],
								caveMap[SAMPLE_RATE_HORIZONTAL + offsetX][offsetY + SAMPLE_RATE_VERTICAL][offsetZ],
								caveMap[SAMPLE_RATE_HORIZONTAL + offsetX][offsetY][offsetZ + SAMPLE_RATE_HORIZONTAL],
								caveMap[SAMPLE_RATE_HORIZONTAL + offsetX][offsetY + SAMPLE_RATE_VERTICAL][offsetZ + SAMPLE_RATE_HORIZONTAL],
								offsetX, SAMPLE_RATE_HORIZONTAL + offsetX, offsetY,
								SAMPLE_RATE_VERTICAL + offsetY, offsetZ, offsetZ + SAMPLE_RATE_HORIZONTAL);
					}
				}
			}
		}
	}
	
	private Chunk generateChunk(int xc, int zc) {
		Chunk chunk = new Chunk(xc, zc, world);

		double[][][] densityMap = getChunkDensityMap(xc, zc);
		double[][][] caveMap = getChunkCaveMap(xc, zc);
		
		Random cRand = new Random((xc*31+zc)+seed+1);
		
		for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
			for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
				int depth = 0;
				
				for (int y = 127; y >= 0; y--) {
					double density = densityMap[x][y][z];
					boolean cave = caveMap[x][y][z] > (0.3 + 1/(8.0+depth/2));
					
					if (!cave && density > 0 - densityOffsets[y]) {
						if (depth == 0) {
							if (y < 52) {
								chunk.setBlock(x, y, z, Block.sand.getId(), false);
							} else {
								chunk.setBlock(x, y, z, Block.grass.getId(), false);
								
								int r = cRand.nextInt(100);
								
								if (r <= 20) {
									chunk.setBlock(x, y+1, z, Block.tallgrass.getId(), false);
								} else {
									switch(r) {
									case 30:
										chunk.setBlock(x, y+1, z, Block.flower_1.getId(), false);
										break;
									case 40:
										chunk.setBlock(x, y+1, z, Block.flower_2.getId(), false);
										break;
									case 50:
										chunk.setBlock(x, y+1, z, Block.flower_3.getId(), false);
										break;
									default:	
									}
								}
							}
						} else if (depth < 4) {
							if (y < 52) {
								chunk.setBlock(x, y, z, Block.sand.getId(), false);
							} else {
								chunk.setBlock(x, y, z, Block.dirt.getId(), false);
							}
						} else {
							chunk.setBlock(x, y, z, Block.stone.getId(), false);
						}

						depth++;
					} else {
						if ((!cave || depth == 0) && y < 50) {
							chunk.setBlock(x, y, z, Block.water.getId(), false);
						}
						
						if (!cave) {
							depth = 0;
						}
					}
					
					if (y == 0) {
						chunk.setBlock(x, y, z, Block.bedrock.getId(), false);
					} 
				}
			}
		}
		return chunk;

	}

	public Chunk getChunk(int xc, int zc, boolean canLoad) {
		if (loader.isChunkSaved(xc, zc) && canLoad) {
			System.out.println("Loading " + xc + ":" + zc);
			return loader.loadChunk(xc, zc);
			
		} else {
			return generateChunk(xc, zc);
		}
	}

	
}
