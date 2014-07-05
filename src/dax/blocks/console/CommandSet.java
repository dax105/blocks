package dax.blocks.console;

import dax.blocks.Game;

public class CommandSet extends Command {

	@Override
	public String getName() {
		return "set";
	}

	@Override
	public boolean execute(String[] args) {
		
		if (args.length >= 2) {
			if(Game.settings.hasObject(args[0])) {
				Game.settings.setValue(args[0], args[1]);
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

	@Override
	public String getUsage() {
		return "set [variable name] [value]";
	}

}
