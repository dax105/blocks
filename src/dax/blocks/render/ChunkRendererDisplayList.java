package dax.blocks.render;

import org.lwjgl.opengl.GL11;

import dax.blocks.Game;

public class ChunkRendererDisplayList implements IChunkRenderer {

	private int handle = 0;
	private boolean drawing = false;
	
	private float lastR = -5;
	private float lastG = -5;
	private float lastB = -5;
	private float lastA = -5;
	
	private float lastNX = -5;
	private float lastNY = -5;
	private float lastNZ = -5;
	
	private float lastS = -5;
	private float lastT = -5;
	
	private void reset() {
		lastR = -5;
		lastG = -5;
		lastB = -5;
		lastA = -5;
		
		lastNX = -5;
		lastNY = -5;
		lastNZ = -5;
		
		lastS = -5;
		lastT = -5;
	}
	
	@Override
	public void begin() {
		reset();
		this.drawing = true;
		this.handle = GL11.glGenLists(1);
		GL11.glNewList(this.handle, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_QUADS);
	}

	@Override
	public void end() {
		GL11.glEnd();
		GL11.glEndList();
		this.drawing = false;
	}

	@Override
	public int getHandle() {
		return this.handle;
	}

	@Override
	public void delete(int handle) {
		if (!this.drawing) {
			GL11.glDeleteLists(handle, 1);
		}
	}

	@Override
	public void render(int handle) {
		if (!this.drawing) {
			GL11.glCallList(handle);
		}
	}

	@Override
	public void vertex(float x, float y, float z) {
		GL11.glVertex3f(x, y, z);
	}

	@Override
	public void color(float r, float b, float g) {
		color(r, g, b, 1);
	}

	@Override
	public void color(float r, float b, float g, float a) {
		if (r != lastR || b != lastB || g != lastG || a != lastA) {
			lastR = r;
			lastG = g;
			lastB = b;
			lastA = a;
			GL11.glColor4f(r, g, b, a);
		}	
	}

	@Override
	public void normal(float nx, float ny, float nz) {
		if (nx != lastNX || ny != lastNY || nz != lastNZ) {
			lastNX = nx;
			lastNY = ny;
			lastNZ = nz;
			GL11.glNormal3f(nx, ny, nz);
		}	
	}

	@Override
	public void texCoord(float s, float t) {
		if (s != lastS || t != lastT) {
			lastS = s;
			lastT = t;
			GL11.glTexCoord2f(s, t);
		}
	}

	@Override
	public void vertexWithAO(float x, float y, float z, boolean s1, boolean s2, boolean c) {
		float ao;
		
		if (s1 && s2) {
			ao = 3;
		} else {
			ao = 0;
			if (s1)
				ao++;
			if (s2)
				ao++;
			if (c)
				ao++;
		}
	
		if (ao == 1) ao += 0.5f;
		
		float aom = ao * Game.settings.ao_intensity.getValue();
	
		color(1 - aom, 1 - aom, 1 - aom);
		vertex(x, y, z);
	}

}
