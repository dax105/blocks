package dax.blocks;

public class Start {
	public static void main(String[] args) {
		Base base = new Base();
		Thread t = new Thread(base);
		t.start();
	}
}
