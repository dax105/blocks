package cz.dat.oots.sound;

import cz.dat.oots.Game;

import java.util.Random;

public class MusicProvider {
    private final int startGameMusicProbability = 10;
    private String[] gameMusic;
    private String[] menuMusic;
    private String[] menuQueue;
    private String[] gameQueue;
    private int menuIndex = 0;
    private int gameIndex = 0;
    private SoundManager sound;

    private Random rand = new Random();
    private boolean isGame = false;
    private boolean wasInGame = false;

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
        this.gameMusic = new String[]{"music1", "music2", "music3"};
        this.gameQueue = new String[this.gameMusic.length];
        for (String music : this.gameMusic) {
            this.addMusicToField(this.gameQueue, music);
        }

        this.menuMusic = new String[]{"menu1", "menu2"};
        this.menuQueue = new String[this.menuMusic.length];
        for (String music : this.menuMusic) {
            this.addMusicToField(this.menuQueue, music);
        }
    }

    private void addMusicToField(String[] queue, String name) {
        int i = this.rand.nextInt(queue.length);
        if (queue[i] != null) {
            this.addMusicToField(queue, name);
        } else {
            queue[i] = name;
        }
    }

    public void updateMusic(Game game) {
        this.wasInGame = this.isGame;
        this.isGame = game.getWorldsManager().isInGame();

        if (this.isGame ^ this.wasInGame) {
            this.stopMusic();
            this.sound.updatePlaying();
            return;
        }

        if (!this.sound.isMusicPlaying()) {
            if (this.rand.nextInt(500) == 69) {
                this.sound.playMusic("69", false);
            } else {
                if (this.isGame) {
                    if (this.rand.nextInt(this.startGameMusicProbability) == 1) {
                        this.sound.playMusic(gameQueue[gameIndex], false);

                        this.gameIndex++;

                        if (this.gameIndex > (this.gameQueue.length - 1)) {
                            this.gameIndex = 0;
                        }
                    }
                } else {
                    this.sound.playMusic(this.menuQueue[this.menuIndex], false);

                    this.menuIndex++;

                    if (this.menuIndex > (this.menuQueue.length - 1)) {
                        this.menuIndex = 0;
                    }
                }
            }
        }
    }

    public void stopMusic() {
        this.sound.stopMusic();
    }
}
