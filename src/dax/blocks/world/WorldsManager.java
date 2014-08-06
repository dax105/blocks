package dax.blocks.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WorldsManager {

	private static WorldsManager instance;

	public static WorldsManager getInstance() {
		if(WorldsManager.instance == null) {
			WorldsManager.instance = new WorldsManager();
		}
		
		return WorldsManager.instance;
	}
	
	public static final String SAVES_DIR = "saves";
	private File savesDir;

	private WorldsManager() {
	}
	
	public void load() {
		savesDir = new File(SAVES_DIR);
		if(!savesDir.exists()) {
			savesDir.mkdir();
		}
	}

	public List<File> getWorldsDirs() {
		ArrayList<File> worlds = new ArrayList<File>();

		String[] names = this.savesDir.list();
			for(String name : names) {
				File d = new File(WorldsManager.SAVES_DIR, name);
				if(d.isDirectory()) {
					worlds.add(d);
				}
			}
		

		return worlds;
	}

	public List<WorldInfo> getWorldsInfo() {
		ArrayList<WorldInfo> worlds = new ArrayList<WorldInfo>();

		List<File> dirs = this.getWorldsDirs();
		for(File d : dirs) {
			try {
				worlds.add(WorldInfo
						.constructFromFile(new File(d, "world.txt")));
			} catch (FileNotFoundException e) {
				Logger.getGlobal().info("World definition file doesn't exist");
			}
		}

		return worlds;
	}

	public WorldInfo getWorld(String name) {
		List<WorldInfo> worlds = this.getWorldsInfo();
		for(WorldInfo i : worlds) {
			if(i.getWorldName().equalsIgnoreCase(name)) {
				return i;
			}
		}

		return null;
	}


}
