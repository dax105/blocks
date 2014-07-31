package dax.blocks.render;

import java.util.Iterator;
import java.util.List;

public class ChunkCull {

	public static List<RenderChunk> cull(List<RenderChunk> allChunks, Frustum frustum, boolean frustumCulling, boolean advancedCulling) {
		Iterator<RenderChunk> iterator = allChunks.iterator();
		while (iterator.hasNext()) {
			RenderChunk r = iterator.next();		
			int x = r.getX() * 16;
			int y = r.getY() * 16;
			int z = r.getZ() * 16;			
			if (!r.isBuilt() || r.isEmpty() || (frustumCulling && !frustum.cuboidInFrustum(x, y, z, x+16, y+16, z+16))) {
				iterator.remove();
			}
		}
		
		
		return allChunks;
	}
	
}
