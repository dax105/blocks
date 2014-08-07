package dax.blocks.settings;

import dax.blocks.Game;
import dax.blocks.model.ModelManager;

public class ApplierAO extends Applier {

	@Override
	public boolean apply(Object val) {

		if(Game.getInstance().getWorldsManager().isInGame()) {
			String wName = Game.getInstance().getCurrentWorld().name;
			Game.getInstance().getWorldsManager().exitWorld();
			Game.getInstance().getWorldsManager().startWorld(wName);
		}
		
		Game.getInstance().displayLoadingScreen();
		ModelManager.getInstance().load();
		Game.getInstance().closeGuiScreen();
		
		return true;
	}

}
