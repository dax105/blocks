package dax.blocks.world.chunk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import dax.blocks.block.BlockPlant;
import dax.blocks.settings.Settings;
import dax.blocks.util.Coord2D;
import dax.blocks.util.GameMath;
import dax.blocks.world.ChunkDistanceComparator;
import dax.blocks.world.CoordDistanceComparator;
import dax.blocks.world.IDRegister;
import dax.blocks.world.World;
import dax.blocks.world.generator.Biome;
import dax.blocks.world.generator.SimplexNoise;
import dax.blocks.world.generator.TreeGenerator;

public class ChunkProvider {

	private static final int SAMPLE_RATE_HORIZONTAL = 8;
	private static final int SAMPLE_RATE_VERTICAL = 4;

	public boolean loading = false;

	private TreeGenerator treeGen;
	Map<Coord2D, Chunk> loadedChunks;
	LinkedHashMap<Coord2D, Chunk> cachedChunks;

	SimplexNoise simplex3D_1;
	SimplexNoise simplex3D_2;
	SimplexNoise simplex3D_caves;
	
	SimplexNoise simplex2D_rainfall;
	SimplexNoise simplex2D_temperature;

	World world;
	public ChunkSaveManager loader;

	int seed;

	public boolean isChunkLoaded(Coord2D coord) {
		return loadedChunks.containsKey(coord);
	}

	public Chunk getChunk(Coord2D coord) {
		return loadedChunks.get(coord);
	}

	public void updateLoadedChunksInRadius(int x, int y, int r) {
		int loaded = 0;

		List<Coord2D> sortedCoords = new ArrayList<Coord2D>();

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
			if (loaded >= Settings.getInstance().loadsPerTick.getValue()) {
				loading = true;
				break;
			} 
			
			loadChunk(c);
			loaded++;
			loading = false;
		}

		List<Chunk> unpopulated = new LinkedList<Chunk>();
		
		Iterator<Entry<Coord2D, Chunk>> it = loadedChunks.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Coord2D, Chunk> pairs = (Map.Entry<Coord2D, Chunk>) it.next();

			Chunk c = (Chunk) pairs.getValue();
			Coord2D coord = (Coord2D) pairs.getKey();

			int xdist = Math.abs(x - c.x);
			int ydist = Math.abs(y - c.z);

			if ((xdist > r || ydist > r) && loadedChunks.containsKey(coord)) {
				//loader.saveChunk(c);
				//c.deleteAllRenderChunks();
				cacheChunk(c);
				it.remove();
			} 
			
