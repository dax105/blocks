package cz.dat.oots.settings;

import cz.dat.oots.Game;
import cz.dat.oots.sound.SoundManager;

public class ApplierSound extends Applier {

    public ApplierSound(Game game) {
        super(game);
    }

    @Override
    public boolean apply(Object val) {
        if (super.applyingObject == this.settings.sound) {
            SoundManager.getInstance().updateVolume((boolean) val,
                    this.settings.soundVolume.getValue());
        } else {
            SoundManager.getInstance().updateVolume(
                    this.settings.sound.getValue(), (float) val);
        }

        return true;
    }

    @Override
    public void afterApplying() {
    }

}
