package dax.blocks.world;

import dax.blocks.collisions.AABB;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import dax.blocks.Coord2D;
import dax.blocks.Game;
import dax.blocks.Particle;
import dax.blocks.Player;
import dax.blocks.block.Block;
import dax.blocks.render.ChunkMesh;
import dax.blocks.render.Frustum;
import dax.blocks.world.chunk.Chunk;
import dax.blocks.world.generator.TreeGenerator;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class World {

	private Coord2D c2d;
	
	public int size;
	public int sizeBlocks;
	public Player player;
	public ChunkProvider chunkProvider;
	
    float[] rightMod = new float[3];
    float[] upMod = new float[3];
	
	Random rand = new Random();

	public static final int DRAW_DISTANCE = 12;
	
	float multipler;
	private int vertices;

	TreeGenerator treeGen;

	Frustum frustum;

	public int chunksDrawn;

	boolean trees;
	
	float emitterX = 0;
	float emitterY = 40;
	float emitterZ = 0;

	public int getVertices() {
		return this.vertices;
	}
	
	public World(int size, float multipler, boolean trees, Game game, boolean load) {
		this.size = size;
		this.sizeBlocks = size * Chunk.CHUNK_SIZE;
		player = new Player(this);
		this.treeGen = new TreeGenerator(this);

		this.multipler = multipler;
		
		chunkProvider = new ChunkProvider(this);

		this.frustum = new Frustum();
		this.c2d = new Coord2D(-1, -1);

		//long start = System.nanoTime();
		/*for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				chunks[x][z] = chunkProvider.getChunk(x, z, load);
				if (System.nanoTime() - lastTime > 100000000 && game.guiScreen instanceof GuiScreenLoading) {
					GuiScreenLoading scr = (GuiScreenLoading) game.guiScreen;
					scr.update("Generating chunk " + (x*size+z) + "/" + (size*size));
					lastTime = System.nanoTime();
				}
			}
		}

		if (trees) {
			Random rand = new Random();
			int maxTrees = size * size - (rand.nextInt((size * size) - 5));
			for (int i = 0; i < maxTrees; i++) {
				int sy = 0;
				int x = 1 + rand.nextInt(sizeBlocks - 1);
				int z = 1 + rand.nextInt(sizeBlocks - 1);
				for (int y = 0; y < Chunk.CHUNK_HEIGHT; y++) {
					sy = y;
					if (getBlock(x, sy + 1, z) == 0) {
						break;
					}
				}
				if (getBlock(x,sy,z) == Block.grass.getId()) {
					treeGen.generateTree(x, sy, z);
				}	
			}
		}*/

		//System.out.println("Chunks created in " + (System.nanoTime() - start) / 1000000 + "ms");

		/*start = System.nanoTime();
		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				chunks[x][z].rebuildEntireChunk();
				if (System.nanoTime() - lastTime > 100000000 && game.guiScreen instanceof GuiScreenLoading) {
					GuiScreenLoading scr = (GuiScreenLoading) game.guiScreen;
					scr.update("Building chunk " + (x*size+z) + "/" + (size*size));
					lastTime = System.nanoTime();
				}
			}
		}*/

		//System.out.println("World geometry built in " + (System.nanoTime() - start) / 1000000 + "ms");
		
		game.isIngame = true;
	}
	
	public Coord2D getCoord2D(int x, int y) {
		this.c2d.set(x, y);
		return this.c2d;
	}

	public List<Particle> particles = new ArrayList<Particle>();
	
	public void update() {
		player.update();
		
		for(int i = 0; i < 20; i++) {

			
			float velocity = 0.15f + rand.nextFloat()*0.15f;
			float heading = 180 - rand.nextFloat()*360f;
			float tilt = 180 - rand.nextFloat()*360f;
			
			float velY = (float) (Math.cos(tilt)*velocity);
			float mult = (float) (Math.sin(tilt));
			
			float velX = (float) (Math.cos(heading)*velocity*mult);
			float velZ = (float) (Math.sin(heading)*velocity*mult);
			
			
			Particle p = new Particle(emitterX, emitterY, emitterZ, velX, velY, velZ, 200, 1, 0.08f, 0.5f);
			particles.add(p);
		}	
		
		for (Iterator<Particle> iter = particles.iterator(); iter.hasNext(); ) {
		    Particle pt = iter.next();
		    pt.update(getBBs(pt.aabb));
		    if (pt.dead) {
				iter.remove();
			}
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			emitterX = player.posX;
			emitterY = player.posY;
			emitterZ = player.posZ;
		}
		
		chunkProvider.updateLoadedChunksInRadius(((int)player.posX) / 16, ((int)player.posZ) / 16, DRAW_DISTANCE+1);
	}

	public void rebuild(int x, int y, int z) {
			Coord2D coord = getCoord2D(x, z);
			
			if (chunkProvider.isChunkLoaded(coord)) {
				chunkProvider.getChunk(coord).setDirty(y);
			}
	}


	public byte getBlock(int x, int y, int z) {
		int icx = x & 15;
		int icz = z & 15;

		int cx = x >> 4;
		int cz = z >> 4;
		
		Coord2D coord = getCoord2D(cx, cz);
		
		Chunk c = chunkProvider.getChunk(coord);
		return c != null ? c.getBlock(icx, y, icz) : 0;
	}

	public void setBlock(int x, int y, int z, byte id) {
		int icx = x & 15;
		int icz = z & 15;

		int cx = x >> 4;
		int cz = z >> 4;
		
		Coord2D coord = getCoord2D(cx, cz);
		
		if (chunkProvider.isChunkLoaded(coord)) {
			chunkProvider.getChunk(coord).setBlock(icx, y, icz, id, true);
		}
	}

	public void setBlockNoRebuild(int x, int y, int z, byte id) {
		int icx = x & 15;
		int icz = z & 15;

		int cx = x >> 4;
		int cz = z >> 4;
		
		Coord2D coord = getCoord2D(cx, cz);
		
		if (chunkProvider.isChunkLoaded(coord)) {
			chunkProvider.getChunk(coord).setBlock(icx, y, icz, id, false);
		}
	}
	
	public ArrayList<AABB> getBBs(AABB aABB) {
		ArrayList<AABB> aABBs = new ArrayList<AABB>();
		int x0 = (int) (aABB.x0 - 1.1F);
		int x1 = (int) (aABB.x1 + 1.1F);
		int y0 = (int) (aABB.y0 - 1.1F);
		int y1 = (int) (aABB.y1 + 1.1F);
		int z0 = (int) (aABB.z0 - 1.1F);
		int z1 = (int) (aABB.z1 + 1.1F);

		for (int x = x0; x < x1; ++x) {
			for (int y = y0; y < y1; ++y) {
				for (int z = z0; z < z1; ++z) {
					if (getBlock(x, y, z) > 0 && getBlock(x, y, z) != Block.water.getId()) {
						aABBs.add(new AABB((float) x, (float) y, (float) z, (float) x + 1, (float) y + 1, (float) z + 1));
					}
				}
			}
		}

		return aABBs;
	}

	public void saveAllChunks() {
		chunkProvider.saveAll();
	}

	public void onRender() {
		player.onRender();
	}

}
