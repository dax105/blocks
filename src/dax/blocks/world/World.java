package dax.blocks.world;

import dax.blocks.collisions.AABB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import dax.blocks.Coord2D;
import dax.blocks.Game;
import dax.blocks.Particle;
import dax.blocks.block.Block;
import dax.blocks.block.BlockPlant;
import dax.blocks.movable.entity.PlayerEntity;
import dax.blocks.render.Frustum;
import dax.blocks.render.ITickListener;
import dax.blocks.world.chunk.Chunk;
import dax.blocks.world.chunk.ChunkProvider;
import dax.blocks.world.generator.TreeGenerator;

import org.lwjgl.input.Keyboard;

public class World implements ITickListener {

	public static final float GRAVITY = 0.06f;
	public static final float WATER_GRAVITY = 0.04f;
	public static final int MAX_PARTICLES = 10000;
	
	private List<ScheduledUpdate> scheduledUpdates;
	private List<ScheduledUpdate> newlyScheduledUpdates;
	
	private Coord2D c2d;
	
	public int size;
	public int sizeBlocks;
	public PlayerEntity player;
	public ChunkProvider chunkProvider;
	public String name;
	
    float[] rightMod = new float[3];
    float[] upMod = new float[3];
	
	Random rand = new Random();
	private int vertices;

	public TreeGenerator treeGen;

	Frustum frustum;

	public int chunksDrawn;

	boolean trees;
	
	float emitterX = 0;
	float emitterY = 40;
	float emitterZ = 0;

	public int getVertices() {
		return this.vertices;
	}
	
	public World(boolean trees, Game game, boolean load, String worldName) {
		this.name = worldName;
		player = new PlayerEntity(this, 0, 128, 0);
		this.treeGen = new TreeGenerator(this);
		
		chunkProvider = new ChunkProvider(this, load);

		this.frustum = new Frustum();
		this.c2d = new Coord2D(-1, -1);
		
		this.scheduledUpdates = new LinkedList<ScheduledUpdate>();
		this.newlyScheduledUpdates = new LinkedList<ScheduledUpdate>();
		
		chunkProvider.updateLoadedChunksInRadius((int)player.getPosX(), (int)player.getPosZ(), Game.settings.drawDistance.getValue());
	}
	
	public Coord2D getCoord2D(int x, int y) {
		this.c2d.set(x, y);
		return this.c2d;
	}

	public List<Particle> particles = new LinkedList<Particle>();
	
