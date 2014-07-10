package dax.blocks.world;

import java.util.Random;
import dax.blocks.Game;
import dax.blocks.block.Block;

public class Explosion {

	static Random rand = new Random();

	public static void explode(World world, float ex, float ey, float ez) {
		int radius = Game.settings.explosion_radius.getValue();
		float fuzzyness = 1.5f;

		Game.sound.playSound("explosion", 0.8f + rand.nextFloat() * 0.4f);

		for (int x = (int) ex - radius; x < ex + radius; x++) {
			for (int y = (int) ey - radius; y < ey + radius; y++) {
				for (int z = (int) ez - radius; z < ez + radius; z++) {

					int blockid;

					blockid = world.getBlock(x, y, z);

					if (blockid > 0 && blockid != Block.bedrock.getId()) {
						float distX = Math.abs(ex - x);
						float distY = Math.abs(ey - y);
						float distZ = Math.abs(ez - z);

						float distSq = distX * distX + distY * distY + distZ
								* distZ;

						float fuzz = rand.nextFloat();

						float radiusFuzzed = radius - fuzz * fuzzyness;

						if (distSq < radiusFuzzed * radiusFuzzed) {
							world.setBlock(x, y, z, (byte) 0, true);

							if (rand.nextFloat() > 0.9f) {
								world.spawnParticleWithRandomDirectionFast(x
										+ rand.nextFloat(),
										y + rand.nextFloat(),
										z + rand.nextFloat(), 2, 0.5f);
							}
						}

					}
				}
			}
		}
	}
}
