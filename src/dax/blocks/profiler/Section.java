package dax.blocks.profiler;

public class Section {

	public static final int MAX_RECORDS = 500;
	
	public String name;
	private long start;
	private float[] times = new float[MAX_RECORDS];
	private int currentPos = 0;

	public Section(String name) {
		this.name = name;
	}
	
	public void start() {
		this.start = System.nanoTime();
	}
	
	public void end() {
		this.times[this.currentPos] = (System.nanoTime() - this.start) / 1000000f;
		this.currentPos = this.currentPos >= Section.MAX_RECORDS-1 ? 0 : this.currentPos + 1;
	}
	
	public float[] getTimes() {
		return this.times;
	}
	
	public float avg() {
		float total = 0;
		for (float f : this.times) {
			total += f;
		}
		return total / (float)Section.MAX_RECORDS;
	}
	
	public void printSnapshot() {
		System.out.println(this.getSnapshot());
	}
	
	public String getSnapshot() {
		return null;
	}
}	
