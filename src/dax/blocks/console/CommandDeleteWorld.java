package dax.blocks.console;

import java.io.File;

import dax.blocks.Game;
import dax.blocks.util.GameUtil;

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
