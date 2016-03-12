package cz.dat.oots.console;

import cz.dat.oots.Game;
import cz.dat.oots.util.GameUtil;

import java.io.File;

public class CommandDeleteWorld extends Command {

    public CommandDeleteWorld(Console console) {
        super(console);
    }

    @Override
    public String getName() {
        return "deleteworld";
    }

    @Override
    public String getUsage() {
        return "deleteworld";
    }

    @Override
    public boolean execute(String[] args) {
        Game game = this.console.getGame();

        String wName = this.console.getGame().getCurrentWorld().name;
        game.getWorldsManager().exitWorld();

        GameUtil.deleteDirectory(new File("saves", wName));
        game.closeGuiScreen();
        return true;
    }

}
