package cz.dat.oots.world;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.activation.UnsupportedDataTypeException;

import cz.dat.oots.block.Block;
import cz.dat.oots.block.BlockBasic;
import cz.dat.oots.block.BlockFluid;
import cz.dat.oots.block.BlockPlant;
import cz.dat.oots.block.BlockSand;
import cz.dat.oots.block.BlockSlab;
import cz.dat.oots.block.BlockStone;
import cz.dat.oots.item.Item;
import cz.dat.oots.item.ItemImaginaryChocolate;
import cz.dat.oots.render.RenderPass;
import cz.dat.oots.sound.SoundManager;
import cz.dat.oots.util.RegisterException;
import cz.dat.oots.world.loading.WorldManager;

public class IDRegister {

	private World world;
	private Block[] blocks;
	private int blockCount;

	private Item[] items;
	private int itemCount;

	private Map<String, Integer> ids;

	public static File dataFile;

	public static Block bedrock;
	public static Block grass;
	public static Block dirt;
	public static Block stone;
	public static Block stoneMossy;
	public static Block wood;
	public static Block leaves;
	public static Block bricks;
	public static Block sand;
	public static Block log;
	public static Block glass;
	public static Block water;
	public static Block lava;
	public static Block ice;
	public static Block tallGrass;
	public static Block flower1;
	public static Block flower2;
	public static Block flower3;
	public static Block slab;

	public static Item itemImaginaryChocolate;

