package dax.blocks.block;

import java.util.Random;

import dax.blocks.data.IDataObject;
import dax.blocks.data.block.StoneDataObject;
import dax.blocks.sound.SoundManager;
import dax.blocks.world.IDRegister;
import dax.blocks.world.World;

public class BlockStone extends BlockBasic {
	Random rnd;

	public BlockStone(IDRegister r) {
		super("oots/blockStone", r);
		rnd = new Random();
		this.setAllTextures(0).setFootStepSound(SoundManager.footstep_stone).setFallSound("fall_hard");
	}

	public void updateColor(int x, int y, int z, World w) {
		if(w.hasData(x, y, z)) {
			StoneDataObject d = (StoneDataObject) w.getData(x, y, z);
			
			this.setColor(d.getColorR(), d.getColorG(), d.getColorB());
		}
	}

	public void recolor(int x, int y, int z, World w) {
		StoneDataObject d;
		
		if(!w.hasData(x, y, z)) {
			d = (StoneDataObject) w.createData(x, y, z);
		} else {
			d = (StoneDataObject) w.getData(x, y, z);
		}

		d.setColorR(rnd.nextFloat());
		d.setColorG(rnd.nextFloat());
		d.setColorB(rnd.nextFloat());
	}
	
	@Override
	public IDataObject createDataObject() {
		return new StoneDataObject(this);
	}

	@Override
	public void onClick(int button, int x, int y, int z, World world) {
		recolor(x, y, z, world);

		if(button != 10) {
			for(int x2 = (x - 1); x2 < (x + 2); x2++) {
				for(int y2 = (y - 1); y2 < (y + 2); y2++) {
					for(int z2 = (z - 1); z2 < (z + 2); z2++) {
						if(world.getBlock(x2, y2, z2) == this.getID()) {
							IDRegister.stone.onClick(10, x2, y2, z2, world);
						}
					}
				}
			}
		}

		world.setChunkDirty(x >> 4, y / 16, z >> 4);
	}
}
