package dax.blocks.console;

import java.io.File;

import dax.blocks.Game;

public class CommandDeleteWorld extends Command {

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
		if (Game.getInstance().world != null) {
			Game.getInstance().world.saveAllChunks();
		}	
		Game.getInstance().displayLoadingScreen("Deleting world save...");
		Game.getInstance().ingame = false;
		Game.getInstance().world = null;
		Game.deleteDirectory(new File("saves"));
		Game.getInstance().closeGuiScreen();
		return true;
	}

}