	public IDRegister(World world) {
		this.world = world;
		this.ids = new HashMap<>();
		this.blocks = new Block[512];
		this.blockCount = 0;
		this.items = new Item[1024];
		this.itemCount = 0;

		IDRegister.dataFile = new File(new File(WorldManager.SAVES_DIR,
				world.name), "ids");

		try {
			this.loadIDs(IDRegister.dataFile);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void registerDefaultBlocks() {
		try {
			IDRegister.grass = registerBlock(new BlockBasic("oots/blockGrass",
					this).setTopTexture(4).setSideTexture(5)
					.setBottomTexture(3)
					.setFootStepSound(SoundManager.footstep_grass)
					.setFallSound("fall_soft"));
			IDRegister.dirt = registerBlock(new BlockBasic("oots/blockDirt",
					this).setAllTextures(3)
					.setFootStepSound(SoundManager.footstep_dirt)
					.setFallSound("fall_soft"));
			IDRegister.stone = registerBlock(new BlockStone(this));
			IDRegister.wood = registerBlock(new BlockBasic("oots/blockWood",
					this).setAllTextures(2)
					.setFootStepSound(SoundManager.footstep_wood)
					.setFallSound("fall_hard"));
			IDRegister.stoneMossy = registerBlock(new BlockBasic(
					"oots/blockStoneMossy", this).setAllTextures(1)
					.setFootStepSound(SoundManager.footstep_stone)
					.setFallSound("fall_hard"));
			IDRegister.bricks = registerBlock(new BlockBasic(
					"oots/blockBricks", this).setAllTextures(8)
					.setFootStepSound(SoundManager.footstep_stone)
					.setFallSound("fall_hard"));
			IDRegister.sand = registerBlock(new BlockSand(this));
			IDRegister.log = registerBlock(new BlockBasic("oots/blockLog", this)
					.setAllTextures(11).setSideTexture(7)
					.setFootStepSound(SoundManager.footstep_wood)
					.setFallSound("fall_hard"));
			IDRegister.glass = registerBlock(new BlockBasic("oots/blockGlass",
					this).setAllTextures(9).setOpaque(false).setCullSame(true)
					.setFootStepSound(SoundManager.footstep_stone)
					.setFallSound("fall_hard"));
			IDRegister.leaves = registerBlock(new BlockBasic(
					"oots/blockLeaves", this)
					.setAllTextures(
							this.world.getGame().s().transparentLeaves
									.getValue() ? 10 : 19)
					.setOpaque(
							!this.world.getGame().s().transparentLeaves
									.getValue())
					.setFootStepSound(SoundManager.footstep_grass)
					.setFallSound("fall_soft"));
			IDRegister.bedrock = registerBlock(new BlockBasic(
					"oots/blockBedrock", this).setAllTextures(12)
					.setFootStepSound(SoundManager.footstep_stone)
					.setFallSound("fall_hard"));
			IDRegister.water = registerBlock(new BlockFluid("oots/fluidWater",
					this).setAllTextures(13).setCullSame(true)
					.setOccluder(true).setOpaque(false).setDensity(1.175f));
			
			IDRegister.lava = registerBlock(new BlockFluid("oots/fluidLava",
					this).setAllTextures(20).setCullSame(true)
					.setOccluder(false).setOpaque(false).setDensity(2.55f));
			
			IDRegister.ice = registerBlock(new BlockBasic("oots/blockIce", this)
					.setAllTextures(14).setOpaque(false).setCullSame(true)
					.setRenderPass(RenderPass.TRANSLUCENT));
			IDRegister.tallGrass = registerBlock(new BlockPlant("oots/plantGrass", this)
					.setAllTextures(15));
			IDRegister.flower1 = registerBlock(new BlockPlant("oots/plantFlowerRed", this)
					.setAllTextures(16));
			IDRegister.flower2 = registerBlock(new BlockPlant("oots/plantFlowerYellow",
					this).setAllTextures(17));
			IDRegister.flower3 = registerBlock(new BlockPlant("oots/plantFlowerWhite",
					this).setAllTextures(18));
			IDRegister.slab = registerBlock(new BlockSlab("oots/slab", this));
		} catch (RegisterException e) {
			e.printStackTrace();
		}
	}

	public void registerDefaultItems() {
		try {
			IDRegister.itemImaginaryChocolate = this
					.registerItem(new ItemImaginaryChocolate(this));
		} catch(RegisterException e) {
			e.printStackTrace();
		}
	}

	public void loadIDs(File file) throws IOException {
		if(!file.exists())
			return;

		BufferedReader br = new BufferedReader(new FileReader(file));

		String line;

		while((line = br.readLine()) != null) {
			String[] parts = line.split(";");
			if(parts.length != 2) {
				br.close();
				throw new UnsupportedDataTypeException("Invalid file!");
			}

			int finalID = Integer.parseInt(parts[1]);

			this.ids.put(parts[0], finalID);
		}

		br.close();
	}

	public void saveIDs(File file) throws IOException {
		if(!file.exists()) {
			file.createNewFile();
		}

		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		for(Entry<String, Integer> id : this.ids.entrySet()) {
			bw.write(id.getKey() + ";" + id.getValue().toString());
			bw.newLine();
		}

		bw.close();
	}

	public int getIDForName(String name) {
		if(this.ids.get(name) == null) {

			try {
				return this.registerName(name);
			} catch(RegisterException e) {
				e.printStackTrace();
			}

		} else {
			return this.ids.get(name);
		}

		return 0;
	}

	public Block getBlock(int id) {
		return blocks[id];
	}

	public Item getItem(int id) {
		return items[id];
	}

	public int registerName(String name) throws RegisterException {
		if(this.ids.get(name) == null) {
			this.ids.put(name, this.getBlockCount() + 1);
			return this.getBlockCount() + 1;
		} else {
			throw new RegisterException(name);
		}
	}

	public Block registerBlock(Block block) throws RegisterException {
		if(this.blocks[block.getID()] == null) {
			this.blocks[block.getID()] = block;
			this.blockCount++;
			return block;
		} else {
			throw new RegisterException(block.getID());
		}
	}

	public Item registerItem(Item item) throws RegisterException {
		if(this.items[item.getID()] == null
				&& this.blocks[item.getID()] == null) {
			this.items[item.getID()] = item;
			this.itemCount++;
			return item;
		} else {
			throw new RegisterException(item.getID());
		}
	}

	public int getBlockCount() {
		return this.blockCount;
	}

	public int getItemCount() {
		return this.itemCount;
	}

}
