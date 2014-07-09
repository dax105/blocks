package dax.blocks.sound;

import java.io.IOException;
import java.util.Random;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;

import dax.blocks.Game;

public class SoundManager {
	private Random rand = new Random();

	public static Audio footstep_grass[];
	public static Audio footstep_wood[];
	public static Audio footstep_dirt[];
	public static Audio footstep_stone[];
	public static Audio fall_soft;
	public static Audio fall_hard;
	public static Audio explosion;
	public static Audio music_piano;

	public static Audio loadSound(String path) {
		return loadSound(path, false);
	}
	
	public static Audio loadSound(String path, boolean loadAsStream) {
		try {
			Audio sound;
			sound = loadAsStream ? AudioLoader.getStreamingAudio("OGG",
					ResourceLoader.getResource(path)) : AudioLoader.getAudio(
					"WAV", ResourceLoader.getResourceAsStream(path));
			Game.console.out("Successfully loaded sound from " + path);
			return sound;
		} catch (IOException e) {
			// e.printStackTrace();
			System.err.println("Can't load sound from " + path
					+ ", perhaps the file doesn't exist?");
			System.exit(1);
		}
		return null;
	}

	public static void load() {
		explosion = loadSound("dax/blocks/res/sound/explosion.wav", false);

		fall_soft = loadSound("dax/blocks/res/sound/fall_soft.wav", false);
		fall_hard = loadSound("dax/blocks/res/sound/fall_hard.wav", false);

		footstep_grass = new Audio[4];
		footstep_grass[0] = loadSound(
				"dax/blocks/res/sound/footstep_grass_0.wav", false);
		footstep_grass[1] = loadSound(
				"dax/blocks/res/sound/footstep_grass_1.wav", false);
		footstep_grass[2] = loadSound(
				"dax/blocks/res/sound/footstep_grass_2.wav", false);
		footstep_grass[3] = loadSound(
				"dax/blocks/res/sound/footstep_grass_3.wav", false);

		footstep_wood = new Audio[4];
		footstep_wood[0] = loadSound(
				"dax/blocks/res/sound/footstep_wood_0.wav", false);
		footstep_wood[1] = loadSound(
				"dax/blocks/res/sound/footstep_wood_1.wav", false);
		footstep_wood[2] = loadSound(
				"dax/blocks/res/sound/footstep_wood_2.wav", false);
		footstep_wood[3] = loadSound(
				"dax/blocks/res/sound/footstep_wood_3.wav", false);

		footstep_dirt = new Audio[3];
		footstep_dirt[0] = loadSound(
				"dax/blocks/res/sound/footstep_dirt_0.wav", false);
		footstep_dirt[1] = loadSound(
				"dax/blocks/res/sound/footstep_dirt_1.wav", false);
		// footstep_dirt[2] =
		// loadSound("dax/blocks/res/sound/footstep_dirt_2.wav");
		footstep_dirt[2] = loadSound(
				"dax/blocks/res/sound/footstep_dirt_3.wav", false);

		footstep_stone = new Audio[2];
		footstep_stone[0] = loadSound(
				"dax/blocks/res/sound/footstep_stone_0.wav", false);
		footstep_stone[1] = loadSound(
				"dax/blocks/res/sound/footstep_stone_1.wav", false);

		music_piano = loadSound("dax/blocks/res/sound/alb_esp2.ogg", true);
	}

	public SoundManager() {
		SoundManager.load();
	}
	
	private Audio actualMusic;
	private boolean isPlaying_;
	private boolean shouldRepeat;
	private boolean isPaused = false;
	private float pausePosition;
	
	public boolean isPlaying() {
		return isPlaying_;
	}

	public void actualizeVolume() {
		actualizeVolume(Game.settings.sound_volume.getValue());
	}

	public void actualizeVolume(float volume) {
		if (actualMusic != null && actualMusic.isPlaying()) {
			if (Game.settings.sound.getValue()) {
				SoundStore.get().setMusicVolume(volume);
			} else {
				pause();
			}
		} else if (actualMusic != null && !actualMusic.isPlaying()
				 && Game.settings.sound.getValue()) {
			resume();
		}
	}
	
	public void pause() {
		if(!isPaused && isPlaying_) {
			this.pausePosition = actualMusic.getPosition();
			this.stopPlaying();
			this.isPaused = true;
		}
	}
	
	public void resume() {
		if(isPaused && !isPlaying_) {
			this.playMusic(actualMusic, shouldRepeat);
			this.actualMusic.setPosition(pausePosition);
			this.isPaused = false;
		}
	}

	public void stopPlaying() {
		if (actualMusic != null && actualMusic.isPlaying()) {
			actualMusic.stop();
			this.isPlaying_ = false;
		}
	}

	public void playMusic(Audio music, boolean repeat) {
		playMusic(music, 1f, Game.settings.sound_volume.getValue(), repeat);
	}

	public void playMusic(Audio music, float pitch, float volume, boolean repeat) {
		if (!Game.settings.sound.getValue())
			return;
		stopPlaying();

		this.actualMusic = music;
		this.isPlaying_ = true;
		this.shouldRepeat = repeat;

		music.playAsMusic(pitch, volume, repeat);
		actualizeVolume();
	}

	public void play(Audio[] soundVariants) {
		play(soundVariants, 1f, Game.settings.sound_volume.getValue(), false);
	}

	public void play(Audio[] soundVariants, float pitch, float volume,
			boolean repeat) {
		if (!Game.settings.sound.getValue())
			return;
		int index = rand.nextInt(soundVariants.length);
		soundVariants[index].playAsSoundEffect(pitch, volume, repeat);
	}

	public void play(Audio sound) {
		play(sound, 1f, Game.settings.sound_volume.getValue(), false);
	}

	public void play(Audio sound, float pitch, float volume, boolean repeat) {
		if (!Game.settings.sound.getValue())
			return;
		sound.playAsSoundEffect(pitch, volume, repeat);
	}

}
