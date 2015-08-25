package cz.dat.oots.console;

public class CommandDefaults extends Command {

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
		this.console.getGame().resetSettings();
		this.console.out("Settings were reset to defaults!");

		return true;
	}

}
