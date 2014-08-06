package dax.blocks.item;

public abstract class Item {
	
	//Special positions: 0 - null item; 1 - null block; 2 - air
	private static Item[] allItems = new Item[512];
	private static int itemsCount;
	public static Item nullItem = new ItemEmpty();
	
	private int id;
	
	public static void registerItem(Item i, int id) throws RegisterException {
		if(id < Item.allItems.length) {
			if(Item.allItems[id] == null) {
				Item.allItems[id] = i;
				Item.itemsCount++;
				return;
			}
		}
		
		throw new RegisterException(i, id);
	}
	
	public static int getItemsCount() {
		return Item.itemsCount;
	}
	
	public static Item getItem(int id) {
		if(id < Item.allItems.length)
			return Item.allItems[id];
		else
			return null;
	}
	
	public Item(int itemID) {
		this.id = itemID;
		
		try {
			Item.registerItem(this, this.id);
		} catch (RegisterException e) {
			e.printStackTrace();
		}
	}
	
	public int getId() {
		return this.id;
	}
	
	public abstract void onTick(int itemIdentifier);
}
