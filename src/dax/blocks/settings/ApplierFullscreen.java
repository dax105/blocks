package dax.blocks.settings;

import java.awt.Dimension;
import java.awt.Toolkit;

import dax.blocks.Game;
import dax.blocks.util.GLHelper;

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

			GLHelper.setDisplayMode(screenSize.width, screenSize.height, true);
		} else if(wasFullscreen && !((Boolean) value)) {
			GLHelper.setDisplayMode(Settings.getInstance().windowWidth.getValue(),
					Settings.getInstance().windowHeight.getValue(), false);
		} else {
			return false;
		}

		return true;
	}

	@Override
	public void afterApplying() {
	}

}
