package cz.dat.oots.world.chunk;

import cz.dat.oots.block.Block;
import cz.dat.oots.block.BlockPlant;
import cz.dat.oots.util.Coord2D;
import cz.dat.oots.util.GameMath;
import cz.dat.oots.world.ChunkDistanceComparator;
import cz.dat.oots.world.CoordDistanceComparator;
import cz.dat.oots.world.IDRegister;
import cz.dat.oots.world.World;
import cz.dat.oots.world.generator.Biome;
import cz.dat.oots.world.generator.ChunkLoaderThread;
import cz.dat.oots.world.generator.GenTempStorage;
import cz.dat.oots.world.generator.SimplexNoise;
import cz.dat.oots.world.generator.TreeGenerator;
import cz.dat.oots.world.loading.ChunkSaveManager;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ChunkProvider {

    private static final int SAMPLE_RATE_HORIZONTAL = 8;
    private static final int SAMPLE_RATE_VERTICAL = 4;

    private static final int GEN_LAVA_HEIGHT = 12;

    public boolean loading = false;
    public Map<Coord2D, Chunk> loadedChunks;
    public ConcurrentHashMap<Coord2D, Chunk> loaded = new ConcurrentHashMap<Coord2D, Chunk>();
    public Queue<Coord2D> loadNeeded = new ConcurrentLinkedQueue<Coord2D>();
    public Set<Coord2D> loadingChunks = Collections
            .newSetFromMap(new ConcurrentHashMap<Coord2D, Boolean>());
    public World world;
    public ChunkSaveManager loader;
    public int seed;
    private TreeGenerator treeGen;
    private LinkedHashMap<Coord2D, Chunk> cachedChunks;
    private SimplexNoise simplex3D_1;
    //private SimplexNoise simplex3D_2;
    private SimplexNoise simplex3D_caves;
    private SimplexNoise simplex2D_rainfall;
    // TODO: Temperature
    //private SimplexNoise simplex2D_temperature;
    private ChunkLoaderThread[] loaders;
    private Thread[] loaderThreads;
    private Random rnd = new Random();

    public ChunkProvider(World world, boolean shouldLoad) {
        this(new Random().nextInt(), world, shouldLoad);
    }

    public ChunkProvider(int seed, final World world, boolean shouldLoad) {
        this.loaders = new ChunkLoaderThread[world.getGame().s().loaderThreads
                .getValue()];
        this.loaderThreads = new Thread[world.getGame().s().loaderThreads
                .getValue()];

        this.loadedChunks = new HashMap<Coord2D, Chunk>();
        this.cachedChunks = new LinkedHashMap<Coord2D, Chunk>() {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean removeEldestEntry(Map.Entry<Coord2D, Chunk> eldest) {
                if (size() > world.getGame().s().chunkCacheSize.getValue()) {
                    eldest.getValue().deleteAllRenderChunks();
                    loader.saveChunk(eldest.getValue());
                }

                return size() > world.getGame().s().chunkCacheSize.getValue();
            }
        };

        this.seed = seed;
        this.world = world;
        this.loader = new ChunkSaveManager(this, world.name);
        this.loader.tryToLoadWorld(world.getGame());

        this.treeGen = new TreeGenerator(this.world);
        this.simplex3D_1 = new SimplexNoise(512, 0.425, this.seed);
        //this.simplex3D_2 = new SimplexNoise(512, 0.525, this.seed * 2);
        this.simplex3D_caves = new SimplexNoise(64, 0.55, this.seed * 3);
        this.simplex2D_rainfall = new SimplexNoise(2048, 0.3, this.seed + 1);
        //this.simplex2D_temperature = new SimplexNoise(1024, 0.2, this.seed + 2);

        for (int i = 0; i < world.getGame().s().loaderThreads.getValue(); i++) {
            this.loaders[i] = new ChunkLoaderThread(this);
            this.loaderThreads[i] = new Thread(this.loaders[i]);
            this.loaderThreads[i].setName("Chunk loader " + i);
            this.loaderThreads[i].start();
        }
    }

    public boolean isChunkLoaded(Coord2D coord) {
        return this.loadedChunks.containsKey(coord);
    }

    public Chunk getChunk(Coord2D coord) {
        return this.loadedChunks.get(coord);
    }

    public void resumeAllLoaders() {
        for (ChunkLoaderThread cl : loaders) {
            cl.resume();
        }
    }

    public void updateLoadedChunksInRadius(int x, int y, int r) {

        for (Iterator<Entry<Coord2D, Chunk>> it = this.loaded.entrySet()
                .iterator(); it.hasNext(); ) {
            Entry<Coord2D, Chunk> e = it.next();
            this.loadedChunks.put(e.getKey(), e.getValue());
            it.remove();
        }

        List<Coord2D> sortedCoords = new ArrayList<Coord2D>();

        for (int ix = x - r; ix <= x + r; ix++) {
            for (int iy = y - r; iy <= y + r; iy++) {
                Coord2D coord = new Coord2D(ix, iy);
                if (!this.loadedChunks.containsKey(coord)
                        && !this.loadingChunks.contains(coord)) {
                    sortedCoords.add(coord);
                }
            }
        }

        Collections.sort(sortedCoords, new CoordDistanceComparator(x, y));

        this.loadNeeded.clear();

        for (Coord2D c : sortedCoords) {
            loadNeeded.add(c);
        }

        this.loading = loadNeeded.size() > 0;

        if (this.loading) {
            resumeAllLoaders();
        }

        List<Chunk> unpopulated = new LinkedList<Chunk>();

        Iterator<Entry<Coord2D, Chunk>> it = loadedChunks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Coord2D, Chunk> pairs = it.next();

            Chunk c = pairs.getValue();
            Coord2D coord = pairs.getKey();

            int xdist = Math.abs(x - c.x);
            int ydist = Math.abs(y - c.z);

            float rtFalloff = 1 / (1 + (float)Math.sqrt(xdist * xdist + ydist * ydist));
            this.makeRandomTicks(c, rtFalloff);

            if ((xdist > r || ydist > r) && this.loadedChunks.containsKey(coord)) {
                // loader.saveChunk(c);
                // c.deleteAllRenderChunks();
                this.cacheChunk(c);
                it.remove();
            }

            if (!c.populated && this.loadedChunks.get(coord) != null) {

                unpopulated.add(c);

            }

        }

        int populated = 0;

        ChunkDistanceComparator cdc = new ChunkDistanceComparator();
        cdc.setFrontToBack();

        Collections.sort(unpopulated, cdc);

        for (Chunk c : unpopulated) {

            if (populated >= world.getGame().s().decorationsPerTick.getValue()) {
                break;
            }

            Coord2D q00 = new Coord2D(c.x - 1, c.z - 1);
            Coord2D q10 = new Coord2D(c.x, c.z - 1);
            Coord2D q20 = new Coord2D(c.x + 1, c.z - 1);
            Coord2D q01 = new Coord2D(c.x - 1, c.z);
            Coord2D q21 = new Coord2D(c.x + 1, c.z);
            Coord2D q02 = new Coord2D(c.x - 1, c.z + 1);
            Coord2D q12 = new Coord2D(c.x, c.z + 1);
            Coord2D q22 = new Coord2D(c.x + 1, c.z + 1);

            if (this.isChunkLoaded(q00) && isChunkLoaded(q10)
                    && isChunkLoaded(q20) && isChunkLoaded(q01)
                    && isChunkLoaded(q21) && isChunkLoaded(q02)
                    && isChunkLoaded(q12) && isChunkLoaded(q22)) {
                c.populated = true;
                populated++;
                Random rand = new Random((c.x * 31 + c.z) + seed);

                int tries = rand.nextInt(3);

                for (int i = 0; i < tries; i++) {
                    int tx = c.x * 16 + rand.nextInt(16);
                    int tz = c.z * 16 + rand.nextInt(16);

                    for (int h = 127; h >= 0; h--) {
                        int block = this.world.getBlock(tx, h, tz);
                        if (block == IDRegister.grass.getID()) {

                            if (world.getGame().s().treeGeneration.getValue()) {
                                this.treeGen.generateTree(tx, h + 1, tz);
                            }

                        } else if (block != 0
                                && !(this.world.getBlockObject(block) instanceof BlockPlant)) {
                            break;
                        }
                    }
                }

            }
        }
    }

    private void makeRandomTicks(Chunk c, float falloff) {
        int blockCount = (int)(this.world.getGame().s().randomtickBlockCount.getValue() * falloff);

        for(int i = 0; i < blockCount; i++) {
            int x = this.rnd.nextInt(16);
            int y = this.rnd.nextInt(128);
            int z = this.rnd.nextInt(16);

            Block b = this.world.getRegister().getBlock(c.getBlock(x, y, z));
            if(b.isRequiringRandomTick()) {
                b.onRandomTick(x + c.x * 16, y, z + c.z * 16, world);
            }
        }
    }

    public void cacheChunk(Chunk c) {
        this.cachedChunks.put(new Coord2D(c.x, c.z), c);
    }

    public void unloadChunk(Coord2D coord) {
        Chunk c = loadedChunks.get(coord);
        c.deleteAllRenderChunks();
        this.loader.saveChunk(c);
        this.loadedChunks.remove(coord);
    }

    public LinkedList<Chunk> getAllLoadedChunks() {
        return new LinkedList<Chunk>(this.loadedChunks.values());
    }

    public List<Chunk> getChunksInRadius(int x, int y, int r) {
        List<Chunk> chunks = new LinkedList<Chunk>();

        for (int ix = x - r; ix <= x + r; ix++) {
            for (int iy = y - r; iy <= y + r; iy++) {
                Coord2D coord = this.world.getCoord2D(ix, iy);
                chunks.add(this.getChunk(coord));
            }
        }

        return chunks;
    }



    //private double[][][] densityMap = new double[16 + 1][128 + 1][16 + 1];
    //private double[][][] caveMap = new double[16 + 1][128 + 1][16 + 1];
    
    public float[][][] getChunkDensityMap(float[][][] data, int cx, int cz) {
        for (int x = 0; x <= 16; x += ChunkProvider.SAMPLE_RATE_HORIZONTAL) {
            for (int z = 0; z <= 16; z += ChunkProvider.SAMPLE_RATE_HORIZONTAL) {
                for (int y = 0; y <= 128; y += ChunkProvider.SAMPLE_RATE_VERTICAL) {
                    float[] densityOffsets = this.getOffsetsAtLocation(cx * 16 + x, cz * 16 + z);
                    data[x][y][z] = (float) this.simplex3D_1.getNoise(cx * 16 + x, y, cz * 16 + z) + densityOffsets[y];
                }
            }
        }

        this.triLerpDensityMap(data);

        return data;
    }

    public float[][][] getChunkCaveMap(float[][][] data, int cx, int cz) {
        for (int x = 0; x <= 16; x += ChunkProvider.SAMPLE_RATE_HORIZONTAL) {
            for (int z = 0; z <= 16; z += ChunkProvider.SAMPLE_RATE_HORIZONTAL) {
                for (int y = 0; y <= 128; y += ChunkProvider.SAMPLE_RATE_VERTICAL) {
                    data[x][y][z] = (float) this.simplex3D_caves.getNoise(cx * 16
                            + x, y, cz * 16 + z);
                }
            }
        }

        this.triLerpDensityMap(data);

        return data;
    }

    private void triLerpDensityMap(float[][][] densityMap) {
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 128; y++) {
                for (int z = 0; z < 16; z++) {
                    if (!(x % ChunkProvider.SAMPLE_RATE_HORIZONTAL == 0
                            && y % ChunkProvider.SAMPLE_RATE_VERTICAL == 0 && z
                            % ChunkProvider.SAMPLE_RATE_HORIZONTAL == 0)) {
                        int offsetX = (x / ChunkProvider.SAMPLE_RATE_HORIZONTAL)
                                * ChunkProvider.SAMPLE_RATE_HORIZONTAL;
                        int offsetY = (y / ChunkProvider.SAMPLE_RATE_VERTICAL)
                                * ChunkProvider.SAMPLE_RATE_VERTICAL;
                        int offsetZ = (z / ChunkProvider.SAMPLE_RATE_HORIZONTAL)
                                * ChunkProvider.SAMPLE_RATE_HORIZONTAL;
                        densityMap[x][y][z] = GameMath
                                .triLerp(
                                        x,
                                        y,
                                        z,
                                        densityMap[offsetX][offsetY][offsetZ],
                                        densityMap[offsetX][ChunkProvider.SAMPLE_RATE_VERTICAL
                                                + offsetY][offsetZ],
                                        densityMap[offsetX][offsetY][offsetZ
                                                + ChunkProvider.SAMPLE_RATE_HORIZONTAL],
                                        densityMap[offsetX][offsetY
                                                + ChunkProvider.SAMPLE_RATE_VERTICAL][offsetZ
                                                + ChunkProvider.SAMPLE_RATE_HORIZONTAL],
                                        densityMap[ChunkProvider.SAMPLE_RATE_HORIZONTAL
                                                + offsetX][offsetY][offsetZ],
                                        densityMap[ChunkProvider.SAMPLE_RATE_HORIZONTAL
                                                + offsetX][offsetY
                                                + ChunkProvider.SAMPLE_RATE_VERTICAL][offsetZ],
                                        densityMap[ChunkProvider.SAMPLE_RATE_HORIZONTAL
                                                + offsetX][offsetY][offsetZ
                                                + ChunkProvider.SAMPLE_RATE_HORIZONTAL],
                                        densityMap[ChunkProvider.SAMPLE_RATE_HORIZONTAL
                                                + offsetX][offsetY
                                                + ChunkProvider.SAMPLE_RATE_VERTICAL][offsetZ
                                                + ChunkProvider.SAMPLE_RATE_HORIZONTAL],
                                        offsetX,
                                        ChunkProvider.SAMPLE_RATE_HORIZONTAL
                                                + offsetX,
                                        offsetY,
                                        ChunkProvider.SAMPLE_RATE_VERTICAL
                                                + offsetY,
                                        offsetZ,
                                        offsetZ
                                                + ChunkProvider.SAMPLE_RATE_HORIZONTAL);
                    }
                }
            }
        }
    }

    public Biome getBiomeAtLocation(int x, int z) {
        float noise = (float) this.simplex2D_rainfall.getNoise(x, z);
        noise *= 0.5f;
        return noise > 0 ? Biome.mountains : Biome.plains;
    }

    private float[] getOffsetsAtLocation(int x, int z) {
        float smoothening = 0.025f;
        float noise = (float) this.simplex2D_rainfall.getNoise(x, z);
        float offset = 0 - noise;
        if (noise > smoothening) {
            return Biome.mountains.getOffsets();
        } else if (noise < -smoothening) {
            return Biome.plains.getOffsets();
        } else {
            float[] interpolated = new float[129];

            for (int i = 0; i < 129; i++) {
                interpolated[i] = GameMath.lerp(
                        Biome.mountains.getOffsets()[i],
                        Biome.plains.getOffsets()[i], 1 / smoothening / 2
                                * offset + 0.5f); // GameMath.lerp(i,
                // Biome.mountains.getOffsets()[i],
                // Biome.plains.getOffsets()[i],
                // smoothening,
                // -smoothening);
            }

            return interpolated;
        }
    }

    private Chunk generateChunk(GenTempStorage storage, int xc, int zc) {
    	Chunk chunk = new Chunk(xc, zc, world);

    	float[][][] densityMap = this.getChunkDensityMap(storage.getTempStorageT(), xc, zc);
        float[][][] caveMap = this.getChunkCaveMap(storage.getTempStorageC(), xc, zc);

        Random cRand = new Random((xc * 31 + zc) + seed + 1);

        for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
            for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
                int depth = 0;

                int bedrockHeight = Math.abs((((xc + x) ^ 0x5218CFAF * z) * ((zc + z) ^ 0x558802E3 * x))) % 5;

                for (int y = 127; y >= 0; y--) {
                    float density = densityMap[x][y][z];
                    boolean cave = caveMap[x][y][z] > (0.3 + 1 / (8.0 + depth / 2));

                    if (!cave && density > 0) {
                        if (depth == 0) {
                            if (y < 52) {
                                chunk.setBlock(x, y, z,
                                        IDRegister.sand.getID(), false);
                            } else {
                                chunk.setBlock(x, y, z,
                                        IDRegister.grass.getID(), false);

                                int r = cRand.nextInt(100);

                                if (r <= 20) {
                                    chunk.setBlock(x, y + 1, z,
                                            IDRegister.tallGrass.getID(), false);
                                } else {
                                    switch (r) {
                                        case 30:
                                            chunk.setBlock(x, y + 1, z,
                                                    IDRegister.flower1.getID(),
                                                    false);
                                            break;
                                        case 40:
                                            chunk.setBlock(x, y + 1, z,
                                                    IDRegister.flower2.getID(),
                                                    false);
                                            break;
                                        case 50:
                                            chunk.setBlock(x, y + 1, z,
                                                    IDRegister.flower3.getID(),
                                                    false);
                                            break;
                                        default:
                                    }
                                }
                            }
                        } else if (depth < 4) {
                            if (y < 52-depth) {
                                chunk.setBlock(x, y, z,
                                        IDRegister.sand.getID(), false);
                            } else {
                                chunk.setBlock(x, y, z,
                                        IDRegister.dirt.getID(), false);
                            }
                        } else {
                            chunk.setBlock(x, y, z, IDRegister.stone.getID(),
                                    false);
                        }

                        depth++;
                    } else {
                        if ((!cave || depth == 0) && y < 50) {
                            chunk.setBlock(x, y, z, IDRegister.water.getID(),
                                    false);
                        }

                        if (!cave) {
                            depth = 0;
                        }
                    }

                    if (y <= bedrockHeight) {
                        chunk.setBlock(x, y, z, IDRegister.bedrock.getID(),
                                false);
                    } else if (y <= GEN_LAVA_HEIGHT && cave) {
                        chunk.setBlock(x, y, z, IDRegister.lava.getID(), false);
                    }
                }
            }
        }

        return chunk;

    }

    public boolean isChunkCached(int x, int z) {
        return this.cachedChunks.get(new Coord2D(x, z)) != null;
    }

    public Chunk getChunk(GenTempStorage storage, int xc, int zc) {
        if (this.isChunkCached(xc, zc)) {
            Coord2D coord = new Coord2D(xc, zc);
            Chunk c = this.cachedChunks.get(coord);
            this.cachedChunks.remove(coord);
            return c;
        } else if (this.loader.isChunkSaved(xc, zc)) {
            System.out.println("Loading " + xc + ":" + zc);
            return this.loader.loadChunk(xc, zc);

        } else {
            return this.generateChunk(storage, xc, zc);
        }
    }

    private void stopAllLoaders() {
        for (ChunkLoaderThread loader : this.loaders) {
            loader.stop();
            loader.resume();
        }
    }

    public void cleanup() {
        stopAllLoaders();
    }
}
