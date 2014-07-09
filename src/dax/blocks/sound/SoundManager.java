package dax.blocks.sound;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.newdawn.slick.openal.Audio;

import dax.blocks.Game;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOgg;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

public class SoundManager {
	private SoundSystem system;

	public static Map<String, String> sounds;
	public static Map<String, String> music;

	public static Audio[] footstep_dirt;
	public static Audio fall_hard;

	public static Audio[] footstep_grass;

	public static Audio fall_soft;

	public static Audio[] footstep_stone;

	public static Audio[] footstep_wood;

	public static Audio explosion;

	public static void loadSounds(SoundSystem system) {
		sounds = new HashMap<>();
		music = new HashMap<>();

		sounds.put("footstep_dirt_0", "footstep_dirt_0.wav");
		sounds.put("footstep_dirt_1", "footstep_dirt_1.wav");

		sounds.put("footstep_grass_0", "footstep_grass_0.wav");
		sounds.put("footstep_grass_1", "footstep_grass_1.wav");

		sounds.put("footstep_wood_0", "footstep_wood_0.wav");
		sounds.put("footstep_wood_1", "footstep_wood_1.wav");

		sounds.put("footstep_stone_0", "footstep_stone_0.wav");
		sounds.put("footstep_stone_1", "footstep_stone_1.wav");

		sounds.put("fall_hard", "fall_hard.wav");
		sounds.put("fall_soft", "fall_soft.wav");
		sounds.put("explosion", "explosion.wav");

		music.put("music1", "alb_esp2.ogg");

		for (Entry<String, String> sound : sounds.entrySet()) {
			system.newSource(false, sound.getKey(), sound.getValue(), false, 0,
					0, 0, SoundSystemConfig.ATTENUATION_NONE, 0);
		}
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
		this.actualizeVolume();
	}

	private String musicPlaying = null;

	public void playMusic(String name, boolean loop) {
		if (music.containsKey(name)) {
			system.backgroundMusic(name, music.get(name), loop);
			musicPlaying = name;
		} else {
			Game.console.out("Music called " + name + " does not exist");
		}
	}

	public void pauseMusic() {
		if (system.playing(musicPlaying)) {
			system.pause(musicPlaying);
		}
	}

	public void playMusic() {
		if (!system.playing(musicPlaying)) {
			system.play(musicPlaying);
		}
	}

	public void stopMusic() {
		if (system.playing(musicPlaying)) {
			system.stop(musicPlaying);
		}
	}

	public void actualizeVolume() {
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

	public void play(org.newdawn.slick.openal.Audio a, float b, float c,
			boolean d) {

	}

	public void play(org.newdawn.slick.openal.Audio[] a, float b, float c,
			boolean d) {

	}

}
