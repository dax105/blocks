package dax.blocks.world;

import dax.blocks.collisions.AABB;
import dax.blocks.data.DataManager;
import dax.blocks.data.DataValue;
import dax.blocks.data.IBlockDataManager;
import dax.blocks.data.IItemDataManager;
import dax.blocks.gui.ingame.GuiManager;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.Random;

import dax.blocks.Game;
import dax.blocks.Particle;
import dax.blocks.block.Block;
import dax.blocks.movable.entity.PlayerEntity;
import dax.blocks.render.ITickListener;
import dax.blocks.render.RenderEngine;
import dax.blocks.settings.Settings;
import dax.blocks.util.Coord2D;
import dax.blocks.util.Coord3D;
import dax.blocks.world.chunk.Chunk;
import dax.blocks.world.chunk.ChunkProvider;

public class World implements ITickListener {

	public static final float GRAVITY = 0.06f;
	public static final int MAX_PARTICLES = 10000;

	private List<ScheduledUpdate> scheduledUpdates;
	private List<ScheduledUpdate> newlyScheduledUpdates;
	
	private List<ITickListener> tickListeners;
	private List<ITickListener> scheduledTickListenersRemoval;
	private List<ITickListener> scheduledTickListenersAdding;
	
	private Coord2D c2d;
	private PlayerEntity player;
	private ChunkProvider chunkProvider;
	private IBlockDataManager blockDataManager;
	private IItemDataManager itemDataManager;
	private IDRegister idRegister;
	private RenderEngine renderEngine;
	
	public int size;
	public int sizeBlocks;

	public String name;

	float[] rightMod = new float[3];
	float[] upMod = new float[3];

	Random rand = new Random();
	private int vertices;

	public int chunksDrawn;

	boolean trees;

	public int getVertices() {
		return this.vertices;
	}

	public World(boolean trees, Game game, boolean load, String worldName, RenderEngine e) {
		this.name = worldName;
		this.renderEngine = e;
		
		this.idRegister = new IDRegister(this);
		this.idRegister.registerDefaultBlocks();
		this.idRegister.registerDefaultItems();

		this.player = new PlayerEntity(this, 0, 128, 0);
		game.getOverlayManager().addOverlay(this.player);

		this.chunkProvider = new ChunkProvider(this, load);

		this.c2d = new Coord2D(-1, -1);
		
		this.scheduledUpdates = new LinkedList<ScheduledUpdate>();
		this.newlyScheduledUpdates = new LinkedList<ScheduledUpdate>();
		this.tickListeners = new LinkedList<ITickListener>();
		this.scheduledTickListenersAdding = new LinkedList<ITickListener>();
		this.scheduledTickListenersRemoval = new LinkedList<ITickListener>();

		chunkProvider.updateLoadedChunksInRadius((int) player.getPosX(),
				(int) player.getPosZ(), Settings.getInstance().drawDistance.getValue());
	}
	
	public RenderEngine getRenderEngine() {
		return this.renderEngine;
	}

	public IDRegister getRegister() {
		return this.idRegister;
	}
	
	public Block getBlockObject(int x, int y, int z) {
		return this.idRegister.getBlock(this.getBlock(x, y, z));
	}
	
	public Block getBlockObject(int id) {
		return this.idRegister.getBlock(id);
	}
	
	public Coord2D getCoord2D(int x, int y) {
		this.c2d.set(x, y);
		return this.c2d;
	}
	

	public PlayerEntity getPlayer() {
		return this.player;
	}
	
	public ChunkProvider getChunkProvider() {
		return this.chunkProvider;
	}
	
	public IBlockDataManager getBlockDataManager() {
		return this.blockDataManager;
	}
	
	public IItemDataManager getItemDataManager() {
		return this.itemDataManager;
	}
	
