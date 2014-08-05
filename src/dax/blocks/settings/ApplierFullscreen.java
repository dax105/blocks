package dax.blocks.settings;

import java.awt.Dimension;
import java.awt.Toolkit;

import dax.blocks.Game;
import dax.blocks.util.GLHelper;

public class ApplierFullscreen extends Applier {
	int lastWidth;
	int lastHeight;
	
	@Override
	public boolean apply(Object value) {
		if (!((Boolean)this.applyingObject.getValue()) && (Boolean)value) {
			this.lastWidth = Settings.getInstance().windowWidth.getValue();
			this.lastHeight = Settings.getInstance().windowHeight.getValue();
			
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension screensize = toolkit.getScreenSize();
			Settings.getInstance().windowWidth.setValue(screensize.width, true);
			Settings.getInstance().windowHeight.setValue(screensize.height, true);
		} else if((Boolean)this.applyingObject.getValue() && (Boolean)value) {
			Settings.getInstance().windowWidth.setValue(lastWidth, true);
			Settings.getInstance().windowHeight.setValue(lastHeight, true);
		}
		
		
		GLHelper.setDisplayMode(Settings.getInstance().windowWidth.getValue(), Settings.getInstance().windowHeight.getValue(), (Boolean)value);
		Game.getInstance().init();
		return true;
	}

}
