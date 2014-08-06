package dax.blocks.world.generator;

import java.util.Arrays;

import dax.blocks.GameMath;

public abstract class Biome {

	public static final Biome mountains = new BiomeMountains();
	public static final Biome plains = new BiomePlains();
	
	private double[] tempOffsets = new double[129];
	
	private String name;
	private double[] densityOffsets = new double[129];
	
	public Biome(String name) {
		this.name = name;
		Arrays.fill(tempOffsets, -9999);
		this.setOffsets();
		this.applyOffsets();
		this.registerBiome(this);
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
		this.tempOffsets[h] = val;
	}
	
	public void applyOffsets() {
		for(int i = 0; i < 129; i++) {
			if(this.tempOffsets[i] != -9999) {
				this.densityOffsets[i] = this.tempOffsets[i];
			} else {
				int lp = 0;
				int hp = 0;
				
				for(int x = i; x >= 0; x--) {
					if(this.tempOffsets[x] != -9999) {
						lp = x;
						break;
					}
				}
				
				for(int x = i; x < 129; x++) {
					if(this.tempOffsets[x] != -9999) {
						hp = x;
						break;
					}
				}
				
				this.densityOffsets[i] = GameMath.lerp(i, lp, hp, this.tempOffsets[lp], this.tempOffsets[hp]);
			}
		}
		
		this.tempOffsets = null;
		
	}

	public abstract void setOffsets();
}
