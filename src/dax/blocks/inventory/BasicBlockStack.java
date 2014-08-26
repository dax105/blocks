package dax.blocks.inventory;

import dax.blocks.FontManager;
import dax.blocks.block.Block;
import dax.blocks.util.GLHelper;
import dax.blocks.world.World;

public class BasicBlockStack implements IObjectStack {

	int items;
	String renderString;
	Block innerBlock;
	String desc;
	boolean shouldRecycle;
	
	public BasicBlockStack(Block block, int count) {
		if(count > this.getMaximumItemsPerStack() || count < 1) {
			count = 1;
		}
		
		this.innerBlock = block;
		this.desc = "Block name: " + block.getName();
		this.setCurrentItemsCount(count);
	}
	
	@Override
	public int getMaximumItemsPerStack() {
		return 64;
	}

	@Override
	public int getItemID() {
		return this.innerBlock.getID();
	}

	@Override
	public String getShowedName() {
		return this.innerBlock.getShowedName();
	}

	@Override
	public String getShowedDescription() {
		return this.desc;
	}

	@Override
	public int getCurrentItemsCount() {
		return this.items;
	}

	@Override
	public void setCurrentItemsCount(int count) throws IllegalArgumentException {
		if(count > this.getMaximumItemsPerStack()) {
			throw new IllegalArgumentException("Count must be greater than 0 and smaller the maximum items per stack");
		}
		
		if(count < 1) {
			this.notifyDeletion();
		}
		
		this.items = count;
		this.renderString = "" + this.items;
	}

	@Override
	public void useItem(int mouseButton, int x, int y, int z, int item,
			World world) throws IllegalArgumentException {	
		if(mouseButton == 1) {
			world.setBlock(x, y, z, this.innerBlock.getID(), true, true);
			this.removeItem();
		}	
	}

	@Override
	public void tickItems(World world) {
	}

	@Override
	public void renderTickItems(float partialTickTime, World world) {
	}

	@Override
	public void renderGUITexture(int x, int y, int width, int height) {
		GLHelper.drawFromAtlas(this.innerBlock.getSideTexture(), x, x + width, y, y + height);
		FontManager.getFont().drawString(x + width - FontManager.getFont().getWidth(this.renderString) - 2,
				y + height - FontManager.getFont().getHeight(), this.renderString);
		
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
