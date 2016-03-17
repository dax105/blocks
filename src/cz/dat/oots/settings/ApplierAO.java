package cz.dat.oots.settings;

import cz.dat.oots.Game;

public class ApplierAO extends Applier<Float> {

    public ApplierAO(Game game) {
        super(game);
    }

    @Override
    public boolean apply(Float value, ApplyRequestSource source) {
        this.game.chunkRenderer.setAOIntensity(value);

        if (this.game.getWorldsManager().isInGame()) {
            String wName = this.game.getCurrentWorld().name;
            this.game.getWorldsManager().exitWorld();
            this.game.getWorldsManager().startWorld(wName);
        }

        return true;
    }

}
