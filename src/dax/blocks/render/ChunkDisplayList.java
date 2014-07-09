package dax.blocks.render;

import java.util.Arrays;

import org.lwjgl.opengl.GL11;

public class ChunkDisplayList {
	
	private int[] listIDs = new int[RenderPass.TOTAL_PASSES];
	private boolean[] listsPresent = new boolean[RenderPass.TOTAL_PASSES];
	
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
		for (int i = 0; i < RenderPass.TOTAL_PASSES; i++) {
			if (listsPresent[i]) {
				GL11.glDeleteLists(listIDs[i], 1);
				listsPresent[i] = false;
			}
		}
	}
	
}
