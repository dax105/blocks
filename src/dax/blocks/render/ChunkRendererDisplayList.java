package dax.blocks.render;

import org.lwjgl.opengl.GL11;
import dax.blocks.settings.Settings;

public class ChunkRendererDisplayList implements IChunkRenderer {

	private int handle = 0;
	
	private int vertices = 0;
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
		this.lastR = -5;
		this.lastG = -5;
		this.lastB = -5;
		this.lastA = -5;
		
		this.lastNX = -5;
		this.lastNY = -5;
		this.lastNZ = -5;
		
		this.lastS = -5;
		this.lastT = -5;
		
		this.vertices = 0;
	}
	
	@Override
	public void begin() {
		this.reset();
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
		if(!this.drawing) {
			GL11.glDeleteLists(handle, 1);
		}
	}

	@Override
	public void render(int handle) {
		if(!this.drawing) {
			GL11.glCallList(handle);
		}
	}

	@Override
	public void vertex(float x, float y, float z) {
		GL11.glVertex3f(x, y, z);
		vertices++;
	}

	@Override
	public void color(float r, float b, float g) {
		this.color(r, g, b, 1);
	}

	@Override
	public void color(float r, float b, float g, float a) {
		if(r != this.lastR || b != this.lastB || g != this.lastG || a != this.lastA) {
			this.lastR = r;
			this.lastG = g;
			this.lastB = b;
			this.lastA = a;
			GL11.glColor4f(r, g, b, a);
		}	
	}

	@Override
	public void normal(float nx, float ny, float nz) {
		if (nx != this.lastNX || ny != this.lastNY || nz != this.lastNZ) {
			this.lastNX = nx;
			this.lastNY = ny;
			this.lastNZ = nz;
			GL11.glNormal3f(nx, ny, nz);
		}	
	}

	@Override
	public void texCoord(float s, float t) {
		if (s != this.lastS || t != this.lastT) {
			this.lastS = s;
			this.lastT = t;
			GL11.glTexCoord2f(s, t);
		}
	}

	@Override
	public void vertexWithAO(float x, float y, float z, boolean s1, boolean s2, boolean c) {
		this.vertexWithColoredAO(x, y, z, 1, 1, 1, s1, s2, c);
	}
	
	@Override
	public void vertexWithColoredAO(float x, float y, float z, float r, float g, float b, boolean s1, boolean s2, boolean c) {
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
		
		float aom = ao * Settings.getInstance().aoIntensity.getValue();
	
		this.color(r - aom, g - aom, b - aom);
		this.vertex(x, y, z);
	}

	@Override
	public int getVertexCount() {
		return this.vertices;
	}

}
