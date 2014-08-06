package dax.blocks.item;

import dax.blocks.TextureManager;
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
	}
}
