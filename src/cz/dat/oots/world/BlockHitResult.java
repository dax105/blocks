package cz.dat.oots.world;

public class BlockHitResult {

	private int id, x, y, z, ex, ey, ez;
	private boolean hit;

	public BlockHitResult(int id, int x, int y, int z, int ex, int ey, int ez) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
		this.ex = ex;
		this.ey = ey;
		this.ez = ez;
		this.hit = true;
	}
	
	public BlockHitResult() {
		this.hit = false;
	}
	
	public int getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public int getEx() {
		return ex;
	}

	public int getEy() {
		return ey;
	}

	public int getEz() {
		return ez;
	}

	public boolean isHit() {
		return this.hit;
	}
}
