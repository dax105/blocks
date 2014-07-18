package dax.blocks.settings;

import org.lwjgl.opengl.Display;

import dax.blocks.GLHelper;
import dax.blocks.Game;

public class ApplierAA extends Applier {

	@Override
	public boolean apply(Object val) {	
		Display.destroy();
		
		GLHelper.setDisplayMode(Game.settings.windowWidth.getValue(), Game.settings.windowHeight.getValue(), Game.settings.fullscreen.getValue());
		
		GLHelper.initGL(Game.settings.windowWidth.getValue(), Game.settings.windowHeight.getValue());
		Game.getInstance().load(!Game.getInstance().ingame);
		
		Game.getInstance().world.deleteAllDisplayLists();
		
		return true;
	}

}
