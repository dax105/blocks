package dax.blocks;

public class Start {

	public static final String GAME_VERSION = "1.3 GoT beta #212";
	public static final String GAME_NAME = "Order of the Stone";

	public static void main(String[] args) {
		for(String s : args)
			System.out.println("Argument " + s + " accepted");
		
		Game game = Game.getInstance();
		if(args.length > 2) {
			game.doLogin(args[0], args[1], args[2]);
		} else {
			game.dummyLogin();
		}
		
		Thread t = new Thread(game);
		t.start();
	}
}
