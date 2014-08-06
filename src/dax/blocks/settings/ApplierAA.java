package dax.blocks.settings;

import org.lwjgl.opengl.Display;

import dax.blocks.Game;
import dax.blocks.util.GLHelper;

public class ApplierAA extends Applier {

	@Override
	public boolean apply(Object val) {	
		Display.destroy();
		
		GLHelper.setDisplayMode(
				Settings.getInstance().windowWidth.getValue(), 
				Settings.getInstance().windowHeight.getValue(), 
				Settings.getInstance().fullscreen.getValue()
		);
		
		GLHelper.initGL(
				Settings.getInstance().windowWidth.getValue(), 
				Settings.getInstance().windowHeight.getValue()
		);
		Game.getInstance().load(!Game.getInstance().ingame);
		
		Game.getInstance().world.deleteAllDisplayLists();
		
		return true;
	}

}
