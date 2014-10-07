package cz.dat.oots.inventory;

import cz.dat.oots.block.Block;
import cz.dat.oots.inventory.renderer.BasicBlockRenderer;
import cz.dat.oots.inventory.renderer.IObjectStackRenderer;
import cz.dat.oots.world.World;

public class BasicBlockStack implements IObjectStack {

	private int items;
	private Block innerBlock;
	private String desc;
	private boolean shouldRecycle;
	private BasicBlockRenderer renderer;
	
	public BasicBlockStack(Block block, int count) {
		this.renderer = new BasicBlockRenderer(this, block);
		
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
		this.renderer.updateRenderString(count);
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

	@Override
	public IObjectStackRenderer getRenderer() {
		return this.renderer;
	}

}
