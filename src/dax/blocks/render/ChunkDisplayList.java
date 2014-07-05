package dax.blocks.render;

import java.util.Arrays;

import org.lwjgl.opengl.GL11;

public class ChunkDisplayList {

	public static final int TOTAL_PASSES = 3;
	
	public static final int PASS_OPAQUE = 0;
	public static final int PASS_TRANSPARENT = 1;
	public static final int PASS_TRANSLUCENT = 2;
	
	private int[] listIDs = new int[TOTAL_PASSES];
	private boolean[] listsPresent = new boolean[TOTAL_PASSES];
	
	public ChunkDisplayList() {
		Arrays.fill(listsPresent, false);
	}
	
	public void setListID(int pass, int id) {
		listsPresent[pass] = true;
		listIDs[pass] = id;
	}
	
	public int getListID(int pass) {
		return listIDs[pass];
	}
	
	public boolean isPresent(int pass) {
		return listsPresent[pass];
	}
	
	public void delete() {
		for (int i = 0; i < TOTAL_PASSES; i++) {
			if (listsPresent[i]) {
				GL11.glDeleteLists(listIDs[i], 1);
			}
		}
	}
	
}
