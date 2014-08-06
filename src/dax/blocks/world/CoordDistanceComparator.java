package dax.blocks.world;

import java.util.Comparator;

import dax.blocks.Coord2D;

public class CoordDistanceComparator implements Comparator<Coord2D> {

	int cx, cy;
	
	public CoordDistanceComparator(int x, int y) {
		this.cx = x;
		this.cy = y;
	}

	@Override
	public int compare(Coord2D arg0, Coord2D arg1) {
		float distSquared0 = Math.abs(arg0.x - this.cx) * Math.abs(arg0.x - this.cx) + 
			Math.abs(arg0.y - this.cy) * Math.abs(arg0.y - this.cy);
		int dist0 = (int) Math.sqrt(distSquared0);
		
		float distSquared1 = Math.abs(arg1.x - this.cx) * Math.abs(arg1.x - this.cx) + 
			Math.abs(arg1.y - this.cy) * Math.abs(arg1.y - cy);
		int dist1 = (int) Math.sqrt(distSquared1);
		
		return dist0 - dist1;
	}

}
