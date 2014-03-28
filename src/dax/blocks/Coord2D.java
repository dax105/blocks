package dax.blocks;

public class Coord2D {

	public int x;
	public int y;
	
	public Coord2D(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object o) {
		Coord2D toCompare = (Coord2D) o;
		return toCompare.x == x && toCompare.y == y;
	}
	
	@Override
	public int hashCode() {
		return x * 31 + y;
	}
	
	@Override
	public String toString() {
		return "X: " + x + " Y: " + y;
	}
	
}
