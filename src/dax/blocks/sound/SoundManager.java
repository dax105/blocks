package dax.blocks.sound;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import dax.blocks.console.Console;
import paulscode.sound.ListenerData;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

public class SoundManager {

	private SoundSystem system;
	private MusicProvider provider;
	private boolean isWorking = true;
	
	public static Map<String, String> sounds;
	public static Map<String, String> music;

	public static String[] footstep_dirt;
	public static String[] footstep_grass;
	public static String[] footstep_wood;
	public static String[] footstep_stone;

	private String musicPlaying = null;
	private boolean isMusicPlaying = false;
	private boolean isMusicPaused = false;

	private static SoundManager instance;

	public static SoundManager getInstance() {
		if(SoundManager.instance == null) {
			SoundManager.instance = new SoundManager();
		}
		
		return SoundManager.instance;
	}
	
	private static void loadSounds(SoundSystem system) {
		SoundManager.sounds = new HashMap<>();
		SoundManager.music = new HashMap<>();

		SoundManager.sounds.put("footstep_dirt_0", "footstep_dirt_0.wav");
		SoundManager.sounds.put("footstep_dirt_1", "footstep_dirt_1.wav");
		SoundManager.sounds.put("footstep_dirt_3", "footstep_dirt_3.wav");

		SoundManager.sounds.put("footstep_grass_0", "footstep_grass_0.wav");
		SoundManager.sounds.put("footstep_grass_1", "footstep_grass_1.wav");
		SoundManager.sounds.put("footstep_grass_2", "footstep_grass_2.wav");
		SoundManager.sounds.put("footstep_grass_3", "footstep_grass_3.wav");

		SoundManager.sounds.put("footstep_wood_0", "footstep_wood_0.wav");
		SoundManager.sounds.put("footstep_wood_1", "footstep_wood_1.wav");
		SoundManager.sounds.put("footstep_wood_2", "footstep_wood_2.wav");
		SoundManager.sounds.put("footstep_wood_3", "footstep_wood_3.wav");

		SoundManager.sounds.put("footstep_stone_0", "footstep_stone_0.wav");
		SoundManager.sounds.put("footstep_stone_1", "footstep_stone_1.wav");

		SoundManager.sounds.put("fall_hard", "fall_hard.wav");
		SoundManager.sounds.put("fall_soft", "fall_soft.wav");
		SoundManager.sounds.put("explosion", "explosion.wav");
		
		SoundManager.sounds.put("holy_chorus", "hch.wav");


		ListenerData d = system.getListenerData();
		for(Entry<String, String> sound : SoundManager.sounds.entrySet()) {
			system.newSource(false, sound.getKey(), sound.getValue(), false,
					d.position.x, d.position.y, d.position.z,
					SoundSystemConfig.ATTENUATION_NONE, 0);
		}

		SoundManager.sortSounds();
	}

	private static void sortSounds() {
		SoundManager.footstep_dirt = new String[] { "footstep_dirt_0", "footstep_dirt_1", "footstep_dirt_3" };
		SoundManager.footstep_grass = new String[] { "footstep_grass_0", "footstep_grass_1", "footstep_grass_2", "footstep_grass_3" };
		SoundManager.footstep_wood = new String[] { "footstep_wood_0", "footstep_wood_1", "footstep_grass_2", "footstep_grass_3" };
		SoundManager.footstep_stone = new String[] { "footstep_stone_0", "footstep_stone_1" };
	}

	private SoundManager() {
		try {
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", paulscode.sound.codecs.CodecJOrbis.class);
			SoundSystemConfig.setCodec("wav", CodecWav.class);
			SoundSystemConfig.setSoundFilesPackage("dax/blocks/res/sound/");
		} catch (SoundSystemException e) {
			Logger.getGlobal().warning("Sound system cannot be initialized!");
			this.isWorking = false;
			return;
		}
		
		this.system = new SoundSystem();
		SoundManager.loadSounds(this.system);
		this.provider = new MusicProvider(this);
	}
	
	public MusicProvider getMusicProvider() {
		return this.provider;
	}

	public boolean isMusicPlaying() {
		return this.isMusicPlaying;
	}
	
	public void playMusic(String name, boolean loop) {
		if(this.music.get(name) != null) {
			this.stopMusic();
			this.musicPlaying = name;
			this.isMusicPlaying = true;
			if(this.isWorking)
				this.system.backgroundMusic(name, this.music.get(name), loop);
		} else {
			Console.println("Music called " + name + " does not exist");
		}
	}
	
	public void updatePlaying() {
		if(this.musicPlaying == null) {
			this.isMusicPlaying = false;
			return;
		}
		
		if(this.isWorking && !this.system.playing(musicPlaying) && !this.isMusicPaused) {
			this.isMusicPlaying = false;
			return;
		}
		
		this.isMusicPlaying = true;
	}

	public void pauseMusic() {
		if(this.isMusicPlaying && !this.isMusicPaused) {
			if(this.isWorking)
				this.system.pause(this.musicPlaying);
			this.isMusicPaused = true;
		}
	}

	public void playMusic() {
		if(this.musicPlaying != null && this.isMusicPaused) {
			if(this.isWorking)
				this.system.play(this.musicPlaying);
			this.isMusicPaused = false;
		}
	}

	public void stopMusic() {
		if(this.isMusicPlaying) {
			if(this.isWorking)
				this.system.stop(musicPlaying);
			this.musicPlaying = null;
			this.isMusicPlaying = false;
		}
	}

	public void updateVolume(boolean soundOn, float soundVolume) {
		if(this.isWorking) {
			if(soundOn) {
				this.system.setMasterVolume(soundVolume);
				this.playMusic();
			} else {
				this.system.setMasterVolume(0);
				this.pauseMusic();
			}
		}
	}

	public void shutdown() {
		this.stopMusic();
		if(this.isWorking) {
			this.system.cleanup();
		}
	}
	
	public void playSound(String name) {
		this.playSound(name, 1);
	}
	
	public void playSound(String name, float pitch) {
		this.playSound(name, pitch, this.system.getMasterVolume());
	}

	public void playSound(String name, float pitch, float volume) {
		ListenerData d = this.system.getListenerData();
		this.playSound(name, pitch, volume, d.position.x, d.position.y, d.position.z, false);
	}

	public void playSound(String name, float pitch, float volume, float x,
			float y, float z, boolean loop) {
		if(this.isWorking && this.sounds.get(name) != null) {
			this.system.setVolume(name, volume);
			this.system.setPitch(name, pitch);
			this.system.setPosition(name, x, y, z);
			this.system.setLooping(name, loop);
			
			if(this.system.playing(name))
				this.system.stop(name);
			
			this.system.play(name);
			
			ListenerData d = this.system.getListenerData();
			this.system.setPosition(name, d.position.x, d.position.y, d.position.z);
		}
	}

	public void playSound(String[] names) {
		this.playSound(names, 1);
	}
	
	public void playSound(String[] names, float pitch) {
		this.playSound(names, pitch, this.system.getMasterVolume());
	}

	public void playSound(String[] names, float pitch, float volume) {
		ListenerData d = this.system.getListenerData();
		this.playSound(names, pitch, volume, d.position.x, d.position.y, d.position.z);
	}

	public void playSound(String[] names, float pitch, float volume, float x, float y, float z) {
		int index = this.system.randomNumberGenerator.nextInt(names.length);
		this.playSound(names[index], pitch, volume, x, y, z, false);
	}
	
	public SoundSystem getSoundSystem() {
		return this.system;
	}
}
