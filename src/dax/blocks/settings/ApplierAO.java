package dax.blocks.settings;

import dax.blocks.Game;
import dax.blocks.model.ModelManager;

public class ApplierAO extends Applier {

	public ApplierAO(Game game) {
		super(game);
	}

	@Override
	public boolean apply(Object val) {
		this.game.displayLoadingScreen();
		ModelManager.getInstance().load();
		this.game.closeGuiScreen();
		
		if(this.game.getWorldsManager().isInGame()) {
			String wName = this.game.getCurrentWorld().name;
			this.game.getWorldsManager().exitWorld();
			this.game.getWorldsManager().startWorld(wName);
		}
		
		return true;
	}

	@Override
	public void afterApplying() {
	}

}
