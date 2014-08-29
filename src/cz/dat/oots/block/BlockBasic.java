package cz.dat.oots.block;

import cz.dat.oots.world.IDRegister;
import cz.dat.oots.world.World;

public class BlockBasic extends Block {

	protected float lightColorR = 1;
	protected float lightColorG = 1;
	protected float lightColorB = 1;
	
	public BlockBasic(String name, IDRegister r) {
		super(name, r);
	}

	@Override
	public void onRenderTick(float partialTickTime, int x, int y, int z, World world) {
	}


	@Override
	public void onTick(int x, int y, int z, World world) {
	}

	@Override
	public void onClick(int button, int x, int y, int z, World world) {
	}

	@Override
	public void onUpdate(int x, int y, int z, int type, World world) {	
	}

	@Override
	public void onNeighbourUpdate(int x, int y, int z, World world) {
	}


}
