package cz.dat.oots.world.generator;

import cz.dat.oots.util.GameMath;

import java.util.Arrays;

public abstract class Biome {

    public static final Biome mountains = new BiomeMountains();
    public static final Biome plains = new BiomePlains();
    public static final Biome extraMountains = new BiomeExtraMountains();

    private double[] tempOffsets = new double[129];

    private String name;
    private double[] densityOffsets = new double[129];

    public Biome(String name) {
        this.name = name;
        Arrays.fill(tempOffsets, -9999);
        this.setOffsets();
        this.applyOffsets();

        Biome.registerBiome(this);
    }

    public static void registerBiome(Biome b) {
        // TODO Do something!
    }

    public double[] getOffsets() {
        return this.densityOffsets;
    }

    public String getName() {
        return this.name;
    }

    public void setOffset(int h, double val) {
        this.tempOffsets[h] = val;
    }

    public void applyOffsets() {
        for (int i = 0; i < 129; i++) {
            if (this.tempOffsets[i] != -9999) {
                this.densityOffsets[i] = this.tempOffsets[i];
            } else {
                int lp = 0;
                int hp = 0;

                for (int x = i; x >= 0; x--) {
                    if (this.tempOffsets[x] != -9999) {
                        lp = x;
                        break;
                    }
                }

                for (int x = i; x < 129; x++) {
                    if (this.tempOffsets[x] != -9999) {
                        hp = x;
                        break;
                    }
                }

                this.densityOffsets[i] = GameMath.lerp(i, lp, hp,
                        this.tempOffsets[lp], this.tempOffsets[hp]);
            }
        }

        this.tempOffsets = null;

    }

    public abstract void setOffsets();
}
