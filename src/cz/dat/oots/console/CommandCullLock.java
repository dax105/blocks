package cz.dat.oots.console;

import cz.dat.oots.movable.entity.PlayerEntity;

public class CommandCullLock extends Command {

	public CommandCullLock(Console console) {
		super(console);
	}

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
			PlayerEntity player = this.console.getGame().getCurrentWorld().getPlayer();
			CommandCullLock.lockedX = player.getPosX();
			CommandCullLock.lockedY = player.getPosY();
			CommandCullLock.lockedZ = player.getPosZ();
			
			this.console.println("Culling locked!");
			CommandCullLock.locked = true;
		} else {
			this.console.println("Culling unlocked!");
			CommandCullLock.locked = false;
		}
		
		return true;
	}

	
	
}
