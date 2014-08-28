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
	
	private static volatile Settings _instance;
	private static volatile Game _gInstance;
	
	public void reset() {
		synchronized(Settings.class) {
			_instance = null;
		}
	}
	
	public static void setGame(Game game) {
		Settings._gInstance = game;
	}
	
	public static Settings getInstance() {
		if(_instance == null) {
			synchronized(Settings.class) {
				if(_instance == null) {
					_instance = new Settings();
				}
			}
		}
			
		return _instance;
	}

	
	public Map<String, SettingsObject<?>> objects = new HashMap<String, SettingsObject<?>>();
	
	public SettingsObject<Integer> drawDistance = (SettingsObject<Integer>) registerObject(new SettingsObject<Integer>(
			"draw_distance", 10, "Render distance", "%v chunks", null));
	public SettingsObject<Integer> consoleHeight = (SettingsObject<Integer>) registerObject(new SettingsObject<Integer>(
			"console_height", 200, "Console height", "%v px", null));
	public SettingsObject<Integer> aaSamples = (SettingsObject<Integer>) registerObject(new SettingsObject<Integer>(
			"aa_samples", 0, "AA samples", null, new ApplierAA(Settings._gInstance)));
	public SettingsObject<Integer> anisotropic = (SettingsObject<Integer>) registerObject(new SettingsObject<Integer>(
			"anisotropic", 0, "AF value", null, null));
	public SettingsObject<Integer> explosionRadius = (SettingsObject<Integer>) registerObject(new SettingsObject<Integer>(
			"explosion_radius", 5, "Explosion radius", null, null));
	public SettingsObject<Integer> rebuildsPerFrame = (SettingsObject<Integer>) registerObject(new SettingsObject<Integer>(
			"rebuilds_pf", 5, "Chunk geometry rebuilds per frame", null, null));
	public SettingsObject<Integer> loadsPerTick = (SettingsObject<Integer>) registerObject(new SettingsObject<Integer>(
			"loads_pt", 3, "Loaded chunks per tick", "%v chunks", null));
	public SettingsObject<Integer> decorationsPerTick = (SettingsObject<Integer>) registerObject(new SettingsObject<Integer>(
			"decorations_pt", 3, "Chunks decorated per tick", null, null));
	public SettingsObject<Integer> chunkCacheSize = (SettingsObject<Integer>) registerObject(new SettingsObject<Integer>(
			"chunk_cache_size", 500, "Chunk cache size", null, null));
	public SettingsObject<Boolean> debug = (SettingsObject<Boolean>) registerObject(new SettingsObject<Boolean>(
			"debug", false, "Show debug info", "%o", null));
	public SettingsObject<Boolean> fullscreen = (SettingsObject<Boolean>) registerObject(new SettingsObject<Boolean>(
			"fullscreen", false, "Fullscreen mode", "%o", new ApplierFullscreen(Settings._gInstance)));
	public SettingsObject<Boolean> mipmaps = (SettingsObject<Boolean>) registerObject(new SettingsObject<Boolean>(
			"mipmaps", true, "Mipmapping", "%o", null));
	public SettingsObject<Boolean> sound = (SettingsObject<Boolean>) registerObject(new SettingsObject<Boolean>(
			"sound", true, "Sound", "%o", new ApplierSound(Settings._gInstance)));	
	public SettingsObject<Boolean> frustumCulling = (SettingsObject<Boolean>) registerObject(new SettingsObject<Boolean>(
			"culling_frustum", true, "Frustum culling", "%o", null));
	public SettingsObject<Boolean> advancedCulling = (SettingsObject<Boolean>) registerObject(new SettingsObject<Boolean>(
			"culling_advanced", true, "Advanced culling", "%o", null));
	public SettingsObject<Boolean> treeGeneration = (SettingsObject<Boolean>) registerObject(new SettingsObject<Boolean>(
			"tree_generation", true, "Tree generator", "%o", null));
	public SettingsObject<Boolean> linearFiltering = (SettingsObject<Boolean>) registerObject(new SettingsObject<Boolean>(
			"linear_filtering", false, "Linear filtering", "%o", null));
	public SettingsObject<Boolean> shaders = (SettingsObject<Boolean>) registerObject(new SettingsObject<Boolean>(
			"enable_shaders", false, "Enable shaders", "%o", null));
	public SettingsObject<Boolean> transparentLeaves = (SettingsObject<Boolean>) registerObject(new SettingsObject<Boolean>(
			"transparent_leaves", true, "Transparent leaves", "%o",
			new ApplierLeaves(Settings._gInstance)));
	public SettingsObject<Boolean> twoPassTranslucent = (SettingsObject<Boolean>) registerObject(new SettingsObject<Boolean>(
			"two_pass_translucent", true, "Two pass rendering", "%o", null));
	public SettingsObject<Boolean> clouds = (SettingsObject<Boolean>) registerObject(new SettingsObject<Boolean>(
			"clouds", true, "Enable clouds", "%o", null));
	public SettingsObject<Float> fov = (SettingsObject<Float>) registerObject(new SettingsObject<Float>(
			"fov", 80.0f, "FOV", null, null));
	public SettingsObject<Float> reach = (SettingsObject<Float>) registerObject(new SettingsObject<Float>(
			"reach", 20.0f, "Block reach radius", "%v blocks", null));
	public SettingsObject<Float> aoIntensity = (SettingsObject<Float>) registerObject(new SettingsObject<Float>(
			"ao_intensity", 0.25f, "AO Intensity", null, new ApplierAO(Settings._gInstance)));
	public SettingsObject<Float> soundVolume = (SettingsObject<Float>) registerObject(new SettingsObject<Float>(
			"sound_volume", 1f, "Sound volume (0.0 - 1.0)", null, new ApplierSound(Settings._gInstance)));
	public SettingsObject<String> resolution = (SettingsObject<String>) registerObject(new SettingsObject<String>(
			"resolution", "800x480", "Window resolution", null, new ApplierResolution(Settings._gInstance)));
	public SettingsObject<Integer> windowWidth = (SettingsObject<Integer>) registerObject(new SettingsObject<Integer>(
			"width", 800, "Window width", "%v px", new ApplierResolution(Settings._gInstance)));
	public SettingsObject<Integer> windowHeight = (SettingsObject<Integer>) registerObject(new SettingsObject<Integer>(
			"height", 480, "Window height", "%v px", new ApplierResolution(Settings._gInstance)));
	public SettingsObject<Integer> fpsLimit = (SettingsObject<Integer>) registerObject(new SettingsObject<Integer>(
			"fps_limit", 0, "FPS Limit", null, null));	
	public SettingsObject<Integer> loaderThreads = (SettingsObject<Integer>) registerObject(new SettingsObject<Integer>(
			"loader_threads", 1, "Loader threads", null, null));	
	public SettingsObject<Boolean> peacefulMode = (SettingsObject<Boolean>) registerObject(new SettingsObject<Boolean>(
			"peaceful", false, "What is dead may never die", "%o", null));
	public SettingsObject<Boolean> noclip = (SettingsObject<Boolean>) registerObject(new SettingsObject<Boolean>(
			"noclip", false, "Noclip", "%o", null));

	private Settings() {
	}
	
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
				if (words[0].equalsIgnoreCase("ao_intensity")
						|| words[0].equalsIgnoreCase("aa_samples") || words[0].equalsIgnoreCase("transparent_leaves"))
					setValue(words[0], words[1], false);
				else
					setValue(words[0], words[1], true);
			}
		}

		s.close();
	}

	public void saveToFile(File f) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(f);
		for (SettingsObject<?> ob : objects.values()) {
			pw.println(ob.getName() + " " + ob.getValue());
		}

		pw.close();
	}

	public SettingsObject<?> getObject(String name) {
		if (objects.get(name) != null)
			return objects.get(name);

		return null;
	}

	public ObjectType getType(String name) {
		SettingsObject<?> o = getObject(name);
		if (o != null)
			return o.getObjectType();

		return null;
	}

	public String getValue(String name) {
		return getObject(name).getValue().toString();
	}

	public void setValue(String name, String value) {
		this.setValue(name, value, true);
	}

	public void setValue(String name, String value, boolean apply) {
		ObjectType type = getType(name);
		if (type != null) {
			try {
				switch (type) {
				case INTEGER:
					((SettingsObject<Integer>) getObject(name)).setValue(
							Integer.parseInt(value), apply);
					Settings._gInstance.getConsole().println("Set value of int " + name + " to "
							+ Integer.parseInt(value));
					break;
				case FLOAT:
					((SettingsObject<Float>) getObject(name)).setValue(
							Float.parseFloat(value), apply);
					Settings._gInstance.getConsole().println("Set value of float " + name + " to "
							+ Float.parseFloat(value));
					break;
				case BOOLEAN:
					((SettingsObject<Boolean>) getObject(name)).setValue(
							Boolean.parseBoolean(value), apply);
					Settings._gInstance.getConsole().println("Set value of boolean " + name + " to "
							+ Boolean.parseBoolean(value));
					break;
				case STRING:
					((SettingsObject<String>) getObject(name)).setValue(value,
							apply);
					Settings._gInstance.getConsole().println("Set value of string " + name + " to "
							+ value);
					break;
				default:
					break;
				}
			} catch (NumberFormatException e) {
				Settings._gInstance.getConsole().println("Incorrect value!");
				e.printStackTrace();
			}
		}
	}

	public void setValue(String name, Object value) {
		((SettingsObject<Object>) getObject(name)).setValue(value);
		Settings._gInstance.getConsole().println("Set value of boolean " + name + " to "
				+ value.toString());
	}

}
