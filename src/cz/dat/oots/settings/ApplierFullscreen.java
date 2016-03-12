package cz.dat.oots.settings;

import cz.dat.oots.Game;
import cz.dat.oots.util.GLHelper;

import java.awt.*;

public class ApplierFullscreen extends Applier {

    int lastWidth;
    int lastHeight;
    public ApplierFullscreen(Game game) {
        super(game);
    }

    @Override
    public boolean apply(Object value) {
        boolean wasFullscreen = (Boolean) this.applyingObject.getValue();

        if (!wasFullscreen && (Boolean) value) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();

            GLHelper.setDisplayMode(screenSize.width, screenSize.height,
                    this.settings.aaSamples.getValue(), true);
        } else if (wasFullscreen && !((Boolean) value)) {
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
