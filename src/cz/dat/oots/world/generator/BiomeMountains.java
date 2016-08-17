package cz.dat.oots.world.generator;

public class BiomeMountains extends Biome {

    public BiomeMountains() {
        super("mountains");
    }

    @Override
    public void setOffsets() {
        this.setOffset(0, 1.2f);
        this.setOffset(38, 0.5f);
        this.setOffset(40, 0.3f);
        this.setOffset(52, 0.0f);
        this.setOffset(56, -0.1f);
        this.setOffset(60, -0.14f);
        this.setOffset(68, -0.175f);
        this.setOffset(75, -0.2f);
        this.setOffset(80, -0.3f);
        this.setOffset(82, -0.4f);
        this.setOffset(85, -0.5f);
        this.setOffset(127, -1.2f);
    }

}
