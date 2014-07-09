package dax.blocks.world;

public class SchleduledUpdate {

	public final int x;
	public final int y;
	public final int z;
	public int ticks;
	
	public SchleduledUpdate(int x, int y, int z, int ticks) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.ticks = ticks;
	}
	
}
