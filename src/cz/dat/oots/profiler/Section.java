package cz.dat.oots.profiler;

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

	public int end() {
		long current = System.nanoTime();
		this.times[this.currentPos] = (current - this.start) / 1000000f;
		int t = (int) (current - this.start);
		this.currentPos = this.currentPos >= Section.MAX_RECORDS - 1 ? 0
				: this.currentPos + 1;
		return t;
	}

	public float[] getTimes() {
		return this.times;
	}

	public float avg() {
		float total = 0;
		for(float f : this.times) {
			total += f;
		}
		return total / (float) Section.MAX_RECORDS;
	}

	public void printSnapshot() {
		System.out.println(this.getSnapshot());
	}

	public String getSnapshot() {
		return null;
	}
}
