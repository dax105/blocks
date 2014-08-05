package dax.blocks.util;

public class Coord3D extends Coord2D {
	public int z;
	
	public Coord3D(int x, int y, int z) {
		super(x, y);
		this.z = z;
	}
	
	public void set(int x, int y, int z) {
		super.set(x, y);
		this.z = z;
	}
	
	@Override
	public boolean equals(Object o) {
		Coord3D toCompare = (Coord3D) o;
		return toCompare.x == x && toCompare.y == y && toCompare.z == z;
	}
	
	@Override
	public int hashCode() {
		return x * 31 + y * 17 + z;
	}
	
	@Override
	public String toString() {
		return "X: " + x + " Y: " + y + " Z: " + z;
	}

}
