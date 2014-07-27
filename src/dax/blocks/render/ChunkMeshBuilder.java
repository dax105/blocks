package dax.blocks.render;

import org.lwjgl.opengl.GL11;

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

									int blockIdAbove = c.world.getBlock(calcX, calcY + 1, calcZ);
									Block blockAbove = Block.getBlock(blockIdAbove);

									int blockIdBelow = c.world.getBlock(calcX, calcY - 1, calcZ);
									Block blockBelow = Block.getBlock(blockIdBelow);

									int blockIdInFront = c.world.getBlock(calcX, calcY, calcZ + 1);
									Block blockInFront = Block.getBlock(blockIdInFront);

									int blockIdBehind = c.world.getBlock(calcX, calcY, calcZ - 1);
									Block blockBehind = Block.getBlock(blockIdBehind);

									int blockIdOnRight = c.world.getBlock(calcX + 1, calcY, calcZ);
									Block blockOnRight = Block.getBlock(blockIdOnRight);

									int blockIdOnLeft = c.world.getBlock(calcX - 1, calcY, calcZ);
									Block blockOnLeft = Block.getBlock(blockIdOnLeft);

									//TODO GL20.glVertexAttrib1f(attrib, blockID);
									
									boolean visible = false;
									
									if (blockIdAbove == 0 || !blockAbove.isOpaque()) {
										boolean xnzn = c.world.isOccluder(calcX - 1, calcY + 1, calcZ - 1);
										boolean zn = c.world.isOccluder(calcX, calcY + 1, calcZ - 1);
										boolean xpzn = c.world.isOccluder(calcX + 1, calcY + 1, calcZ - 1);
										boolean xn = c.world.isOccluder(calcX - 1, calcY + 1, calcZ);
										boolean xp = c.world.isOccluder(calcX + 1, calcY + 1, calcZ);
										boolean xnzp = c.world.isOccluder(calcX - 1, calcY + 1, calcZ + 1);
										boolean zp = c.world.isOccluder(calcX, calcY + 1, calcZ + 1);
										boolean xpzp = c.world.isOccluder(calcX + 1, calcY + 1, calcZ + 1);
										
										visible = true;

										block.renderTop(calcX, calcY, calcZ, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, c.world);
									}

									if (blockIdBelow == 0 || !blockBelow.isOpaque()) {
										boolean xnzn = c.world.isOccluder(calcX - 1, calcY - 1, calcZ - 1);
										boolean zn = c.world.isOccluder(calcX, calcY - 1, calcZ - 1);
										boolean xpzn = c.world.isOccluder(calcX + 1, calcY - 1, calcZ - 1);
										boolean xn = c.world.isOccluder(calcX - 1, calcY - 1, calcZ);
										boolean xp = c.world.isOccluder(calcX + 1, calcY - 1, calcZ);
										boolean xnzp = c.world.isOccluder(calcX - 1, calcY - 1, calcZ + 1);
										boolean zp = c.world.isOccluder(calcX, calcY - 1, calcZ + 1);
										boolean xpzp = c.world.isOccluder(calcX + 1, calcY - 1, calcZ + 1);
										
										visible = true;

										block.renderBottom(calcX, calcY, calcZ, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, c.world);
									}

									if (blockIdInFront == 0 || !blockInFront.isOpaque()) {
										boolean xnzn = c.world.isOccluder(calcX - 1, calcY - 1, calcZ + 1);
										boolean zn = c.world.isOccluder(calcX, calcY - 1, calcZ + 1);
										boolean xpzn = c.world.isOccluder(calcX + 1, calcY - 1, calcZ + 1);
										boolean xn = c.world.isOccluder(calcX - 1, calcY, calcZ + 1);
										boolean xp = c.world.isOccluder(calcX + 1, calcY, calcZ + 1);
										boolean xnzp = c.world.isOccluder(calcX - 1, calcY + 1, calcZ + 1);
										boolean zp = c.world.isOccluder(calcX, calcY + 1, calcZ + 1);
										boolean xpzp = c.world.isOccluder(calcX + 1, calcY + 1, calcZ + 1);
										
										visible = true;

										block.renderFront(calcX, calcY, calcZ, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, c.world);
									}

									if (blockIdBehind == 0 || !blockBehind.isOpaque()) {
										boolean xnzn = c.world.isOccluder(calcX - 1, calcY - 1, calcZ - 1);
										boolean zn = c.world.isOccluder(calcX, calcY - 1, calcZ - 1);
										boolean xpzn = c.world.isOccluder(calcX + 1, calcY - 1, calcZ - 1);
										boolean xn = c.world.isOccluder(calcX - 1, calcY, calcZ - 1);
										boolean xp = c.world.isOccluder(calcX + 1, calcY, calcZ - 1);
										boolean xnzp = c.world.isOccluder(calcX - 1, calcY + 1, calcZ - 1);
										boolean zp = c.world.isOccluder(calcX, calcY + 1, calcZ - 1);
										boolean xpzp = c.world.isOccluder(calcX + 1, calcY + 1, calcZ - 1);
										
										visible = true;

										block.renderBack(calcX, calcY, calcZ, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, c.world);
									}

									if (blockIdOnRight == 0 || !blockOnRight.isOpaque()) {
										boolean xnzn = c.world.isOccluder(calcX + 1, calcY + 1, calcZ - 1);
										boolean zn = c.world.isOccluder(calcX + 1, calcY + 1, calcZ);
										boolean xpzn = c.world.isOccluder(calcX + 1, calcY + 1, calcZ + 1);
										boolean xn = c.world.isOccluder(calcX + 1, calcY, calcZ - 1);
										boolean xp = c.world.isOccluder(calcX + 1, calcY, calcZ + 1);
										boolean xnzp = c.world.isOccluder(calcX + 1, calcY - 1, calcZ - 1);
										boolean zp = c.world.isOccluder(calcX + 1, calcY - 1, calcZ);
										boolean xpzp = c.world.isOccluder(calcX + 1, calcY - 1, calcZ + 1);
										
										visible = true;

										block.renderRight(calcX, calcY, calcZ, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, c.world);
									}

									if (blockIdOnLeft == 0 || !blockOnLeft.isOpaque()) {
										boolean xnzn = c.world.isOccluder(calcX - 1, calcY + 1, calcZ - 1);
										boolean zn = c.world.isOccluder(calcX - 1, calcY + 1, calcZ);
										boolean xpzn = c.world.isOccluder(calcX - 1, calcY + 1, calcZ + 1);
										boolean xn = c.world.isOccluder(calcX - 1, calcY, calcZ - 1);
										boolean xp = c.world.isOccluder(calcX - 1, calcY, calcZ + 1);
										boolean xnzp = c.world.isOccluder(calcX - 1, calcY - 1, calcZ - 1);
										boolean zp = c.world.isOccluder(calcX - 1, calcY - 1, calcZ);
										boolean xpzp = c.world.isOccluder(calcX - 1, calcY - 1, calcZ + 1);
										
										visible = true;

										block.renderLeft(calcX, calcY, calcZ, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, c.world);
									}
									
									if (visible) {
										block.renderIndependent(calcX, calcY, calcZ, c.world);
									}			

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

									int blockIdAbove = c.world.getBlock(calcX, calcY + 1, calcZ);
									Block blockAbove = Block.getBlock((byte) blockIdAbove);

									int blockIdBelow = c.world.getBlock(calcX, calcY - 1, calcZ);
									Block blockBelow = Block.getBlock((byte) blockIdBelow);

									int blockIdInFront = c.world.getBlock(calcX, calcY, calcZ + 1);
									Block blockInFront = Block.getBlock((byte) blockIdInFront);

									int blockIdBehind = c.world.getBlock(calcX, calcY, calcZ - 1);
									Block blockBehind = Block.getBlock((byte) blockIdBehind);

									int blockIdOnRight = c.world.getBlock(calcX + 1, calcY, calcZ);
									Block blockOnRight = Block.getBlock((byte) blockIdOnRight);

									int blockIdOnLeft = c.world.getBlock(calcX - 1, calcY, calcZ);
									Block blockOnLeft = Block.getBlock((byte) blockIdOnLeft);

									//GL20.glVertexAttrib1f(attrib, blockID);
									
									boolean visible = false;
									
									if (blockIdAbove == 0 || !blockAbove.isOpaque() && !(blockID == blockIdAbove && blockAbove.shouldCullSame())) {
										boolean xnzn = c.world.isOccluder(calcX - 1, calcY + 1, calcZ - 1);
										boolean zn = c.world.isOccluder(calcX, calcY + 1, calcZ - 1);
										boolean xpzn = c.world.isOccluder(calcX + 1, calcY + 1, calcZ - 1);
										boolean xn = c.world.isOccluder(calcX - 1, calcY + 1, calcZ);
										boolean xp = c.world.isOccluder(calcX + 1, calcY + 1, calcZ);
										boolean xnzp = c.world.isOccluder(calcX - 1, calcY + 1, calcZ + 1);
										boolean zp = c.world.isOccluder(calcX, calcY + 1, calcZ + 1);
										boolean xpzp = c.world.isOccluder(calcX + 1, calcY + 1, calcZ + 1);
										
										visible = true;

										block.renderTop(calcX, calcY, calcZ, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, c.world);
									}

									if (blockIdBelow == 0 || !blockBelow.isOpaque() && !(blockID == blockIdBelow && blockBelow.shouldCullSame())) {
										boolean xnzn = c.world.isOccluder(calcX - 1, calcY - 1, calcZ - 1);
										boolean zn = c.world.isOccluder(calcX, calcY - 1, calcZ - 1);
										boolean xpzn = c.world.isOccluder(calcX + 1, calcY - 1, calcZ - 1);
										boolean xn = c.world.isOccluder(calcX - 1, calcY - 1, calcZ);
										boolean xp = c.world.isOccluder(calcX + 1, calcY - 1, calcZ);
										boolean xnzp = c.world.isOccluder(calcX - 1, calcY - 1, calcZ + 1);
										boolean zp = c.world.isOccluder(calcX, calcY - 1, calcZ + 1);
										boolean xpzp = c.world.isOccluder(calcX + 1, calcY - 1, calcZ + 1);
										
										visible = true;

										block.renderBottom(calcX, calcY, calcZ, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, c.world);
									}

									if (blockIdInFront == 0 || !blockInFront.isOpaque() && !(blockID == blockIdInFront && blockInFront.shouldCullSame())) {
										boolean xnzn = c.world.isOccluder(calcX - 1, calcY - 1, calcZ + 1);
										boolean zn = c.world.isOccluder(calcX, calcY - 1, calcZ + 1);
										boolean xpzn = c.world.isOccluder(calcX + 1, calcY - 1, calcZ + 1);
										boolean xn = c.world.isOccluder(calcX - 1, calcY, calcZ + 1);
										boolean xp = c.world.isOccluder(calcX + 1, calcY, calcZ + 1);
										boolean xnzp = c.world.isOccluder(calcX - 1, calcY + 1, calcZ + 1);
										boolean zp = c.world.isOccluder(calcX, calcY + 1, calcZ + 1);
										boolean xpzp = c.world.isOccluder(calcX + 1, calcY + 1, calcZ + 1);
										
										visible = true;

										block.renderFront(calcX, calcY, calcZ, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, c.world);
									}

									if (blockIdBehind == 0 || !blockBehind.isOpaque() && !(blockID == blockIdBehind && blockBehind.shouldCullSame())) {
										boolean xnzn = c.world.isOccluder(calcX - 1, calcY - 1, calcZ - 1);
										boolean zn = c.world.isOccluder(calcX, calcY - 1, calcZ - 1);
										boolean xpzn = c.world.isOccluder(calcX + 1, calcY - 1, calcZ - 1);
										boolean xn = c.world.isOccluder(calcX - 1, calcY, calcZ - 1);
										boolean xp = c.world.isOccluder(calcX + 1, calcY, calcZ - 1);
										boolean xnzp = c.world.isOccluder(calcX - 1, calcY + 1, calcZ - 1);
										boolean zp = c.world.isOccluder(calcX, calcY + 1, calcZ - 1);
										boolean xpzp = c.world.isOccluder(calcX + 1, calcY + 1, calcZ - 1);
										
										visible = true;

										block.renderBack(calcX, calcY, calcZ, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, c.world);
									}

									if (blockIdOnRight == 0 || !blockOnRight.isOpaque() && !(blockID == blockIdOnRight && blockOnRight.shouldCullSame())) {
										boolean xnzn = c.world.isOccluder(calcX + 1, calcY + 1, calcZ - 1);
										boolean zn = c.world.isOccluder(calcX + 1, calcY + 1, calcZ);
										boolean xpzn = c.world.isOccluder(calcX + 1, calcY + 1, calcZ + 1);
										boolean xn = c.world.isOccluder(calcX + 1, calcY, calcZ - 1);
										boolean xp = c.world.isOccluder(calcX + 1, calcY, calcZ + 1);
										boolean xnzp = c.world.isOccluder(calcX + 1, calcY - 1, calcZ - 1);
										boolean zp = c.world.isOccluder(calcX + 1, calcY - 1, calcZ);
										boolean xpzp = c.world.isOccluder(calcX + 1, calcY - 1, calcZ + 1);
										
										visible = true;

										block.renderRight(calcX, calcY, calcZ, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, c.world);
									}

									if (blockIdOnLeft == 0 || !blockOnLeft.isOpaque() && !(blockID == blockIdOnLeft && blockOnLeft.shouldCullSame())) {
										boolean xnzn = c.world.isOccluder(calcX - 1, calcY + 1, calcZ - 1);
										boolean zn = c.world.isOccluder(calcX - 1, calcY + 1, calcZ);
										boolean xpzn = c.world.isOccluder(calcX - 1, calcY + 1, calcZ + 1);
										boolean xn = c.world.isOccluder(calcX - 1, calcY, calcZ - 1);
										boolean xp = c.world.isOccluder(calcX - 1, calcY, calcZ + 1);
										boolean xnzp = c.world.isOccluder(calcX - 1, calcY - 1, calcZ - 1);
										boolean zp = c.world.isOccluder(calcX - 1, calcY - 1, calcZ);
										boolean xpzp = c.world.isOccluder(calcX - 1, calcY - 1, calcZ + 1);
										
										visible = true;

										block.renderLeft(calcX, calcY, calcZ, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, c.world);
									}
									
									if (visible) {
										block.renderIndependent(calcX, calcY, calcZ, c.world);
									}

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

									int blockIdAbove = c.world.getBlock(calcX, calcY + 1, calcZ);
									Block blockAbove = Block.getBlock((byte) blockIdAbove);

									int blockIdBelow = c.world.getBlock(calcX, calcY - 1, calcZ);
									Block blockBelow = Block.getBlock((byte) blockIdBelow);

									int blockIdInFront = c.world.getBlock(calcX, calcY, calcZ + 1);
									Block blockInFront = Block.getBlock((byte) blockIdInFront);

									int blockIdBehind = c.world.getBlock(calcX, calcY, calcZ - 1);
									Block blockBehind = Block.getBlock((byte) blockIdBehind);

									int blockIdOnRight = c.world.getBlock(calcX + 1, calcY, calcZ);
									Block blockOnRight = Block.getBlock((byte) blockIdOnRight);

									int blockIdOnLeft = c.world.getBlock(calcX - 1, calcY, calcZ);
									Block blockOnLeft = Block.getBlock((byte) blockIdOnLeft);
									
									//TODO GL20.glVertexAttrib1f(attrib, blockID);
									
									boolean visible = false;

									if (blockIdAbove == 0 || !blockAbove.isOpaque() && !(blockID == blockIdAbove && blockAbove.shouldCullSame())) {
										boolean xnzn = c.world.isOccluder(calcX - 1, calcY + 1, calcZ - 1);
										boolean zn = c.world.isOccluder(calcX, calcY + 1, calcZ - 1);
										boolean xpzn = c.world.isOccluder(calcX + 1, calcY + 1, calcZ - 1);
										boolean xn = c.world.isOccluder(calcX - 1, calcY + 1, calcZ);
										boolean xp = c.world.isOccluder(calcX + 1, calcY + 1, calcZ);
										boolean xnzp = c.world.isOccluder(calcX - 1, calcY + 1, calcZ + 1);
										boolean zp = c.world.isOccluder(calcX, calcY + 1, calcZ + 1);
										boolean xpzp = c.world.isOccluder(calcX + 1, calcY + 1, calcZ + 1);
										
										visible = true;

										block.renderTop(calcX, calcY, calcZ, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, c.world);
									}

									if (blockIdBelow == 0 || !blockBelow.isOpaque() && !(blockID == blockIdBelow && blockBelow.shouldCullSame())) {
										boolean xnzn = c.world.isOccluder(calcX - 1, calcY - 1, calcZ - 1);
										boolean zn = c.world.isOccluder(calcX, calcY - 1, calcZ - 1);
										boolean xpzn = c.world.isOccluder(calcX + 1, calcY - 1, calcZ - 1);
										boolean xn = c.world.isOccluder(calcX - 1, calcY - 1, calcZ);
										boolean xp = c.world.isOccluder(calcX + 1, calcY - 1, calcZ);
										boolean xnzp = c.world.isOccluder(calcX - 1, calcY - 1, calcZ + 1);
										boolean zp = c.world.isOccluder(calcX, calcY - 1, calcZ + 1);
										boolean xpzp = c.world.isOccluder(calcX + 1, calcY - 1, calcZ + 1);
										
										visible = true;

										block.renderBottom(calcX, calcY, calcZ, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, c.world);
									}

									if (blockIdInFront == 0 || !blockInFront.isOpaque() && !(blockID == blockIdInFront && blockInFront.shouldCullSame())) {
										boolean xnzn = c.world.isOccluder(calcX - 1, calcY - 1, calcZ + 1);
										boolean zn = c.world.isOccluder(calcX, calcY - 1, calcZ + 1);
										boolean xpzn = c.world.isOccluder(calcX + 1, calcY - 1, calcZ + 1);
										boolean xn = c.world.isOccluder(calcX - 1, calcY, calcZ + 1);
										boolean xp = c.world.isOccluder(calcX + 1, calcY, calcZ + 1);
										boolean xnzp = c.world.isOccluder(calcX - 1, calcY + 1, calcZ + 1);
										boolean zp = c.world.isOccluder(calcX, calcY + 1, calcZ + 1);
										boolean xpzp = c.world.isOccluder(calcX + 1, calcY + 1, calcZ + 1);
										
										visible = true;

										block.renderFront(calcX, calcY, calcZ, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, c.world);
									}

									if (blockIdBehind == 0 || !blockBehind.isOpaque() && !(blockID == blockIdBehind && blockBehind.shouldCullSame())) {
										boolean xnzn = c.world.isOccluder(calcX - 1, calcY - 1, calcZ - 1);
										boolean zn = c.world.isOccluder(calcX, calcY - 1, calcZ - 1);
										boolean xpzn = c.world.isOccluder(calcX + 1, calcY - 1, calcZ - 1);
										boolean xn = c.world.isOccluder(calcX - 1, calcY, calcZ - 1);
										boolean xp = c.world.isOccluder(calcX + 1, calcY, calcZ - 1);
										boolean xnzp = c.world.isOccluder(calcX - 1, calcY + 1, calcZ - 1);
										boolean zp = c.world.isOccluder(calcX, calcY + 1, calcZ - 1);
										boolean xpzp = c.world.isOccluder(calcX + 1, calcY + 1, calcZ - 1);
										
										visible = true;

										block.renderBack(calcX, calcY, calcZ, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, c.world);
									}

									if (blockIdOnRight == 0 || !blockOnRight.isOpaque() && !(blockID == blockIdOnRight && blockOnRight.shouldCullSame())) {
										boolean xnzn = c.world.isOccluder(calcX + 1, calcY + 1, calcZ - 1);
										boolean zn = c.world.isOccluder(calcX + 1, calcY + 1, calcZ);
										boolean xpzn = c.world.isOccluder(calcX + 1, calcY + 1, calcZ + 1);
										boolean xn = c.world.isOccluder(calcX + 1, calcY, calcZ - 1);
										boolean xp = c.world.isOccluder(calcX + 1, calcY, calcZ + 1);
										boolean xnzp = c.world.isOccluder(calcX + 1, calcY - 1, calcZ - 1);
										boolean zp = c.world.isOccluder(calcX + 1, calcY - 1, calcZ);
										boolean xpzp = c.world.isOccluder(calcX + 1, calcY - 1, calcZ + 1);
										
										visible = true;

										block.renderRight(calcX, calcY, calcZ, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, c.world);
									}

									if (blockIdOnLeft == 0 || !blockOnLeft.isOpaque() && !(blockID == blockIdOnLeft && blockOnLeft.shouldCullSame())) {
										boolean xnzn = c.world.isOccluder(calcX - 1, calcY + 1, calcZ - 1);
										boolean zn = c.world.isOccluder(calcX - 1, calcY + 1, calcZ);
										boolean xpzn = c.world.isOccluder(calcX - 1, calcY + 1, calcZ + 1);
										boolean xn = c.world.isOccluder(calcX - 1, calcY, calcZ - 1);
										boolean xp = c.world.isOccluder(calcX - 1, calcY, calcZ + 1);
										boolean xnzp = c.world.isOccluder(calcX - 1, calcY - 1, calcZ - 1);
										boolean zp = c.world.isOccluder(calcX - 1, calcY - 1, calcZ);
										boolean xpzp = c.world.isOccluder(calcX - 1, calcY - 1, calcZ + 1);
										
										visible = true;

										block.renderLeft(calcX, calcY, calcZ, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, c.world);
									}
									
									if (visible) {
										block.renderIndependent(calcX, calcY, calcZ, c.world);
									}

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
