package cz.dat.oots.block;

import cz.dat.oots.block.renderer.BlockRendererPlant;
import cz.dat.oots.data.IDataObject;
import cz.dat.oots.data.block.PlantDataObject;
import cz.dat.oots.world.IDRegister;
import cz.dat.oots.world.World;

public class BlockPlant extends Block {

    public BlockPlant(String name, IDRegister r) {
        super(name, r);
        setOpaque(false);
        setOccluder(false);
        setCollidable(false);
        setRenderer(new BlockRendererPlant());
    }

    @Override
    public IDataObject createDataObject() {
        return new PlantDataObject(this);
    }

    @Override
    public void onTick(int x, int y, int z, World world) {

    }

    @Override
    public void onRenderTick(float partialTickTime, int x, int y, int z,
                             World world) {
    }

    @Override
    public void onClick(int button, int x, int y, int z, World world) {
        PlantDataObject d;

        if (!world.hasData(x, y, z)) {
            d = (PlantDataObject) world.createData(x, y, z, this);
        } else {
            d = (PlantDataObject) world.getData(x, y, z);
        }

        d.setTextured(!d.isTextured());

        world.setChunkDirty(x >> 4, y / 16, z >> 4);
    }

    @Override
    public void onUpdate(int x, int y, int z, int type, World world) {

    }

    @Override
    public void onNeighbourUpdate(int x, int y, int z, World world) {
        int below = world.getBlock(x, y - 1, z);
        if (!(below == IDRegister.grass.getID() || below == IDRegister.dirt
                .getID())) {
            world.setBlock(x, y, z, 0, true, true);
        }
    }
}
