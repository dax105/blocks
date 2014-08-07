package dax.blocks.console;

import java.io.File;

import dax.blocks.Game;
import dax.blocks.util.GameUtil;

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
		String wName = Game.getInstance().getCurrentWorld().name;
		Game.getInstance().getWorldsManager().exitWorld();
		
		GameUtil.deleteDirectory(new File("saves", wName));
		Game.getInstance().closeGuiScreen();
		return true;
	}

}
