package dax.blocks.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import dax.blocks.Game;
import dax.blocks.InfoOverlay;
import dax.blocks.render.RenderEngine;
import dax.blocks.settings.Settings;
import dax.blocks.util.GLHelper;

public class WorldsManager {
	
	public static final String SAVES_DIR = "saves";
	
	private File savesDir;
	private boolean ingame;
	private World currentWorld;
	private InfoOverlay infoOverlay;

	public WorldsManager() {
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

	public boolean isInGame() {
		return this.ingame;
	}
	
	public World getWorld() {
		return this.currentWorld;
	}
	
	public World startWorld(String name) {
		this.ingame = true;
		GLHelper.updateFiltering(Settings.getInstance().linearFiltering.getValue());
		RenderEngine e = new RenderEngine(Settings.getInstance().shaders.getValue());
		this.currentWorld = new World(name, e);
		
		this.infoOverlay = new InfoOverlay(Game.getInstance());
		Game.getInstance().getOverlayManager().addOverlay(this.infoOverlay);
		
		//TODO: Remove dependency on Game
		Game.getInstance().closeGuiScreen();
		
		return this.currentWorld;
	}
	
	public void exitWorld() {
		if(this.ingame) {
			this.currentWorld.saveAllChunks();
			this.currentWorld = null;
			this.ingame = false;
			
			Game.getInstance().getOverlayManager().removeOverlay(this.infoOverlay);
		}
	}
}
