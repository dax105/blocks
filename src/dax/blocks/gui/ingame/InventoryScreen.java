package dax.blocks.gui.ingame;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dax.blocks.inventory.IInventory;

public abstract class InventoryScreen extends GuiScreen {

	private IInventory inventory;
	//private Map<IInventory, Rectangle> inventoryControls;
	
 	public InventoryScreen(int width, int height, float r, float g, float b,
			float a, IInventory invent, GuiManager guiManager) {
		super(width, height, r, g, b, a, guiManager);
		this.updateInventory(invent);
	}

	public InventoryScreen(int width, int height,
			IInventory invent, GuiManager guiManager) {
		super(width, height, guiManager);
		this.updateInventory(invent);
	}

	public InventoryScreen(int x, int y, int width, int height, float r,
			float g, float b, float a, IInventory invent,
			GuiManager guiManager) {
		super(x, y, width, height, r, g, b, a, guiManager);
		this.updateInventory(invent);
	}

	public InventoryScreen(int x, int y, int width, int height,
			IInventory invent, GuiManager guiManager) {
		super(x, y, width, height, guiManager);
		this.updateInventory(invent);
	}

	private void updateInventory(IInventory inventory) {
		this.inventory = inventory;
	}
	
	@Override
	public void renderOverlay(float ptt) {
		super.renderOverlay(ptt);

	}

}
