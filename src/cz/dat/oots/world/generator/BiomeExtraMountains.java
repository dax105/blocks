package cz.dat.oots.world.generator;

public class BiomeExtraMountains extends Biome {

	public BiomeExtraMountains() {
		super("extraMountains");
	}

	@Override
	public void setOffsets() {
		this.setOffset(0, 1.2);
		this.setOffset(38, 0.5);
		this.setOffset(40, 0.3);
		this.setOffset(52, 0.0);
		this.setOffset(56, -0.1);
		this.setOffset(60, -0.14);
		this.setOffset(68, -0.175);
		this.setOffset(75, -0.2);
		this.setOffset(80, -0.3);
		this.setOffset(82, -0.2);
		this.setOffset(85, -0.2);
		this.setOffset(100, -0.3);
		this.setOffset(120, -0.4);		
		this.setOffset(127, -1.2);
	}

}
