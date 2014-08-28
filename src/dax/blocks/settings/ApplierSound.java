package dax.blocks.settings;

import dax.blocks.Game;
import dax.blocks.sound.SoundManager;

public class ApplierSound extends Applier {

	public ApplierSound(Game game) {
		super(game);
	}

	@Override
	public boolean apply(Object val) {
		if(super.applyingObject == Settings.getInstance().sound) {
			SoundManager.getInstance().updateVolume((boolean) val, Settings.getInstance().soundVolume.getValue());
		} else {
			SoundManager.getInstance().updateVolume(Settings.getInstance().sound.getValue(), (float) val);
		}
		
		return true;
	}

	@Override
	public void afterApplying() {
	}

}
