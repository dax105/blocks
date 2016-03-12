package cz.dat.oots.console;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    public Map<String, Command> commands = new HashMap<String, Command>();
    private Console c;

    public CommandManager(Console c) {
        this.c = c;

        this.registerCommand(new CommandSet(this.c));
        this.registerCommand(new CommandGet(this.c));
        this.registerCommand(new CommandReload(this.c));
        this.registerCommand(new CommandHelp(this.c));
        this.registerCommand(new CommandTele(this.c));
        this.registerCommand(new CommandVariables(this.c));
        this.registerCommand(new CommandDeleteWorld(this.c));
        this.registerCommand(new CommandCullLock(this.c));
        this.registerCommand(new CommandDefaults(this.c));
    }

    public Command getCommand(String name) {
        return this.commands.get(name);
    }

    private void registerCommand(Command command) {
        this.commands.put(command.getName(), command);
    }

}
