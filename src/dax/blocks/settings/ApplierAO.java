package dax.blocks.settings;

import dax.blocks.Game;
import dax.blocks.model.ModelManager;
import dax.blocks.world.World;

public class ApplierAO extends Applier {

	@Override
	public boolean apply(Object val) {

		if (Game.getInstance().ingame) {
			Game.getInstance().world.saveAllChunks();
			Game.getInstance().world = new World(false, Game.getInstance(), true, Game.getInstance().world.name, Game.getInstance().world.getRenderEngine());
		}
		
		Game.getInstance().displayLoadingScreen();
		ModelManager.getInstance().load();
		Game.getInstance().closeGuiScreen();
		
		return true;
	}

}
