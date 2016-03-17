package cz.dat.oots.console;

import cz.dat.oots.settings.SettingsObject;

import java.util.Map.Entry;

public class CommandVariables extends Command {

    public CommandVariables(Console c) {
        super(c);
    }

    @Override
    public String getName() {
        return "variables";
    }

    @Override
    public String getUsage() {
        return "variables <page>";
    }

    @Override
    public boolean execute(String[] args) {

        super.console.println("List of all variables and their types:");

        for (Entry<String, SettingsObject<?>> e : this.console.getGame().s().objects
                .entrySet()) {
            System.out.println(e.getKey());


            SettingsObject<?> o = e.getValue();
            super.console.println(SettingsObject.getConsoleRepresentation(o
                    .getObjectType())
                    + " "
                    + o.getReadableName()
                    + " ("
                    + o.getName() + "): " + o.getReadableValue());
        }

        return true;
    }

}
