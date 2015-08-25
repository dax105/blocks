package cz.dat.oots.inventory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BasicInventory implements IInventory {

	private int maxItems;
	private Map<Integer, IObjectStack> items;

	@Override
	public void init(int maxItems) {
		this.maxItems = maxItems;

		if(this.items == null) {
			this.items = new HashMap<Integer, IObjectStack>();
		} else {
			if(this.items.size() > maxItems) {
				int c = 0;

				for(Iterator<Integer> it = this.items.keySet().iterator(); this.items
						.keySet().iterator().hasNext();) {
					if(c >= (maxItems - 1)) {
						it.remove();
					}

					c++;
				}
			}
		}
	}

	@Override
	public void putStack(int position, IObjectStack stack) {
		if(this.items.size() < this.maxItems && position <= this.maxItems) {
			this.removeItem(position);
			this.items.put(position, stack);
		}
	}

	@Override
	public void moveStack(int actualPosition, int newPosition) {
		IObjectStack stack = this.items.get(actualPosition);

		if(stack != null) {
			this.items.remove(actualPosition);
			this.items.put(newPosition, stack);
		}
	}

	@Override
	public int getMaxItems() {
		return this.maxItems;
	}

	@Override
	public int getItemCount() {
		return this.items.size();
	}

	@Override
	public IObjectStack getItem(int position) {
		return this.items.get(position);
	}

	@Override
	public void removeItem(int position) {
		IObjectStack stack = this.items.get(position);
		if(stack != null) {
			stack.notifyDeletion();
			this.items.remove(position);
		}
	}

}
