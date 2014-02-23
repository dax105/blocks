package dax.blocks.world;

import java.nio.IntBuffer;
import java.util.Random;

import dax.blocks.Player;
import dax.blocks.world.chunk.ChunkMesh;
import dax.blocks.world.chunk.Chunk;
import dax.blocks.world.generator.TreeGenerator;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class World {

	public int size;
	public int sizeBlocks;
	public Player player;
	ChunkProvider chunkProvider;
	Chunk[][] chunks;

	float multipler;
	
	TreeGenerator treeGen;

	public World(int size, float multipler) {
		this.size = size;
		this.sizeBlocks = size * Chunk.CHUNK_SIZE;
		player = new Player(this);
		chunks = new Chunk[size][size];
		this.treeGen = new TreeGenerator(this);

		chunkProvider = new ChunkProvider(this);

		this.multipler = multipler;

		long start = System.nanoTime();
		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				chunks[x][z] = chunkProvider.getChunk(x, z);
			}
		}
		
		Random rand = new Random();
		
		int maxTrees = size*size-(rand.nextInt((size*size)-5));
		
		for (int i = 0; i < maxTrees; i++) {
			
			int sy = 0;
			int x = 1+rand.nextInt(sizeBlocks-1);
			int z = 1+rand.nextInt(sizeBlocks-1);
			
			for (int y = 0; y < Chunk.CHUNK_HEIGHT; y++) {
				sy = y;
				if (getBlock(x, sy+1, z) == 0) {
					break;
				}
			}
			
			treeGen.generateTree(x, sy, z);
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

		chunks[cx][cz].setBlock(icx, y, icz, id, true);
	}
	
	public void setBlockNoRebuild(int x, int y, int z, byte id) {
		if (x < 0 || y < 0 || z < 0 || x >= size * Chunk.CHUNK_SIZE || z >= size * Chunk.CHUNK_SIZE || y >= Chunk.CHUNK_HEIGHT) {
			return;
		}

		int icx = x % Chunk.CHUNK_SIZE;
		int icz = z % Chunk.CHUNK_SIZE;

		int cx = x / Chunk.CHUNK_SIZE;
		int cz = z / Chunk.CHUNK_SIZE;

		chunks[cx][cz].setBlock(icx, y, icz, id, false);
	}

	public void render() {
		Chunk c;

		GL11.glEnable(GL11.GL_LIGHTING);

		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);

		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				c = chunks[x][z];
				ChunkMesh cm = c.cm;

				GL11.glPushMatrix();
				GL11.glTranslatef(x * Chunk.CHUNK_SIZE, 0, z * Chunk.CHUNK_SIZE);

				if (!c.hasVBO) {
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

					c.hasVBO = true;
				}

				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cm.vHandleOpaque);
				GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0L);
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cm.tHandleOpaque);
				GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 8, 0L);
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cm.nHandleOpaque);
				GL11.glNormalPointer(GL11.GL_FLOAT, 12, 0L);
				GL11.glDrawArrays(GL11.GL_QUADS, 0, cm.vBufferOpaque.capacity() / 3);

				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cm.vHandleTransparent);
				GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0L);
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cm.tHandleTransparent);
				GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 8, 0L);
				GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cm.nHandleTransparent);
				GL11.glNormalPointer(GL11.GL_FLOAT, 12, 0L);
				GL11.glDrawArrays(GL11.GL_QUADS, 0, cm.vBufferTransparent.capacity() / 3);
				GL11.glDisable(GL11.GL_ALPHA_TEST);

				GL11.glPopMatrix();
			}
		}

		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);

		if (player.hasSelected) {
			GL11.glPushMatrix();
			GL11.glTranslatef(player.lookingAtX, player.lookingAtY, player.lookingAtZ);
			GL11.glDisable(GL11.GL_LIGHTING);
			renderSelectionBox();
			GL11.glPopMatrix();
		}

	}

	public void renderSelectionBox() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glLineWidth(4);
		GL11.glColor4f(0, 1, 0, 0.8f);

		GL11.glPushMatrix();
		GL11.glBegin(GL11.GL_LINES);

		// front
		GL11.glVertex3f(-0.005f, 1.005f, 1.005f);
		GL11.glVertex3f(1.005f, 1.005f, 1.005f);

		GL11.glVertex3f(1.005f, 1.005f, 1.005f);
		GL11.glVertex3f(1.005f, -0.005f, 1.005f);

		GL11.glVertex3f(1.005f, -0.005f, 1.005f);
		GL11.glVertex3f(-0.005f, -0.005f, 1.005f);

		GL11.glVertex3f(-0.005f, -0.005f, 1.005f);
		GL11.glVertex3f(-0.005f, 1.005f, 1.005f);

		// right
		GL11.glVertex3f(1.005f, 1.005f, 1.005f);
		GL11.glVertex3f(1.005f, 1.005f, -0.005f);

		GL11.glVertex3f(1.005f, 1.005f, -0.005f);
		GL11.glVertex3f(1.005f, -0.005f, -0.005f);

		GL11.glVertex3f(1.005f, -0.005f, -0.005f);
		GL11.glVertex3f(1.005f, -0.005f, 1.005f);

		// back
		GL11.glVertex3f(1.005f, 1.005f, -0.005f);
		GL11.glVertex3f(-0.005f, 1.005f, -0.005f);

		GL11.glVertex3f(-0.005f, -0.005f, -0.005f);
		GL11.glVertex3f(1.005f, -0.005f, -0.005f);

		GL11.glVertex3f(-0.005f, -0.005f, -0.005f);
		GL11.glVertex3f(-0.005f, 1.005f, -0.005f);

		// left
		GL11.glVertex3f(-0.005f, 1.005f, -0.005f);
		GL11.glVertex3f(-0.005f, 1.005f, 1.005f);

		GL11.glVertex3f(-0.005f, -0.005f, 1.005f);
		GL11.glVertex3f(-0.005f, -0.005f, -0.005f);
		GL11.glEnd();

		GL11.glPopMatrix();

		GL11.glEnable(GL11.GL_TEXTURE_2D);

	}

}
