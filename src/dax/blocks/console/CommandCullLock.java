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
		if(!this.locked) {
			PlayerEntity player = Game.getInstance().world.getPlayer();
			this.lockedX = player.getPosX();
			this.lockedY = player.getPosY();
			this.lockedZ = player.getPosZ();
			
			Console.println("Culling locked!");
			this.locked = true;
		} else {
			Console.println("Culling unlocked!");
			this.locked = false;
		}
		
		return true;
	}

	
	
}
