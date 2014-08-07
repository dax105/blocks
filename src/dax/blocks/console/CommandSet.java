package dax.blocks.console;

import dax.blocks.settings.ObjectType;
import dax.blocks.settings.Settings;
import dax.blocks.settings.SettingsObject;

public class CommandSet extends Command {

	@Override
	public String getName() {
		return "set";
	}

	@Override
	public boolean execute(String[] args) {
		if(args.length >= 2) {
			SettingsObject<?> o = Settings.getInstance().getObject(args[0]);
			if(o != null) {
				if(o.getObjectType() == ObjectType.BOOLEAN) {
					if(args[1].equalsIgnoreCase("ON")) {
						Settings.getInstance().setValue(o.getName(), true);
						return true;
					}
					
					if(args[0].equalsIgnoreCase("OFF")) {
						Settings.getInstance().setValue(o.getName(), false);
						return true;
					}
				}
				Settings.getInstance().setValue(o.getName(), args[1]);
				return true;
			} else {
				Console.println("Unknown variable \"" + args[0] + "\"");
			}
		} else {
			Console.println("Not enough arguments, correct usage:");
			Console.println(getUsage());
			
		}
		
		return false;
	}

	@Override
	public String getUsage() {
		return "set [variable name] [value]";
	}
}