	public void spawnParticleWithRandomDirectionFast(float x, float y, float z, float vel, float velFuzziness) {
		
		float velhalf = vel*0.5f;
		
		float velX = velhalf - rand.nextFloat()*vel-rand.nextFloat()*velFuzziness;
		float velY = velhalf - rand.nextFloat()*vel-rand.nextFloat()*velFuzziness;
		float velZ = velhalf - rand.nextFloat()*vel-rand.nextFloat()*velFuzziness;
		
		Particle p = new Particle(x, y, z, velX, velY, velZ, 100, rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
		particles.add(p);
		
	}
	
	public void spawnParticle(float x, float y, float z) {
			float velocity = 2.0f + rand.nextFloat()*0.15f;
			float heading = 180 - rand.nextFloat()*360f;
			float tilt = 180 - rand.nextFloat()*360f;
			
			float velY = (float) (Math.cos(tilt)*velocity);
			float mult = (float) (Math.sin(tilt));
			
			float velX = (float) (Math.cos(heading)*velocity*mult);
			float velZ = (float) (Math.sin(heading)*velocity*mult);
			
			
			Particle p = new Particle(x, y, z, velX, velY, velZ, 50+rand.nextInt(20), rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
			particles.add(p);
		
	}
	
	int removedParticles = 0;
	
	
	public boolean isOccluder(int x, int y, int z) {
		int id = getBlock(x, y, z);
		return id > 0 ? Block.getBlock((byte) id).isOccluder() : false; 
	}
	
	public void updateNeighbours(int x, int y, int z) {
		scheduleUpdate(x+1, y, z,1);
		scheduleUpdate(x-1, y, z,1);
		scheduleUpdate(x, y+1, z,1);
		scheduleUpdate(x, y-1, z,1);
		scheduleUpdate(x, y, z+1,1);
		scheduleUpdate(x, y, z-1,1);
	}
	
	public void updateBlock(int x, int y, int z) {
		int id = getBlock(x, y, z);
		if (id > 0) {
			Block.getBlock(id).update(x, y, z, this);
		}
	}
	
	public void scheduleUpdate(int x, int y, int z, int ticks) {
		newlyScheduledUpdates.add(new ScheduledUpdate(x, y, z, ticks));
	}
	
	public void onTick() {
		player.onTick();
		
		int size = particles.size();
		
		for (Iterator<Particle> iter = particles.iterator(); iter.hasNext(); ) {
		    Particle pt = iter.next();
		    pt.update(getBBs(pt.aabb.expand(pt.velX, pt.velY, pt.velZ)));
		    if (pt.dead || size > MAX_PARTICLES) {
				iter.remove();
				size--;
			}		    
		}
		
		for(Iterator<ScheduledUpdate> it = newlyScheduledUpdates.iterator(); it.hasNext();) {
			scheduledUpdates.add(it.next());
			it.remove();
		}
		
		Iterator<ScheduledUpdate> updateIterator = scheduledUpdates.iterator();
		while(updateIterator.hasNext()) {
			ScheduledUpdate s = updateIterator.next();
			if (s.ticks > 0) {
				s.ticks--;
			} else {
				updateBlock(s.x, s.y, s.z);
				updateIterator.remove();
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			emitterX = player.getPosX();
			emitterY = player.getPosY();
			emitterZ = player.getPosZ();
		}
		
		chunkProvider.updateLoadedChunksInRadius(((int)Math.floor(player.getPosX())) >> 4, ((int)Math.floor(player.getPosZ())) >> 4, Game.settings.drawDistance.getValue()+1);
	}
	
	public void menuUpdate() {
		chunkProvider.updateLoadedChunksInRadius(((int)Math.floor(player.getPosX())) >> 4, ((int)Math.floor(player.getPosZ())) >> 4, Game.settings.drawDistance.getValue()+1);
	}

	public void setChunkDirty(int x, int y, int z) {
			Coord2D coord = getCoord2D(x, z);
			
			if (chunkProvider.isChunkLoaded(coord)) {
				chunkProvider.getChunk(coord).setDirty(y);
			}
	}


	public int getBlock(int x, int y, int z) {
		int icx = x & 15;
		int icz = z & 15;

		int cx = x >> 4;
		int cz = z >> 4;
		
		Coord2D coord = getCoord2D(cx, cz);
		
		Chunk c = chunkProvider.getChunk(coord);
		return c != null ? c.getBlock(icx, y, icz) : 0;
	}

	public void setBlock(int x, int y, int z, int id, boolean artificial) {
		int icx = x & 15;
		int icz = z & 15;

		int cx = x >> 4;
		int cz = z >> 4;
		
		Coord2D coord = getCoord2D(cx, cz);
		
		if (chunkProvider.isChunkLoaded(coord)) {
			Chunk c = chunkProvider.getChunk(coord);
			c.setBlock(icx, y, icz, id, true);
			c.changed = artificial;
			if (artificial) {
				scheduleUpdate(x, y, z, 1);
				updateNeighbours(x, y, z);
			}
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
		int x0 = (int) (aABB.x0 - 1.0F);
		int x1 = (int) (aABB.x1 + 1.0F);
		int y0 = (int) (aABB.y0 - 1.0F);
		int y1 = (int) (aABB.y1 + 1.0F);
		int z0 = (int) (aABB.z0 - 1.0F);
		int z1 = (int) (aABB.z1 + 1.0F);

		for (int x = x0; x < x1; ++x) {
			for (int y = y0; y < y1; ++y) {
				for (int z = z0; z < z1; ++z) {
					if (getBlock(x, y, z) > 0 && getBlock(x, y, z) != Block.water.getId() && !(Block.getBlock(getBlock(x, y, z)) instanceof BlockPlant) ) {
						aABBs.add(new AABB((float) x, (float) y, (float) z, (float) x + 1, (float) y + 1, (float) z + 1));
					}
				}
			}
		}

		return aABBs;
	}

	public void setAllChunksDirty() {
		for (Chunk c : chunkProvider.getAllLoadedChunks()) {
			c.setAllDirty();
		}
	}
	
	public void deleteAllDisplayLists() {
		for (Chunk c : chunkProvider.getAllLoadedChunks()) {
			c.deleteAllRenderChunks();
		}
	}
	
	public void saveAllChunks() {
		chunkProvider.loader.saveAll();
	}

	public void onRenderTick(float ptt) {
		player.onRenderTick(ptt);
	}

}
