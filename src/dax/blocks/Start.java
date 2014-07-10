package dax.blocks;

public class Start {
	public static final String GAME_VERSION = "1.0 GoT beta";
	public static final String GAME_NAME = "Order of the Stone";
	public static void main(String[] args) {
		Game game = new Game();
		Thread t = new Thread(game);
		t.start();
	}
}
