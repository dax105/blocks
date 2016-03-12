package cz.dat.oots.block;

import cz.dat.oots.block.renderer.BlockRendererSlab;
import cz.dat.oots.collisions.AABB;
import cz.dat.oots.world.IDRegister;
import cz.dat.oots.world.World;

public class BlockSlab extends Block {

    public BlockSlab(String blockName, IDRegister register) {
        super(blockName, register);
        setAABB(new AABB(0, 0, 0, 1, 0.5f, 1));
        setRenderer(new BlockRendererSlab());
        setOpaque(false);
    }

    @Override
    public void onUpdate(int x, int y, int z, int type, World world) {

    }

    @Override
    public void onNeighbourUpdate(int x, int y, int z, World world) {

    }

    @Override
    public void onTick(int x, int y, int z, World world) {

    }

    @Override
    public void onRenderTick(float partialTickTime, int x, int y, int z, World world) {

    }

    @Override
    public void onClick(int mouseButton, int x, int y, int z, World world) {

    }

}
