package dax.blocks.console;

import dax.blocks.settings.Settings;
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
			SettingsObject<?> o = Settings.getInstance().getObject(args[0]);
			if(o != null) {
				Console.println("Variable " + o.getName() + ": " + o.getReadableValue());
			}
		} else {
			Console.println("Not enough arguments, correct usage:");
			Console.println(getUsage());			
		}
		
		return false;
	}

}
