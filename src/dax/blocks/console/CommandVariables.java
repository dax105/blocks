package dax.blocks.console;

import java.util.Map.Entry;

import dax.blocks.Game;
import dax.blocks.settings.Settings;
import dax.blocks.settings.SettingsObject;

public class CommandVariables extends Command {

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

		Settings settings = Game.settings;

		Game.console.out("List of all variables and their types:");

		for (Entry<String, SettingsObject<?>> e : settings.objects.entrySet()) {
			SettingsObject<?> o = e.getValue();
			Game.console.out(SettingsObject.getConsoleRepresentation(o
					.getObjectType())
					+ " "
					+ o.getReadableName() + " (" + o.getName() + "): " + o.getReadableValue());
		}

		return true;
	}

}
