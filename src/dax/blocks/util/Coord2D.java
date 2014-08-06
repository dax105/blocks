package dax.blocks.util;

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
		return toCompare.x == this.x && toCompare.y == this.y;
	}
	
	@Override
	public int hashCode() {
		return this.x * 31 + this.y;
	}
	
	@Override
	public String toString() {
		return "X: " + this.x + " Y: " + this.y;
	}
	
}
