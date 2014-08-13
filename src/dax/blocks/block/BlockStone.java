package dax.blocks.block;

import java.util.Random;

import org.newdawn.slick.Color;

import dax.blocks.data.IDataObject;
import dax.blocks.data.block.StoneDataObject;
import dax.blocks.gui.ingame.ColorScreen;
import dax.blocks.gui.ingame.GuiManager;
import dax.blocks.gui.ingame.IColorChangeCallback;
import dax.blocks.sound.SoundManager;
import dax.blocks.world.IDRegister;
import dax.blocks.world.World;

public class BlockStone extends BlockBasic {
	Random rnd;
	Color currentColor = Color.white;
	ColorScreen clrScreen;
	int clrScreenID;
	
	
	public BlockStone(IDRegister r) {
		super("oots/blockStone", r);
		rnd = new Random();
		this.setAllTextures(0).setFootStepSound(SoundManager.footstep_stone).setFallSound("fall_hard");
	}

	public void updateColor(int x, int y, int z, World w) {
		if(w.hasData(x, y, z)) {
			StoneDataObject d = (StoneDataObject) w.getData(x, y, z);
			
			this.setColor(d.getColorR(), d.getColorG(), d.getColorB());
		}
	}

	public void recolor(int x, int y, int z, World w) {
		StoneDataObject d;
		
		if(!w.hasData(x, y, z)) {
			d = (StoneDataObject) w.createData(x, y, z, this);
		} else {
			d = (StoneDataObject) w.getData(x, y, z);
		}

		d.setColorR(currentColor.r);
		d.setColorG(currentColor.g);
		d.setColorB(currentColor.b);
		
		System.out.println(currentColor.r + ", " + currentColor.g + ", " + currentColor.b);
	}
	
	@Override
	public IDataObject createDataObject() {
		return new StoneDataObject(this);
	}

	
	@Override
	public void onClick(int button, int x, int y, int z, World world) {
		if(clrScreen == null) {
			clrScreen = new ColorScreen(100, 100, Color.white, new IColorChangeCallback() {
				@Override
				public void onColorChanged(ColorScreen caller, Color newColor) {
					currentColor = newColor;
				}			}, GuiManager.getInstance());

			this.clrScreenID = GuiManager.getInstance().registerNewScreen(clrScreen);
		}
		
		if(button == 0) {
			GuiManager.getInstance().setCurrentScreen(this.clrScreenID);
			GuiManager.getInstance().openScreen();
		} else {
			this.recolor(x, y, z, world);
		}

		world.setChunkDirty(x >> 4, y / 16, z >> 4);
	}
}
