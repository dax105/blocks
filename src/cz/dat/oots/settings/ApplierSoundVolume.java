package cz.dat.oots.settings;

import cz.dat.oots.Game;
import cz.dat.oots.sound.SoundManager;

public class ApplierSoundVolume extends Applier<Float> {
    public ApplierSoundVolume(Game game) {
        super(game);
    }

    @Override
    public boolean apply(Float value, ApplyRequestSource source) {
        SoundManager.getInstance().updateVolume(this.settings.sound.getValue(), value);
        return true;
    }
}
