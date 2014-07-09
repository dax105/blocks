package dax.blocks.settings;

import dax.blocks.Game;
import dax.blocks.ModelManager;
import dax.blocks.world.World;

public class ApplierAO extends Applier {

	@Override
	public void apply() {

		if (Game.getInstance().ingame) {
			Game.getInstance().world.saveAllChunks();
			Game.getInstance().world = new World(false, Game.getInstance(), true, Game.getInstance().world.name);
		}
		
		Game.getInstance().displayLoadingScreen();
		ModelManager.load();
		Game.getInstance().closeGuiScreen();
	}

}
