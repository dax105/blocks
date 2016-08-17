package cz.dat.oots.world.generator;

public class GenTempStorage {
	
    private float[][][] tempStorageT;
	private float[][][] tempStorageC;
	
	public GenTempStorage() {
		this.tempStorageT = new float[16 + 1][128 + 1][16 + 1];
		this.tempStorageC = new float[16 + 1][128 + 1][16 + 1];
	}
	
    public float[][][] getTempStorageT() {
		return tempStorageT;
	}

	public float[][][] getTempStorageC() {
		return tempStorageC;
	}

}
