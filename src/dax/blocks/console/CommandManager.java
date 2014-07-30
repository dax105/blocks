package dax.blocks.console;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {

	public Map<String, Command> commands = new HashMap<String, Command>();
	
	public Command getCommand(String name) {
		return commands.get(name);
	}
	
	public CommandManager() {
		registerCommand(new CommandSet()); 
		registerCommand(new CommandGet()); 
		registerCommand(new CommandReload()); 
		registerCommand(new CommandHelp()); 
		registerCommand(new CommandTele()); 
		registerCommand(new CommandVariables());
		registerCommand(new CommandDeleteWorld());
		registerCommand(new CommandCullLock());
	}
	
	private void registerCommand(Command command) {
		commands.put(command.getName(), command);
	}
	
}
