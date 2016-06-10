package cz.dat.oots.world.generator;

public class BiomePlains extends Biome {

    public BiomePlains() {
        super("plains");
    }

    @Override
    public void setOffsets() {
        this.setOffset(0, 1.2f);
        this.setOffset(38, 0.5f);
        this.setOffset(40, 0.3f);
        this.setOffset(52, 0.0f);
        this.setOffset(80, -1.2f);
        this.setOffset(127, -1.2f);
    }

}
