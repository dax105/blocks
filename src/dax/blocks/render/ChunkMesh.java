package dax.blocks.render;

public class ChunkMesh {

	private int[] handles = new int[RenderPass.TOTAL_PASSES];
	private boolean[] present = new boolean[RenderPass.TOTAL_PASSES];
	private IChunkRenderer renderer;

	public ChunkMesh(IChunkRenderer renderer) {
		this.renderer = renderer;
	}

	public void setHandle(int pass, int id) {
		present[pass] = true;
		handles[pass] = id;
	}

	public int getHandle(int pass) {
		return handles[pass];
	}

	public boolean isPresent(int pass) {
		return present[pass];
	}

	public void render(int pass) {
		renderer.render(handles[pass]);
	}

	public void delete() {
		for (int i = 0; i < RenderPass.TOTAL_PASSES; i++) {
			if (present[i]) {
				renderer.delete(handles[i]);
				present[i] = false;
			}
		}
	}

	public boolean isEmpty() {
		for (int i = 0; i < RenderPass.TOTAL_PASSES; i++)
			if (present[i]) 
				return false;
		return true;
	}

}
