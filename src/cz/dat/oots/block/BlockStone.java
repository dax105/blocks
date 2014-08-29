package cz.dat.oots.block;

import java.util.Random;

import org.newdawn.slick.Color;

import cz.dat.oots.data.IDataObject;
import cz.dat.oots.data.block.StoneDataObject;
import cz.dat.oots.gui.ingame.ColorScreen;
import cz.dat.oots.gui.ingame.IColorChangeCallback;
import cz.dat.oots.sound.SoundManager;
import cz.dat.oots.world.IDRegister;
import cz.dat.oots.world.World;

public class BlockStone extends BlockBasic {
	Random rnd;
	Color currentColor = new Color(255, 255, 255, 255);
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
			clrScreen = new ColorScreen(new Color(255, 255, 255, 255), new IColorChangeCallback() {
				@Override
				public void onColorChanged(ColorScreen caller, Color newColor) {
					currentColor.r = newColor.r;
					currentColor.g = newColor.g;
					currentColor.b = newColor.b;
				}			}, world.getGui());

			this.clrScreenID = world.getGui().registerNewScreen(clrScreen);
		}
		
		if(button == 0) {
			world.getGui().setCurrentScreen(this.clrScreenID);
			world.getGui().openScreen();
		} else {
			this.recolor(x, y, z, world);
		}

		world.setChunkDirty(x >> 4, y / 16, z >> 4);
	}
}
