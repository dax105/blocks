package cz.dat.oots;

public class Start {

	public static final String GAME_VERSION = "1.4 beta #254";
	public static final String GAME_NAME = "Order of the Stone";

	public static void main(String[] args) {
		Game game = new Game();
		
		if(args.length > 2) {
			game.doLogin(args[0], args[1], args[2]);
		} else {
			game.dummyLogin();
		}
		
		Thread t = new Thread(game);
		t.start();
	}
}
