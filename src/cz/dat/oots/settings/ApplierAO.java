package cz.dat.oots.settings;

import cz.dat.oots.Game;
import cz.dat.oots.model.ModelManager;

public class ApplierAO extends Applier {

    public ApplierAO(Game game) {
        super(game);
    }

    @Override
    public boolean apply(Object val) {
        ModelManager.getInstance().load();
        this.game.chunkRenderer.setAOIntensity((Float) val);

        if (this.game.getWorldsManager().isInGame()) {
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
