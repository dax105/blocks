package dax.blocks.model;

import org.lwjgl.opengl.GL11;

import dax.blocks.Game;
import dax.blocks.settings.Settings;

public class Model {

	private int width, height, depth;

	Voxel[][][] voxels;

	private int displayList = -1;

	public void generateDisplayList() {
		displayList = GL11.glGenLists(1);
		GL11.glNewList(displayList, GL11.GL_COMPILE);

		GL11.glColor3f(1, 1, 1);
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glBegin(GL11.GL_QUADS);

		for (int x = 0; x < voxels.length; x++) {
			for (int y = 0; y < voxels[0].length; y++) {
				for (int z = 0; z < voxels[0][0].length; z++) {
					Voxel v = voxels[x][y][z];

					if (v != null) {
						// Top
						if (isVoxelNull(x, y + 1, z)) {
							GL11.glNormal3f(0, 1.0f, 0);
							addVertexWithAO(x + 1f, y + 1f, z + 0f, v.getR(), v.getG(), v.getB(), isVoxelNull(x + 1, y + 1, z), isVoxelNull(x, y + 1, z - 1), isVoxelNull(x + 1, y + 1, z - 1)); // OK
							addVertexWithAO(x + 0f, y + 1f, z + 0f, v.getR(), v.getG(), v.getB(), isVoxelNull(x - 1, y + 1, z), isVoxelNull(x, y + 1, z - 1), isVoxelNull(x - 1, y + 1, z - 1)); // OK
							addVertexWithAO(x + 0f, y + 1f, z + 1f, v.getR(), v.getG(), v.getB(), isVoxelNull(x - 1, y + 1, z), isVoxelNull(x, y + 1, z + 1), isVoxelNull(x - 1, y + 1, z + 1)); // OK
							addVertexWithAO(x + 1f, y + 1f, z + 1f, v.getR(), v.getG(), v.getB(), isVoxelNull(x + 1, y + 1, z), isVoxelNull(x, y + 1, z + 1), isVoxelNull(x + 1, y + 1, z + 1)); // OK
						}

						//Bottom
						if (isVoxelNull(x, y - 1, z)) {
							GL11.glNormal3f(0, -1.0f, 0);
							addVertexWithAO(x + 1f, y + 0f, z + 1f, v.getR(), v.getG(), v.getB(), isVoxelNull(x + 1, y - 1, z), isVoxelNull(x, y - 1, z + 1), isVoxelNull(x + 1, y - 1, z + 1)); // OK
							addVertexWithAO(x + 0f, y + 0f, z + 1f, v.getR(), v.getG(), v.getB(), isVoxelNull(x - 1, y - 1, z), isVoxelNull(x, y - 1, z + 1), isVoxelNull(x - 1, y - 1, z + 1)); // OK
							addVertexWithAO(x + 0f, y + 0f, z + 0f, v.getR(), v.getG(), v.getB(), isVoxelNull(x - 1, y - 1, z), isVoxelNull(x, y - 1, z - 1), isVoxelNull(x - 1, y - 1, z - 1)); // OK
							addVertexWithAO(x + 1f, y + 0f, z + 0f, v.getR(), v.getG(), v.getB(), isVoxelNull(x + 1, y - 1, z), isVoxelNull(x, y - 1, z - 1), isVoxelNull(x + 1, y - 1, z - 1)); // OK
						}

						//Front
						if (isVoxelNull(x, y, z + 1)) {
							GL11.glNormal3f(0.0f, 0.0f, 1.0f);
							addVertexWithAO(x + 1f, y + 1f, z + 1f, v.getR(), v.getG(), v.getB(), isVoxelNull(x, y + 1, z + 1), isVoxelNull(x + 1, y, z + 1), isVoxelNull(x + 1, y + 1, z + 1)); // OK
							addVertexWithAO(x + 0f, y + 1f, z + 1f, v.getR(), v.getG(), v.getB(), isVoxelNull(x - 1, y, z + 1), isVoxelNull(x, y + 1, z + 1), isVoxelNull(x - 1, y + 1, z + 1)); // OK
							addVertexWithAO(x + 0f, y + 0f, z + 1f, v.getR(), v.getG(), v.getB(), isVoxelNull(x - 1, y, z + 1), isVoxelNull(x, y - 1, z + 1), isVoxelNull(x - 1, y - 1, z + 1)); // OK
							addVertexWithAO(x + 1f, y + 0f, z + 1f, v.getR(), v.getG(), v.getB(), isVoxelNull(x, y - 1, z + 1), isVoxelNull(x + 1, y, z + 1), isVoxelNull(x + 1, y - 1, z + 1)); // OK
						}

						//Back
						if (isVoxelNull(x, y, z - 1)) {	
							GL11.glNormal3f(0.0f, 0.0f, -1.0f);
							addVertexWithAO(x + 1f, y + 0f, z + 0f, v.getR(), v.getG(), v.getB(), isVoxelNull(x, y - 1, z - 1), isVoxelNull(x + 1, y, z - 1), isVoxelNull(x + 1, y - 1, z - 1)); // OK
							addVertexWithAO(x + 0f, y + 0f, z + 0f, v.getR(), v.getG(), v.getB(), isVoxelNull(x - 1, y, z - 1), isVoxelNull(x, y - 1, z - 1), isVoxelNull(x - 1, y - 1, z - 1)); // OK
							addVertexWithAO(x + 0f, y + 1f, z + 0f, v.getR(), v.getG(), v.getB(), isVoxelNull(x - 1, y, z - 1), isVoxelNull(x, y + 1, z - 1), isVoxelNull(x - 1, y + 1, z - 1)); // OK
							addVertexWithAO(x + 1f, y + 1f, z + 0f, v.getR(), v.getG(), v.getB(), isVoxelNull(x, y + 1, z - 1), isVoxelNull(x + 1, y, z - 1), isVoxelNull(x + 1, y + 1, z - 1)); // OK
						}

						//Left
						if (isVoxelNull(x - 1, y, z)) {
							GL11.glNormal3f(-1.0f, 0.0f, 0);
							addVertexWithAO(x + 0f, y + 1f, z + 1f, v.getR(), v.getG(), v.getB(), isVoxelNull(x - 1, y, z + 1), isVoxelNull(x - 1, y + 1, z), isVoxelNull(x - 1, y + 1, z + 1)); // OK
							addVertexWithAO(x + 0f, y + 1f, z + 0f, v.getR(), v.getG(), v.getB(), isVoxelNull(x - 1, y, z - 1), isVoxelNull(x - 1, y + 1, z), isVoxelNull(x - 1, y + 1, z - 1)); // OK
							addVertexWithAO(x + 0f, y + 0f, z + 0f, v.getR(), v.getG(), v.getB(), isVoxelNull(x - 1, y, z - 1), isVoxelNull(x - 1, y - 1, z), isVoxelNull(x - 1, y - 1, z - 1)); // OK
							addVertexWithAO(x + 0f, y + 0f, z + 1f, v.getR(), v.getG(), v.getB(), isVoxelNull(x - 1, y, z + 1), isVoxelNull(x - 1, y - 1, z), isVoxelNull(x - 1, y - 1, z + 1)); // OK
						}

						//Right
						if (isVoxelNull(x + 1, y, z)) {
							GL11.glNormal3f(1.0f, 0.0f, 0);
							addVertexWithAO(x + 1f, y + 1f, z + 0f, v.getR(), v.getG(), v.getB(), isVoxelNull(x + 1, y, z - 1), isVoxelNull(x + 1, y + 1, z), isVoxelNull(x + 1, y + 1, z - 1)); // OK
							addVertexWithAO(x + 1f, y + 1f, z + 1f, v.getR(), v.getG(), v.getB(), isVoxelNull(x + 1, y, z + 1), isVoxelNull(x + 1, y + 1, z), isVoxelNull(x + 1, y + 1, z + 1)); // OK
							addVertexWithAO(x + 1f, y + 0f, z + 1f, v.getR(), v.getG(), v.getB(), isVoxelNull(x + 1, y, z + 1), isVoxelNull(x + 1, y - 1, z), isVoxelNull(x + 1, y - 1, z + 1)); // OK
							addVertexWithAO(x + 1f, y + 0f, z + 0f, v.getR(), v.getG(), v.getB(), isVoxelNull(x + 1, y, z - 1), isVoxelNull(x + 1, y - 1, z), isVoxelNull(x + 1, y - 1, z - 1)); // OK
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

		if (!s1 && !s2) {
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

		if (x < 0 || x >= width) {
			return true;
		}

		if (y < 0 || y >= height) {
			return true;
		}

		if (z < 0 || z >= depth) {
			return true;
		}

		return voxels[x][y][z] == null;

	}

	public void createArray() {
		voxels = new Voxel[width][height][depth];
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getDisplayList() {
		return displayList;
	}

}
