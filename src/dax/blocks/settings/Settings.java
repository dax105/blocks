package dax.blocks.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import dax.blocks.Game;

@SuppressWarnings("unchecked")
public class Settings {
	public Map<String, SettingsObject<?>> objects = new HashMap<String, SettingsObject<?>>();
	
	
	public SettingsObject<Integer> drawDistance = (SettingsObject<Integer>) registerObject(new SettingsObject<Integer>("draw_distance", 10, "Render distance", "%v chunks", null));
	public SettingsObject<Integer> consoleHeight = (SettingsObject<Integer>) registerObject(new SettingsObject<Integer>("console_height", 200, "Console height", "%v px", null));
	public SettingsObject<Integer> aa_samples = (SettingsObject<Integer>) registerObject(new SettingsObject<Integer>("aa_samples", 0, "AA samples", null, new ApplierAA()));
	public SettingsObject<Integer> anisotropic = (SettingsObject<Integer>) registerObject(new SettingsObject<Integer>("anisotropic", 0, "AF value", null, null));
	public SettingsObject<Integer> explosion_radius = (SettingsObject<Integer>) registerObject(new SettingsObject<Integer>("explosion_radius", 5, "Explosion radius", null, null));
	public SettingsObject<Integer> rebuilds_pf = (SettingsObject<Integer>) registerObject(new SettingsObject<Integer>("rebuilds_pf", 5, "Chunk geometry rebuilds per frame", null, null));
	public SettingsObject<Integer> loads_pt = (SettingsObject<Integer>) registerObject(new SettingsObject<Integer>("loads_pt", 3, "Loaded chunks per tick", "%v chunks", null));
	public SettingsObject<Integer> decorations_pt = (SettingsObject<Integer>) registerObject(new SettingsObject<Integer>("decorations_pt", 3, "Decorations per tick", null, null));
	
	public SettingsObject<Boolean> fullscreen = (SettingsObject<Boolean>) registerObject(new SettingsObject<Boolean>("fullscreen", false, "Fullscreen mode", "%o", null));
	public SettingsObject<Boolean> mipmaps = (SettingsObject<Boolean>) registerObject(new SettingsObject<Boolean>("mipmaps", true, "Mipmapping", "%o", null));
	public SettingsObject<Boolean> sound = (SettingsObject<Boolean>) registerObject(new SettingsObject<Boolean>("sound", true, "Sound", "%o", null));
	public SettingsObject<Boolean> tree_generation = (SettingsObject<Boolean>) registerObject(new SettingsObject<Boolean>("tree_generation", true, "Tree generator", "%o", null));
	public SettingsObject<Boolean> linear_filtering = (SettingsObject<Boolean>) registerObject(new SettingsObject<Boolean>("linear_filtering", false, "Linear filtering", "%o", null));
	public SettingsObject<Boolean> enable_shaders = (SettingsObject<Boolean>) registerObject(new SettingsObject<Boolean>("enable_shaders", false, "Enable shaders", "%o", null));
	
	public SettingsObject<Float> fov = (SettingsObject<Float>) registerObject(new SettingsObject<Float>("fov", 80.0f, "FOV", null, null));
	public SettingsObject<Float> reach = (SettingsObject<Float>) registerObject(new SettingsObject<Float>("reach", 20.0f, "Block reach radius", "%v blocks", null));
	public SettingsObject<Float> ao_intensity = (SettingsObject<Float>) registerObject(new SettingsObject<Float>("ao_intensity", 0.25f, "AO Intensity", null, new ApplierAO()));
	public SettingsObject<Float> sound_volume = (SettingsObject<Float>) registerObject(new SettingsObject<Float>("sound_volume", 1f, "Sound volume (0.0 - 1.0)", null, null));
	
	private SettingsObject<?> registerObject(SettingsObject<?> object) {
		objects.put(object.getName(), object);
		return object;
	}

	public void loadFromFile(File f) throws FileNotFoundException {
		Scanner s = new Scanner(f);
		while (s.hasNextLine()) {
			String l = s.nextLine();
			String[] words = l.split(" ");

			if (words.length >= 2) {
				setValue(words[0], words[1], false);
			}
		}
		
		s.close();
	}
	
	public void saveToFile(File f) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(f);
		for(SettingsObject<?> ob : objects.values()) {
			pw.println(ob.getName() + " " + ob.getValue());
		}
		
		pw.close();
	}
	
	public SettingsObject<?> getObject(String name) {
		if(objects.containsKey(name))
			return objects.get(name);
		
		return null;
	}

	
	public ObjectType getType(String name) {
		SettingsObject<?> o = getObject(name);
		if(o != null)
			return o.getObjectType();
		
		return null;
	}

	public String getValue(String name) {
		return getObject(name).getValue().toString();
	}
	
	public void setValue(String name, String value) {
		this.setValue(name, value, true);
	}
	
	public void setValue(String name, String value, boolean applyAppliers) {
		ObjectType type = getType(name);
		if (type != null) {
			try {
				switch (type) {
				case INTEGER:
					((SettingsObject<Integer>)getObject(name)).setValue(Integer.parseInt(value), applyAppliers);
					Game.console.out("Set value of int " + name + " to " + Integer.parseInt(value));
					break;
				case FLOAT:
					((SettingsObject<Float>)getObject(name)).setValue(Float.parseFloat(value), applyAppliers);
					Game.console.out("Set value of float " + name + " to " + Float.parseFloat(value));
					break;
				case BOOLEAN:
					((SettingsObject<Boolean>)getObject(name)).setValue(Boolean.parseBoolean(value), applyAppliers);
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
		((SettingsObject<Object>)getObject(name)).setValue(value);
		Game.console.out("Set value of boolean " + name + " to " + value.toString());
	}

}
