package dax.blocks.render;

import org.lwjgl.opengl.GL11;

public class ChunkRendererDisplayList implements IChunkRenderer {

	private int handle = 0;
	private boolean drawing = false;
	
	private float lastR = -1;
	private float lastG = -1;
	private float lastB = -1;
	private float lastA = -1;
	
	private float lastNX = -1;
	private float lastNY = -1;
	private float lastNZ = -1;
	
	private float lastS = -1;
	private float lastT = -1;
	
	private void reset() {
		lastR = -1;
		lastG = -1;
		lastB = -1;
		lastA = -1;
		
		lastNX = -1;
		lastNY = -1;
		lastNZ = -1;
		
		lastS = -1;
		lastT = -1;
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
			GL11.glColor4f(r, g, b, a);
		}	
	}

	@Override
	public void normal(float nx, float ny, float nz) {
		if (nx != lastNX || ny != lastNY || nz != lastNZ) {
			GL11.glNormal3f(nx, ny, nz);
		}	
	}

	@Override
	public void texCoord(float s, float t) {
		if (s != lastS || t != lastT) {
			GL11.glTexCoord2f(s, t);
		}
	}

}