	public void createDataManagers(File blockDataFile, File itemDataFile) {
		try {
			DataManager n = new DataManager(blockDataFile, itemDataFile);
			this.itemDataManager = n;
			this.blockDataManager = n;
		} catch (IOException e) {
			Logger.getGlobal().warning("Can't create data file!");
		}
	}

	
	public void spawnParticleWithRandomDirectionFast(float x, float y, float z,
			float vel, float velFuzziness) {

		float velhalf = vel * 0.5f;

		float velX = velhalf - rand.nextFloat() * vel - rand.nextFloat()
				* velFuzziness;
		float velY = velhalf - rand.nextFloat() * vel - rand.nextFloat()
				* velFuzziness;
		float velZ = velhalf - rand.nextFloat() * vel - rand.nextFloat()
				* velFuzziness;

		Particle p = new Particle(this, x, y, z, velX, velY, velZ, 100, rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
		this.renderEngine.registerNewRenderable(p);
		this.registerNewTickListener(p);
	}

	public void spawnParticle(float x, float y, float z) {
		float velocity = 2.0f + rand.nextFloat() * 0.15f;
		float heading = 180 - rand.nextFloat() * 360f;
		float tilt = 180 - rand.nextFloat() * 360f;

		float velY = (float) (Math.cos(tilt) * velocity);
		float mult = (float) (Math.sin(tilt));

		float velX = (float) (Math.cos(heading) * velocity * mult);
		float velZ = (float) (Math.sin(heading) * velocity * mult);

		Particle p = new Particle(this, x, y, z, velX, velY, velZ,
				50 + rand.nextInt(20), rand.nextFloat(), rand.nextFloat(),
				rand.nextFloat());
		this.renderEngine.registerNewRenderable(p);
		this.registerNewTickListener(p);
	}

	
	public void registerNewTickListener(ITickListener l) {
		if(!this.tickListeners.contains(l)) {
			this.scheduledTickListenersAdding.add(l);
		}
	}
	
	public void removeTickListener(ITickListener l) {
		if(this.tickListeners.contains(l) && !this.scheduledTickListenersRemoval.contains(l)) {
			this.scheduledTickListenersRemoval.add(l);
		}
	}

	public boolean isOccluder(int x, int y, int z) {
		int id = getBlock(x, y, z);
		return id > 0 ? this.getBlockObject(id).isOccluder() : false;
	}

	public void updateNeighbours(int x, int y, int z) {
		scheduleUpdate(x + 1, y, z, 1);
		scheduleUpdate(x - 1, y, z, 1);
		scheduleUpdate(x, y + 1, z, 1);
		scheduleUpdate(x, y - 1, z, 1);
		scheduleUpdate(x, y, z + 1, 1);
		scheduleUpdate(x, y, z - 1, 1);
	}

	public void updateBlock(int x, int y, int z) {
		int id = getBlock(x, y, z);
		if (id > 0) {
			this.getBlockObject(id).onTick(x, y, z, this);
		}

	}

	public void scheduleUpdate(int x, int y, int z, int ticks) {
		newlyScheduledUpdates.add(new ScheduledUpdate(x, y, z, ticks));
	}

	public void menuUpdate() {
		chunkProvider.updateLoadedChunksInRadius(
				((int) Math.floor(player.getPosX())) >> 4,
				((int) Math.floor(player.getPosZ())) >> 4,
				Settings.getInstance().drawDistance.getValue() + 1);
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

	public void setBlock(int x, int y, int z, int id, boolean artificial, boolean notify) {
		int icx = x & 15;
		int icz = z & 15;

		int cx = x >> 4;
		int cz = z >> 4;

		Coord2D coord = getCoord2D(cx, cz);

		if (chunkProvider.isChunkLoaded(coord)) {
			Chunk c = chunkProvider.getChunk(coord);
			
			if (notify) {
			Block before = this.getBlockObject(x, y, z);
			
			if (before != null) { 
				before.onRemoved(x, y, z, this);
			}
			}
			
			c.setBlock(icx, y, icz, id, true);
			c.changed = artificial;
			if (artificial) {
				scheduleUpdate(x, y, z, 1);
				updateNeighbours(x, y, z);
			}
		}
		
		if (notify) {
			if(id != 0) {
				this.getBlockObject(id).onPlaced(x, y, z, this);
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

	public float[] clipMovement(AABB bb, float xm, float ym, float zm) {

		float _x0 = bb.x0;
		float _y0 = bb.y0;
		float _z0 = bb.z0;
		float _x1 = bb.x1;
		float _y1 = bb.y1;
		float _z1 = bb.z1;

		if (xm < 0.0F) {
			_x0 += xm;
		}

		if (xm > 0.0F) {
			_x1 += xm;
		}

		if (ym < 0.0F) {
			_y0 += ym;
		}

		if (ym > 0.0F) {
			_y1 += ym;
		}

		if (zm < 0.0F) {
			_z0 += zm;
		}

		if (zm > 0.0F) {
			_z1 += zm;
		}

		int x0 = (int) (_x0 - 1.0F);
		int x1 = (int) (_x1 + 1.0F);
		int y0 = (int) (_y0 - 1.0F);
		int y1 = (int) (_y1 + 1.0F);
		int z0 = (int) (_z0 - 1.0F);
		int z1 = (int) (_z1 + 1.0F);

		for (int x = x0; x < x1; ++x) {
			for (int y = y0; y < y1; ++y) {
				for (int z = z0; z < z1; ++z) {
					int blockId = getBlock(x, y, z);
					if (blockId > 0) {
						Block block = this.getBlockObject(blockId);
						if (block.isCollidable()) {
							AABB blockBB = block.getOffsetAABB(x, y, z);
							xm = blockBB.clipXCollide(bb, xm);
						}
					}
				}
			}
		}
		bb.move(xm, 0.0F, 0.0F);

		for (int x = x0; x < x1; ++x) {
			for (int y = y0; y < y1; ++y) {
				for (int z = z0; z < z1; ++z) {
					int blockId = getBlock(x, y, z);
					if (blockId > 0) {
						Block block = this.getBlockObject(blockId);
						if (block.isCollidable()) {
							AABB blockBB = block.getOffsetAABB(x, y, z);
							ym = blockBB.clipYCollide(bb, ym);
						}
					}
				}
			}
		}
		bb.move(0.0F, ym, 0.0F);

		for (int x = x0; x < x1; ++x) {
			for (int y = y0; y < y1; ++y) {
				for (int z = z0; z < z1; ++z) {
					int blockId = getBlock(x, y, z);
					if (blockId > 0) {
						Block block = this.getBlockObject(blockId);
						if (block.isCollidable()) {
							AABB blockBB = block.getOffsetAABB(x, y, z);
							zm = blockBB.clipZCollide(bb, zm);
						}
					}
				}
			}
		}
		bb.move(0.0F, 0.0F, zm);

		return new float[] { xm, ym, zm };
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
		
		try {
			this.idRegister.saveIDs(IDRegister.dataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//.... DATA ....
	public void setData(int x, int y, int z, int key, String value) {
		Map<Integer, DataValue> coordData = blockDataManager.getValuesForCoord(
				x, y, z);
		if (coordData.get(key) != null)
			coordData.get(key).setData(value);
		else
			coordData.put(key, new DataValue(value));
	}

	public String getDataString(int x, int y, int z, int key) {
		if (containsData(x, y, z, key))
			return blockDataManager.getValuesForCoord(x, y, z).get(key)
					.getDataString();

		return null;
	}

	public int getDataInt(int x, int y, int z, int key) {
		if (containsData(x, y, z, key))
			return blockDataManager.getValuesForCoord(x, y, z).get(key)
					.getDataInt();

		return 0;
	}

	public float getDataFloat(int x, int y, int z, int key) {
		if (containsData(x, y, z, key))
			return blockDataManager.getValuesForCoord(x, y, z).get(key)
					.getDataFloat();

		return 0;
	}

	public boolean getDataBoolean(int x, int y, int z, int key) {
		if (containsData(x, y, z, key))
			return blockDataManager.getValuesForCoord(x, y, z).get(key)
					.getDataBoolean();

		return false;
	}

	public boolean containsData(int x, int y, int z, int key) {
		if(blockDataManager == null)
			return false;
		
		if(!blockDataManager.containsData(x, y, z))
			return false;
		
		return (blockDataManager.getValuesForCoord(x, y, z).get(key) != null);
	}
	
	public void removeData(int x, int y, int z) {
		blockDataManager.getValuesForCoord(x, y, z).clear();
	}

	//..Item Data..
	public void setData(int identificator, int key, String value) {
		Map<Integer, DataValue> coordData = itemDataManager.getValuesForIdentificator(identificator);
		if (coordData.get(key) != null)
			coordData.get(key).setData(value);
		else
			coordData.put(key, new DataValue(value));
	}

	public String getDataString(int identificator, int key) {
		if (containsData(identificator, key)) {
			return this.itemDataManager
					.getValuesForIdentificator(identificator).get(key)
					.getDataString();
		}
		
		return null;
	}
	
	public float getDataFloat(int identificator, int key) {
		if (containsData(identificator, key)) {
			return this.itemDataManager
					.getValuesForIdentificator(identificator).get(key)
					.getDataFloat();
		}

		return 0;
	}

	public boolean getDataBoolean(int identificator, int key) {
		if (containsData(identificator, key)) {
			return this.itemDataManager
					.getValuesForIdentificator(identificator).get(key)
					.getDataBoolean();
		}

		return false;
	}

	public int getDataInt(int identificator, int key) {
		if (containsData(identificator, key)) {
			return this.itemDataManager
					.getValuesForIdentificator(identificator).get(key)
					.getDataInt();
		}

		return 0;
	}
	
	public boolean containsData(int identificator, int key) {
		if(this.itemDataManager == null) {
			return false;
		}
		
		if(!this.itemDataManager.containsData(identificator)) {
			return false;
		}
		
		return (this.itemDataManager.getValuesForIdentificator(identificator).get(key) != null);
	}
	
	public void removeData(int identificator) {
		if(this.itemDataManager.containsData(identificator))
			this.itemDataManager.getValuesForIdentificator(identificator).clear();
	}
	
	//....  OVERRIDE METHODS ....
	@Override
	public void onTick() {	
		GuiManager.getInstance().onTick();
		this.player.onTick();
		
		for(Entry<Coord3D, Block> b : Block.tickingBlocks.entrySet()) {
			if(b.getValue().isRequiringTick())
				b.getValue().onTick(b.getKey().x, b.getKey().y, b.getKey().z, this);
		}

		
		for (Iterator<ScheduledUpdate> it = newlyScheduledUpdates.iterator(); it
				.hasNext();) {
			scheduledUpdates.add(it.next());
			it.remove();
		}

		Iterator<ScheduledUpdate> updateIterator = scheduledUpdates.iterator();
		while (updateIterator.hasNext()) {
			ScheduledUpdate s = updateIterator.next();
			if (s.ticks > 0) {
				s.ticks--;
			} else {
				updateBlock(s.x, s.y, s.z);
				updateIterator.remove();
			}
		}
		
		

		chunkProvider.updateLoadedChunksInRadius(
				((int) Math.floor(player.getPosX())) >> 4,
				((int) Math.floor(player.getPosZ())) >> 4,
				Settings.getInstance().drawDistance.getValue() + 1);

	}

	@Override
	public void onRenderTick(float partialTickTime) {
		this.player.onRenderTick(partialTickTime);
		
		
		for(Entry<Coord3D, Block> b : Block.tickingBlocks.entrySet()) {
			if(b.getValue().isRequiringRenderTick())
				b.getValue().onRenderTick(partialTickTime, b.getKey().x, b.getKey().y, b.getKey().z, this);	
		}
		
		GuiManager.getInstance().onRenderTick(partialTickTime);
	}

}
