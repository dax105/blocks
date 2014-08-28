package dax.blocks.settings;

import org.lwjgl.opengl.Display;

import dax.blocks.Game;
import dax.blocks.util.GLHelper;

public class ApplierAA extends Applier {

	public ApplierAA(Game game) {
		super(game);
	}

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
		this.game.load(!this.game.getWorldsManager().isInGame());
		this.game.getCurrentWorld().deleteAllRenderChunks();
		
		return true;
	}

	@Override
	public void afterApplying() {
	}

}
