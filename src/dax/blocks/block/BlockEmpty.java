package dax.blocks.block;

import java.util.logging.Logger;

import dax.blocks.world.World;

public class BlockEmpty extends Block {

	boolean isAir = false;
	
	public BlockEmpty() {
		this(false);
	}
	
	public BlockEmpty(boolean isAir) {
		super(isAir ? 2 : 1);
		this.isAir = isAir;
	}

	@Override
	public void onTick(int x, int y, int z, World world) {
		if(!isAir)
			Logger.getGlobal().warning(String.format("Something went wrong - tick on null block at %1-%2-%3 in world %4", x, y, z, world.name));
	}

	@Override
	public void onRenderTick(float partialTickTime, int x, int y, int z,
			World world) {
		if(!isAir)
			Logger.getGlobal().warning(String.format("Something went wrong - render tick on null block at %1-%2-%3 in world %4", x, y, z, world.name));
	}

	@Override
	public void onClicked(int button, int x, int y, int z, World world) {
		if(!isAir)
			Logger.getGlobal().warning(String.format("Something went wrong - clicked on null block at %1-%2-%3 in world %4", x, y, z, world.name));
	}

}
