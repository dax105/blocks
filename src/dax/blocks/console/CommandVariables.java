package dax.blocks.console;

import java.util.Map.Entry;
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

		Console.println("List of all variables and their types:");

		for(Entry<String, SettingsObject<?>> e : Settings.getInstance().objects.entrySet()) {
			SettingsObject<?> o = e.getValue();
			Console.println(SettingsObject.getConsoleRepresentation(
					o.getObjectType()) + " " + o.getReadableName() + " (" + o.getName() + "): " + o.getReadableValue()
			);
		}

		return true;
	}

}
