package dax.blocks.world;

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

import dax.blocks.block.Block;
import dax.blocks.block.BlockBasic;
import dax.blocks.block.BlockFluid;
import dax.blocks.block.BlockPlant;
import dax.blocks.block.BlockSand;
import dax.blocks.block.BlockStone;
import dax.blocks.render.RenderPass;
import dax.blocks.settings.Settings;
import dax.blocks.sound.SoundManager;
import dax.blocks.util.RegisterException;

public class IDRegister {

	private Block[] blocks;
	private int blockCount;
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
	public static Block ice;
	public static Block tallGrass;
	public static Block flower1;
	public static Block flower2;
	public static Block flower3;

	public IDRegister(World world) {
		ids = new HashMap<>();
		blocks = new Block[512];
		this.blockCount = 0;
		
		IDRegister.dataFile = new File(new File(WorldsManager.SAVES_DIR, world.name), "ids");
		
		try {
			this.loadIDs(IDRegister.dataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void registerDefaultBlocks() {
		try {
			grass = registerBlock(new BlockBasic("oots/blockGrass", this)
					.setTopTexture(4).setSideTexture(5).setBottomTexture(3)
					.setFootStepSound(SoundManager.footstep_grass)
					.setFallSound("fall_soft"));
			dirt = registerBlock(new BlockBasic("oots/blockDirt", this)
					.setAllTextures(3)
					.setFootStepSound(SoundManager.footstep_dirt)
					.setFallSound("fall_soft"));
			stone = registerBlock(new BlockStone(this));
			wood = registerBlock(new BlockBasic("oots/blockWood", this)
					.setAllTextures(2)
					.setFootStepSound(SoundManager.footstep_wood)
					.setFallSound("fall_hard"));
			stoneMossy = registerBlock(new BlockBasic("oots/blockStoneMossy",
					this).setAllTextures(1)
					.setFootStepSound(SoundManager.footstep_stone)
					.setFallSound("fall_hard"));
			bricks = registerBlock(new BlockBasic("oots/blockBricks", this)
					.setAllTextures(8)
					.setFootStepSound(SoundManager.footstep_stone)
					.setFallSound("fall_hard"));
			sand = registerBlock(new BlockSand(this));
			log = registerBlock(new BlockBasic("oots/blockLog", this)
					.setAllTextures(11).setSideTexture(7)
					.setFootStepSound(SoundManager.footstep_wood)
					.setFallSound("fall_hard"));
			glass = registerBlock(new BlockBasic("oots/blockGlass", this)
					.setAllTextures(9).setOpaque(false).setCullSame(true)
					.setFootStepSound(SoundManager.footstep_stone)
					.setFallSound("fall_hard")
					.setRenderPass(RenderPass.TRANSPARENT));
			leaves = registerBlock(new BlockBasic("oots/blockLeaves", this)
					.setAllTextures(
							Settings.getInstance().transparentLeaves.getValue() ? 10
									: 19)
					.setOpaque(
							!Settings.getInstance().transparentLeaves
									.getValue())
					.setFootStepSound(SoundManager.footstep_grass)
					.setFallSound("fall_soft")
					.setRenderPass(
							Settings.getInstance().transparentLeaves.getValue() ? RenderPass.TRANSPARENT
									: RenderPass.OPAQUE));
			bedrock = registerBlock(new BlockBasic("oots/blockBedrock", this)
					.setAllTextures(12)
					.setFootStepSound(SoundManager.footstep_stone)
					.setFallSound("fall_hard"));
			water = registerBlock(new BlockFluid("oots/fluidWater", this)
					.setAllTextures(13).setCullSame(true).setOccluder(false)
					.setOpaque(false).setDensity(1.175f));
			ice = registerBlock(new BlockBasic("oots/blockIce", this)
					.setAllTextures(14).setOpaque(false).setCullSame(true)
					.setRenderPass(RenderPass.TRANSLUCENT));
			tallGrass = registerBlock(new BlockPlant("oots/plantGrass", this)
					.setAllTextures(15));
			flower1 = registerBlock(new BlockPlant("oots/plantFlowerRed", this)
					.setAllTextures(16));
			flower2 = registerBlock(new BlockPlant("oots/plantFlowerYellow",
					this).setAllTextures(17));
			flower3 = registerBlock(new BlockPlant("oots/plantFlowerWhite",
					this).setAllTextures(18));
		} catch (RegisterException e) {
			e.printStackTrace();
		}
	}

	public void loadIDs(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));

		String line;

		while ((line = br.readLine()) != null) {
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
		for(Entry<String, Integer> id : ids.entrySet()) {
			bw.write(id.getKey() + ";" + id.getValue().toString());
			bw.newLine();
		}
		
		bw.close();
	}
	
	public int getIDForName(String name) {
		if (ids.get(name) == null) {

			try {
				return registerName(name);
			} catch (RegisterException e) {
				e.printStackTrace();
			}

		} else {
			return ids.get(name);
		}

		return 0;
	}

	public Block getBlock(int id) {
		return blocks[id];
	}

	public int registerName(String name) throws RegisterException {
		if (ids.get(name) == null) {
			ids.put(name, getBlockCount() + 1);
			return getBlockCount() + 1;
		} else {
			throw new RegisterException(name);
		}
	}

	public Block registerBlock(Block block) throws RegisterException {
		if (blocks[block.getID()] == null) {
			blocks[block.getID()] = block;
			this.blockCount = (getBlockCount() + 1);
			return block;
		} else {
			throw new RegisterException(block.getID());
		}
	}

	public int getBlockCount() {
		return blockCount;
	}


}
