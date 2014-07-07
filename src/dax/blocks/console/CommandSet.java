package dax.blocks.console;

import dax.blocks.Game;
import dax.blocks.settings.SettingsObject;

public class CommandSet extends Command {

	@Override
	public String getName() {
		return "set";
	}

	@Override
	public boolean execute(String[] args) {
		
		if (args.length >= 2) {
			SettingsObject<?> o = Game.settings.getObject(args[0]);
			if(o != null) {
				Game.settings.setValue(o.getName(), args[1]);
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
