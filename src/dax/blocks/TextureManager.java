package dax.blocks;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class TextureManager {

	public static final int ATLAS_SIZE = 8;
	public static float textureSizeCoord;
	public static float texSize;
	
	public static Texture atlas;
	public static Texture logo;
	public static Texture menuBg;
	public static Texture clouds;
	
	public static Texture skybox_back;
	public static Texture skybox_bottom;
	public static Texture skybox_front;
	public static Texture skybox_left;
	public static Texture skybox_right;
	public static Texture skybox_top;
	public static Texture skybox_side;
	
	public static Texture life_full;
	public static Texture life_zero;

	private static Texture loadTex(String path) {
		try {
			Texture tex = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(path));
			Game.console.out("Successfully loaded texture from " + path);
			return tex;
		} catch (IOException e) {
			System.err.println("Can't load texture from " + path + ", perhaps the file doesn't exist?");
			System.exit(1);
		}
		return null;
	}
	
	public static BufferedImage loadImage(String path) {
		try {
			BufferedImage image = ImageIO.read(ResourceLoader.getResourceAsStream(path));
			Game.console.out("Successfully loaded texture as image from " + path);
			return image;
		} catch (IOException e) {
			System.err.println("Can't load texture as image from " + path + ", perhaps the file doesn't exist?");
			System.exit(1);
		}
		return null;
	}

	public static void load() {
		logo = loadTex("dax/blocks/res/textures/logo.png");
		menuBg = loadTex("dax/blocks/res/textures/menubg.png");
		clouds = loadTex("dax/blocks/res/textures/clouds.png");
		
		skybox_back = loadTex("dax/blocks/res/textures/skybox_back.png");
		skybox_bottom = loadTex("dax/blocks/res/textures/skybox_bottom.png");
		skybox_front = loadTex("dax/blocks/res/textures/skybox_front.png");
		skybox_left = loadTex("dax/blocks/res/textures/skybox_left.png");
		skybox_right = loadTex("dax/blocks/res/textures/skybox_right.png");
		skybox_top = loadTex("dax/blocks/res/textures/skybox_top.png");
		skybox_side = loadTex("dax/blocks/res/textures/skybox_side.png");
		
		life_full = loadTex("dax/blocks/res/textures/life_full.png");
		life_zero = loadTex("dax/blocks/res/textures/life_null.png");
		
		skybox_back.bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		skybox_bottom.bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		skybox_front.bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		skybox_left.bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		skybox_right.bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		skybox_top.bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		skybox_side.bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		
		clouds.bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		AtlasBuilder ab = new AtlasBuilder();
		ab.addTexture(loadImage("dax/blocks/res/textures/blocks/stone.png"));
		ab.addTexture(loadImage("dax/blocks/res/textures/blocks/cobblestone_mossy.png"));
		ab.addTexture(loadImage("dax/blocks/res/textures/blocks/planks_oak.png"));
		ab.addTexture(loadImage("dax/blocks/res/textures/blocks/dirt.png"));
		ab.addTexture(loadImage("dax/blocks/res/textures/blocks/grass_top.png"));
		ab.addTexture(loadImage("dax/blocks/res/textures/blocks/grass_side.png"));
		ab.addTexture(loadImage("dax/blocks/res/textures/blocks/sand.png"));
		ab.addTexture(loadImage("dax/blocks/res/textures/blocks/log_oak.png"));
		ab.addTexture(loadImage("dax/blocks/res/textures/blocks/brick.png"));
		ab.addTexture(loadImage("dax/blocks/res/textures/blocks/glass.png"));
		ab.addTexture(loadImage("dax/blocks/res/textures/blocks/leaves.png"));
		ab.addTexture(loadImage("dax/blocks/res/textures/blocks/log_oak_top.png"));
		ab.addTexture(loadImage("dax/blocks/res/textures/blocks/bedrock.png"));
		ab.addTexture(loadImage("dax/blocks/res/textures/blocks/water.png"));
		ab.addTexture(loadImage("dax/blocks/res/textures/blocks/ice.png"));
		ab.addTexture(loadImage("dax/blocks/res/textures/blocks/tallgrass.png"));
		ab.addTexture(loadImage("dax/blocks/res/textures/blocks/flower_y.png"));
		ab.addTexture(loadImage("dax/blocks/res/textures/blocks/flower_r.png"));
		ab.addTexture(loadImage("dax/blocks/res/textures/blocks/flower_w.png"));
		ab.addTexture(loadImage("dax/blocks/res/textures/blocks/leaves_opaque.png"));
		
		atlas = ab.buildAtlas(true);
		
		atlas.bind();
		
		textureSizeCoord = atlas.getWidth() / ATLAS_SIZE / (AtlasBuilder.EXPAND_RATIO*2+1);
		texSize = atlas.getWidth();
		
		if (Game.settings.mipmaps.getValue()) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		}
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 0);
		
		try {
			EXTFramebufferObject.glGenerateMipmapEXT(GL11.GL_TEXTURE_2D);
		} catch (Exception e) {
			
			JOptionPane.showMessageDialog(null, "An error occured while generating mipmaps! \n" + e + e.getStackTrace());
			
			e.printStackTrace();
		}
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, Game.settings.anisotropic.getValue());
		
		//GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
		//GL11.glTexParameteri (GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 2-1);

	}
	
	public static float getX1(int texid) {
		return (texid % ATLAS_SIZE) * (texSize / ATLAS_SIZE) + textureSizeCoord/(1/AtlasBuilder.EXPAND_RATIO);
	}

	public static float getX2(int texid) {
		return (texid % ATLAS_SIZE) * (texSize / ATLAS_SIZE) + textureSizeCoord + textureSizeCoord/(1/AtlasBuilder.EXPAND_RATIO);
	}

	public static float getY1(int texid) {
		return (texid / ATLAS_SIZE) * (texSize / ATLAS_SIZE) + textureSizeCoord/(1/AtlasBuilder.EXPAND_RATIO);
	}

	public static float getY2(int texid) {
		return (texid / ATLAS_SIZE) * (texSize / ATLAS_SIZE) + textureSizeCoord + textureSizeCoord/(1/AtlasBuilder.EXPAND_RATIO);
	}



}
