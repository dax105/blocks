package dax.blocks.block;

import java.util.HashMap;
import java.util.Map;

import dax.blocks.Coord3D;
import dax.blocks.Game;
import dax.blocks.block.renderer.BlockRendererBasic;
import dax.blocks.block.renderer.IBlockRenderer;
import dax.blocks.collisions.AABB;
import dax.blocks.item.Item;
import dax.blocks.render.RenderPass;
import dax.blocks.sound.SoundManager;
import dax.blocks.world.World;

public abstract class Block extends Item {

	public int topTexture = 0;
	public int sideTexture = 0;
	public int bottomTexture = 0;
	
	private int fallHurt = 5;
	private boolean opaque = true;
	private int renderPass = RenderPass.OPAQUE;
	private boolean cullSame = false;
	private boolean occluder = true;
	private float density = 1.0f;
	private boolean requiresTick = false;
	private boolean requiresRenderTick = false;
	private String[] footStep = SoundManager.footstep_dirt;
	private String fall = "fall_hard";
	private AABB aabb = new AABB(0, 0, 0, 1, 1, 1);
	private boolean collidable = true;
	private IBlockRenderer renderer = new BlockRendererBasic();
	
	protected float colorR = 1f;
	protected float colorG = 1f;
	protected float colorB = 1f;
	
	public static Map<Coord3D, Block> tickingBlocks = new HashMap<>();
	public static int blocksCount = 0;
	
	public static final Block air = new BlockEmpty(true);
	public static final Block nullBlock = new BlockEmpty(false);
	public static final Block grass = new BlockBasic(3).setTopTexture(4).setSideTexture(5).setBottomTexture(3).setStepSound(SoundManager.footstep_grass).setFallSound("fall_soft");
	public static final Block dirt = new BlockBasic(4).setAllTextures(3).setStepSound(SoundManager.footstep_dirt).setFallSound("fall_soft");
	public static final Block stone = new BlockStone();
	public static final Block wood = new BlockBasic(6).setAllTextures(2).setStepSound(SoundManager.footstep_wood).setFallSound("fall_hard");
	public static final Block stoneMossy = new BlockBasic(7).setAllTextures(1).setStepSound(SoundManager.footstep_stone).setFallSound("fall_hard");
	public static final Block bricks = new BlockBasic(8).setAllTextures(8).setStepSound(SoundManager.footstep_stone).setFallSound("fall_hard");
	public static final Block sand = new BlockSand();
	public static final Block log = new BlockBasic(10).setAllTextures(11).setSideTexture(7).setStepSound(SoundManager.footstep_wood).setFallSound("fall_hard");
	public static final Block glass = new BlockBasic(11).setAllTextures(9).setOpaque(false).setCullSame(true).setStepSound(SoundManager.footstep_stone).setFallSound("fall_hard").setRenderPass(RenderPass.TRANSPARENT);
	public static final Block leaves = new BlockBasic(12).setAllTextures(Game.settings.transparent_leaves.getValue() ? 10 : 19).setOpaque(!Game.settings.transparent_leaves.getValue()).setStepSound(SoundManager.footstep_grass).setFallSound("fall_soft").setRenderPass(Game.settings.transparent_leaves.getValue() ? RenderPass.TRANSPARENT : RenderPass.OPAQUE);
	public static final Block bedrock = new BlockBasic(13).setAllTextures(12).setStepSound(SoundManager.footstep_stone).setFallSound("fall_hard");
	public static final Block water = new BlockFluid(14).setAllTextures(13).setCullSame(true).setOccluder(false).setOpaque(false).setDensity(1.175f);
	public static final Block ice = new BlockBasic(15).setAllTextures(14).setOpaque(false).setCullSame(true).setRenderPass(RenderPass.TRANSLUCENT);
	public static final Block tallgrass = new BlockPlant(16).setAllTextures(15);
	public static final Block flower_1 = new BlockPlant(17).setAllTextures(16);
	public static final Block flower_2 = new BlockPlant(18).setAllTextures(17);
	public static final Block flower_3 = new BlockPlant(19).setAllTextures(18);
	
	public static Block getBlock(int id) {
		Item i = Item.getItem(id);
		
		if(i == Item.nullItem || i == nullBlock) {
			return nullBlock;
		}
		
		return (Block) i;
	}
	
	public Block(int id) {
		super(id);
		blocksCount++;
	}
		
	public Block requiresTick() {
		this.requiresTick = true;
		return this;
	}
	
	public Block requiresRenderTick() {
		this.requiresRenderTick = true;
		return this;
	}
	
	public boolean isRequiringRenderTick() {
		return this.requiresRenderTick;
	}
	
	public boolean isRequiringTick() {
		return this.requiresTick;
	}
	
	public Block setFallHurt(int value) {
		this.fallHurt = value;
		return this;
	}
	
	public int getFallHurt() {
		return this.fallHurt;
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

	public Block setDensity(float density) {
		this.density = density;
		return this;
	}
	
	public Block setAABB(AABB bb) {
		this.aabb = bb;
		return this;
	}
	
	public Block setCollidable(boolean collidable) {
		this.collidable = collidable;
		return this;
	}
	
	public Block setRenderer(IBlockRenderer renderer) {
		this.renderer = renderer;
		return this;
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

	public float getDensity() {
		return density;
	}

	public int getRenderPass() {
		return renderPass;
	}
	
	public AABB getAABB() {
		this.aabb.resetOffset();
		return this.aabb;
	}
	
	public AABB getOffsetAABB(float x, float y, float z) {
		this.aabb.resetOffset();
		this.aabb.setOffset(x, y, z);
		return this.aabb;
	}
	
	public boolean isCollidable() {
		return this.collidable;
	}
	
	public IBlockRenderer getRenderer() {
		return this.renderer;
	}
	
	public float getColorR() {
		return this.colorR;
	}
	
	public float getColorG() {
		return this.colorG;
	}
	
	public float getColorB() {
		return this.colorB;
	}
	
	public void onPlaced(int x, int y, int z, World world) {
		if(this.isRequiringTick() || this.isRequiringRenderTick()) {
			Block.tickingBlocks.put(new Coord3D(x, y, z), this);
		}
	}
	
	public void onRemoved(int x, int y, int z, World world) {
		Coord3D c = new Coord3D(x, y, z);
		if(Block.tickingBlocks.get(c) != null) {
			Block.tickingBlocks.remove(c);
		}
		
		world.removeData(x, y, z);
	}
	

	public void setColor(int x, int y, int z, World w) {

	}

	public void restoreColor() {
		this.colorR = 1;
		this.colorG = 1;
		this.colorB = 1;
	}
	
	public void onTick(int itemIdentifier) {
		
	}
	
	public abstract void onTick(int x, int y, int z, World world);
	public abstract void onRenderTick(float partialTickTime, int x, int y, int z, World world);
	public abstract void onClicked(int button, int x, int y, int z, World world);
}
