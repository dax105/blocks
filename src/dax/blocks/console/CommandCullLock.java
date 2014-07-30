package dax.blocks.console;

import dax.blocks.Game;
import dax.blocks.movable.entity.PlayerEntity;

public class CommandCullLock extends Command {

	public static boolean locked = false;
	public static float lockedX;
	public static float lockedY;
	public static float lockedZ;
	
	@Override
	public String getName() {
		return "cullLock";
	}

	@Override
	public String getUsage() {
		return "";
	}

	@Override
	public boolean execute(String[] args) {
		if (!locked) {
			PlayerEntity player = Game.getInstance().world.getPlayer();
			lockedX = player.getPosX();
			lockedY = player.getPosY();
			lockedZ = player.getPosZ();
		}
		
		locked = !locked;
		
		return true;
	}

	
	
}
