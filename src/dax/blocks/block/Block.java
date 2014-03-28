package dax.blocks.block;

public abstract class Block {

	private final byte id;
	private final boolean opaque;
	private final boolean cullSame;

	public boolean shouldCullSame() {
		return this.cullSame;
	}

	public Block(int id) {
		this.id = (byte) id;
		this.opaque = true;
		this.cullSame = false;
		blocks[id] = this;
	}

	public Block(int id, boolean opaque) {
		this.id = (byte) id;
		this.opaque = opaque;
		this.cullSame = false;
		blocks[id] = this;
	}
	
	public Block(int id, boolean opaque, boolean cullSame) {
		this.id = (byte) id;
		this.opaque = opaque;
		this.cullSame = cullSame;
		blocks[id] = this;
	}

	public static Block[] blocks = new Block[256];

	public static final Block grass = new BlockBasic(1, 4, 5, 3);
	public static final Block dirt = new BlockBasic(2, 3);
	public static final Block stone = new BlockBasic(3, 0);
	public static final Block wood = new BlockBasic(4, 2);
	public static final Block stoneMossy = new BlockBasic(5, 1);
	public static final Block bricks = new BlockBasic(6, 8);
	public static final Block sand = new BlockBasic(7, 6);
	public static final Block log = new BlockBasic(8, 11, 7, 11);
	public static final Block glass = new BlockBasic(9, 9, false, true);
	public static final Block leaves = new BlockBasic(10, 10, false);
	public static final Block bedrock = new BlockBasic(11, 12);
	public static final Block water = new BlockFluid(12, 13);

	public byte getId() {
		return this.id;
	}

	public boolean isOpaque() {
		return this.opaque;
	}

	public static Block getBlock(byte id) {
		return blocks[id];
	}

	public abstract float[] getVTop(int x, int y, int z);

	public abstract float[] getVBottom(int x, int y, int z);

	public abstract float[] getVLeft(int x, int y, int z);

	public abstract float[] getVRight(int x, int y, int z);

	public abstract float[] getVFront(int x, int y, int z);

	public abstract float[] getVBack(int x, int y, int z);

	public abstract float[] getTTop();

	public abstract float[] getTBottom();

	public abstract float[] getTLeft();

	public abstract float[] getTRight();

	public abstract float[] getTFront();

	public abstract float[] getTBack();

	public abstract float[] getNTop();

	public abstract float[] getNBottom();

	public abstract float[] getNLeft();

	public abstract float[] getNRight();

	public abstract float[] getNFront();

	public abstract float[] getNBack();

}
