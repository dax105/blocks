package dax.blocks.block;

import dax.blocks.TextureManager;

public class BlockBasic extends Block {

	public int topTexture;
	public int sideTexture;
	public int bottomTexture;

	public BlockBasic(int id, int texture) {
		super(id);
		setTexture(texture);
	}

	public BlockBasic(int id, int texture, boolean opaque) {
		super(id, opaque);
		setTexture(texture);
	}

	public BlockBasic(int id, int topTexture, int sideTexture, int bottomTexture) {
		super(id);
		setTopTexture(topTexture);
		setSideTexture(sideTexture);
		setBottomTexture(bottomTexture);
	}

	public BlockBasic(int id, int topTexture, int sideTexture, int bottomTexture, boolean opaque) {
		super(id, opaque);
		setTopTexture(topTexture);
		setSideTexture(sideTexture);
		setBottomTexture(bottomTexture);
	}

	public void setTopTexture(int topTexture) {
		this.topTexture = topTexture;
	}

	public void setSideTexture(int sideTexture) {
		this.sideTexture = sideTexture;
	}

	public void setBottomTexture(int bottomTexture) {
		this.bottomTexture = bottomTexture;
	}

	public void setTexture(int texture) {
		this.topTexture = texture;
		this.sideTexture = texture;
		this.bottomTexture = texture;
	}

	@Override
	public float[] getVTop(int x, int y, int z) {
		float[] v = new float[12];

		v[0] = x + 1;
		v[1] = y + 1;
		v[2] = z + 1;

		v[3] = x + 1;
		v[4] = y + 1;
		v[5] = z;

		v[6] = x;
		v[7] = y + 1;
		v[8] = z;

		v[9] = x;
		v[10] = y + 1;
		v[11] = z + 1;

		return v;
	}

	@Override
	public float[] getVBottom(int x, int y, int z) {
		float[] v = new float[12];

		v[0] = x;
		v[1] = y;
		v[2] = z;

		v[3] = x + 1;
		v[4] = y;
		v[5] = z;

		v[6] = x + 1;
		v[7] = y;
		v[8] = z + 1;

		v[9] = x;
		v[10] = y;
		v[11] = z + 1;

		return v;
	}

	@Override
	public float[] getVLeft(int x, int y, int z) {
		float[] v = new float[12];

		v[0] = x;
		v[1] = y;
		v[2] = z;

		v[3] = x;
		v[4] = y;
		v[5] = z + 1;

		v[6] = x;
		v[7] = y + 1;
		v[8] = z + 1;

		v[9] = x;
		v[10] = y + 1;
		v[11] = z;

		return v;
	}

	@Override
	public float[] getVRight(int x, int y, int z) {
		float[] v = new float[12];

		v[0] = x + 1;
		v[1] = y + 1;
		v[2] = z + 1;

		v[3] = x + 1;
		v[4] = y;
		v[5] = z + 1;

		v[6] = x + 1;
		v[7] = y;
		v[8] = z;

		v[9] = x + 1;
		v[10] = y + 1;
		v[11] = z;

		return v;
	}

	@Override
	public float[] getVFront(int x, int y, int z) {
		float[] v = new float[12];

		v[0] = x + 1;
		v[1] = y + 1;
		v[2] = z + 1;

		v[3] = x;
		v[4] = y + 1;
		v[5] = z + 1;

		v[6] = x;
		v[7] = y;
		v[8] = z + 1;

		v[9] = x + 1;
		v[10] = y;
		v[11] = z + 1;

		return v;
	}

	@Override
	public float[] getVBack(int x, int y, int z) {
		float[] v = new float[12];

		v[0] = x;
		v[1] = y;
		v[2] = z;

		v[3] = x;
		v[4] = y + 1;
		v[5] = z;

		v[6] = x + 1;
		v[7] = y + 1;
		v[8] = z;

		v[9] = x + 1;
		v[10] = y;
		v[11] = z;

		return v;
	}

	@Override
	public float[] getTTop() {
		float[] v = new float[8];

		v[0] = TextureManager.getX2(topTexture);
		v[1] = TextureManager.getY1(topTexture);
		v[2] = TextureManager.getX1(topTexture);
		v[3] = TextureManager.getY1(topTexture);
		v[4] = TextureManager.getX1(topTexture);
		v[5] = TextureManager.getY2(topTexture);
		v[6] = TextureManager.getX2(topTexture);
		v[7] = TextureManager.getY2(topTexture);

		return v;
	}

	@Override
	public float[] getTBottom() {
		float[] v = new float[8];

		v[0] = TextureManager.getX1(bottomTexture);
		v[1] = TextureManager.getY1(bottomTexture);
		v[2] = TextureManager.getX1(bottomTexture);
		v[3] = TextureManager.getY2(bottomTexture);
		v[4] = TextureManager.getX2(bottomTexture);
		v[5] = TextureManager.getY2(bottomTexture);
		v[6] = TextureManager.getX2(bottomTexture);
		v[7] = TextureManager.getY1(bottomTexture);

		return v;
	}

