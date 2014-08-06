package dax.blocks.item;

public interface IItemStack {
	public int getMaximumItemsPerStack();
	public int getItemID();
	public int getItemTexture();
	public String getShowedName();
	public String getShowedDescription();
	public int getCurrentItemsCount();
	
	public void tickItems();
	public void renderTickItems(float partialTickTime);
}
