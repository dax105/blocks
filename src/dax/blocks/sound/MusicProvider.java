package dax.blocks.sound;

import java.util.Random;

public class MusicProvider {
	public String[] gameMusic;
	public String[] menuMusic;

	private boolean isMenuPlaying;
	private boolean isGamePlaying;

	private String[] menuQueue;
	private String[] gameQueue;

	private int menuIndex = 0;
	private int gameIndex = 0;

	private final int startGameMusicProbability = 100;

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

	public void startMenuMusic() {
		if (!isMenuPlaying) {
			stopGameMusic();
			this.isMenuPlaying = true;
		}
	}

	public void updateMenuMusic() {
		if(isMenuPlaying) {
			if(!sound.isMusicPlaying()) {
				sound.playMusic(menuQueue[menuIndex], false);
				menuIndex++;
				
				if(menuIndex > (menuQueue.length - 1)) {
					menuIndex = 0;
				}
			}
		}
	}

	public void stopMenuMusic() {
		if (isMenuPlaying) {
			sound.stopMusic();
			this.isMenuPlaying = false;
		}
	}

	public void startGameMusic() {
		if (!isGamePlaying) {
			stopMenuMusic();
			this.isGamePlaying = true;
		}
	}

	public void updateGameMusic() {
		if (isGamePlaying) {
			if (!sound.isMusicPlaying()) {
				int r = rand.nextInt(startGameMusicProbability);
				if (r == 1) {
					sound.playMusic(gameQueue[gameIndex], false);
					gameIndex++;
					
					if(gameIndex > (gameQueue.length - 1)) {
						gameIndex = 0;
					}
				}
			}
		}
	}

	public void stopGameMusic() {
		if (isGamePlaying) {
			sound.stopMusic();
			this.isGamePlaying = false;
		}
	}
}
