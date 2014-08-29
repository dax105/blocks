package cz.dat.oots.console;

import cz.dat.oots.Game;

public class CommandReload extends Command {

	public CommandReload(Console console) {
		super(console);
	}

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
		
		Game game = this.console.getGame();
		
		if (game.getWorldsManager().isInGame()) {
			game.getWorldsManager().startWorld(game.getCurrentWorld().name);
			return true;
		} else {
			this.console.println("You must be ingame to use command " + getName());
			return false;
		}
	}

}
