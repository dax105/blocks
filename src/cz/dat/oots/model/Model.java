package cz.dat.oots.model;

import org.lwjgl.opengl.GL11;

import cz.dat.oots.settings.Settings;

public class Model {

	private int width, height, depth;
	private int displayList = -1;
	protected Voxel[][][] voxels;

	public void generateDisplayList() {
		displayList = GL11.glGenLists(1);
		GL11.glNewList(displayList, GL11.GL_COMPILE);

		GL11.glColor3f(1, 1, 1);
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glBegin(GL11.GL_QUADS);

		for(int x = 0; x < this.voxels.length; x++) {
			for(int y = 0; y < this.voxels[0].length; y++) {
				for(int z = 0; z < this.voxels[0][0].length; z++) {
					Voxel v = this.voxels[x][y][z];

					if(v != null) {
						// Top
						if(this.isVoxelNull(x, y + 1, z)) {
							GL11.glNormal3f(0, 1.0f, 0);
							this.addVertexWithAO(
									x + 1f, y + 1f, z + 0f, v.getR(), v.getG(), v.getB(), 
									this.isVoxelNull(x + 1, y + 1, z), this.isVoxelNull(x, y + 1, z - 1), 
									this.isVoxelNull(x + 1, y + 1, z - 1)
							); // OK
							this.addVertexWithAO(
									x + 0f, y + 1f, z + 0f, v.getR(), v.getG(), v.getB(), 
									this.isVoxelNull(x - 1, y + 1, z), this.isVoxelNull(x, y + 1, z - 1), 
									this.isVoxelNull(x - 1, y + 1, z - 1)
							); // OK
							this.addVertexWithAO(
									x + 0f, y + 1f, z + 1f, v.getR(), v.getG(), v.getB(), 
									this.isVoxelNull(x - 1, y + 1, z), this.isVoxelNull(x, y + 1, z + 1), 
									this.isVoxelNull(x - 1, y + 1, z + 1)
							); // OK
							this.addVertexWithAO(
									x + 1f, y + 1f, z + 1f, v.getR(), v.getG(), v.getB(), 
									this.isVoxelNull(x + 1, y + 1, z), this.isVoxelNull(x, y + 1, z + 1), 
									this.isVoxelNull(x + 1, y + 1, z + 1)
							); // OK
						}

						//Bottom
						if(this.isVoxelNull(x, y - 1, z)) {
							GL11.glNormal3f(0, -1.0f, 0);
							this.addVertexWithAO(
									x + 1f, y + 0f, z + 1f, v.getR(), v.getG(), v.getB(), 
									this.isVoxelNull(x + 1, y - 1, z), this.isVoxelNull(x, y - 1, z + 1), 
									this.isVoxelNull(x + 1, y - 1, z + 1)
							); // OK
							this.addVertexWithAO(
									x + 0f, y + 0f, z + 1f, v.getR(), v.getG(), v.getB(), 
									this.isVoxelNull(x - 1, y - 1, z), this.isVoxelNull(x, y - 1, z + 1), 
									this.isVoxelNull(x - 1, y - 1, z + 1)
							); // OK
							this.addVertexWithAO(
									x + 0f, y + 0f, z + 0f, v.getR(), v.getG(), v.getB(), 
									this.isVoxelNull(x - 1, y - 1, z), this.isVoxelNull(x, y - 1, z - 1), 
									this.isVoxelNull(x - 1, y - 1, z - 1)
							); // OK
							this.addVertexWithAO(
									x + 1f, y + 0f, z + 0f, v.getR(), v.getG(), v.getB(), 
									this.isVoxelNull(x + 1, y - 1, z), this.isVoxelNull(x, y - 1, z - 1), 
									this.isVoxelNull(x + 1, y - 1, z - 1)
							); // OK
						}

						//Front
						if(this.isVoxelNull(x, y, z + 1)) {
							GL11.glNormal3f(0.0f, 0.0f, 1.0f);
							this.addVertexWithAO(
									x + 1f, y + 1f, z + 1f, v.getR(), v.getG(), v.getB(), 
									this.isVoxelNull(x, y + 1, z + 1), this.isVoxelNull(x + 1, y, z + 1), 
									this.isVoxelNull(x + 1, y + 1, z + 1)
							); // OK
							this.addVertexWithAO(
									x + 0f, y + 1f, z + 1f, v.getR(), v.getG(), v.getB(), 
									this.isVoxelNull(x - 1, y, z + 1), this.isVoxelNull(x, y + 1, z + 1), 
									this.isVoxelNull(x - 1, y + 1, z + 1)
							); // OK
							this.addVertexWithAO(
									x + 0f, y + 0f, z + 1f, v.getR(), v.getG(), v.getB(), 
									this.isVoxelNull(x - 1, y, z + 1), this.isVoxelNull(x, y - 1, z + 1), 
									this.isVoxelNull(x - 1, y - 1, z + 1)
							); // OK
							this.addVertexWithAO(
									x + 1f, y + 0f, z + 1f, v.getR(), v.getG(), v.getB(), 
									this.isVoxelNull(x, y - 1, z + 1), this.isVoxelNull(x + 1, y, z + 1), 
									this.isVoxelNull(x + 1, y - 1, z + 1)
							); // OK
						}

						//Back
						if(this.isVoxelNull(x, y, z - 1)) {	
							GL11.glNormal3f(0.0f, 0.0f, -1.0f);
							this.addVertexWithAO(
									x + 1f, y + 0f, z + 0f, v.getR(), v.getG(), v.getB(), 
									this.isVoxelNull(x, y - 1, z - 1), this.isVoxelNull(x + 1, y, z - 1), 
									this.isVoxelNull(x + 1, y - 1, z - 1)
							); // OK
							this.addVertexWithAO(
									x + 0f, y + 0f, z + 0f, v.getR(), v.getG(), v.getB(), 
									this.isVoxelNull(x - 1, y, z - 1), this.isVoxelNull(x, y - 1, z - 1), 
									this.isVoxelNull(x - 1, y - 1, z - 1)
							); // OK
							this.addVertexWithAO(
									x + 0f, y + 1f, z + 0f, v.getR(), v.getG(), v.getB(), 
									this.isVoxelNull(x - 1, y, z - 1), this.isVoxelNull(x, y + 1, z - 1), 
									this.isVoxelNull(x - 1, y + 1, z - 1)
							); // OK
							this.addVertexWithAO(
									x + 1f, y + 1f, z + 0f, v.getR(), v.getG(), v.getB(), 
									this.isVoxelNull(x, y + 1, z - 1), this.isVoxelNull(x + 1, y, z - 1), 
									this.isVoxelNull(x + 1, y + 1, z - 1)
							); // OK
						}

						//Left
						if(this.isVoxelNull(x - 1, y, z)) {
							GL11.glNormal3f(-1.0f, 0.0f, 0);
							this.addVertexWithAO(
									x + 0f, y + 1f, z + 1f, v.getR(), v.getG(), v.getB(), 
									this.isVoxelNull(x - 1, y, z + 1), this.isVoxelNull(x - 1, y + 1, z), 
									this.isVoxelNull(x - 1, y + 1, z + 1)
							); // OK
							this.addVertexWithAO(
									x + 0f, y + 1f, z + 0f, v.getR(), v.getG(), v.getB(),
									this.isVoxelNull(x - 1, y, z - 1), this.isVoxelNull(x - 1, y + 1, z), 
									this.isVoxelNull(x - 1, y + 1, z - 1)
							); // OK
							this.addVertexWithAO(
									x + 0f, y + 0f, z + 0f, v.getR(), v.getG(), v.getB(), 
									this.isVoxelNull(x - 1, y, z - 1), this.isVoxelNull(x - 1, y - 1, z), 
									this.isVoxelNull(x - 1, y - 1, z - 1)
							); // OK
							this.addVertexWithAO(
									x + 0f, y + 0f, z + 1f, v.getR(), v.getG(), v.getB(), 
									this.isVoxelNull(x - 1, y, z + 1), this.isVoxelNull(x - 1, y - 1, z), 
									this.isVoxelNull(x - 1, y - 1, z + 1)
							); // OK
						}

						//Right
						if(this.isVoxelNull(x + 1, y, z)) {
							GL11.glNormal3f(1.0f, 0.0f, 0);
							this.addVertexWithAO(
									x + 1f, y + 1f, z + 0f, v.getR(), v.getG(), v.getB(), 
									this.isVoxelNull(x + 1, y, z - 1), this.isVoxelNull(x + 1, y + 1, z), 
									this.isVoxelNull(x + 1, y + 1, z - 1)
							); // OK
							this.addVertexWithAO(
									x + 1f, y + 1f, z + 1f, v.getR(), v.getG(), v.getB(), 
									this.isVoxelNull(x + 1, y, z + 1), this.isVoxelNull(x + 1, y + 1, z), 
									this.isVoxelNull(x + 1, y + 1, z + 1)
							); // OK
							this.addVertexWithAO(
									x + 1f, y + 0f, z + 1f, v.getR(), v.getG(), v.getB(), 
									this.isVoxelNull(x + 1, y, z + 1), this.isVoxelNull(x + 1, y - 1, z), 
									this.isVoxelNull(x + 1, y - 1, z + 1)
							); // OK
							this.addVertexWithAO(
									x + 1f, y + 0f, z + 0f, v.getR(), v.getG(), v.getB(), 
									this.isVoxelNull(x + 1, y, z - 1), this.isVoxelNull(x + 1, y - 1, z), 
									this.isVoxelNull(x + 1, y - 1, z - 1)
							); // OK
						}

					}
				}
			}
		}

		GL11.glEnd();

		GL11.glEndList();
	}

	private void addVertexWithAO(float x, float y, float z, float r, float g, float b, boolean s1, boolean s2, boolean c) {
		int ao;

		if(!s1 && !s2) {
			ao = 3;
		} else {
			ao = 0;
			if (!s1)
				ao++;
			if (!s2)
				ao++;
			if (!c)
				ao++;
		}

		float aom = ao * Settings.getInstance().aoIntensity.getValue();

		GL11.glColor3f(r * (1.0f - aom), g * (1.0f - aom), b * (1.0f - aom));
		GL11.glVertex3f(x, y, z);
	}

	private boolean isVoxelNull(int x, int y, int z) {

		if (x < 0 || x >= this.width) {
			return true;
		}

		if (y < 0 || y >= this.height) {
			return true;
		}

		if (z < 0 || z >= this.depth) {
			return true;
		}

		return this.voxels[x][y][z] == null;

	}

	public void createArray() {
		this.voxels = new Voxel[this.width][this.height][this.depth];
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getDepth() {
		return this.depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getDisplayList() {
		return this.displayList;
	}

}
