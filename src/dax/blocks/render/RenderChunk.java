package dax.blocks.render;

public class RenderChunk {
	
	private boolean dirty = true;
	private boolean built = false;
	
	private ChunkMesh cm;
	
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
		
}
