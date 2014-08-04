package dax.blocks.settings;

import dax.blocks.GLHelper;
import dax.blocks.Game;

public class ApplierResolution extends Applier {

	@Override
	public boolean apply(Object val) {
		int width = Game.settings.windowWidth.getValue();
		int height = Game.settings.windowHeight.getValue();

		if (this.applyingObject == Game.settings.windowWidth) {
			width = (Integer) val;
		}

		if (this.applyingObject == Game.settings.windowHeight) {
			height = (Integer) val;
		}

		if (width > 200 && height > 200) {

			GLHelper.setDisplayMode(width, height, Game.settings.fullscreen.getValue());

			if (Game.getInstance().guiScreen != null) {
				Game.getInstance().closeGuiScreen();
			}
		}
		
		Game.settings.resolution.setValue(width + "x" + height, false);

		if(Game.ingameGuiManager.getCurrentScreen() != null)
			Game.ingameGuiManager.getCurrentScreen().updateCenteredPosition(width, height);
		
		return true;

	}
}
