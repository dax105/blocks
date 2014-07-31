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
			ChunkMesh cm = r.getCm();
			if (!r.isBuilt() || r.isEmpty() || (frustumCulling && !frustum.cuboidInFrustum(x+cm.minX, y+cm.minY, z+cm.minZ, x+1+cm.maxX, y+1+cm.maxY, z+1+cm.maxZ))) {
				iterator.remove();
			}
		}
		
		
		return allChunks;
	}
	
}
