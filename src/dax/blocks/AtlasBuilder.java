package dax.blocks;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

import dax.blocks.console.Console;
import dax.blocks.util.GameMath;

public class AtlasBuilder {

	public static final int ATLAS_SIZE = 8;
	public static final float EXPAND_RATIO = 1.0f;
	
	private List<BufferedImage> textures = new ArrayList<BufferedImage>();
	
	private int texSize;
	
	private boolean isEmpty = true;
	
	public int getTexSize() {
		return this.texSize;
	}
	
	public void addTexture(BufferedImage tex) {
		if(this.isEmpty) {
			this.isEmpty = false;	
			
			if(tex.getWidth() != tex.getHeight()) {
				Console.getInstance().out("Texture w/h mismatch, ratio must be 1:1! Exiting!");
				System.exit(1);
			}	
			
			this.texSize = tex.getWidth();
			
			if(!GameMath.isPowerOfTwo(this.texSize)) {
				Console.getInstance().out("Texture size is not power of two! Exiting!");
				System.exit(1);
			}
			
		} else if(tex.getWidth() != this.texSize || tex.getHeight() != this.texSize){
			Console.getInstance().out("Texture size mismatch while building texture atlas! Exiting!");
			System.exit(1);
		}
		
		this.textures.add(tex);
	}
	
	public Texture buildAtlas(boolean avoidBleeding) {
		Console.getInstance().out("Building texture atlas...");
		if(this.textures.size() == 0) {
			Console.getInstance().out("No textures added, cannot return empty texture atlas! Exiting!");
			System.exit(1);
		}
		
		BufferedImage img = new BufferedImage(
				AtlasBuilder.ATLAS_SIZE * (int)Math.round((AtlasBuilder.EXPAND_RATIO * 2 + 1)) * this.texSize, 
				AtlasBuilder.ATLAS_SIZE * (int)Math.round((AtlasBuilder.EXPAND_RATIO * 2 + 1)) * this.texSize, 
				BufferedImage.TYPE_INT_ARGB
		);
		Graphics2D g = (Graphics2D) img.getGraphics();
		
		Console.println("Atlas size: " + img.getWidth() + "px"); 
		
		for(int i = 0; i < this.textures.size(); i++) {
			Console.getInstance().out("Adding texture with index " + i + " to the texture atlas image!");
			BufferedImage tex = this.textures.get(i);
			
			int offsetX = (int) ((i % 8) * (AtlasBuilder.EXPAND_RATIO*2+1) * this.texSize);
			int offsetY = (int) ((i / 8) * (AtlasBuilder.EXPAND_RATIO*2+1) * this.texSize);
			
			int expand = (int)Math.round(AtlasBuilder.EXPAND_RATIO * this.texSize);
			int size = this.texSize;
			
			g.drawImage(tex.getSubimage(size-expand, size-expand, expand, expand), offsetX, offsetY, null);
			g.drawImage(tex.getSubimage(0, size-expand, size, expand), offsetX+expand, offsetY, null);
			g.drawImage(tex.getSubimage(0, size-expand, expand, expand), offsetX+size+expand, offsetY, null);
			
			g.drawImage(tex.getSubimage(size-expand, 0, expand, size), offsetX, offsetY+expand, null);
			g.drawImage(tex, offsetX+expand, offsetY+expand, null);
			g.drawImage(tex.getSubimage(0, 0, expand, size), offsetX+size+expand, offsetY+expand, null);
			
			g.drawImage(tex.getSubimage(size-expand, 0, expand, expand), offsetX, offsetY+size+expand, null);
			g.drawImage(tex.getSubimage(0, 0, size, expand), offsetX+expand, offsetY+size+expand, null);
			g.drawImage(tex.getSubimage(0, 0, expand, expand), offsetX+size+expand, offsetY+size+expand, null);
			
		}
		
		Texture atlas = null;
		try {
			atlas = BufferedImageUtil.getTexture(null, img);
		} catch (IOException e1) {
			System.err.println("Something went wrong while loading the atlas texture!");
			e1.printStackTrace();
		}
		
		try {
		    BufferedImage bi = img;
		    File outputfile = new File("saved.png");
		    ImageIO.write(bi, "png", outputfile);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		Console.println("Texture atlas built successfully!");
		
		return atlas;
	}
	
}
