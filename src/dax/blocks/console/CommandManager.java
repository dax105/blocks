package dax.blocks.console;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {

	public Map<String, Command> commands = new HashMap<String, Command>();
	
	public Command getCommand(String name) {
		return this.commands.get(name);
	}
	
	public CommandManager() {
		this.registerCommand(new CommandSet()); 
		this.registerCommand(new CommandGet()); 
		this.registerCommand(new CommandReload()); 
		this.registerCommand(new CommandHelp()); 
		this.registerCommand(new CommandTele()); 
		this.registerCommand(new CommandVariables());
		this.registerCommand(new CommandDeleteWorld());
		this.registerCommand(new CommandCullLock());
	}
	
	private void registerCommand(Command command) {
		this.commands.put(command.getName(), command);
	}
	
}
