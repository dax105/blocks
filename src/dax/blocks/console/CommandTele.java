package dax.blocks.console;

import dax.blocks.Game;

public class CommandTele extends Command {

	@Override
	public String getName() {
		return "tele";
	}

	@Override
	public String getUsage() {
		return "tele [x] [y] [z]";
	}

	@Override
	public boolean execute(String[] args) {
		if (args.length >= 3) {
			
			try {
				float x = Float.parseFloat(args[0]);
				float y = Float.parseFloat(args[1]);
				float z = Float.parseFloat(args[2]);
				
				Game game = Game.getInstance();
				
				if (game.world == null) {
					Game.console.out("You must be ingame to use command " + getName());
					return false;
				}
				
				game.world.player.setPosition(x, y, z);
				
				return true;
			} catch (NumberFormatException e) {
				Game.console.out("Invalid values!");
				e.printStackTrace();
				return false;
			}
		} else {
			Game.console.out("Not enough arguments, correct usage: ");
			Game.console.out(getUsage());
		}
		
		return false;
	}

}
