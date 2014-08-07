package dax.blocks.console;

import dax.blocks.Game;
import dax.blocks.movable.entity.PlayerEntity;

public class CommandCullLock extends Command {

	// I know, this is ugly, but I DONT CARE!
	public static boolean locked = false;
	public static float lockedX;
	public static float lockedY;
	public static float lockedZ;
	
	@Override
	public String getName() {
		return "culllock";
	}

	@Override
	public String getUsage() {
		return "";
	}

	@Override
	public boolean execute(String[] args) {
		if(!CommandCullLock.locked) {
			PlayerEntity player = Game.getInstance().getCurrentWorld().getPlayer();
			CommandCullLock.lockedX = player.getPosX();
			CommandCullLock.lockedY = player.getPosY();
			CommandCullLock.lockedZ = player.getPosZ();
			
			Console.println("Culling locked!");
			CommandCullLock.locked = true;
		} else {
			Console.println("Culling unlocked!");
			CommandCullLock.locked = false;
		}
		
		return true;
	}

	
	
}
