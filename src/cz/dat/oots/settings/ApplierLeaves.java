package cz.dat.oots.settings;

import cz.dat.oots.Game;
import cz.dat.oots.world.IDRegister;

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
