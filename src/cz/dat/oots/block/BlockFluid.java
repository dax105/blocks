package cz.dat.oots.block;

import cz.dat.oots.block.renderer.BlockRendererFluid;
import cz.dat.oots.render.RenderPass;
import cz.dat.oots.world.IDRegister;
import cz.dat.oots.world.UpdateType;
import cz.dat.oots.world.World;

public class BlockFluid extends BlockBasic {

    private int flowRate;

    public BlockFluid(String name, IDRegister r, int flowRate) {
        super(name, r);

        this.flowRate = flowRate;

        this.setCullSame(true);
        this.setOpaque(false);
        this.setOccluder(false);
        this.setRenderPass(RenderPass.TRANSLUCENT);
        this.setCollidable(false);
        this.setRenderer(new BlockRendererFluid());
    }

    @Override
    public void onNeighbourUpdate(int x, int y, int z, World world) {
        world.scheduleUpdate(x, y, z, this.flowRate, UpdateType.FLUID_FLOW);
    }

    @Override
    public void onUpdate(int x, int y, int z, int type, World world) {
        if (type == UpdateType.FLUID_FLOW) {

            if (world.getBlockObject(x, y - 1, z) == this) {
                return;
            }

            if (world.getBlock(x, y - 1, z) == 0) {
                world.setBlock(x, y - 1, z, this.getID(), true,
                        true);
                return;
            }
            if (world.getBlock(x + 1, y, z) == 0) {
                world.setBlock(x + 1, y, z, this.getID(), true,
                        true);
            }
            if (world.getBlock(x - 1, y, z) == 0) {
                world.setBlock(x - 1, y, z, this.getID(), true,
                        true);
            }
            if (world.getBlock(x, y, z + 1) == 0) {
                world.setBlock(x, y, z + 1, this.getID(), true,
                        true);
            }
            if (world.getBlock(x, y, z - 1) == 0) {
                world.setBlock(x, y, z - 1, this.getID(), true,
                        true);
            }
        }
    }

    @Override
    public void onRandomTick(int x, int y, int z, World world) {
        world.spawnParticleWithRandomDirectionFast(x + 0.5f, y, z + 0.5f, 0.1f, 0);
    }

}
