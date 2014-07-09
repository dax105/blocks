package dax.blocks.render;

public class RenderChunk {
	
	private boolean dirty = true;
	private boolean generated = false;
	
	private ChunkDisplayList cdl;
	
	public ChunkDisplayList getCdl() {
		return cdl;
	}
	
	public void setCdl(ChunkDisplayList cdl) {
		setGenerated();
		this.cdl = cdl;
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
	public boolean isGenerated() {
		return generated;
	}
	
	public void clear() {
		cdl.delete();
		this.generated = false;
		this.dirty = true;
	}
	
	public void setGenerated() {
		this.generated = true;
		this.dirty = false;
	}	
		
}
