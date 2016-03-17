package cz.dat.oots.settings;

import cz.dat.oots.Game;
import cz.dat.oots.world.IDRegister;

public class ApplierLeaves extends Applier<Boolean> {

    public ApplierLeaves(Game game) {
        super(game);
    }

    @Override
    public boolean apply(Boolean val, ApplyRequestSource source) {
        if(source == ApplyRequestSource.STARTUP)
            return true;

        IDRegister.leaves.setOpaque(!val);
        IDRegister.leaves.setAllTextures(val ? 10 : 19);

        if (this.game.getWorldsManager().isInGame()) {
            this.game.getCurrentWorld().setAllChunksDirty();
        }

        return true;
    }

}
