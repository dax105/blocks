package dax.blocks.gui.ingame;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dax.blocks.inventory.IInventory;

public abstract class MultiInventoryScreen extends GuiScreen {

	private List<IInventory> inventories;
	//private Map<IInventory, Rectangle> inventoryControls;
	
 	public MultiInventoryScreen(int width, int height, float r, float g, float b,
			float a, GuiManager guiManager) {
		super(width, height, r, g, b, a, guiManager);
		this.createInventories();
	}

	public MultiInventoryScreen(int width, int height,
			GuiManager guiManager) {
		super(width, height, guiManager);
		this.createInventories();
	}

	public MultiInventoryScreen(int x, int y, int width, int height, float r,
			float g, float b, float a,
			GuiManager guiManager) {
		super(x, y, width, height, r, g, b, a, guiManager);
		this.createInventories();
	}

	public MultiInventoryScreen(int x, int y, int width, int height,
			 GuiManager guiManager) {
		super(x, y, width, height, guiManager);
		this.createInventories();
	}

	private void createInventories() {
		this.inventories = new LinkedList<IInventory>();
	}
	
	@Override
	public void renderOverlay(float ptt) {
		super.renderOverlay(ptt);

	}

}
