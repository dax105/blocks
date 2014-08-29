package cz.dat.oots.inventory;

import java.util.Random;

import cz.dat.oots.item.Item;
import cz.dat.oots.util.GLHelper;
import cz.dat.oots.world.World;

public class BasicItemStack implements IObjectStack {

	int items = 1;
	Item innerItem;
	int[] itemInstanceIdentificators;
	Random rand = new Random();
	boolean shouldRecycle;
	
	public BasicItemStack(Item itemFor, int count) {
		if(count > this.getMaximumItemsPerStack() || count < 1) {
			count = 1;
		}
		
		this.itemInstanceIdentificators = new int[this.getMaximumItemsPerStack()];
		this.innerItem = itemFor;
		
		this.setCurrentItemsCount(count);
	}
	
	@Override
	public int getMaximumItemsPerStack() {
		return 16;
	}

	@Override
	public int getItemID() {
		return innerItem.getID();
	}

	@Override
	public String getShowedName() {
		return innerItem.getShowedName();
	}

	@Override
	public String getShowedDescription() {
		return null;
	}

	@Override
	public int getCurrentItemsCount() {
		return this.items;
	}

	@Override
	public void tickItems(World w) {
		for(int i = 0; i < this.getCurrentItemsCount(); i++) {
			innerItem.onTick(this.itemInstanceIdentificators[i], w);
		}
	}

	@Override
	public void renderTickItems(float partialTickTime, World w) {
		for(int i = 0; i < this.getCurrentItemsCount(); i++) {
			innerItem.onRenderTick(partialTickTime, this.itemInstanceIdentificators[i], w);
		}
	}

	@Override
	public void renderGUITexture(int x, int y, int width, int height) {
		GLHelper.drawTexture(innerItem.getTexture(), x, x + width, y, y + height);
	}

	@Override
	public void setCurrentItemsCount(int count) throws IllegalArgumentException {
		if(count > this.getMaximumItemsPerStack()) {
			throw new IllegalArgumentException("Count must be greater than 0 and smaller the maximum items per stack");
		}
		
		if(count < 1) {
			this.notifyDeletion();
			return;
		}
		
		this.items = count;
		
		for(int i = 0; i < this.getCurrentItemsCount(); i++) {
			if(this.itemInstanceIdentificators[i] == 0) {
				this.itemInstanceIdentificators[i] = innerItem.getName().hashCode() + rand.nextInt(7) * rand.nextInt(13);
			}
		}
		
		for(int i = this.getCurrentItemsCount(); i < this.getMaximumItemsPerStack(); i++) {
			if(this.itemInstanceIdentificators[i] != 0) {
				this.itemInstanceIdentificators[i] = 0;
			}
		}
	}

	@Override
	public void useItem(int mouseButton, int x, int y, int z, int item,
			World world) throws IllegalArgumentException {
		if(this.itemInstanceIdentificators[item] == 0) {
			throw new IllegalArgumentException("This ItemStack holds only " + this.getCurrentItemsCount() + " items!");
		}
		
		this.innerItem.onUse(mouseButton, x, y, z, this.itemInstanceIdentificators[item], world);
	}

	@Override
	public boolean addItem() {
		if(this.items != this.getMaximumItemsPerStack()) {
			this.setCurrentItemsCount(items + 1);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean removeItem() {
		if(this.items != 1) {
			this.setCurrentItemsCount(items - 1);
			return true;
		} else {
			this.notifyDeletion();
			return true;
		}
	}

	@Override
	public boolean shouldRecycle() {
		return this.shouldRecycle;
	}

	@Override
	public void notifyDeletion() {
		this.shouldRecycle = true;
	}

}
