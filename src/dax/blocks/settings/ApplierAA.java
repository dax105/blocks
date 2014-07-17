package dax.blocks.settings;

import org.lwjgl.opengl.Display;

import dax.blocks.GLHelper;
import dax.blocks.Game;

public class ApplierAA extends Applier {

	@Override
	public boolean apply(Object val) {	
		Display.destroy();
		
		GLHelper.setDisplayMode(Game.getInstance().width, Game.getInstance().height, Game.getInstance().isFullscreen);
		
		GLHelper.initGL(Game.getInstance().width, Game.getInstance().height);
		Game.getInstance().load(!Game.getInstance().ingame);
		
		Game.getInstance().world.deleteAllDisplayLists();
		
		return true;
	}

}
