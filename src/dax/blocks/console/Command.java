package dax.blocks.console;

public abstract class Command {

	public abstract String getName();	
	public abstract String getUsage();
	public abstract boolean execute(String[] args);
	
}
