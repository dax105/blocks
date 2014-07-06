package dax.blocks.settings;

import dax.blocks.Game;
import dax.blocks.ModelManager;
import dax.blocks.world.World;

public class ApplyerAO extends Applyer {

	@Override
	public void apply() {
		if (Game.getInstance().ingame) {
			Game.getInstance().world.saveAllChunks();
			Game.getInstance().world = new World(0, false, Game.getInstance(), true);
		}
		
		Game.getInstance().displayLoadingScreen();
		ModelManager.load();
		Game.getInstance().closeGuiScreen();
	}

}
