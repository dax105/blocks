package dax.blocks.world;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;

import dax.blocks.Coord2D;
import dax.blocks.block.Block;
import dax.blocks.world.chunk.Chunk;
import dax.blocks.world.generator.SimplexNoise;

public class ChunkProvider {

	Map<Coord2D, Chunk> loadedChunks;
	
	SimplexNoise simplex2D;
	SimplexNoise simplex3D;
	
	World world;
	
	int seed;
	
	public boolean isChunkLoaded(Coord2D coord) {
		return loadedChunks.containsKey(coord);
	}
	
	public Chunk getChunk(Coord2D coord) {
		return loadedChunks.get(coord);
	}
	
	@SuppressWarnings("rawtypes")
	public void updateLoadedChunksInRadius(int x, int y, int r) {
		//(Math.sqrt(cdistX * cdistX + cdistZ * cdistZ) < r)
		for (int ix = x - r; ix <= x + r; ix++) {
			for (int iy = y - r; iy <= y + r; iy++) {
				Coord2D coord = new Coord2D(ix, iy);
				if (!loadedChunks.containsKey(coord)) {
					loadChunk(coord);
				}
			}
		}
		
		Iterator it = loadedChunks.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        
	        Chunk c = (Chunk) pairs.getValue();
	        Coord2D coord = (Coord2D) pairs.getKey();
	        
	        int xdist = Math.abs(x - c.x);
			int ydist = Math.abs(y - c.z);
			
			if ((xdist > r || ydist > r) && loadedChunks.containsKey(coord)) {
				saveChunk(c);
				c.deleteVBO();
				c = null;
				it.remove();
			}
	        
	    }
		
		/*for (Chunk c : loadedChunks.values()) {
			int xdist = Math.abs(x - c.x);
			int ydist = Math.abs(y - c.z);
			Coord2D coord = new Coord2D(c.x, c.z);
			if (xdist >= r || ydist >= r && loadedChunks.containsKey(coord)) {
				saveChunk(c);
				
				System.out.println("Unloaded chunk " + coord);
			}
		}*/
	}
	
	public void unloadChunk(Coord2D coord) {
		Chunk c = loadedChunks.get(coord);
		c.deleteVBO();
		saveChunk(c);
		loadedChunks.remove(coord);
	}
	
	public ChunkProvider(World world) {
		this(new Random().nextInt(), world);
	}
	
	public ArrayList<Chunk> getAllLoadedChunks() {
		return new ArrayList<Chunk>(loadedChunks.values());
	}
	
	public ArrayList<Chunk> getChunksInRadius(int x, int y, int r) {
		ArrayList<Chunk> chunks = new ArrayList<Chunk>();
		
		for (int ix = x - r; ix <= x + r; ix++) {
			for (int iy = y - r; iy <= y + r; iy++) {
				Coord2D coord = world.getCoord2D(ix, iy);
				chunks.add(getChunk(coord));
			}
		}
		
		return chunks;
		
		//return new ArrayList<Chunk>(loadedChunks.values());
	}

	public ChunkProvider(int seed, World world) {
		this.loadedChunks = new HashMap<Coord2D, Chunk>();		
		this.seed = seed;
		this.world = world;
		tryToLoadWorld();
		this.simplex2D = new SimplexNoise(256, 0.3, this.seed);
		this.simplex3D = new SimplexNoise(32, 0.1, this.seed);
	}
	
	public void loadChunk(Coord2D coord) {
		loadedChunks.put(coord, getChunk(coord.x, coord.y, true));
	}
	
	public void tryToLoadWorld() {		
		try {
			File dir = new File("saves");

			if (!dir.exists()) {
				dir.mkdir();
			}

			File file = new File(dir, "world" + ".txt");
			
			if (!file.exists()) {
				return;
			}
			
			Scanner s = new Scanner(file);

			while (s.hasNextLine()) {
				String l = s.nextLine();
				String[] words = l.split(" ");
				if (words.length >= 2) {
					if (words[0].equals("seed")) {
						this.seed = Integer.parseInt(words[1]);
					} else if (words[0].equals("mult")) {
						this.world.multipler = Float.parseFloat(words[1]);
					}
				}
			}
			
			s.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Chunk getChunk(int xc, int zc, boolean canLoad) {
		if (isChunkSaved(xc, zc) && canLoad) {
			return loadChunk(xc, zc);
		}
		Chunk chunk = new Chunk(xc, zc, world);

		for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
			for (int z = 0; z < Chunk.CHUNK_SIZE; z++) {
				int h = (int) Math.round(40 + simplex2D.getNoise(xc*16 + x, zc*16 + z) * world.multipler);
				for (int y = 0; y < Chunk.CHUNK_HEIGHT; y++) {
					//if ((y <= h) && !(simplex3D.getNoise(x + (xc * Chunk.CHUNK_SIZE), y, z + (zc * Chunk.CHUNK_SIZE)) < -0.2D * (y / 80.0D))) {
					
					if (y <= h) {
						if (y == h) {
							if (h > 40) {
								chunk.setBlock(x, y, z, Block.grass.getId(), false);
							} else {
								chunk.setBlock(x, y, z, Block.sand.getId(), false);
							}
						} else {
							if (y < h - 4) {
								chunk.setBlock(x, y, z, Block.stone.getId(), false);
							} else {
								if (h > 40) {
									chunk.setBlock(x, y, z, Block.dirt.getId(), false);
								} else {
									chunk.setBlock(x, y, z, Block.sand.getId(), false);
								}
							}

						}

					} else if (y <= 38){
						chunk.setBlock(x, y, z, Block.water.getId(), false);
					}
					
					if (y == 0) {
						chunk.setBlock(x, y, z, Block.bedrock.getId(), false);
					}
				}
			}
		}

		//saveChunk(chunk);
		return chunk;
	}
	
	public boolean isChunkSaved(int cx, int cz) {
		File dir = new File("saves");

		if (!dir.exists()) {
			dir.mkdir();
		}

		File file = new File(dir, "x" + cx + "z" + cz + ".txt");
		
		return file.exists();
	}
	
	public void saveAll() {
		Iterator<Entry<Coord2D, Chunk>> it = loadedChunks.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<Coord2D, Chunk> pairs = it.next();
			Chunk c = (Chunk) pairs.getValue();       
			saveChunk(c);       
	    }
	    
	    saveProviderInfo();
	}
	
	public void saveProviderInfo() {
		try {
			File dir = new File("saves");

			if (!dir.exists()) {
				dir.mkdir();
			}

			File file = new File(dir, "world" + ".txt");
			
			PrintWriter pw = new PrintWriter(file);
			
			pw.println("seed " + this.seed);
			pw.println("mult " + this.world.multipler);
			
			pw.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Chunk loadChunk(int cx, int cz) {
		File dir = new File("saves");
		File file = new File(dir, "x" + cx + "z" + cz + ".txt");
	    byte[] fileData = new byte[(int) file.length()];
	    try {
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			dis.readFully(fileData);
			dis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    Chunk c = new Chunk(cx, cz, world);
	    c.blocks.put(fileData);
	    return c;
	}
	
	public void saveChunk(Chunk c) {
		try {
			// FileOutputStream stream = new FileOutputStream("x" + this.x + "z" + this.z + ".txt");
			File dir = new File("saves");

			if (!dir.exists()) {
				dir.mkdir();
			}

			File file = new File(dir, "x" + c.x + "z" + c.z + ".txt");
			FileOutputStream stream = new FileOutputStream(file);
			try {
				stream.write(c.blocks.array());
			} finally {
				stream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
