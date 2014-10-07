package cz.dat.oots.inventory;

import cz.dat.oots.inventory.renderer.IObjectStackRenderer;
import cz.dat.oots.world.World;

public interface IObjectStack {
	public int getMaximumItemsPerStack();
	public int getItemID();
	public String getShowedName();
	public String getShowedDescription();
	public int getCurrentItemsCount();
	public void setCurrentItemsCount(int count) throws IllegalArgumentException;
	
	public void notifyDeletion();
	
	public boolean addItem();
	public boolean removeItem();
	public boolean shouldRecycle();
	
	public void useItem(int mouseButton, int x, int y, int z, int item, World world) throws IllegalArgumentException;
	public void tickItems(World w);
	public void renderTickItems(float partialTickTime, World w);
	
	public IObjectStackRenderer getRenderer();
}
