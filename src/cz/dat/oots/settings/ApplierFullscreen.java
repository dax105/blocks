package cz.dat.oots.settings;

import cz.dat.oots.Game;
import cz.dat.oots.util.GLHelper;

import java.awt.*;

public class ApplierFullscreen extends Applier<Boolean> {

    int lastWidth;
    int lastHeight;
    public ApplierFullscreen(Game game) {
        super(game);
    }

    @Override
    public boolean apply(Boolean value, ApplyRequestSource source) {
        boolean wasFullscreen = this.applyingObject.getValue();

        if (!wasFullscreen && value) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();

            this.lastWidth = this.settings.resolution.width();
            this.lastHeight = this.settings.resolution.height();
            this.settings.resolution.setValue(screenSize.width, screenSize.height, true);
        } else if (wasFullscreen && !value) {
            this.settings.resolution.setValue(this.lastWidth, this.lastHeight, false);
        } else {
            return false;
        }

        return true;
    }

}
