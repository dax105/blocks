package dax.blocks.console;

import dax.blocks.Game;

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
		
		if (game.getWorldsManager().isInGame()) {
			game.getWorldsManager().startWorld(game.getCurrentWorld().name);
			return true;
		} else {
			Console.println("You must be ingame to use command " + getName());
			return false;
		}
	}

}
