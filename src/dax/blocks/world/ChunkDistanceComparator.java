package dax.blocks.world;

import java.util.Comparator;

import dax.blocks.world.chunk.Chunk;

public class ChunkDistanceComparator implements Comparator<Chunk> {
	
	private boolean backToFront = true;
	
	public void setBackToFront() {
		backToFront = true;
	}
	
	public void setFrontToBack() {
		backToFront = false;
	}
	
	@Override
	public int compare(Chunk arg0, Chunk arg1) {
		return backToFront ? ((int) arg1.getDistanceToPlayer() - (int) arg0.getDistanceToPlayer()) : ((int) arg0.getDistanceToPlayer() - (int) arg1.getDistanceToPlayer());
	}
}