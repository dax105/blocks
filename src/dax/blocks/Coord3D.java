package dax.blocks;

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
		return toCompare.x == this.x && toCompare.y == this.y && toCompare.z == this.z;
	}
	
	@Override
	public int hashCode() {
		return this.x * 31 + this.y * 17 + this.z;
	}
	
	@Override
	public String toString() {
		return "X: " + this.x + " Y: " + this.y + " Z: " + this.z;
	}

}
