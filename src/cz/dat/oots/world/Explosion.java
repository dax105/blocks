package cz.dat.oots.world;

import java.util.Random;

import cz.dat.oots.sound.SoundManager;

public class Explosion {

	private static Random rand = new Random();

	public static void explode(World world, float ex, float ey, float ez) {
		int radius = world.getGame().s().explosionRadius.getValue();
		float fuzzyness = 1.5f;

		SoundManager.getInstance().playSound("explosion",
				0.8f + Explosion.rand.nextFloat() * 0.4f);

		for(int x = (int) ex - radius; x < ex + radius; x++) {
			for(int y = (int) ey - radius; y < ey + radius; y++) {
				for(int z = (int) ez - radius; z < ez + radius; z++) {

					int blockid;

					blockid = world.getBlock(x, y, z);

					if(blockid > 0 && blockid != IDRegister.bedrock.getID()) {
						float distX = Math.abs(ex - x);
						float distY = Math.abs(ey - y);
						float distZ = Math.abs(ez - z);

						float distSq = distX * distX + distY * distY + distZ
								* distZ;

						float fuzz = Explosion.rand.nextFloat();

						float radiusFuzzed = radius - fuzz * fuzzyness;

						if(distSq < radiusFuzzed * radiusFuzzed) {
							world.setBlock(x, y, z, (byte) 0, true, false);
							
							world.spawnParticleWithRandomDirectionFast(x+0.5f, y+0.5f, z+0.5f, 3, 1);
						}
					}
				}
			}
		}
	}
	
	public static void fill(World world, float ex, float ey, float ez, int id) {
		int radius = world.getGame().getSettings().explosionRadius.getValue();
		float fuzzyness = 1.5f;

		SoundManager.getInstance().playSound("explosion", 0.8f + Explosion.rand.nextFloat() * 0.4f);

		for(int x = (int) ex - radius; x < ex + radius; x++) {
			for(int y = (int) ey - radius; y < ey + radius; y++) {
				for(int z = (int) ez - radius; z < ez + radius; z++) {

					int blockid;

					blockid = world.getBlock(x, y, z);

					if(blockid != IDRegister.bedrock.getID()) {
						float distX = Math.abs(ex - x);
						float distY = Math.abs(ey - y);
						float distZ = Math.abs(ez - z);

						float distSq = distX * distX + distY * distY + distZ
								* distZ;

						float fuzz = Explosion.rand.nextFloat();

						float radiusFuzzed = radius - fuzz * fuzzyness;

						if(distSq < radiusFuzzed * radiusFuzzed) {
							world.setBlock(x, y, z, id, true, false);
							
							//world.spawnParticleWithRandomDirectionFast(x, y, z, 3, 1);
						}
					}
				}
			}
		}
	}
}
