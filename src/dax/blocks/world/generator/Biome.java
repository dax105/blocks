package dax.blocks.world.generator;

import java.util.Arrays;

import dax.blocks.GameMath;

public abstract class Biome {

	public static final Biome mountains = new BiomeMountains();
	
	private double[] tempOffsets = new double[128];
	
	private String name;
	private double[] densityOffsets = new double[128];
	
	public Biome(String name) {
		name = this.name;
		Arrays.fill(tempOffsets, -9999);
		setOffsets();
		applyOffsets();
		registerBiome(this);
	}
	
	public double[] getOffsets() {
		return this.densityOffsets;
	}
	
	public String getName() {
		return this.name;
	}

	public static void registerBiome(Biome b) {
		//TODO Do something!
	}
	
	public void setOffset(int h, double val) {
		tempOffsets[h] = val;
	}
	
	public void applyOffsets() {
		for (int i = 0; i < 128; i++) {
			if (tempOffsets[i] != -9999) {
				densityOffsets[i] = tempOffsets[i];
			} else {
				int lp = 0;
				int hp = 0;
				
				for (int x = i; x >= 0; x--) {
					if (tempOffsets[x] != -9999) {
						lp = x;
						break;
					}
				}
				
				for (int x = i; x < 128; x++) {
					if (tempOffsets[x] != -9999) {
						hp = x;
						break;
					}
				}
				
				densityOffsets[i] = GameMath.lerp(i, lp, hp, tempOffsets[lp], tempOffsets[hp]);
			}
		}
		
		tempOffsets = null;
		
	}

	public abstract void setOffsets();
	
}
