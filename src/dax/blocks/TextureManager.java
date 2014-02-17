package dax.blocks;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class TextureManager {

	public static final int ATLAS_SIZE = 4;
	public static final float TEXTURE_SIZE = 1.0f / ATLAS_SIZE;
	public static float texel_offset;

	public static Texture atlas;
	public static Texture logo;
	public static Texture menuBg;

	private static Texture loadTex(String tp) {
		try {
			Texture tex = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(tp));
			System.out.println("Successfully loaded texture from " + tp);
			return tex;
		} catch (IOException e) {
			System.err.println("Can't load texture from " + tp + ", perhaps the file doesn't exist?");
			System.exit(1);
		}
		return null;
	}

	public static void load() {
		atlas = loadTex("dax/blocks/res/textures/texatlas.png");
		logo = loadTex("dax/blocks/res/textures/logo.png");
		menuBg = loadTex("dax/blocks/res/textures/menubg.png");
		calculateTexOffset();
	}

	public static void calculateTexOffset() {
		texel_offset = (1.0f / atlas.getImageWidth()) / 2;
	}

	public static float getX1(int texid) {
		return (texid % ATLAS_SIZE) * (1.0f / ATLAS_SIZE) + texel_offset;
	}

	public static float getX2(int texid) {
		return (texid % ATLAS_SIZE) * (1.0f / ATLAS_SIZE) + TEXTURE_SIZE - texel_offset;
	}

	public static float getY1(int texid) {
		return (texid / ATLAS_SIZE) * (1.0f / ATLAS_SIZE) + texel_offset;
	}

	public static float getY2(int texid) {
		return (texid / ATLAS_SIZE) * (1.0f / ATLAS_SIZE) + TEXTURE_SIZE - texel_offset;
	}

	public static float getU(float x, float texW) {
		float u = (x + 0.5f) / texW;
		return u;
	}

	public static float getV(float y, float texH) {
		float v = (y + 0.5f) / texH;
		return v;
	}

}
