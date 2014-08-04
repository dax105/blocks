package dax.blocks.item;

public class RegisterException extends Exception {

	private static final long serialVersionUID = -6278217466183667900L;
	private int causeID;
	private Item causeItem;
	
	public RegisterException(Item item, int id) {
		super("Can't register item " + item.toString() + " at ID " + id + ", because this ID is already registered!");
		
		this.causeID = id;
		this.causeItem = item;
	}
	
	public int getIDCaused() {
		return this.causeID;
	}
	
	public Item getItemCaused() {
		return this.causeItem;
	}
}
