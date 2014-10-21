package cz.dat.oots.console;

import cz.dat.oots.settings.ObjectType;
import cz.dat.oots.settings.SettingsObject;

public class CommandSet extends Command {

	public CommandSet(Console console) {
		super(console);
	}

	@Override
	public String getName() {
		return "set";
	}

	@Override
	public boolean execute(String[] args) {
		if(args.length >= 2) {
			SettingsObject<?> o = this.console.getGame().s().getObject(args[0]);

			if(o != null) {
				if(o.getObjectType() == ObjectType.BOOLEAN) {
					if(args[1].equalsIgnoreCase("ON")) {
						this.console.getGame().s()
								.setValue(o.getName(), "true");
						return true;
					}

					if(args[0].equalsIgnoreCase("OFF")) {
						this.console.getGame().s()
								.setValue(o.getName(), "false");
						return true;
					}
				}

				this.console.getGame().s().setValue(o.getName(), args[1]);
				return true;
			} else {
				this.console.println("Unknown variable \"" + args[0] + "\"");
			}
		} else {
			this.console.println("Not enough arguments, correct usage:");
			this.console.println(getUsage());
		}

		return false;
	}

	@Override
	public String getUsage() {
		return "set [variable name] [value]";
	}
}
