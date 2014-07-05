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
		
		for (Entry<String, SettingsObject<Boolean>> e : settings.objectsBoolean.entrySet()) {
			SettingsObject<Boolean> val = e.getValue();
			Game.console.out("[BOOL] " + val.getName() + ": " + val.getValue());
		}
		
		for (Entry<String, SettingsObject<Integer>> e : settings.objectsInteger.entrySet()) {
			SettingsObject<Integer> val = e.getValue();
			Game.console.out("[INT] " + val.getName() + ": " + val.getValue());
		}
		
		for (Entry<String, SettingsObject<Float>> e : settings.objectsFloat.entrySet()) {
			SettingsObject<Float> val = e.getValue();
			Game.console.out("[FLOAT] " + val.getName() + ": " + val.getValue());
		}
		
		return true;
	}

}
