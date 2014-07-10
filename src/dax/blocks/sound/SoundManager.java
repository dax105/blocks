package dax.blocks.sound;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import dax.blocks.Game;
import paulscode.sound.ListenerData;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOgg;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

public class SoundManager {
	private SoundSystem system;
	private MusicProvider provider;
	
	public static Map<String, String> sounds;
	public static Map<String, String> music;

	public static String[] footstep_dirt;
	public static String[] footstep_grass;
	public static String[] footstep_wood;
	public static String[] footstep_stone;

	private static void loadSounds(SoundSystem system) {
		sounds = new HashMap<>();
		music = new HashMap<>();

		sounds.put("footstep_dirt_0", "footstep_dirt_0.wav");
		sounds.put("footstep_dirt_1", "footstep_dirt_1.wav");
		sounds.put("footstep_dirt_1", "footstep_dirt_2.wav");
		sounds.put("footstep_dirt_1", "footstep_dirt_3.wav");

		sounds.put("footstep_grass_0", "footstep_grass_0.wav");
		sounds.put("footstep_grass_1", "footstep_grass_1.wav");
		sounds.put("footstep_grass_1", "footstep_grass_2.wav");
		sounds.put("footstep_grass_1", "footstep_grass_3.wav");

		sounds.put("footstep_wood_0", "footstep_wood_0.wav");
		sounds.put("footstep_wood_1", "footstep_wood_1.wav");
		sounds.put("footstep_wood_1", "footstep_wood_2.wav");
		sounds.put("footstep_wood_1", "footstep_wood_3.wav");

		sounds.put("footstep_stone_0", "footstep_stone_0.wav");
		sounds.put("footstep_stone_1", "footstep_stone_1.wav");

		sounds.put("fall_hard", "fall_hard.wav");
		sounds.put("fall_soft", "fall_soft.wav");
		sounds.put("explosion", "explosion.wav");


		ListenerData d = system.getListenerData();
		for (Entry<String, String> sound : sounds.entrySet()) {
			system.newSource(false, sound.getKey(), sound.getValue(), false,
					d.position.x, d.position.y, d.position.z,
					SoundSystemConfig.ATTENUATION_NONE, 0);
		}

		sortSounds();
	}

	private static void sortSounds() {
		footstep_dirt = new String[] { "footstep_dirt_0", "footstep_dirt_1", "footstep_dirt_2", "footstep_dirt_3" };
		footstep_grass = new String[] { "footstep_grass_0", "footstep_grass_1", "footstep_grass_2", "footstep_grass_3" };
		footstep_wood = new String[] { "footstep_wood_0", "footstep_wood_1", "footstep_grass_2", "footstep_grass_3" };
		footstep_stone = new String[] { "footstep_stone_0", "footstep_stone_1" };
	}

	public SoundManager() {
		try {
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOgg.class);
			SoundSystemConfig.setCodec("wav", CodecWav.class);
			SoundSystemConfig.setSoundFilesPackage("dax/blocks/res/sound/");
		} catch (SoundSystemException e) {
			// TODO Sound system fail message
			e.printStackTrace();
		}
		
		system = new SoundSystem();
		SoundManager.loadSounds(this.system);
		this.updateVolume();
		provider = new MusicProvider(this);
	}
	
	public MusicProvider getMusicProvider() {
		return provider;
	}

	private String musicPlaying = null;
	private boolean isMusicPlaying = false;
	private boolean isMusicPaused = false;
	
	public boolean isMusicPlaying() {
		return isMusicPlaying;
	}
	
	public void playMusic(String name, boolean loop) {
		if (music.containsKey(name)) {
			stopMusic();
			system.backgroundMusic(name, music.get(name), loop);
			musicPlaying = name;
			isMusicPlaying = true;
		} else {
			Game.console.out("Music called " + name + " does not exist");
		}
	}
	
	public void updatePlaying() {
		if(isMusicPlaying) {
			isMusicPlaying = !(!this.isMusicPaused && !system.playing(musicPlaying));
		}
	}

	public void pauseMusic() {
		if (musicPlaying != null && system.playing(musicPlaying)) {
			system.pause(musicPlaying);
			this.isMusicPaused = true;
		}
	}

	public void playMusic() {
		if (musicPlaying != null && !system.playing(musicPlaying)) {
			system.play(musicPlaying);
			this.isMusicPaused = false;
		}
	}

	public void stopMusic() {
		if (musicPlaying != null && system.playing(musicPlaying)) {
			system.stop(musicPlaying);
			musicPlaying = null;
			isMusicPlaying = false;
		}
	}

	public void updateVolume() {
		if (Game.settings.sound.getValue()) {
			system.setMasterVolume(Game.settings.sound_volume.getValue());
			this.playMusic();
		} else {
			system.setMasterVolume(0);
			this.pauseMusic();
		}
	}

	public void shutdown() {
		stopMusic();
		system.cleanup();
	}
	
	public void playSound(String name) {
		playSound(name, 1);
	}
	
	public void playSound(String name, float pitch) {
		playSound(name, pitch, system.getMasterVolume());
	}

	public void playSound(String name, float pitch, float volume) {
		ListenerData d = system.getListenerData();
		playSound(name, pitch, volume, d.position.x, d.position.y, d.position.z, false);
	}

	public void playSound(String name, float pitch, float volume, float x,
			float y, float z, boolean loop) {
		if (sounds.containsKey(name)) {
			system.setVolume(name, volume);
			system.setPitch(name, pitch);
			system.setPosition(name, x, y, z);
			system.setLooping(name, loop);
			
			if(system.playing(name))
				system.stop(name);
			system.play(name);

			ListenerData d = system.getListenerData();
			system.setPosition(name, d.position.x, d.position.y, d.position.z);
		}
	}

	public void playSound(String[] names) {
		playSound(names, 1);
	}
	
	public void playSound(String[] names, float pitch) {
		playSound(names, pitch, system.getMasterVolume());
	}

	public void playSound(String[] names, float pitch, float volume) {
		ListenerData d = system.getListenerData();
		playSound(names, pitch, volume, d.position.x, d.position.y, d.position.z);
	}

	public void playSound(String[] names, float pitch, float volume, float x, float y, float z) {
		int index = system.randomNumberGenerator.nextInt(names.length);
		playSound(names[index], pitch, volume, x, y, z, false);
	}
	
	public SoundSystem getSoundSystem() {
		return system;
	}
}
