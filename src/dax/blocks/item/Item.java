package dax.blocks.item;

public abstract class Item {
	//Special positions: 0 - null item; 1 - null block; 2 - air
	private static Item[] allItems = new Item[512];
	private static int itemsCount;
	
	public static Item nullItem = new ItemEmpty();
	
	public static void registerItem(Item i, int id) throws RegisterException {
		if(id < allItems.length) {
			if(allItems[id] == null) {
				allItems[id] = i;
				itemsCount++;
				return;
			}
		}
		
		throw new RegisterException(i, id);
	}
	
	public static int getItemsCount() {
		return itemsCount;
	}
	
	public static Item getItem(int id) {
		if(id < allItems.length)
			return allItems[id];
		else
			return null;
	}
	
	
	private int id;
	
	public Item(int itemID) {
		this.id = itemID;
		
		try {
			Item.registerItem(this, id);
		} catch (RegisterException e) {
			e.printStackTrace();
		}
	}
	
	public int getId() {
		return this.id;

	}
	
	public abstract void onTick(int itemIdentifier);
}
