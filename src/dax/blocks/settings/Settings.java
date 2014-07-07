package dax.blocks.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import dax.blocks.Game;

public class Settings {
	
	private List<SettingsObject<?>> allObjects = new ArrayList<SettingsObject<?>>();
	public Map<String, SettingsObject<Integer>> objectsInteger = new HashMap<String, SettingsObject<Integer>>();
	public Map<String, SettingsObject<Float>> objectsFloat = new HashMap<String, SettingsObject<Float>>();
	public Map<String, SettingsObject<Boolean>> objectsBoolean = new HashMap<String, SettingsObject<Boolean>>();

	public SettingsObject<Integer> drawDistance = registerObjectInteger(new SettingsObject<Integer>("draw_distance", 10));
	public SettingsObject<Integer> consoleHeight = registerObjectInteger(new SettingsObject<Integer>("console_height", 200));
	public SettingsObject<Integer> aa_samples = registerObjectInteger(new SettingsObject<Integer>("aa_samples", 0, new ApplierAA()));
	public SettingsObject<Integer> anisotropic = registerObjectInteger(new SettingsObject<Integer>("anisotropic", 0));
	public SettingsObject<Integer> explosion_radius = registerObjectInteger(new SettingsObject<Integer>("explosion_radius", 5));
	public SettingsObject<Integer> rebuilds_pf = registerObjectInteger(new SettingsObject<Integer>("rebuilds_pf", 5));
	public SettingsObject<Integer> loads_pt = registerObjectInteger(new SettingsObject<Integer>("loads_pt", 3));
	public SettingsObject<Integer> decorations_pt = registerObjectInteger(new SettingsObject<Integer>("decorations_pt", 3));
	public SettingsObject<Float> fov = registerObjectFloat(new SettingsObject<Float>("fov", 80.0f));
	public SettingsObject<Float> reach = registerObjectFloat(new SettingsObject<Float>("reach", 20.0f));
	public SettingsObject<Boolean> fullscreen = registerObjectBoolean(new SettingsObject<Boolean>("fullscreen", false));
	public SettingsObject<Boolean> mipmaps = registerObjectBoolean(new SettingsObject<Boolean>("mipmaps", true));
	public SettingsObject<Boolean> sound = registerObjectBoolean(new SettingsObject<Boolean>("sound", true));
	public SettingsObject<Float> ao_intensity = registerObjectFloat(new SettingsObject<Float>("ao_intensity", 0.25f, new ApplierAO()));
	public SettingsObject<Boolean> tree_generation = registerObjectBoolean(new SettingsObject<Boolean>("tree_generation", true));
	public SettingsObject<Boolean> linear_filtering = registerObjectBoolean(new SettingsObject<Boolean>("linear_filtering", false));
	public SettingsObject<Boolean> enable_shaders = registerObjectBoolean(new SettingsObject<Boolean>("enable_shaders", false));
	
	private SettingsObject<Integer> registerObjectInteger(SettingsObject<Integer> object) {
		objectsInteger.put(object.getName(), object);
		allObjects.add(object);
		return object;
	}

	private SettingsObject<Boolean> registerObjectBoolean(SettingsObject<Boolean> object) {
		objectsBoolean.put(object.getName(), object);
		allObjects.add(object);
		return object;
	}

	private SettingsObject<Float> registerObjectFloat(SettingsObject<Float> object) {
		objectsFloat.put(object.getName(), object);
		allObjects.add(object);
		return object;
	}

	public void loadFromFile(File f) throws FileNotFoundException {
		Scanner s = new Scanner(f);
		while (s.hasNextLine()) {
			String l = s.nextLine();
			String[] words = l.split(" ");

			if (words.length >= 2) {
				setValue(words[0], words[1]);
			}
		}
		
		s.close();
	}
	
	public void saveToFile(File f) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(f);
		for(SettingsObject<?> ob : allObjects) {
			pw.println(ob.getName() + " " + ob.getValue());
		}
		
		pw.close();
	}
	
	public SettingsObject<Integer> getInt(String name) {
		return objectsInteger.get(name);
	}

	public SettingsObject<Float> getFloat(String name) {
		return objectsFloat.get(name);
	}

	public SettingsObject<Boolean> getBoolean(String name) {
		return objectsBoolean.get(name);
	}

	public boolean hasObject(String name) {
		if (objectsInteger.containsKey(name))
			return true;
		if (objectsFloat.containsKey(name))
			return true;
		if (objectsBoolean.containsKey(name))
			return true;
		return false;
	}
	
	public ObjectType getType(String name) {
		if (objectsInteger.containsKey(name))
			return ObjectType.INTEGER;
		if (objectsFloat.containsKey(name))
			return ObjectType.FLOAT;
		if (objectsBoolean.containsKey(name))
			return ObjectType.BOOLEAN;
		return null;
	}

	public String getValue(String name) {
		ObjectType type = getType(name);
		if (type != null) {
			try {
				switch (type) {
				case INTEGER:
					return "" + getInt(name).getValue();
				case FLOAT:
					return "" + getFloat(name).getValue();
				case BOOLEAN:
					return "" + getBoolean(name).getValue();
				default:
					break;
				}
			} catch (Exception e) {
				Game.console.out("Error!");
				e.printStackTrace();
			}
		}
		
		return "null";
	}
	
	public void setValue(String name, String value) {
		ObjectType type = getType(name);
		if (type != null) {
			try {
				switch (type) {
				case INTEGER:
					getInt(name).setValue(Integer.parseInt(value));
					Game.console.out("Set value of int " + name + " to " + Integer.parseInt(value));
					break;
				case FLOAT:
					getFloat(name).setValue(Float.parseFloat(value));
					Game.console.out("Set value of float " + name + " to " + Float.parseFloat(value));
					break;
				case BOOLEAN:
					getBoolean(name).setValue(Boolean.parseBoolean(value));
					Game.console.out("Set value of boolean " + name + " to " + Boolean.parseBoolean(value));
					break;
				default:
					break;
				}
			} catch (NumberFormatException e) {
				Game.console.out("Incorrect value!");
				e.printStackTrace();
			}
		}
	}
	
	public void setValue(String name, Object value) {
		ObjectType type = getType(name);
		if (type != null) {
			switch (type) {
			case INTEGER:
				getInt(name).setValue((Integer) value);
				break;
			case FLOAT:
				getFloat(name).setValue((Float) value);
				break;
			case BOOLEAN:
				getBoolean(name).setValue((Boolean) value);
				break;
			default:
				break;
			}
		}
	}

}
