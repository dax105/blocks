package cz.dat.oots.settings;

import cz.dat.oots.Game;
import cz.dat.oots.util.Coord2D;
import cz.dat.oots.util.GLHelper;

public class ApplierResolution extends Applier<Coord2D> {

    public ApplierResolution(Game game) {
        super(game);
    }

    @Override
    public boolean apply(Coord2D value, ApplyRequestSource source) {
        if(value.x > 200 && value.y > 200) {
            GLHelper.setDisplayMode(value.x, value.y, this.settings.aaSamples.getValue(),
                    ((SettingsObjectResolution) this.applyingObject).fullScreen());

            if (this.game.getWorldsManager().isInGame()) {
                String wName = this.game.getCurrentWorld().name;
                this.game.getWorldsManager().exitWorld();
                this.game.getWorldsManager().startWorld(wName);
            }

            return true;
        }

        return false;
    }

}
