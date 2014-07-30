package dax.blocks.render;

public class RenderChunk {
	
	private boolean dirty = true;
	private boolean built = false;
	
	private int x, y, z;
	
	private ChunkMesh cm;
	
	public RenderChunk(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public ChunkMesh getCm() {
		return cm;
	}
	
	public void setCm(ChunkMesh cm) {
		setGenerated();
		this.cm = cm;
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
	public boolean isEmpty() {
		return this.cm.isEmpty();
	}
	
	public boolean isBuilt() {
		return built;
	}
	
	public void clear() {
		cm.delete();
		this.built = false;
		this.dirty = true;
	}
	
	public void setGenerated() {
		this.built = true;
		this.dirty = false;
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
		
}
