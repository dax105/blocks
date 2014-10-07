package cz.dat.oots.inventory.renderer;

import cz.dat.oots.inventory.IObjectStack;
import cz.dat.oots.world.World;

public interface IObjectStackRenderer {
	public IObjectStack getObjectStack();
	
	public void preRender(float ptt, World world);
	public void render(float ptt, int x, int y, int width, int height, World world);
	public void render(float ptt, int x, int y, World world);
	
	
}
