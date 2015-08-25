package cz.dat.oots.world;

import java.util.Comparator;

import cz.dat.oots.world.chunk.Chunk;

public class ChunkDistanceComparator implements Comparator<Chunk> {

	private boolean backToFront = true;

	public void setBackToFront() {
		this.backToFront = true;
	}

	public void setFrontToBack() {
		this.backToFront = false;
	}

	@Override
	public int compare(Chunk arg0, Chunk arg1) {
		return this.backToFront ? ((int) arg1.getDistanceToPlayer() - (int) arg0
				.getDistanceToPlayer())
				: ((int) arg0.getDistanceToPlayer() - (int) arg1
						.getDistanceToPlayer());
	}
}
