package cz.dat.oots.inventory;

public interface IInventory {
	public void init(int maxItems);

	public int getMaxItems();

	public void putStack(int position, IObjectStack stack);

	public void moveStack(int position, int newPosition);

	public int getItemCount();

	public IObjectStack getItem(int position);

	public void removeItem(int position);
}
