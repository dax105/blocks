package dax.blocks;


public class CoordUtil {
	public static Coord2D relativeToAbsolute(int x, int y, int parentX, int parentY) {
		return new Coord2D(Math.abs(parentX) + Math.abs(x), Math.abs(parentY) + Math.abs(y));
	}
	
	public static Coord2D relativeToAbsolute(Coord2D child, Coord2D parent) {
		return add(child, parent);
	}
	
	public static Coord2D add(Coord2D... coords) {
		Coord2D c = new Coord2D(0, 0);
		for(Coord2D coord : coords) {
			c.set(c.x + coord.x, c.y + coord.y);
		}
		
		return c;
	}
}
