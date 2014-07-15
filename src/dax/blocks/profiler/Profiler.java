package dax.blocks.profiler;

public class Profiler {
	
	public static final int MAX_RECORDS = 500;
	
	private Section mainSection = new Section("main");
	
	private long tickStart;
	private long renderStart;

	private float[] tickTimes = new float[MAX_RECORDS];
	private float[] renderTimes = new float[MAX_RECORDS];
	
	private int currentTickPos = 0;
	private int currentRenderPos = 0;
	
	public void startTick() {
		tickStart = System.nanoTime();
	}
	
	public void endTick() {
		tickTimes[currentTickPos] = (System.nanoTime()-tickStart)/1000000f;
		currentTickPos = currentTickPos >= MAX_RECORDS-1 ? 0 : currentTickPos + 1;
		tickTimes[currentTickPos] = 0;
	}
	
	public void startRender() {
		renderStart = System.nanoTime();
	}
	
	public void endRender() {
		renderTimes[currentRenderPos] = (System.nanoTime()-renderStart)/1000000f;
		currentRenderPos = currentRenderPos >= MAX_RECORDS-1 ? 0 : currentRenderPos + 1;
		renderTimes[currentRenderPos] = 0;
	}
	
	public float[] getTickTimes() {
		return tickTimes;
	}
	
	public float[] getRenderTimes() {
		return renderTimes;
	}
	
	public float avgTick() {
		float total = 0;
		for (float f : tickTimes) {
			total += f;
		}
		return total/(float)MAX_RECORDS;
	}
	
	public float avgRender() {
		float total = 0;
		for (float f : renderTimes) {
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
