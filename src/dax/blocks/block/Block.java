package dax.blocks.block;

import org.lwjgl.opengl.GL11;
import dax.blocks.Game;
import dax.blocks.render.RenderPass;
import dax.blocks.sound.SoundManager;
import dax.blocks.world.World;

public abstract class Block {

	public int topTexture = 0;
	public int sideTexture = 0;
	public int bottomTexture = 0;
	
	private int fallDamage = 5;
	private int id;
	private boolean opaque = true;
	private int renderPass = RenderPass.PASS_OPAQUE;
	private boolean cullSame = false;
	private boolean occluder = true;
	
	private String[] footStep = SoundManager.footstep_dirt;
	private String fall = "fall_hard";
	
	private static int lastAO = -1;

	public abstract void update(int x, int y, int z, World world);
	
	public Block(int id) {
		this.id = id;
		blocks[id] = this;
		blocksCount++;
	}
	
	public Block setFallHurt(int value) {
		this.fallDamage = value;
		return this;
	}
	
	public int getFallHurt() {
		return this.fallDamage;
	}

	public Block setAllTextures(int texture) {
		this.topTexture = texture;
		this.sideTexture = texture;
		this.bottomTexture = texture;
		return this;
	}

	public Block setTopTexture(int topTexture) {
		this.topTexture = topTexture;
		return this;
	}

	public Block setSideTexture(int sideTexture) {
		this.sideTexture = sideTexture;
		return this;
	}

	public Block setBottomTexture(int bottomTexture) {
		this.bottomTexture = bottomTexture;
		return this;
	}

	public Block setStepSound(String[] sound) {
		this.footStep = sound;
		return this;
	}

	public Block setFallSound(String sound) {
		this.fall = sound;
		return this;
	}

	public Block setOccluder(boolean occluder) {
		this.occluder = occluder;
		return this;
	}
	
	public Block setCullSame(boolean cullSame) {
		this.cullSame = cullSame;
		return this;
	}

	public Block setRenderPass(int pass) {
		this.renderPass = pass;
		return this;
	}
	
	public Block setOpaque(boolean opaque) {
		this.opaque = opaque;
		return this;
	}

	/**
	 * This array holds all the possible block instances.
	 */
	public static Block[] blocks = new Block[256];
	public static int blocksCount = 0;
	
	public static final Block grass = new BlockBasic(1).setTopTexture(4).setSideTexture(5).setBottomTexture(3).setStepSound(SoundManager.footstep_grass).setFallSound("fall_soft");
	public static final Block dirt = new BlockBasic(2).setAllTextures(3).setStepSound(SoundManager.footstep_dirt).setFallSound("fall_soft");
	public static final Block stone = new BlockBasic(3).setAllTextures(0).setStepSound(SoundManager.footstep_stone).setFallSound("fall_hard");
	public static final Block wood = new BlockBasic(4).setAllTextures(2).setStepSound(SoundManager.footstep_wood).setFallSound("fall_hard");
	public static final Block stoneMossy = new BlockBasic(5).setAllTextures(1).setStepSound(SoundManager.footstep_stone).setFallSound("fall_hard");
	public static final Block bricks = new BlockBasic(6).setAllTextures(8).setStepSound(SoundManager.footstep_stone).setFallSound("fall_hard");
	public static final Block sand = new BlockBasic(7).setAllTextures(6).setStepSound(SoundManager.footstep_dirt).setFallSound("fall_sotf");
	public static final Block log = new BlockBasic(8).setAllTextures(11).setSideTexture(7).setStepSound(SoundManager.footstep_wood).setFallSound("fall_hard");
	public static final Block glass = new BlockBasic(9).setAllTextures(9).setOpaque(false).setCullSame(true).setStepSound(SoundManager.footstep_stone).setFallSound("fall_hard").setRenderPass(RenderPass.PASS_TRANSPARENT);
	public static final Block leaves = new BlockBasic(10).setAllTextures(Game.settings.transparent_leaves.getValue() ? 10 : 19).setOpaque(!Game.settings.transparent_leaves.getValue()).setStepSound(SoundManager.footstep_grass).setFallSound("fall_soft").setRenderPass(Game.settings.transparent_leaves.getValue() ? RenderPass.PASS_TRANSPARENT : RenderPass.PASS_OPAQUE);
	public static final Block bedrock = new BlockBasic(11).setAllTextures(12).setStepSound(SoundManager.footstep_stone).setFallSound("fall_hard");
	public static final Block water = new BlockFluid(12).setAllTextures(13).setCullSame(true).setOccluder(false).setOpaque(false);
	public static final Block ice = new BlockBasic(13).setAllTextures(14).setOpaque(false).setCullSame(true).setRenderPass(RenderPass.PASS_TRANSLUCENT);
	public static final Block tallgrass = new BlockPlant(14).setAllTextures(15);
	public static final Block flower_1 = new BlockPlant(15).setAllTextures(16);
	public static final Block flower_2 = new BlockPlant(16).setAllTextures(17);
	public static final Block flower_3 = new BlockPlant(17).setAllTextures(18);

	/**
	 * Returns an instance of block based on the id
	 * @param id
	 * @return block with corresponding id
	 */
	public static Block getBlock(int id) {
		return blocks[id];
	}

	public int getId() {
		return this.id;
	}

	public String[] getStepSound() {
		return this.footStep;
	}

	public String getFallSound() {
		return this.fall;
	}

	public boolean shouldCullSame() {
		return this.cullSame;
	}

	public boolean isOccluder() {
		return this.occluder;
	}

	public boolean isOpaque() {
		return this.opaque;
	}

	public int getRenderPass() {
		return renderPass;
	}

	/**
	 * Draws a vertex with color based on the occluding sides and corner.
	 * @param x x position of the vertex
	 * @param y y position of the vertex
	 * @param z z position of the vertex
	 * @param s1 occlusion of the first side
	 * @param s2 occlusion of the second side
	 * @param c occlusion of the corner
	 */
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

	public abstract void renderIndependent(int x, int y, int z);
	public abstract void renderFront(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp);
	public abstract void renderBack(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp);
	public abstract void renderRight(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp);
	public abstract void renderLeft(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp);
	public abstract void renderTop(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp);
	public abstract void renderBottom(int x, int y, int z, boolean xnzn, boolean zn, boolean xpzn, boolean xn, boolean xp, boolean xnzp, boolean zp, boolean xpzp);

}
