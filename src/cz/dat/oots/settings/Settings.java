package cz.dat.oots.settings;

import cz.dat.oots.Game;
import cz.dat.oots.util.Coord2D;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Settings {

    public Map<String, SettingsObject<?>> objects;
    public SettingsObject<Integer> randomtickBlockCount, maxParticles, drawDistance, consoleHeight, aaSamples, anisotropic, explosionRadius, rebuildsPerFrame, loadsPerTick, decorationsPerTick, chunkCacheSize, fpsLimit, loaderThreads;
    public SettingsObject<Boolean> debug, fullscreen, mipmaps, sound, frustumCulling, advancedCulling, treeGeneration, linearFiltering, shaders, transparentLeaves, twoPassTranslucent, clouds, peacefulMode, noclip, explosionParticles;
    public SettingsObject<Float> fov, reach, aoIntensity, soundVolume;
    public SettingsObjectResolution resolution;
    private Game game;

    public Settings(Game game) {
        this.game = game;
    }

    public void initObjects() {
        this.objects = new HashMap<>();

        this.drawDistance = registerObject(new SettingsObject<>(
                "draw_distance", 10, "Render distance", "%v chunks", null));
        this.consoleHeight = registerObject(new SettingsObject<>(
                "console_height", 200, "Console height", "%v px", null));
        this.aaSamples = registerObject(new SettingsObject<>(
                "aa_samples", 0, "AA samples", null, new ApplierAASamples(this.game)));
        this.anisotropic = registerObject(new SettingsObject<>(
                "anisotropic", 0, "AF value", null, null));
        this.explosionRadius = registerObject(new SettingsObject<>(
                "explosion_radius", 5, "Explosion radius", null, null));
        this.rebuildsPerFrame = registerObject(new SettingsObject<>(
                "rebuilds_pf", 5, "Chunk geometry rebuilds per frame", null, null));
        this.loadsPerTick = registerObject(new SettingsObject<>(
                "loads_pt", 3, "Loaded chunks per tick", "%v chunks", null));
        this.decorationsPerTick = registerObject(new SettingsObject<>(
                "decorations_pt", 3, "Chunks decorated per tick", null, null));
        this.chunkCacheSize = registerObject(new SettingsObject<>(
                "chunk_cache_size", 500, "Chunk cache size", null, null));
        this.debug = registerObject(new SettingsObject<>(
                "debug", false, "Show debug info", "%o", null));
        this.fullscreen = registerObject(new SettingsObject<>(
                "fullscreen", false, "Fullscreen mode", "%o",
                new ApplierFullscreen(this.game)));
        this.mipmaps = registerObject(new SettingsObject<>(
                "mipmaps", true, "Mipmapping", "%o", null));
        this.sound = registerObject(new SettingsObject<>(
                "sound", true, "Sound", "%o", new ApplierSound(this.game)));
        this.frustumCulling = registerObject(new SettingsObject<>(
                "culling_frustum", true, "Frustum culling", "%o", null));
        this.advancedCulling = registerObject(new SettingsObject<>(
                "culling_advanced", true, "Advanced culling", "%o", null));
        this.treeGeneration = registerObject(new SettingsObject<>(
                "tree_generation", true, "Tree generator", "%o", null));
        this.linearFiltering = registerObject(new SettingsObject<>(
                "linear_filtering", false, "Linear filtering", "%o", null));
        this.shaders = registerObject(new SettingsObject<>(
                "enable_shaders", false, "Enable shaders", "%o", null));
        this.transparentLeaves = registerObject(new SettingsObject<>(
                "transparent_leaves", true, "Transparent leaves", "%o",
                new ApplierLeaves(this.game)));
        this.twoPassTranslucent = registerObject(new SettingsObject<>(
                "two_pass_translucent", true, "Two pass rendering", "%o", null));
        this.clouds = registerObject(new SettingsObject<>(
                "clouds", true, "Enable clouds", "%o", null));
        this.fov = registerObject(new SettingsObject<>(
                "fov", 80.0f, "FOV", null, null));
        this.reach = registerObject(new SettingsObject<>(
                "reach", 20.0f, "Block reach radius", "%v blocks", null));
        this.aoIntensity = registerObject(new SettingsObject<>(
                "ao_intensity", 0.25f, "AO Intensity", null, new ApplierAO(
                this.game)));
        this.soundVolume = registerObject(new SettingsObject<>(
                "sound_volume", 1f, "Sound volume (0.0 - 1.0)", null,
                new ApplierSoundVolume(this.game)));
        this.resolution = (SettingsObjectResolution) registerObject(
                new SettingsObjectResolution("resolution", new Coord2D(800, 480), new ApplierResolution(this.game)));
        this.fpsLimit = registerObject(new SettingsObject<>(
                "fps_limit", 0, "FPS Limit", null, null));
        this.loaderThreads = registerObject(new SettingsObject<>(
                "loader_threads", 1, "Loader threads", null, null));
        this.peacefulMode = registerObject(new SettingsObject<>(
                "peaceful", false, "Peaceful mode", "%o", null));
        this.noclip = registerObject(new SettingsObject<>(
                "noclip", false, "Noclip", "%o", null));
        this.maxParticles = registerObject(new SettingsObject<>(
                "max_particles", 16384, "Maximum particle count", null, null));
        this.explosionParticles = registerObject(new SettingsObject<>(
                "explosion_particles", true, "Explosions spawn particles", "%o", null));
        this.randomtickBlockCount = registerObject(new SettingsObject<>("rt_block_count", 500,
                "Base random tick block count", "%v blocks", null));
    }

    private <T> SettingsObject<T> registerObject(SettingsObject<T> object) {
        objects.put(object.getName(), object);
        return object;
    }

    public void loadFromFile(File f) throws FileNotFoundException {
        Scanner s = new Scanner(f);
        while (s.hasNextLine()) {
            String l = s.nextLine();
            if(!l.equals("") && l.contains(":")) {
                String name = l.substring(0, l.indexOf(':'));
                String value = l.substring(l.indexOf(':') + 1).trim();

                this.setValue(name, value, ApplyRequestSource.STARTUP);
            }
        }

        s.close();
    }

    public void saveToFile(File f) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(f);
        for (SettingsObject<?> ob : objects.values()) {
            pw.println(ob.getName() + ": " + ob.getValueSerialized());
        }

        pw.close();
    }

    public SettingsObject<?> getObject(String name) {
        if (objects.get(name) != null)
            return objects.get(name);

        return null;
    }

    public void setValue(String name, String value, ApplyRequestSource source) {
        try {
            this.objects.get(name).deserializeValue(value, source);
        } catch (NumberFormatException e) {
            this.game.getConsole().println("Incorrect value!");
            e.printStackTrace();
        }

    }
}
