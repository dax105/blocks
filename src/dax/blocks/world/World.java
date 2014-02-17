package dax.blocks.world;

import dax.blocks.Player;
import dax.blocks.world.chunk.ChunkMesh;
import dax.blocks.world.chunk.Chunk;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;

public class World {

	public int size;
	public Player player;
	ChunkProvider chunkProvider;
	Chunk[][] chunks;

	float multipler;

	public World(int size, float multipler) {
		this.size = size;
		player = new Player(this);
		chunks = new Chunk[size][size];
		
		chunkProvider = new ChunkProvider(this);

		this.multipler = multipler;

		long start = System.nanoTime();
		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				chunks[x][z] = chunkProvider.getChunk(x, z);
			}
		}
		
		System.out.println("Chunks created in " + (System.nanoTime() - start) / 1000000 + "ms");

		start = System.nanoTime();
		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				chunks[x][z].rebuild();
			}
		}
		
		System.out.println("World geometry built in " + (System.nanoTime() - start) / 1000000 + "ms");
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
		if (x < 0 || y < 0 || z < 0 || x >= size * Chunk.CHUNK_SIZE || z >= size * Chunk.CHUNK_SIZE || y >= Chunk.CHUNK_HEIGHT) {
			return 0;
		}

		int icx = x % Chunk.CHUNK_SIZE;
		int icz = z % Chunk.CHUNK_SIZE;

		int cx = x / Chunk.CHUNK_SIZE;
		int cz = z / Chunk.CHUNK_SIZE;

		return chunks[cx][cz].getBlock(icx, y, icz);
	}

	public void setBlock(int x, int y, int z, byte id) {
		if (x < 0 || y < 0 || z < 0 || x >= size * Chunk.CHUNK_SIZE || z >= size * Chunk.CHUNK_SIZE || y >= Chunk.CHUNK_HEIGHT) {
			return;
		}

		int icx = x % Chunk.CHUNK_SIZE;
		int icz = z % Chunk.CHUNK_SIZE;

		int cx = x / Chunk.CHUNK_SIZE;
		int cz = z / Chunk.CHUNK_SIZE;

		chunks[cx][cz].setBlock(icx, y, icz, (byte) id, true);
	}

	public void render() {
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		glEnableClientState(GL_NORMAL_ARRAY);

		ChunkMesh cm;

		GL11.glEnable(GL11.GL_LIGHTING);

		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				cm = chunks[x][z].cm;

				glPushMatrix();
				glTranslatef(x * Chunk.CHUNK_SIZE, 0, z * Chunk.CHUNK_SIZE);

				glVertexPointer(3, 12, cm.vBufferOpaque);
				glTexCoordPointer(2, 8, cm.tBufferOpaque);
				glNormalPointer(0, cm.nBufferOpaque);
				glDrawArrays(GL_QUADS, 0, cm.vBufferOpaque.capacity() / 3);

				GL11.glEnable(GL11.GL_ALPHA_TEST);
				glVertexPointer(3, 12, cm.vBufferTransparent);
				glTexCoordPointer(2, 8, cm.tBufferTransparent);
				glNormalPointer(0, cm.nBufferTransparent);
				glDrawArrays(GL_QUADS, 0, cm.vBufferTransparent.capacity() / 3);
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
		GL11.glColor4f(0, 1, 0, 0.8f);

		GL11.glPushMatrix();
		glBegin(GL_LINES);

		// front
		glVertex3f(-0.005f, 1.005f, 1.005f);
		glVertex3f(1.005f, 1.005f, 1.005f);

		glVertex3f(1.005f, 1.005f, 1.005f);
		glVertex3f(1.005f, -0.005f, 1.005f);

		glVertex3f(1.005f, -0.005f, 1.005f);
		glVertex3f(-0.005f, -0.005f, 1.005f);

		glVertex3f(-0.005f, -0.005f, 1.005f);
		glVertex3f(-0.005f, 1.005f, 1.005f);

		// right
		glVertex3f(1.005f, 1.005f, 1.005f);
		glVertex3f(1.005f, 1.005f, -0.005f);

		glVertex3f(1.005f, 1.005f, -0.005f);
		glVertex3f(1.005f, -0.005f, -0.005f);

		glVertex3f(1.005f, -0.005f, -0.005f);
		glVertex3f(1.005f, -0.005f, 1.005f);

		// back
		glVertex3f(1.005f, 1.005f, -0.005f);
		glVertex3f(-0.005f, 1.005f, -0.005f);

		glVertex3f(-0.005f, -0.005f, -0.005f);
		glVertex3f(1.005f, -0.005f, -0.005f);

		glVertex3f(-0.005f, -0.005f, -0.005f);
		glVertex3f(-0.005f, 1.005f, -0.005f);

		// left
		glVertex3f(-0.005f, 1.005f, -0.005f);
		glVertex3f(-0.005f, 1.005f, 1.005f);

		glVertex3f(-0.005f, -0.005f, 1.005f);
		glVertex3f(-0.005f, -0.005f, -0.005f);
		glEnd();

		GL11.glPopMatrix();

		glEnable(GL_TEXTURE_2D);

	}

}
