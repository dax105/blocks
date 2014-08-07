package dax.blocks.console;

import java.util.Map.Entry;

public class CommandHelp extends Command {

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

		Console.println("List of available commands:");
		
		for(Entry<String, Command> e : Console.getInstance().manager.commands.entrySet()) {
			Console.println(e.getValue().getName());
		}
		
		return true;
	}

}
