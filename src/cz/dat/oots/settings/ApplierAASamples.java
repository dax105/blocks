package cz.dat.oots.settings;

import cz.dat.oots.Game;
import cz.dat.oots.util.GLHelper;
import org.lwjgl.opengl.Display;

public class ApplierAASamples extends Applier<Integer> {

    public ApplierAASamples(Game game) {
        super(game);
    }

    @Override
    public boolean apply(Integer val, ApplyRequestSource source) {
        if(source == ApplyRequestSource.STARTUP)
            return true;

        Display.destroy();

        GLHelper.setDisplayMode(this.settings.resolution.width(),
                this.settings.resolution.height(),
                val,
                this.settings.fullscreen.getValue());

        GLHelper.initGL(this.settings.resolution.width(),
                this.settings.resolution.height(),
                this.settings.fov.getValue());

        this.game.load(!this.game.getWorldsManager().isInGame());
        this.game.getCurrentWorld().deleteAllRenderChunks();

        return true;
    }

}
