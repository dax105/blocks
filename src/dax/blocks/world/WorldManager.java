package dax.blocks.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import dax.blocks.Game;
import dax.blocks.overlay.InfoOverlay;
import dax.blocks.render.RenderEngine;
import dax.blocks.settings.Settings;
import dax.blocks.util.GLHelper;

public class WorldManager {
	
	public static final String SAVES_DIR = "saves";
	
	private File savesDir;
	private boolean ingame;
	private World currentWorld;
	private InfoOverlay infoOverlay;
	private Game game;
	
	public WorldManager(Game g) {
		this.game = g;
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
				File d = new File(WorldManager.SAVES_DIR, name);
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
		RenderEngine e = new RenderEngine(this.game, Settings.getInstance().shaders.getValue());
		this.currentWorld = new World(this.game, name, e);
		
		this.infoOverlay = new InfoOverlay(this.game);
		this.game.getOverlayManager().addOverlay(this.infoOverlay);
		this.game.closeGuiScreen();
		
		return this.currentWorld;
	}
	
	public void exitWorld() {
		if(this.ingame) {
			this.game.getOverlayManager().removeOverlay(this.infoOverlay);
			this.game.getOverlayManager().removeOverlay(this.currentWorld.getPlayer());
			
			this.currentWorld.exit();
			this.currentWorld = null;
			this.ingame = false;	
		}
	}
}
