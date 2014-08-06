package dax.blocks.block;

import dax.blocks.gui.ingame.IngameGuiManager;
import dax.blocks.world.IDRegister;
import dax.blocks.world.World;

public class BlockBasic extends Block {

	protected float lightColorR = 1;
	protected float lightColorG = 1;
	protected float lightColorB = 1;
	
	public BlockBasic(String name, IDRegister r) {
		super(name, r);
		IngameGuiManager.getInstance().registerNewScreen(new dax.blocks.gui.ingame.GuiScreen(450, 450, IngameGuiManager.getInstance()) {

			@Override
			public void onOpening() {
				System.out.println("OPENING");
			}

			@Override
			public void onClosing() {
				System.out.println("CLOSING");
			}
			
		});
	}

	@Override
	public void onRenderTick(float partialTickTime, int x, int y, int z, World world) {
	}


	@Override
	public void onTick(int x, int y, int z, World world) {
	}

	@Override
	public void onClick(int button, int x, int y, int z, World world) {
		IngameGuiManager.getInstance().setCurrentScreen(0);
		
		if(IngameGuiManager.getInstance().isOpened())
			IngameGuiManager.getInstance().closeScreen();
		else
			IngameGuiManager.getInstance().openScreen();
	}


}
