package dax.blocks.console;

import dax.blocks.Game;
import dax.blocks.world.ChunkProvider;
import dax.blocks.world.World;

public class CommandReload extends Command {

	@Override
	public String getName() {
		return "reload";
	}

	@Override
	public String getUsage() {
		return "reload";
	}

	@Override
	public boolean execute(String[] args) {
		
		Game game = Game.getInstance();
		
		if (game.ingame) {
			game.world = new World(0, 0, false, game, true);
			return true;
		} else {
			Game.console.out("You must be ingame to use command " + getName());
			return false;
		}
	}

}
