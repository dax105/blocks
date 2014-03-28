package dax.blocks.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import dax.blocks.Particle;
import dax.blocks.Player;
import dax.blocks.world.World;
import dax.blocks.world.chunk.Chunk;


public class RenderEngine {

	public static final int DRAW_DISTANCE = 12;
	
	Frustum frustum;
	ChunkDistanceComparator chunkDistComp;
	ChunkMeshBuilder cmb;
	float ptt;

	float[] rightModelviewVec;
	float[] upModelviewVec;

	public RenderEngine() {
		this.frustum = new Frustum();
		this.rightModelviewVec = new float[3];
		this.upModelviewVec = new float[3];
		this.chunkDistComp = new ChunkDistanceComparator();
		this.cmb = new ChunkMeshBuilder();
	}

	public void updateBeforeRendering(float ptt) {
		this.frustum.calculateFrustum();

		FloatBuffer modelviewMatrix = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelviewMatrix);
		rightModelviewVec[0] = modelviewMatrix.get(0);
		upModelviewVec[0] = modelviewMatrix.get(1);
		rightModelviewVec[1] = modelviewMatrix.get(4);
		upModelviewVec[1] = modelviewMatrix.get(5);
		rightModelviewVec[2] = modelviewMatrix.get(8);
		upModelviewVec[2] = modelviewMatrix.get(9);
	}

	public void pushPlayerMatrix(Player player) {
		GL11.glPushMatrix();
		GL11.glRotatef(-player.tilt, 1f, 0f, 0f);
		GL11.glRotatef(player.heading, 0f, 1f, 0f);
		GL11.glTranslated(-player.getPartialX(this.ptt), -player.getPartialY(this.ptt) - Player.EYES_HEIGHT, -player.getPartialZ(ptt));
	}

	public void renderWorld(World world, float ptt) {
		this.ptt = ptt;
		pushPlayerMatrix(world.player);
		updateBeforeRendering(ptt);
		
		FloatBuffer lp = BufferUtils.createFloatBuffer(4);
		lp.put(-10).put(10).put(-10).put(0).flip();

		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, lp);

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		
		// Render particles
		GL11.glBegin(GL11.GL_QUADS);
		for (Particle p : world.particles) {
			renderParticle(p, ptt);
		}
		GL11.glEnd();

		// Render selection box
		GL11.glLineWidth(4);
		GL11.glColor4f(0, 1, 0, 0.8f);
		if (world.player.hasSelected) {
			renderLinedBox(world.player.lookingAtX-0.001f, world.player.lookingAtY-0.001f, world.player.lookingAtZ-0.001f, world.player.lookingAtX+1+0.001f, world.player.lookingAtY+1+0.001f, world.player.lookingAtZ+1+0.001f);
		}

		// Render chunks
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);		

		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);

		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);

		int vertices = 0;
		int cd = 0;

		int pcx = (int) world.player.posX / 16;
		int pcz = (int) world.player.posZ / 16;
		
		ArrayList<Chunk> visibleChunks = world.chunkProvider.getChunksInRadius(pcx, pcz, DRAW_DISTANCE);

		for (Iterator<Chunk> iter = visibleChunks.iterator(); iter.hasNext(); ) {
		    Chunk c = iter.next();
		    if (c == null) {
				iter.remove();
			}
		}

		chunkDistComp.setFrontToBack();
		Collections.sort(visibleChunks, chunkDistComp);

		int generatedMeshes = 0;
		
		for (Chunk c : visibleChunks) {
			int x = c.x;
			int z = c.z;
			
			for (int y = 0; y < Chunk.CHUNK_HEIGHT / Chunk.CHUNK_SIZE; y++) {
			
				ChunkMesh cm = c.meshes[y];
				if (cm.isDirty && generatedMeshes < 2) {
					cm.generateMesh(c, y);
					generatedMeshes++;
				}
				
				if (generatedMeshes >= 2) {
					break;
				}
			
			}
		}
		
		chunkDistComp.setBackToFront();
		Collections.sort(visibleChunks, chunkDistComp);
		
		for (Chunk c : visibleChunks) {

			int x = c.x;
			int z = c.z;
			
			for (int y = 0; y < Chunk.CHUNK_HEIGHT / Chunk.CHUNK_SIZE; y++) {

				ChunkMesh cm = c.meshes[y];
			
				if ((frustum.cuboidInFrustum(x * Chunk.CHUNK_SIZE, y * Chunk.CHUNK_SIZE, z * Chunk.CHUNK_SIZE, (x + 1) * Chunk.CHUNK_SIZE, (y + 1) * Chunk.CHUNK_SIZE, (z + 1) * Chunk.CHUNK_SIZE) && cm.vertices > 0)) {
					cd++;

					vertices += cm.vertices;

					GL11.glPushMatrix();
					GL11.glTranslatef(x * Chunk.CHUNK_SIZE, 0, z * Chunk.CHUNK_SIZE);

					if (!cm.hasVBO || cm.needsNewVBO && !cm.isDirty) {
						if (cm.needsNewVBO) {
							c.deleteVBO(y);
						}
						
						IntBuffer ib = BufferUtils.createIntBuffer(6);
						GL15.glGenBuffers(ib);

						int vHandleOpaque = ib.get(0);
						int tHandleOpaque = ib.get(1);
						int nHandleOpaque = ib.get(2);

						int vHandleTransparent = ib.get(3);
						int tHandleTransparent = ib.get(4);
						int nHandleTransparent = ib.get(5);

						cm.vHandleOpaque = vHandleOpaque;
						cm.tHandleOpaque = tHandleOpaque;
						cm.nHandleOpaque = nHandleOpaque;

						cm.vHandleTransparent = vHandleTransparent;
						cm.tHandleTransparent = tHandleTransparent;
						cm.nHandleTransparent = nHandleTransparent;

						GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vHandleOpaque);
						GL15.glBufferData(GL15.GL_ARRAY_BUFFER, cm.vBufferOpaque, GL15.GL_STATIC_DRAW);

						GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, tHandleOpaque);
						GL15.glBufferData(GL15.GL_ARRAY_BUFFER, cm.tBufferOpaque, GL15.GL_STATIC_DRAW);

						GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, nHandleOpaque);
						GL15.glBufferData(GL15.GL_ARRAY_BUFFER, cm.nBufferOpaque, GL15.GL_STATIC_DRAW);

						GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vHandleTransparent);
						GL15.glBufferData(GL15.GL_ARRAY_BUFFER, cm.vBufferTransparent, GL15.GL_STATIC_DRAW);

						GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, tHandleTransparent);
						GL15.glBufferData(GL15.GL_ARRAY_BUFFER, cm.tBufferTransparent, GL15.GL_STATIC_DRAW);

						GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, nHandleTransparent);
						GL15.glBufferData(GL15.GL_ARRAY_BUFFER, cm.nBufferTransparent, GL15.GL_STATIC_DRAW);

						cm.hasVBO = true;
						cm.needsNewVBO = false;
					}

					GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cm.vHandleOpaque);
					GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0L);
					GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cm.tHandleOpaque);
					GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 8, 0L);
					GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cm.nHandleOpaque);
					GL11.glNormalPointer(GL11.GL_FLOAT, 12, 0L);
					GL11.glDrawArrays(GL11.GL_QUADS, 0, cm.vBufferOpaque.capacity() / 3);

					if (cm.hasTransparent) {
						GL11.glEnable(GL11.GL_ALPHA_TEST);
						GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cm.vHandleTransparent);
						GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0L);
						GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cm.tHandleTransparent);
						GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 8, 0L);
						GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cm.nHandleTransparent);
						GL11.glNormalPointer(GL11.GL_FLOAT, 12, 0L);
						GL11.glDrawArrays(GL11.GL_QUADS, 0, cm.vBufferTransparent.capacity() / 3);
						GL11.glDisable(GL11.GL_ALPHA_TEST);
					}

					GL11.glPopMatrix();
				}
			}

		
		}	

		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);


		//this.vertices = vertices;
		//this.chunksDrawn = cd;

		GL11.glPopMatrix();
		
	}

	public void renderParticle(Particle p, float ptt) {
		GL11.glColor4f(p.r, p.g, p.b, 1);

		float h = Particle.PARTICLE_SIZE / 2;
		float sizemutipler = (h / 1);

		float rightup0p = (rightModelviewVec[0] + upModelviewVec[0]) * sizemutipler;
		float rightup1p = (rightModelviewVec[1] + upModelviewVec[1]) * sizemutipler;
		float rightup2p = (rightModelviewVec[2] + upModelviewVec[2]) * sizemutipler;
		float rightup0n = (rightModelviewVec[0] - upModelviewVec[0]) * sizemutipler;
		float rightup1n = (rightModelviewVec[1] - upModelviewVec[1]) * sizemutipler;
		float rightup2n = (rightModelviewVec[2] - upModelviewVec[2]) * sizemutipler;

		float px = p.getPartialX(ptt);
		float py = p.getPartialY(ptt);
		float pz = p.getPartialZ(ptt);

		GL11.glVertex3f(px - rightup0p, py - rightup1p, pz - rightup2p);
		GL11.glVertex3f(px + rightup0n, py + rightup1n, pz + rightup2n);
		GL11.glVertex3f(px + rightup0p, py + rightup1p, pz + rightup2p);
		GL11.glVertex3f(px - rightup0n, py - rightup1n, pz - rightup2n);

		// GL11.glVertex3f(px, py, pz);

		// GL11.glVertex3f(px-Particle.PARTICLE_SIZE/2, py-Particle.PARTICLE_SIZE/2, pz-Particle.PARTICLE_SIZE/2);
		// GL11.glVertex3f(px+Particle.PARTICLE_SIZE/2, py+Particle.PARTICLE_SIZE/2, pz+Particle.PARTICLE_SIZE/2);
	}

	public void renderLinedBox(float x0, float y0, float z0, float x1, float y1, float z1) {
		GL11.glBegin(GL11.GL_LINES);

		// front
		GL11.glVertex3f(x0, y1, z1);
		GL11.glVertex3f(x1, y1, z1);

		GL11.glVertex3f(x1, y1, z1);
		GL11.glVertex3f(x1, y0, z1);

		GL11.glVertex3f(x1, y0, z1);
		GL11.glVertex3f(x0, y0, z1);

		GL11.glVertex3f(x0, y0, z1);
		GL11.glVertex3f(x0, y1, z1);

		// right
		GL11.glVertex3f(x1, y1, z1);
		GL11.glVertex3f(x1, y1, z0);

		GL11.glVertex3f(x1, y1, z0);
		GL11.glVertex3f(x1, y0, z0);

		GL11.glVertex3f(x1, y0, z0);
		GL11.glVertex3f(x1, y0, z1);

		// back
		GL11.glVertex3f(x1, y1, z0);
		GL11.glVertex3f(x0, y1, z0);

		GL11.glVertex3f(x0, y0, z0);
		GL11.glVertex3f(x1, y0, z0);

		GL11.glVertex3f(x0, y0, z0);
		GL11.glVertex3f(x0, y1, z0);

		// left
		GL11.glVertex3f(x0, y1, z0);
		GL11.glVertex3f(x0, y1, z1);

		GL11.glVertex3f(x0, y0, z1);
		GL11.glVertex3f(x0, y0, z0);

		GL11.glEnd();
	}

}
