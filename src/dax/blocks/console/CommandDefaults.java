package dax.blocks.console;

import dax.blocks.settings.Settings;

public class CommandDefaults extends Command{

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
		Console.getInstance().out("Settings were reset to defaults!");
		
		return true;
	}

	
	
}
