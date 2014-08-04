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
		if (!((Boolean)this.applyingObject.getValue()) && (Boolean)value) {
			this.lastWidth = Game.settings.windowWidth.getValue();
			this.lastHeight = Game.settings.windowHeight.getValue();
			
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension screensize = toolkit.getScreenSize();
			Game.settings.windowWidth.setValue(screensize.width, true);
			Game.settings.windowHeight.setValue(screensize.height, true);
		} else if((Boolean)this.applyingObject.getValue() && (Boolean)value) {
			Game.settings.windowWidth.setValue(lastWidth, true);
			Game.settings.windowHeight.setValue(lastHeight, true);
		}
		
		
		GLHelper.setDisplayMode(Game.settings.windowWidth.getValue(), Game.settings.windowHeight.getValue(), (Boolean)value);
		Game.getInstance().init();
		return true;
	}

}
