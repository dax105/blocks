package dax.blocks.settings;

import java.awt.Dimension;
import java.awt.Toolkit;

import dax.blocks.GLHelper;
import dax.blocks.Game;

public class ApplierFullscreen extends Applier {
	int lastWidth;
	int lastHeight;
	
	@Override
	public boolean apply(Object value) {
		if (!((boolean)this.applyingObject.getValue()) && ((boolean)value)) {
			this.lastWidth = Game.settings.windowWidth.getValue();
			this.lastHeight = Game.settings.windowHeight.getValue();
			
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension screensize = toolkit.getScreenSize();
			Game.settings.windowWidth.setValue(screensize.width, false);
			Game.settings.windowHeight.setValue(screensize.height, false);
		} else if((boolean)this.applyingObject.getValue() && !((boolean)value)) {
			Game.settings.windowWidth.setValue(lastWidth, false);
			Game.settings.windowHeight.setValue(lastHeight, false);
		}
		
		
		GLHelper.setDisplayMode(Game.settings.windowWidth.getValue(), Game.settings.windowHeight.getValue(), (boolean)value);
		Game.getInstance().init();
		return true;
	}

}
