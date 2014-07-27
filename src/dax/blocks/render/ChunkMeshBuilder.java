package dax.blocks.render;

import dax.blocks.Game;
import dax.blocks.block.Block;
import dax.blocks.profiler.Profiler;
import dax.blocks.world.chunk.Chunk;

public class ChunkMeshBuilder {

	public static ChunkMesh genDisplayList(Chunk c, int cy) {
		
		Profiler profiler = Game.getInstance().getProfiler();
		
		profiler.build.start();
		
		if (c != null) {

			IChunkRenderer renderer = Game.getInstance().chunkRenderer;
			ChunkMesh cm = new ChunkMesh(renderer);
			
			int startY = cy * 16;
			int endY = startY + 16;

			int blockOffsetX = c.x * 16;
			int blockOffsetZ = c.z * 16;

			int opaqueCount = 0, transparentCount = 0, translucentCount = 0;
			
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					for (int y = startY; y < endY; y++) {
						int blockID = c.getBlock(x, y, z);

						if (blockID > 0) {
							Block block = Block.getBlock(blockID);

							switch (block.getRenderPass()) {
							case RenderPass.OPAQUE:
								opaqueCount++;
								break;
							case RenderPass.TRANSPARENT:
								transparentCount++;
								break;
							case RenderPass.TRANSLUCENT:
								translucentCount++;
								break;	
							}

						}
					}
				}
			}

			if (opaqueCount == 0 && transparentCount == 0 && translucentCount == 0) {
				profiler.build.end();
				return cm;
			}

			//int listIDs = GL11.glGenLists(listsNeeded);
			//int currentOffset = 0;

			//int attrib = Game.getInstance().renderEngine.blockAttributeID;
			
			if (opaqueCount > 0) {
				//GL11.glNewList(listIDs + currentOffset, GL11.GL_COMPILE);
				renderer.begin();
				cm.setHandle(RenderPass.OPAQUE, renderer.getHandle());
				//currentOffset++;

				// All gl calls to draw the chunk (OPAQUE PASS) should be here

				for (int x = 0; x < 16; x++) {
					for (int z = 0; z < 16; z++) {
						for (int y = startY; y < endY; y++) {
							int blockID = c.getBlock(x, y, z);
							if (blockID > 0) {
								Block block = Block.getBlock(blockID);

								if (block.getRenderPass() == RenderPass.OPAQUE) {
									
									int calcX = x + blockOffsetX;
									int calcY = y;
									int calcZ = z + blockOffsetZ;
									
									block.getRenderer().preRender(c.world, block, calcX, calcY, calcZ);
									block.getRenderer().render(renderer, c.world, block, calcX, calcY, calcZ);
									block.getRenderer().postRender(c.world, block, calcX, calcY, calcZ);

								}
								
								//TODO GL20.glVertexAttrib1f(attrib, 0);
							}
						}
					}
				}

				renderer.end();
				
			}

			if (transparentCount > 0) {
				renderer.begin();
				cm.setHandle(RenderPass.TRANSPARENT, renderer.getHandle());


				// All gl calls to draw the chunk (TRANSPARENT PASS) should be here

				for (int x = 0; x < 16; x++) {
					for (int z = 0; z < 16; z++) {
						for (int y = startY; y < endY; y++) {
							int blockID = c.getBlock(x, y, z);
							if (blockID > 0) {
								Block block = Block.getBlock(blockID);

								if (block.getRenderPass() == RenderPass.TRANSPARENT) {

									int calcX = x + blockOffsetX;
									int calcY = y;
									int calcZ = z + blockOffsetZ;

									block.getRenderer().preRender(c.world, block, calcX, calcY, calcZ);
									block.getRenderer().render(renderer, c.world, block, calcX, calcY, calcZ);
									block.getRenderer().postRender(c.world, block, calcX, calcY, calcZ);

								}
								
								//GL20.glVertexAttrib1f(attrib, 0);
							}
						}
					}
				}
				renderer.end();
			}
			
			if (translucentCount > 0) {
				renderer.begin();
				cm.setHandle(RenderPass.TRANSLUCENT, renderer.getHandle());

				// All gl calls to draw the chunk (TRANSLUCENT PASS) should be here
				for (int x = 0; x < 16; x++) {
					for (int z = 0; z < 16; z++) {
						for (int y = startY; y < endY; y++) {
							int blockID = c.getBlock(x, y, z);
							if (blockID > 0) {
								Block block = Block.getBlock(blockID);

								if (block.getRenderPass() == RenderPass.TRANSLUCENT) {

									int calcX = x + blockOffsetX;
									int calcY = y;
									int calcZ = z + blockOffsetZ;

									block.getRenderer().preRender(c.world, block, calcX, calcY, calcZ);
									block.getRenderer().render(renderer, c.world, block, calcX, calcY, calcZ);
									block.getRenderer().postRender(c.world, block, calcX, calcY, calcZ);

								}
								
								//TODO GL20.glVertexAttrib1f(attrib, 0);
							}
						}
					}
				}
				renderer.end();
			}

			profiler.build.end();
			return cm;

		}
		profiler.build.end();
		return null;
	}

}
