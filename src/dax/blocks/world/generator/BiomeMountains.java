package dax.blocks.world.generator;

public class BiomeMountains extends Biome {

	public BiomeMountains() {
		super("mountains");
	}

	@Override
	public void setOffsets() {
		setOffset(0, 1.2);
		setOffset(38, 0.5);
		setOffset(40, 0.3);
		setOffset(52, 0.0);
		setOffset(56, -0.1);
		setOffset(60, -0.14);
		setOffset(68, -0.175);
		setOffset(75, -0.2);
		setOffset(80, -0.3);
		setOffset(82, -0.4);
		setOffset(85, -0.5);
		setOffset(127, -1.2);
	}

}
