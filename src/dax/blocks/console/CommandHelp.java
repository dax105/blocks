package dax.blocks.console;

import java.util.Map.Entry;

public class CommandHelp extends Command {

	public CommandHelp(Console console) {
		super(console);
	}

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getUsage() {
		return "help <page>";
	}

	@Override
	public boolean execute(String[] args) {

		this.console.println("List of available commands:");
		
		for(Entry<String, Command> e : this.console.manager.commands.entrySet()) {
			this.console.println(e.getValue().getName());
		}
		
		return true;
	}

}
