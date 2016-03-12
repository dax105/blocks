package cz.dat.oots.world.generator;

public class BiomePlains extends Biome {

    public BiomePlains() {
        super("plains");
    }

    @Override
    public void setOffsets() {
        this.setOffset(0, 1.2);
        this.setOffset(38, 0.5);
        this.setOffset(40, 0.3);
        this.setOffset(52, 0.0);
        this.setOffset(80, -1.2);
        this.setOffset(127, -1.2);
    }

}
