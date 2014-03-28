package dax.blocks.block;

public class BlockFluid extends BlockBasic {

	public BlockFluid(int id, int texture) {
		super(id, texture, false, true);
	}
	
	@Override
	public float[] getVTop(int x, int y, int z) {
		float[] v = new float[12];

		v[0] = x + 1;
		v[1] = y + 0.85f;
		v[2] = z + 1;

		v[3] = x + 1;
		v[4] = y + 0.85f;
		v[5] = z;

		v[6] = x;
		v[7] = y + 0.85f;
		v[8] = z;

		v[9] = x;
		v[10] = y + 0.85f;
		v[11] = z + 1;

		return v;
	}

}
