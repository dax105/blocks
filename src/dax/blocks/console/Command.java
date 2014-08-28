package dax.blocks.console;

public abstract class Command {

	protected Console console;
	public Command(Console console) {
		this.console = console;
	}
	
	public abstract String getName();	
	public abstract String getUsage();
	public abstract boolean execute(String[] args);
	
}
