package cz.dat.oots.settings;

import cz.dat.oots.Game;
import cz.dat.oots.util.GLHelper;

public class ApplierResolution extends Applier {

	public ApplierResolution(Game game) {
		super(game);
	}

	@Override
	public boolean apply(Object val) {
		int w = this.settings.windowWidth.getValue();
		int h = this.settings.windowHeight.getValue();

		if(this.applyingObject == this.settings.windowWidth) {
			w = (Integer) val;
			this.changeBoth((Integer) val, h);
		} else if(this.applyingObject == this.settings.windowHeight) {
			h = (Integer) val;
			this.changeBoth(w, (Integer) val);
		} else if(this.applyingObject == this.settings.resolution) {
			String[] parts = val.toString().split("x");
			if(parts.length == 2) {
				try {
					w = Integer.parseInt(parts[0]);
					h = Integer.parseInt(parts[1]);

					this.changeBoth(w, h);
				} catch(Exception e) {
					return false;
				}
			} else {
				return false;
			}
		}

		if(this.game.guiScreen != null) {
			this.game.closeGuiScreen();
		}

		if(this.game.getCurrentWorld() != null)
			if(this.game.getCurrentWorld().getGui().getCurrentScreen() != null)
				this.game.getCurrentWorld().getGui().getCurrentScreen()
						.updateCenteredPosition(w, h);

		return true;

	}

	private void changeBoth(int w, int h) {
		GLHelper.setDisplayMode(w, h, this.settings.aaSamples.getValue(),
				this.settings.fullscreen.getValue());
	}

	@Override
	public void afterApplying() {
	}
}
