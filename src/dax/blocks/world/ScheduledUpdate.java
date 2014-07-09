package dax.blocks.world;

public class ScheduledUpdate {

	public final int x;
	public final int y;
	public final int z;
	public int ticks;
	
	public ScheduledUpdate(int x, int y, int z, int ticks) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.ticks = ticks;
	}
	
}
