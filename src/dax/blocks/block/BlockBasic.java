package dax.blocks.block;

import dax.blocks.world.World;

public class BlockBasic extends Block {

	protected float lightColorR = 1;
	protected float lightColorG = 1;
	protected float lightColorB = 1;
	
	public BlockBasic(int id) {
		super(id);
		dax.blocks.Game.ingameGuiManager.registerNewScreen(new dax.blocks.gui.ingame.GuiScreen(450, 450, dax.blocks.Game.ingameGuiManager) {

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
	public void onClicked(int button, int x, int y, int z, World world) {
		dax.blocks.Game.ingameGuiManager.setCurrentScreen(0);
		
		if(dax.blocks.Game.ingameGuiManager.isOpened())
			dax.blocks.Game.ingameGuiManager.closeScreen();
		else
			dax.blocks.Game.ingameGuiManager.openScreen();
	}

}