	@Override
	public float[] getTLeft() {
		float[] v = new float[8];

		v[0] = TextureManager.getX1(sideTexture);
		v[1] = TextureManager.getY2(sideTexture);
		v[2] = TextureManager.getX2(sideTexture);
		v[3] = TextureManager.getY2(sideTexture);
		v[4] = TextureManager.getX2(sideTexture);
		v[5] = TextureManager.getY1(sideTexture);
		v[6] = TextureManager.getX1(sideTexture);
		v[7] = TextureManager.getY1(sideTexture);

		return v;
	}

	@Override
	public float[] getTRight() {
		float[] v = new float[8];

		v[0] = TextureManager.getX1(sideTexture);
		v[1] = TextureManager.getY1(sideTexture);
		v[2] = TextureManager.getX1(sideTexture);
		v[3] = TextureManager.getY2(sideTexture);
		v[4] = TextureManager.getX2(sideTexture);
		v[5] = TextureManager.getY2(sideTexture);
		v[6] = TextureManager.getX2(sideTexture);
		v[7] = TextureManager.getY1(sideTexture);

		return v;
	}

	@Override
	public float[] getTFront() {
		float[] v = new float[8];

		v[0] = TextureManager.getX2(sideTexture);
		v[1] = TextureManager.getY1(sideTexture);
		v[2] = TextureManager.getX1(sideTexture);
		v[3] = TextureManager.getY1(sideTexture);
		v[4] = TextureManager.getX1(sideTexture);
		v[5] = TextureManager.getY2(sideTexture);
		v[6] = TextureManager.getX2(sideTexture);
		v[7] = TextureManager.getY2(sideTexture);

		return v;
	}

	@Override
	public float[] getTBack() {
		float[] v = new float[8];

		v[0] = TextureManager.getX2(sideTexture);
		v[1] = TextureManager.getY2(sideTexture);
		v[2] = TextureManager.getX2(sideTexture);
		v[3] = TextureManager.getY1(sideTexture);
		v[4] = TextureManager.getX1(sideTexture);
		v[5] = TextureManager.getY1(sideTexture);
		v[6] = TextureManager.getX1(sideTexture);
		v[7] = TextureManager.getY2(sideTexture);

		return v;
	}

	@Override
	public float[] getNTop() {
		float[] n = new float[12];

		n[0] = 0;
		n[1] = 1;
		n[2] = 0;

		n[3] = 0;
		n[4] = 1;
		n[5] = 0;

		n[6] = 0;
		n[7] = 1;
		n[8] = 0;

		n[9] = 0;
		n[10] = 1;
		n[11] = 0;

		return n;
	}

	@Override
	public float[] getNBottom() {
		float[] n = new float[12];

		n[0] = 0;
		n[1] = 0;
		n[2] = 0;

		n[3] = 0;
		n[4] = 0;
		n[5] = 0;

		n[6] = 0;
		n[7] = 0;
		n[8] = 0;

		n[9] = 0;
		n[10] = 0;
		n[11] = 0;

		return n;
	}

	@Override
	public float[] getNLeft() {
		float[] n = new float[12];

		n[0] = 1;
		n[1] = 0;
		n[2] = 1;

		n[3] = 1;
		n[4] = 0;
		n[5] = 1;

		n[6] = 1;
		n[7] = 0;
		n[8] = 1;

		n[9] = 1;
		n[10] = 0;
		n[11] = 1;

		return n;
	}

	@Override
	public float[] getNRight() {
		float[] n = new float[12];

		n[0] = 1;
		n[1] = 0;
		n[2] = 1;

		n[3] = 1;
		n[4] = 0;
		n[5] = 1;

		n[6] = 1;
		n[7] = 0;
		n[8] = 1;

		n[9] = 1;
		n[10] = 0;
		n[11] = 1;

		return n;
	}

	@Override
	public float[] getNFront() {
		float[] n = new float[12];

		n[0] = -0.5f;
		n[1] = 0;
		n[2] = 0;

		n[3] = -0.5f;
		n[4] = 0;
		n[5] = 0;

		n[6] = -0.5f;
		n[7] = 0;
		n[8] = 0;

		n[9] = -0.5f;
		n[10] = 0;
		n[11] = 0;

		return n;
	}

	@Override
	public float[] getNBack() {
		float[] n = new float[12];

		n[0] = -0.5f;
		n[1] = 0;
		n[2] = 0;

		n[3] = -0.5f;
		n[4] = 0;
		n[5] = 0;

		n[6] = -0.5f;
		n[7] = 0;
		n[8] = 0;

		n[9] = -0.5f;
		n[10] = 0;
		n[11] = 0;

		return n;
	}

}
