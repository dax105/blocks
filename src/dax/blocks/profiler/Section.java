package dax.blocks.profiler;

public class Section {

	public static final int MAX_RECORDS = 500;
	
	public String name;

	public Section(String name) {
		this.name = name;
	}
	
	private long start;

	private float[] times = new float[MAX_RECORDS];
	
	private int currentPos = 0;
	
	public void start() {
		start = System.nanoTime();
	}
	
	public void end() {
		times[currentPos] = (System.nanoTime()-start)/1000000f;
		currentPos = currentPos >= MAX_RECORDS-1 ? 0 : currentPos + 1;
	}
	
	public float[] getTimes() {
		return times;
	}
	
	public float avg() {
		float total = 0;
		for (float f : times) {
			total += f;
		}
		return total/(float)MAX_RECORDS;
	}
	
	public void printSnapshot() {
		System.out.println(getSnapshot());
	}
	
	public String getSnapshot() {
		return null;
	}
}	
