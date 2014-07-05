package dax.blocks.console;

import dax.blocks.Game;

public class CommandGet extends Command {

	@Override
	public String getName() {
		return "get";
	}

	@Override
	public String getUsage() {
		return "get [variable name]";
	}

	@Override
	public boolean execute(String[] args) {

		if (args.length >= 1) {
			if(Game.settings.hasObject(args[0])) {
				Game.console.out("Value of variable " + args[0] + " is " + Game.settings.getValue(args[0]));
				return true;
			} else {
				Game.console.out("Unknown variable \"" + args[0] + "\"");
			}
		} else {
			Game.console.out("Not enough arguments, correct usage:");
			Game.console.out(getUsage());			
		}
		
		return false;
	}

}
