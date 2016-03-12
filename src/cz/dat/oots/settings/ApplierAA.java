package cz.dat.oots.settings;

import cz.dat.oots.Game;
import cz.dat.oots.util.GLHelper;
import org.lwjgl.opengl.Display;

public class ApplierAA extends Applier {

    public ApplierAA(Game game) {
        super(game);
    }

    @Override
    public boolean apply(Object val) {
        Display.destroy();

        GLHelper.setDisplayMode(this.settings.windowWidth.getValue(),
                this.settings.windowHeight.getValue(),
                this.settings.aaSamples.getValue(),
                this.settings.fullscreen.getValue());

        GLHelper.initGL(this.settings.windowWidth.getValue(),
                this.settings.windowHeight.getValue(),
                this.settings.fov.getValue());

        this.game.load(!this.game.getWorldsManager().isInGame());
        this.game.getCurrentWorld().deleteAllRenderChunks();

        return true;
    }

    @Override
    public void afterApplying() {
    }

}
