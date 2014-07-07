package dax.blocks.console;

import dax.blocks.Game;
import dax.blocks.settings.SettingsObject;

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
			SettingsObject<?> o = Game.settings.getObject(args[0]);
			if(o != null) {
				Game.console.out("Setting " + o.getName() + ": " + o.getReadableValue());
			}
		} else {
			Game.console.out("Not enough arguments, correct usage:");
			Game.console.out(getUsage());			
		}
		
		return false;
	}

}
