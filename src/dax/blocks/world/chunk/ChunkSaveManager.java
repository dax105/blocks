package dax.blocks.world.chunk;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.xerial.snappy.Snappy;

import dax.blocks.Game;
import dax.blocks.console.Console;
import dax.blocks.util.Coord2D;
import dax.blocks.world.World;
import dax.blocks.world.WorldInfo;
import dax.blocks.world.WorldsManager;

public class ChunkSaveManager {

	private World world;
	private ChunkProvider provider;
	private String name;
	
	public static final int WORLD_VERSION = 1;
	
	public void tryToLoadWorld() {
			File dir = new File(WorldsManager.SAVES_DIR, this.name);
			
			if(!dir.exists()) {
				dir.mkdir();
			}	

			this.world.createDataManagers(new File(dir, "bdf"), new File(dir, "idf"));
			
			File file = new File(dir, "world" + ".txt");

			if(!file.exists()) {
				Console.println("World save not found!");
				return;
			}

			WorldInfo i = Game.getInstance().getWorldsManager().getWorld(name);
			this.world.getPlayer().setPosition(i.getPlayerX(), i.getPlayerY(), i.getPlayerZ());
			this.world.getPlayer().setTilt(i.getPlayerTilt());
			this.world.getPlayer().setHeading(i.getPlayerHeading());
			this.provider.seed = i.getWorldSeed();
			
			Console.println("World info sucessfully loaded!");
	}
	
	public ChunkSaveManager(ChunkProvider provider, String saveName) {
		this.provider = provider;
		this.world = provider.world;
		this.name = saveName;
	}
	
	public boolean isChunkSaved(int cx, int cz) {
		File dir = new File(WorldsManager.SAVES_DIR, this.name);

		if(!dir.exists()) {
			dir.mkdir();
		}

		File file = new File(dir, "x" + cx + "z" + cz + ".ccf");

		return file.exists();
	}

	public void saveAll() {
		Game.getInstance().displayLoadingScreen("Saving...");
		Iterator<Entry<Coord2D, Chunk>> it = provider.loadedChunks.entrySet().iterator();
		while(it.hasNext()) {
			Entry<Coord2D, Chunk> pairs = it.next();
			Chunk c = (Chunk) pairs.getValue();

			c.deleteAllRenderChunks();

			this.saveChunk(c);
		}

		WorldInfo i = Game.getInstance().getWorldsManager().getWorld(this.name);
		if(i == null) i = new WorldInfo(this.name);
		
		i.setPlayerX(this.world.getPlayer().getPosX());
		i.setPlayerY(this.world.getPlayer().getPosY());
		i.setPlayerZ(this.world.getPlayer().getPosZ());
		i.setPlayerTilt(this.world.getPlayer().getTilt());
		i.setPlayerHeading(this.world.getPlayer().getHeading());
		i.setWorldSeed(this.provider.seed);
		i.setWorldVersion("" + ChunkSaveManager.WORLD_VERSION);
		
		i.saveWorldInfo();
		
		try {
			this.world.getBlockDataManager().save();
		} catch (IOException e) {
			Logger.getGlobal().warning("Can't save data file!");
		}
		Game.getInstance().closeGuiScreen();
	}

	

	public Chunk loadChunk(int cx, int cz) {
		File dir = new File(WorldsManager.SAVES_DIR, this.name);
		File file = new File(dir, "x" + cx + "z" + cz + ".ccf");
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

		Chunk c = new Chunk(cx, cz, this.world);
		try {
			c.blocksBuffer.put(Snappy.uncompressShortArray(fileData));
		} catch (IOException e) {
			e.printStackTrace();
		}

		c.changed = true;

		return c;
	}

	public void saveChunk(Chunk c) {
		if(!c.changed) {
			return;
		}

		try {
			File dir = new File(WorldsManager.SAVES_DIR, this.name);

			if(!dir.exists()) {
				dir.mkdir();
			}

			File file = new File(dir, "x" + c.x + "z" + c.z + ".ccf");
			FileOutputStream stream = new FileOutputStream(file);
			try {
				stream.write(Snappy.compress(c.blocksBuffer.array()));
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
