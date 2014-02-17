import dax.blocks.Game;

public class Start {
	public static void main(String[] args) {
		Game game = new Game();
		Thread t = new Thread(game);
		t.start();
	}
}
