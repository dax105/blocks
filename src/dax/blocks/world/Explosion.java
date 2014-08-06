package dax.blocks.world;

import java.util.Random;
import dax.blocks.block.Block;
import dax.blocks.settings.Settings;
import dax.blocks.sound.SoundManager;

public class Explosion {

	private static Random rand = new Random();

	public static void explode(World world, float ex, float ey, float ez) {
		int radius = Settings.getInstance().explosionRadius.getValue();
		float fuzzyness = 1.5f;

		SoundManager.getInstance().playSound("explosion", 0.8f + Explosion.rand.nextFloat() * 0.4f);

		for(int x = (int) ex - radius; x < ex + radius; x++) {
			for(int y = (int) ey - radius; y < ey + radius; y++) {
				for(int z = (int) ez - radius; z < ez + radius; z++) {

					int blockid;

					blockid = world.getBlock(x, y, z);

					if(blockid > 0 && blockid != Block.bedrock.getId()) {
						float distX = Math.abs(ex - x);
						float distY = Math.abs(ey - y);
						float distZ = Math.abs(ez - z);

						float distSq = distX * distX + distY * distY + distZ
								* distZ;

						float fuzz = Explosion.rand.nextFloat();

						float radiusFuzzed = radius - fuzz * fuzzyness;

						if(distSq < radiusFuzzed * radiusFuzzed) {
							world.setBlock(x, y, z, (byte) 0, true, false);
						}
					}
				}
			}
		}
	}
}
