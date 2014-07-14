package dax.blocks.sound;

import java.util.Random;

import dax.blocks.Game;

public class MusicProvider {
	public String[] gameMusic;
	public String[] menuMusic;

	// private boolean isMenuPlaying;
	// private boolean isGamePlaying;

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
	}

	private void sortMusic() {
		gameMusic = new String[] { "music1" };
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

	public void stopMusic() {
		sound.stopMusic();
	}

}
