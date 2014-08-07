package dax.blocks.block;

import dax.blocks.block.renderer.BlockRendererPlant;
import dax.blocks.data.DataFlags;
import dax.blocks.render.RenderPass;
import dax.blocks.world.IDRegister;
import dax.blocks.world.World;

public class BlockPlant extends Block {

	public BlockPlant(String name, IDRegister r) {
		super(name, r);
		setOpaque(false);
		setOccluder(false);
		setRenderPass(RenderPass.TRANSPARENT);
		setCollidable(false);
		setRenderer(new BlockRendererPlant());
	}

	@Override
	public void onTick(int x, int y, int z, World world) {

	}

	@Override
	public void onRenderTick(float partialTickTime, int x, int y, int z, World world) {
	}

	@Override
	public void onClick(int button, int x, int y, int z, World world) {
		world.setData(x, y, z, DataFlags.SPECIAL_TEXTURE, "true");
		world.setChunkDirty(x >> 4, y/16, z >> 4);
	}

	@Override
	public void onUpdate(int x, int y, int z, int type, World world) {

	}

	@Override
	public void onNeighbourUpdate(int x, int y, int z, World world) {
		int below = world.getBlock(x, y-1, z);
		if(!(below == IDRegister.grass.getID() || below == IDRegister.dirt.getID())) {
			world.setBlock(x, y, z, 0, true, true);
		}
	}
}
