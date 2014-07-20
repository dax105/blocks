package dax.blocks.block;

import java.util.Random;
import dax.blocks.sound.SoundManager;
import dax.blocks.world.World;

public class BlockStone extends BlockBasic {
	Random rnd;

	public BlockStone() {
		super(3);
		rnd = new Random();
		this.setAllTextures(0).setStepSound(SoundManager.footstep_stone)
				.setFallSound("fall_hard");
	}

	private void setColor(int x, int y, int z, World w) {
		if (w.containsData(x, y, z, "recolor_r")) {
			this.lightColorR = w.getDataFloat(x, y, z, "recolor_r");
			this.lightColorG = w.getDataFloat(x, y, z, "recolor_g");
			this.lightColorB = w.getDataFloat(x, y, z, "recolor_b");
			// GL11.glColor3f(w.getDataFloat(x, y, z, "recolor_r"),
			// w.getDataFloat(x, y, z, "recolor_g"), w.getDataFloat(x, y, z,
			// "recolor_b"));
		}
	}

	private void restoreColor() {
		this.lightColorR = 1;
		this.lightColorG = 1;
		this.lightColorB = 1;
	}

	private void recolor(int x, int y, int z, World w) {
		w.setData(x, y, z, "recolor_r", "" + rnd.nextFloat());
		w.setData(x, y, z, "recolor_g", "" + rnd.nextFloat());
		w.setData(x, y, z, "recolor_b", "" + rnd.nextFloat());
	}

	@Override
	public void renderFront(int x, int y, int z, boolean xnzn, boolean zn,
			boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp,
			boolean xpzp, World w) {
		setColor(x, y, z, w);
		super.renderFront(x, y, z, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, w);
		restoreColor();
	}

	@Override
	public void renderBack(int x, int y, int z, boolean xnzn, boolean zn,
			boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp,
			boolean xpzp, World w) {
		setColor(x, y, z, w);
		super.renderBack(x, y, z, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, w);
		restoreColor();
	}

	@Override
	public void renderRight(int x, int y, int z, boolean xnzn, boolean zn,
			boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp,
			boolean xpzp, World w) {
		setColor(x, y, z, w);
		super.renderRight(x, y, z, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, w);
		restoreColor();
	}

	@Override
	public void renderLeft(int x, int y, int z, boolean xnzn, boolean zn,
			boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp,
			boolean xpzp, World w) {
		setColor(x, y, z, w);
		super.renderLeft(x, y, z, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, w);
		restoreColor();
	}

	@Override
	public void renderTop(int x, int y, int z, boolean xnzn, boolean zn,
			boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp,
			boolean xpzp, World w) {
		setColor(x, y, z, w);
		super.renderTop(x, y, z, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, w);
		restoreColor();
	}

	@Override
	public void renderBottom(int x, int y, int z, boolean xnzn, boolean zn,
			boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp,
			boolean xpzp, World w) {
		setColor(x, y, z, w);
		super.renderBottom(x, y, z, xnzn, zn, xpzn, xn, xp, xnzp, zp, xpzp, w);
		restoreColor();
	}

	@Override
	public void onClicked(int button, int x, int y, int z, World world) {
		recolor(x, y, z, world);

		if (button != 10) {
			for (int x2 = (x - 1); x2 < (x + 2); x2++) {
				for (int y2 = (y - 1); y2 < (y + 2); y2++) {
					for (int z2 = (z - 1); z2 < (z + 2); z2++) {
						if (world.getBlock(x2, y2, z2) == this.getId()) {
							Block.stone.onClicked(10, x2, y2, z2, world);
						}
					}
				}
			}
		}

		world.setChunkDirty(x >> 4, y / 16, z >> 4);
	}

}
