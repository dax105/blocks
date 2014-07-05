package dax.blocks.settings;

import org.lwjgl.opengl.Display;

import dax.blocks.Game;
import dax.blocks.world.World;

public class ApplyerAA extends Applyer {

	@Override
	public void apply() {
		if (Game.getInstance().ingame) {
			Game.getInstance().world.saveAllChunks();
			Game.getInstance().world = new World(0, 0, false, Game.getInstance(), true);
		}
		
		Display.destroy();
		
		Game.getInstance().setDisplayMode(Game.getInstance().width, Game.getInstance().height, Game.getInstance().isFullscreen);
		
		Game.getInstance().initGL();
		Game.getInstance().load(!Game.getInstance().ingame);
		
	}

}
