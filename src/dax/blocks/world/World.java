package dax.blocks.world;

import dax.blocks.Player;
import dax.blocks.world.chunk.BufferedChunk;
import dax.blocks.world.chunk.Chunk;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;

public class World {
	
	public int size;
	public Player player;
	public int[][] heightMap;
	Chunk[][] chunks;
	
	float multipler;
	
	public World(int size, float multipler) {
		this.size = size;
		player = new Player(this);
		chunks = new Chunk[size][size];
		
		this.multipler = multipler;

		generateHeightMap();

		long start = System.nanoTime();
		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {		
				chunks[x][z] = new Chunk(x, z, this);
			}
		}
		System.out.println("Chunks created in " + (System.nanoTime()-start)/1000000 + "ms");
		
		start = System.nanoTime();
		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {		
				chunks[x][z].rebuild();
			}
		}
		System.out.println("World geometry built in " + (System.nanoTime()-start)/1000000 + "ms");
	}
	
	public void generateHeightMap() {
		long start = System.nanoTime();
		
		double[][] dhm;
		HeightMapGenerator hmg = new HeightMapGenerator();
		hmg.setSize(size*Chunk.CHUNK_SIZE, size*Chunk.CHUNK_SIZE);
		dhm = hmg.generate();
		heightMap = new int[dhm.length][dhm[0].length];
		for (int x = 0; x < dhm.length; x++) {
			for (int z = 0; z < dhm[0].length; z++) {
				int precalc = (int)(Math.round(dhm[x][z]*multipler));
				if (precalc > Chunk.CHUNK_HEIGHT-1) {
					precalc = Chunk.CHUNK_HEIGHT-1;
				}
				if (precalc < 0) {
					precalc = 0;
				}
				
				heightMap[x][z] = precalc;
			}
		}
		
		System.out.println("World generated in " + (System.nanoTime()-start)/1000000 + "ms");
	}
	
	public void update(float delta) {
		player.update(delta);
	}
	
	public void updateChunk(int x, int z) {
		if (!(x < 0 || x >= size || z < 0 || z >= size)) {
			chunks[x][z].rebuild();
		}
	}
	
	public byte getBlock(int x, int y, int z) {
		if (x < 0 || y < 0 || z < 0 || x >= size*Chunk.CHUNK_SIZE || z >= size*Chunk.CHUNK_SIZE || y >= Chunk.CHUNK_HEIGHT) {
			return 0;
		}		
		
		int icx = x % Chunk.CHUNK_SIZE;
		int icz = z % Chunk.CHUNK_SIZE;
		
		int cx = x / Chunk.CHUNK_SIZE;
		int cz = z / Chunk.CHUNK_SIZE;

		return chunks[cx][cz].getBlock(icx, y, icz);
	}
	
	public void setBlock(int x, int y, int z, byte id) {
		if (x < 0 || y < 0 || z < 0 || x >= size*Chunk.CHUNK_SIZE || z >= size*Chunk.CHUNK_SIZE || y >= Chunk.CHUNK_HEIGHT) {
			return;
		}		
		
		int icx = x % Chunk.CHUNK_SIZE;
		int icz = z % Chunk.CHUNK_SIZE;
		
		int cx = x / Chunk.CHUNK_SIZE;
		int cz = z / Chunk.CHUNK_SIZE;

		chunks[cx][cz].setBlock(icx, y, icz, (byte)id, true);
	}
	
	public void render() {		
		glEnableClientState(GL_VERTEX_ARRAY);
	    glEnableClientState(GL_TEXTURE_COORD_ARRAY);
	    glEnableClientState(GL_NORMAL_ARRAY);
	    
	    BufferedChunk bc;

		GL11.glEnable(GL11.GL_LIGHTING);
		
		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {						
				bc = chunks[x][z].bc;
				
				glPushMatrix();
				glTranslatef(x*Chunk.CHUNK_SIZE, 0, z*Chunk.CHUNK_SIZE);
				
				glVertexPointer(3, 12, bc.vBufferOpaque);
				glTexCoordPointer(2, 8, bc.tBufferOpaque);
				glNormalPointer(0, bc.nBufferOpaque);
				glDrawArrays(GL_QUADS, 0, bc.vBufferOpaque.capacity() / 3);	
				
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				glVertexPointer(3, 12, bc.vBufferTransparent);
				glTexCoordPointer(2, 8, bc.tBufferTransparent);
				glNormalPointer(0, bc.nBufferTransparent);
				glDrawArrays(GL_QUADS, 0, bc.vBufferTransparent.capacity() / 3);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				
				glPopMatrix();
			}
		}
		
	    glDisableClientState(GL_TEXTURE_COORD_ARRAY);
	    glDisableClientState(GL_VERTEX_ARRAY);
	    glDisableClientState(GL_NORMAL_ARRAY);
	    
	    if (player.hasSelected) {
	    	GL11.glPushMatrix();
	    	GL11.glTranslatef(player.lookingAtX, player.lookingAtY, player.lookingAtZ);
	    	GL11.glDisable(GL11.GL_LIGHTING);
	    	renderCube();
	    	GL11.glPopMatrix();
	    }	
	
	}
	
	public void renderCube() {
		glDisable(GL_TEXTURE_2D);
		
		GL11.glLineWidth(4);
		GL11.glColor4f(0, 1, 0 , 0.8f);

		GL11.glPushMatrix();
		glBegin(GL_LINES);

		//front
		glVertex3f(-0.005f, 1.005f, 1.005f);
		glVertex3f(1.005f, 1.005f, 1.005f);

		glVertex3f(1.005f, 1.005f, 1.005f);
		glVertex3f(1.005f, -0.005f, 1.005f);

		glVertex3f(1.005f, -0.005f, 1.005f);
		glVertex3f(-0.005f, -0.005f, 1.005f);

		glVertex3f(-0.005f, -0.005f, 1.005f);
		glVertex3f(-0.005f, 1.005f, 1.005f);

		//right
		glVertex3f(1.005f, 1.005f, 1.005f);
		glVertex3f(1.005f, 1.005f, -0.005f);

		glVertex3f(1.005f, 1.005f, -0.005f);
		glVertex3f(1.005f, -0.005f, -0.005f);

		glVertex3f(1.005f, -0.005f, -0.005f);	
		glVertex3f(1.005f, -0.005f, 1.005f);

		//back
		glVertex3f(1.005f, 1.005f, -0.005f);
		glVertex3f(-0.005f, 1.005f, -0.005f);

		glVertex3f(-0.005f, -0.005f, -0.005f);
		glVertex3f(1.005f, -0.005f, -0.005f);

		glVertex3f(-0.005f, -0.005f, -0.005f);
		glVertex3f(-0.005f, 1.005f, -0.005f);

		//left
		glVertex3f(-0.005f, 1.005f, -0.005f);
		glVertex3f(-0.005f, 1.005f, 1.005f);

		glVertex3f(-0.005f, -0.005f, 1.005f);
		glVertex3f(-0.005f, -0.005f, -0.005f);
		glEnd();

		GL11.glPopMatrix();
		
		glEnable(GL_TEXTURE_2D);

	}

}
