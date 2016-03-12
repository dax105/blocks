package cz.dat.oots.console;

import cz.dat.oots.settings.SettingsObject;

public class CommandGet extends Command {

    public CommandGet(Console console) {
        super(console);
    }

    @Override
    public String getName() {
        return "get";
    }

    @Override
    public String getUsage() {
        return "get [variable name]";
    }

    @Override
    public boolean execute(String[] args) {

        if (args.length >= 1) {
            SettingsObject<?> o = this.console.getGame().s().getObject(args[0]);
            if (o != null) {
                this.console.println("Variable " + o.getName() + ": "
                        + o.getReadableValue());
            }
        } else {
            this.console.println("Not enough arguments, correct usage:");
            this.console.println(getUsage());
        }

        return false;
    }

}
