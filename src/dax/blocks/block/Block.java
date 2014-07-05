package dax.blocks.block;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.openal.Audio;

import dax.blocks.Game;
import dax.blocks.SoundManager;
import dax.blocks.render.ChunkDisplayList;

public abstract class Block {

	private final byte id;
	private final boolean opaque;
	protected int renderPass;
	private final boolean cullSame;
	private boolean occluder = true;
	
	private Audio[] footStep = SoundManager.footstep_dirt;
	private Audio fall = SoundManager.fall_hard;
	
	private static int lastAO = -1;

	public void addVertexWithAO(float x, float y, float z, boolean s1, boolean s2, boolean c) {
		float ao;

		if (s1 && s2) {
			ao = 3;
		} else {
			ao = 0;
			if (s1)
				ao++;
			if (s2)
				ao++;
			if (c)
				ao++;
		}

		if (ao == 1) ao += 0.5f;
		
		float aom = ao * Game.settings.ao_intensity.getValue();

		if (ao != lastAO) {
			GL11.glColor3f(1.0f - aom, 1.0f - aom, 1.0f - aom);
		}	
		
		GL11.glVertex3f(x, y, z);
	}
	
	public Audio[] getStepSound() {
		return this.footStep;
	}
	
	public Audio getFallSound() {
		return this.fall;
	}
	
	public boolean shouldCullSame() {
		return this.cullSame;
	}

	public Block setStepSound(Audio[] sound) {
		this.footStep = sound;
		return this;
	}
	
	public Block setFallSound(Audio sound) {
		this.fall = sound;
		return this;
	}
	
	public Block setOccluder(boolean occluder) {
		this.occluder = occluder;
		return this;
	}
	
	public boolean isOccluder() {
		return this.occluder;
	}
	
	public Block(int id) {
		this.id = (byte) id;
		this.opaque = true;
		this.cullSame = false;
		this.renderPass = ChunkDisplayList.PASS_OPAQUE;
		blocks[id] = this;
	}

	public Block(int id, boolean opaque) {
		this.id = (byte) id;
		this.opaque = opaque;
		this.cullSame = false;
		this.renderPass = ChunkDisplayList.PASS_OPAQUE;
		blocks[id] = this;
	}
	
	public Block(int id, boolean opaque, boolean cullSame) {
		this.id = (byte) id;
		this.opaque = opaque;
		this.cullSame = cullSame;
		this.renderPass = ChunkDisplayList.PASS_OPAQUE;
		blocks[id] = this;
	}

	public static Block[] blocks = new Block[256];

	public static final Block grass = new BlockBasic(1, 4, 5, 3).setStepSound(SoundManager.footstep_grass).setFallSound(SoundManager.fall_soft);
	public static final Block dirt = new BlockBasic(2, 3).setStepSound(SoundManager.footstep_dirt).setFallSound(SoundManager.fall_soft);
	public static final Block stone = new BlockBasic(3, 0).setStepSound(SoundManager.footstep_stone).setFallSound(SoundManager.fall_hard);
	public static final Block wood = new BlockBasic(4, 2).setStepSound(SoundManager.footstep_wood).setFallSound(SoundManager.fall_hard);
	public static final Block stoneMossy = new BlockBasic(5, 1).setStepSound(SoundManager.footstep_stone).setFallSound(SoundManager.fall_hard);
	public static final Block bricks = new BlockBasic(6, 8).setStepSound(SoundManager.footstep_stone).setFallSound(SoundManager.fall_hard);
	public static final Block sand = new BlockBasic(7, 6).setStepSound(SoundManager.footstep_dirt).setFallSound(SoundManager.fall_soft);
	public static final Block log = new BlockBasic(8, 11, 7, 11).setStepSound(SoundManager.footstep_wood).setFallSound(SoundManager.fall_hard);
	public static final Block glass = new BlockBasic(9, 9, false, true, ChunkDisplayList.PASS_TRANSPARENT).setStepSound(SoundManager.footstep_stone).setFallSound(SoundManager.fall_hard);
	public static final Block leaves = new BlockBasic(10, 10, false, false, ChunkDisplayList.PASS_TRANSPARENT).setStepSound(SoundManager.footstep_grass).setFallSound(SoundManager.fall_soft);
	public static final Block bedrock = new BlockBasic(11, 12).setStepSound(SoundManager.footstep_stone).setFallSound(SoundManager.fall_hard);
	public static final Block water = new BlockFluid(12, 13).setOccluder(false);
	public static final Block ice = new BlockBasic(13, 14, false, true, ChunkDisplayList.PASS_TRANSLUCENT);
	public static final Block tallgrass = new BlockPlant(14, 15).setOccluder(false);
	public static final Block flower_1 = new BlockPlant(15, 16).setOccluder(false);
	public static final Block flower_2 = new BlockPlant(16, 17).setOccluder(false);
	public static final Block flower_3 = new BlockPlant(17, 18).setOccluder(false);

	public byte getId() {
		return this.id;
	}

	public boolean isOpaque() {
		return this.opaque;
	}

	public static Block getBlock(int id) {
		return blocks[id];
	}

	public abstract float[] getVTop(int x, int y, int z);

	public abstract float[] getVBottom(int x, int y, int z);

	public abstract float[] getVLeft(int x, int y, int z);

	public abstract float[] getVRight(int x, int y, int z);

	public abstract float[] getVFront(int x, int y, int z);

	public abstract float[] getVBack(int x, int y, int z);

	public abstract float[] getTTop();

	public abstract float[] getTBottom();

	public abstract float[] getTLeft();

	public abstract float[] getTRight();

	public abstract float[] getTFront();

	public abstract float[] getTBack();

	public abstract float[] getNTop();

	public abstract float[] getNBottom();

	public abstract float[] getNLeft();

	public abstract float[] getNRight();

	public abstract float[] getNFront();

	public abstract float[] getNBack();
	
	public abstract void renderFront(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp);
	public abstract void renderBack(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp);
	public abstract void renderRight(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp);
	public abstract void renderLeft(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp);
	public abstract void renderTop(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp);
	public abstract void renderBottom(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp);

	public abstract void renderIndependent(int x, int y, int z);
	
	public void setRenderPass(int pass) {
		this.renderPass = pass;
	}
	
	public int getRenderPass() {
		return renderPass;
	}

}
