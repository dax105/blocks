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
		return this.cm;
	}
	
	public void setCm(ChunkMesh cm) {
		this.setGenerated();
		this.cm = cm;
	}
	
	public boolean isDirty() {
		return this.dirty;
	}
	
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
	public boolean isEmpty() {
		return this.cm.isEmpty();
	}
	
	public boolean isBuilt() {
		return this.built;
	}
	
	public void clear() {
		this.cm.delete();
		this.built = false;
		this.dirty = true;
	}
	
	public void setGenerated() {
		this.built = true;
		this.dirty = false;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getZ() {
		return this.z;
	}	
}
