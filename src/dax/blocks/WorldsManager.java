package dax.blocks;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import dax.blocks.world.WorldInfo;

public class WorldsManager {
	public static final String SAVES_DIR = "saves";

	public WorldsManager() {

	}

	public List<File> getWorldsDirs() {
		ArrayList<File> worlds = new ArrayList<File>();

		File file = new File(SAVES_DIR);
		String[] names = file.list();

		if (names != null) {
			for (String name : names) {
				File d = new File(SAVES_DIR, name);
				if (d.isDirectory()) {
					worlds.add(d);
				}
			}
		}

		return worlds;
	}

	public List<WorldInfo> getWorldsInfo() {
		ArrayList<WorldInfo> worlds = new ArrayList<WorldInfo>();

		List<File> dirs = getWorldsDirs();
		for (File d : dirs) {
			try {
				worlds.add(WorldInfo
						.constructFromFile(new File(d, "world.txt")));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		return worlds;
	}

	public WorldInfo getWorld(String name) {
		List<WorldInfo> worlds = getWorldsInfo();
		for (WorldInfo i : worlds) {
			if (i.getWorldName().equalsIgnoreCase(name)) {
				return i;
			}
		}

		return null;
	}
}
