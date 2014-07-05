package dax.blocks;

import java.io.IOException;
import java.util.Random;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class SoundManager {
	private static Random rand = new Random();
	
	public static Audio footstep_grass[];
	public static Audio footstep_wood[];
	public static Audio footstep_dirt[];
	public static Audio footstep_stone[];
	
	public static Audio fall_soft;
	public static Audio fall_hard;
	
	public static Audio explosion;
	
	public static Audio loadSound(String path) {
	try {
			Audio sound;
			sound = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream(path));
			Game.console.out("Successfully loaded sound from " + path);
			return sound;
		} catch (IOException e) {
			System.err.println("Can't load sound from " + path + ", perhaps the file doesn't exist?");
			System.exit(1);
		}
		return null;
	}
	
	public static void load() {
		explosion = loadSound("dax/blocks/res/sound/explosion.wav");
		
		fall_soft = loadSound("dax/blocks/res/sound/fall_soft.wav");
		fall_hard = loadSound("dax/blocks/res/sound/fall_hard.wav");
		
		footstep_grass = new Audio[4];
		footstep_grass[0] = loadSound("dax/blocks/res/sound/footstep_grass_0.wav");
		footstep_grass[1] = loadSound("dax/blocks/res/sound/footstep_grass_1.wav");
		footstep_grass[2] = loadSound("dax/blocks/res/sound/footstep_grass_2.wav");
		footstep_grass[3] = loadSound("dax/blocks/res/sound/footstep_grass_3.wav");
		
		footstep_wood = new Audio[4];
		footstep_wood[0] = loadSound("dax/blocks/res/sound/footstep_wood_0.wav");
		footstep_wood[1] = loadSound("dax/blocks/res/sound/footstep_wood_1.wav");
		footstep_wood[2] = loadSound("dax/blocks/res/sound/footstep_wood_2.wav");
		footstep_wood[3] = loadSound("dax/blocks/res/sound/footstep_wood_3.wav");
		
		footstep_dirt = new Audio[3];
		footstep_dirt[0] = loadSound("dax/blocks/res/sound/footstep_dirt_0.wav");
		footstep_dirt[1] = loadSound("dax/blocks/res/sound/footstep_dirt_1.wav");
		//footstep_dirt[2] = loadSound("dax/blocks/res/sound/footstep_dirt_2.wav");
		footstep_dirt[2] = loadSound("dax/blocks/res/sound/footstep_dirt_3.wav");
		
		footstep_stone = new Audio[2];
		footstep_stone[0] = loadSound("dax/blocks/res/sound/footstep_stone_0.wav");
		footstep_stone[1] = loadSound("dax/blocks/res/sound/footstep_stone_1.wav");

	}
	
	public static void play(Audio[] soundVariants, float pitch, float volume) {
		if (!Game.settings.sound.getValue()) return;
		int index = rand.nextInt(soundVariants.length);
		soundVariants[index].playAsSoundEffect(pitch, volume, false);
	}
	
	public static void play(Audio sound, float pitch, float volume) {
		if (!Game.settings.sound.getValue()) return;
		sound.playAsSoundEffect(pitch, volume, false);
	}

}