			if (!c.populated && loadedChunks.get(coord) != null){
				
				unpopulated.add(c);
				
			}

		}
		
		int populated = 0;
		
		ChunkDistanceComparator cdc = new ChunkDistanceComparator();
		cdc.setFrontToBack();
		
		Collections.sort(unpopulated, cdc);
		
		for (Chunk c : unpopulated) {
			
			if (populated >= Settings.getInstance().decorationsPerTick.getValue()) {
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
						if (block == IDRegister.grass.getID()) {
							
							if(Settings.getInstance().treeGeneration.getValue())
								this.treeGen.generateTree(tx, h+1, tz);
							
						} else if (block != 0 && !(world.getBlockObject(block) instanceof BlockPlant)) {
							break;
						}
					}
				}
				
			}
		}
	}

	public void cacheChunk(Chunk c) {
		cachedChunks.put(new Coord2D(c.x, c.z), c);
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
		this.cachedChunks = new LinkedHashMap<Coord2D, Chunk>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry(Map.Entry<Coord2D, Chunk> eldest) {
				if (size() > Settings.getInstance().chunkCacheSize.getValue()) {
					eldest.getValue().deleteAllRenderChunks();
					loader.saveChunk(eldest.getValue());
				}
				
				return size() > Settings.getInstance().chunkCacheSize.getValue();
			}
		};
		
		this.seed = seed;
		this.world = world;
		this.loader = new ChunkSaveManager(this, world.name);
		loader.tryToLoadWorld();
		
		this.treeGen = new TreeGenerator(this.world);
		this.simplex3D_1 = new SimplexNoise(512, 0.425, this.seed);
		this.simplex3D_2 = new SimplexNoise(512, 0.525, this.seed*2);
		this.simplex3D_caves = new SimplexNoise(64, 0.55, this.seed*3);
		this.simplex2D_rainfall = new SimplexNoise(2048, 0.3, this.seed+1);
		this.simplex2D_temperature = new SimplexNoise(1024, 0.2, this.seed+2);

	}

	public void loadChunk(Coord2D coord) {
		loadedChunks.put(coord, getChunk(coord.x, coord.y));
	}

	public double[][][] getChunkDensityMap(int cx, int cz) {
		double[][][] densityMap = new double[16+1][128+1][16+1];

		for (int x = 0; x <= 16; x += SAMPLE_RATE_HORIZONTAL) {
			for (int z = 0; z <= 16; z += SAMPLE_RATE_HORIZONTAL) {
				for (int y = 0; y <= 128; y += SAMPLE_RATE_VERTICAL) {
					double[] densityOffsets = getOffsetsAtLocation(cx*16+x, cz*16+z);
					densityMap[x][y][z] = (float) (Math.max(simplex3D_1.getNoise(cx*16+x, y, cz*16+z), simplex3D_2.getNoise(cx*16+x, y, cz*16+z)))+densityOffsets[y];				
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
	
	public Biome getBiomeAtLocation(int x, int z) {
		float noise = (float) simplex2D_rainfall.getNoise(x, z);
		return noise > 0 ? Biome.mountains : Biome.plains;
	}
	
	private double[] getOffsetsAtLocation(int x, int z) {
		float smoothening = 0.025f;
		float noise = (float) simplex2D_rainfall.getNoise(x, z);
		float offset = 0-noise;
		if (noise > smoothening) {
			return Biome.mountains.getOffsets();
		} else if (noise < -smoothening) {
			return Biome.plains.getOffsets();
		} else {
			double[] interpolated = new double[129];
			
			for (int i = 0; i < 129; i++) {
				interpolated[i] = GameMath.lerp(Biome.mountains.getOffsets()[i], Biome.plains.getOffsets()[i], 1/smoothening/2*offset+0.5); //GameMath.lerp(i, Biome.mountains.getOffsets()[i], Biome.plains.getOffsets()[i], smoothening, -smoothening);
			}
			
			return interpolated;
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
					
					if (!cave && density > 0) {
						if (depth == 0) {
							if (y < 52) {
								chunk.setBlock(x, y, z, IDRegister.sand.getID(), false);
							} else {
								chunk.setBlock(x, y, z, IDRegister.grass.getID(), false);
								
								int r = cRand.nextInt(100);
								
								if (r <= 20) {
									chunk.setBlock(x, y+1, z, IDRegister.tallGrass.getID(), false);
								} else {
									switch(r) {
									case 30:
										chunk.setBlock(x, y+1, z, IDRegister.flower1.getID(), false);
										break;
									case 40:
										chunk.setBlock(x, y+1, z, IDRegister.flower2.getID(), false);
										break;
									case 50:
										chunk.setBlock(x, y+1, z, IDRegister.flower3.getID(), false);
										break;
									default:	
									}
								}
							}
						} else if (depth < 4) {
							if (y < 52) {
								chunk.setBlock(x, y, z, IDRegister.sand.getID(), false);
							} else {
								chunk.setBlock(x, y, z, IDRegister.dirt.getID(), false);
							}
						} else {
							chunk.setBlock(x, y, z, IDRegister.stone.getID(), false);
						}

						depth++;
					} else {
						if ((!cave || depth == 0) && y < 50) {
							chunk.setBlock(x, y, z, IDRegister.water.getID(), false);
						}
						
						if (!cave) {
							depth = 0;
						}
					}
					
					if (y == 0) {
						chunk.setBlock(x, y, z, IDRegister.bedrock.getID(), false);
					} 
				}
			}
		}
		return chunk;

	}

	public boolean isChunkCached(int x, int z) {
		return cachedChunks.get(new Coord2D(x, z)) != null;
	}
	
	public Chunk getChunk(int xc, int zc) {
		if (isChunkCached(xc, zc)) {
			Coord2D coord = new Coord2D(xc, zc);
			Chunk c = cachedChunks.get(coord);
			cachedChunks.remove(coord);
			return c;
		} else if (loader.isChunkSaved(xc, zc)) {
			System.out.println("Loading " + xc + ":" + zc);
			return loader.loadChunk(xc, zc);
			
		} else {
			return generateChunk(xc, zc);
		}
	}

	
}
