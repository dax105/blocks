package dax.blocks.world;

public class ScheduledUpdate {

	public final int type;
	public int ticks;

	public ScheduledUpdate(int type, int ticks) {
		this.type = type;
		this.ticks = ticks;
	}

	@Override
	public int hashCode() {
		return type;
	}

	@Override
	public boolean equals(Object obj) {
		ScheduledUpdate u = (ScheduledUpdate) obj;
		return this.type == u.type && this.ticks == u.ticks;
	}

}
