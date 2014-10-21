package cz.dat.oots.settings;

import java.awt.Dimension;
import java.awt.Toolkit;

import cz.dat.oots.Game;
import cz.dat.oots.util.GLHelper;

public class ApplierFullscreen extends Applier {

	public ApplierFullscreen(Game game) {
		super(game);
	}

	int lastWidth;
	int lastHeight;

	@Override
	public boolean apply(Object value) {
		boolean wasFullscreen = (Boolean) this.applyingObject.getValue();

		if(!wasFullscreen && (Boolean) value) {
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension screenSize = toolkit.getScreenSize();

			GLHelper.setDisplayMode(screenSize.width, screenSize.height,
					this.settings.aaSamples.getValue(), true);
		} else if(wasFullscreen && !((Boolean) value)) {
			GLHelper.setDisplayMode(this.settings.windowWidth.getValue(),
					this.settings.windowHeight.getValue(),
					this.settings.aaSamples.getValue(), false);
		} else {
			return false;
		}

		return true;
	}

	@Override
	public void afterApplying() {
	}

}
