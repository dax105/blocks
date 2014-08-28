package dax.blocks.settings;

import dax.blocks.Game;
import dax.blocks.world.IDRegister;

public class ApplierLeaves extends Applier {

	public ApplierLeaves(Game game) {
		super(game);
	}

	@Override
	public boolean apply(Object val) {
		boolean transp = (boolean) val;
		
		IDRegister.leaves.setOpaque(!transp);
		IDRegister.leaves.setAllTextures(transp ? 10 : 19);
		
		if(this.game.getWorldsManager().isInGame()) {
			this.game.getCurrentWorld().setAllChunksDirty();
		}
		
		return true;
	}

	@Override
	public void afterApplying() {
	}

}
