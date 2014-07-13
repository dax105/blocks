package dax.blocks.settings;

import org.lwjgl.opengl.Display;

import dax.blocks.Game;

public class ApplierAA extends Applier {

	@Override
	public void apply() {	
		Display.destroy();
		
		Game.getInstance().setDisplayMode(Game.getInstance().width, Game.getInstance().height, Game.getInstance().isFullscreen);
		
		Game.getInstance().initGL();
		Game.getInstance().load(!Game.getInstance().ingame);
		
		Game.getInstance().world.deleteAllDisplayLists();
		
	}

}
