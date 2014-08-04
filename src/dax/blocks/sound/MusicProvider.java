package dax.blocks.sound;

import java.util.Random;

import dax.blocks.Game;

public class MusicProvider {
	private String[] gameMusic;
	private String[] menuMusic;

	private String[] menuQueue;
	private String[] gameQueue;

	private int menuIndex = 0;
	private int gameIndex = 0;

	private final int startGameMusicProbability = 10;

	private SoundManager sound;

	Random rand = new Random();

	public MusicProvider(SoundManager soundManager) {
		this.sound = soundManager;
		this.loadMusic();
		this.sortMusic();
	}

	private void loadMusic() {
		SoundManager.music.put("music1", "got_roc.ogg");
		SoundManager.music.put("menu1", "got_main.ogg");
		SoundManager.music.put("menu2", "got_north.ogg");
		SoundManager.music.put("music2", "piano1.ogg");
		SoundManager.music.put("music3", "got_kta.ogg");
		SoundManager.music.put("69", "s.ogg");
	}

	private void sortMusic() {
		gameMusic = new String[] { "music1", "music2", "music3" };
		gameQueue = new String[gameMusic.length];
		for (String music : gameMusic) {
			addMusicToField(gameQueue, music);
		}

		menuMusic = new String[] { "menu1", "menu2" };
		menuQueue = new String[menuMusic.length];
		for (String music : menuMusic) {
			addMusicToField(menuQueue, music);
		}
	}

	private void addMusicToField(String[] queue, String name) {
		int i = rand.nextInt(queue.length);
		if (queue[i] != null) {
			addMusicToField(queue, name);
		} else {
			queue[i] = name;
		}
	}

	boolean isGame = false;
	boolean wasInGame = false;

	public void updateMusic() {
		wasInGame = isGame;
		isGame = Game.getInstance().ingame;

		if (isGame ^ wasInGame) {
			stopMusic();
			sound.updatePlaying();
			return;
		}

		if (!sound.isMusicPlaying()) {
			if(rand.nextInt(500) == 69) {
				sound.playMusic("69", false);
			} else {
			
			if (isGame) {
				if (rand.nextInt(this.startGameMusicProbability) == 1) {
					sound.playMusic(gameQueue[gameIndex], false);

					gameIndex++;

					if (gameIndex > (gameQueue.length - 1)) {
						gameIndex = 0;
					}
				}
			} else {
				sound.playMusic(menuQueue[menuIndex], false);

				menuIndex++;

				if (menuIndex > (menuQueue.length - 1)) {
					menuIndex = 0;
				}
			}
			}

		}

	}

	public void stopMusic() {
		sound.stopMusic();
	}

}
