package cz.dat.oots.settings;

import cz.dat.oots.Game;
import cz.dat.oots.sound.SoundManager;

public class ApplierSound extends Applier<Boolean> {
    public ApplierSound(Game game) {
        super(game);
    }

    @Override
    public boolean apply(Boolean value, ApplyRequestSource source) {
        SoundManager.getInstance().updateVolume(value, this.settings.soundVolume.getValue());
        return true;
    }
}
