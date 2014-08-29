package cz.dat.oots.console;

import cz.dat.oots.settings.Settings;

public class CommandDefaults extends Command{

	public CommandDefaults(Console console) {
		super(console);
	}

	@Override
	public String getName() {
		return "defaults";
	}

	@Override
	public String getUsage() {
		return "defaults";
	}

	@Override
	public boolean execute(String[] args) {
		Settings.getInstance().reset();
		this.console.out("Settings were reset to defaults!");
		
		return true;
	}

	
	
}
