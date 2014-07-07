package dax.blocks.settings;

import org.lwjgl.opengl.Display;

import dax.blocks.Game;
import dax.blocks.world.World;

public class ApplierAA extends Applier {

	@Override
	public void apply() {
		Game.console.out("Applying AA");
		if (Game.getInstance().ingame) {
			Game.getInstance().world.saveAllChunks();
			Game.getInstance().world = new World(false, Game.getInstance(), true, Game.getInstance().world.name);
		}
		
		Display.destroy();
		
		Game.getInstance().setDisplayMode(Game.getInstance().width, Game.getInstance().height, Game.getInstance().isFullscreen);
		
		Game.getInstance().initGL();
		Game.getInstance().load(!Game.getInstance().ingame);
		
	}

}
