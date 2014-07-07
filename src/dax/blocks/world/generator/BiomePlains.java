package dax.blocks.world.generator;

public class BiomePlains extends Biome {

	public BiomePlains() {
		super("plains");
	}

	@Override
	public void setOffsets() {
		setOffset(0, 1.2);
		setOffset(38, 0.5);
		setOffset(40, 0.3);
		setOffset(52, 0.0);
		setOffset(80, -1.2);
		setOffset(127, -1.2);
	}

}
