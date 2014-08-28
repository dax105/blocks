package dax.blocks.item;

import dax.blocks.TextureManager;
import dax.blocks.gui.ingame.GuiScreen;
import dax.blocks.gui.ingame.ISliderUpdateCallback;
import dax.blocks.gui.ingame.SliderControl;
import dax.blocks.sound.SoundManager;
import dax.blocks.world.IDRegister;
import dax.blocks.world.World;

public class ItemImaginaryChocolate extends Item {
	public ItemImaginaryChocolate(IDRegister register) {
		super("oots/itemFoodImaginaryChocolate", register);
		this.setTexture(TextureManager.imaginary_chocolate).setShowedName("Imaginary Chocolate");
	}

	@Override
	public void onTick(int uniqueIdentifier, World world) {
	}

	@Override
	public void onRenderTick(float partialTickTime, int uniqueIdentifier,
			World world) {
	}

	@Override
	public void onUse(int mouseButton, int targetX, int targetY, int targetZ,
			int uniqueIdentifier, World world) {
		world.getPlayer().setLifes(1);
		SoundManager.getInstance().playSound("holy_chorus");
		world.getGui().setCurrentScreen(
				world.getGui().registerNewScreen(new GuiScreen(100, 100, world.getGui()) {

			@Override
			public void onOpening() {
				
				this.addControl(new SliderControl(new ISliderUpdateCallback() {

					@Override
					public void onUpdate(SliderControl caller, float value) {
						System.out.println(caller.toString() + ": " + value);
					}
				}, this));
				
			}

			@Override
			public void onClosing() {
				
			}
			
		}));
		
		world.getGui().openScreen();
	}
}
