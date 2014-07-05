package dax.blocks.console;

import java.util.Map.Entry;

import dax.blocks.Game;

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

		Game.console.out("List of available commands:");
		
		for (Entry<String, Command> e : Game.console.manager.commands.entrySet()) {
			Game.console.out(e.getValue().getName());
		}
		
		return true;
	}

}
