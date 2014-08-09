package dax.blocks.world.generator;

import dax.blocks.util.Coord2D;
import dax.blocks.world.ChunkProvider;
import dax.blocks.world.chunk.Chunk;

public class ChunkLoaderThread implements Runnable {

	private ChunkProvider provider;
	
	public ChunkLoaderThread(ChunkProvider provider) {
		this.provider = provider;
	}
	
	@Override
	public void run() {
		boolean work = true;
		
		while(work) {
			Coord2D coord = provider.loadNeeded.poll();
			if (coord != null) {
				provider.loadingChunks.add(coord);
				Chunk c = provider.getChunk(coord.x, coord.y);
				if (c != null) {
					provider.loaded.putIfAbsent(coord, c);
				}
				provider.loadingChunks.remove(coord);
			}
		}
	}
	
}
