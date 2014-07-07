package dax.blocks;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;



public class FontManager {

	public static TrueTypeFont text;
	
	public static void load() {
		text = loadFont(ResourceLoader.getResourceAsStream("dax/blocks/res/fonts/roboto.ttf"), Font.PLAIN, 12, true);
	}
	
	public static TrueTypeFont loadFont(InputStream in, int style, int size, boolean antiAlias) {
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, in);
			font = font.deriveFont(style, size);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loadFont(font, antiAlias);
	}
	
	public static TrueTypeFont loadFont(String name, int style, int size, boolean antiAlias) {
		Font font = new Font(name, style, size);
		return loadFont(font, antiAlias);
	}
	
	public static TrueTypeFont loadFont(Font font, boolean antiAlias) {
		return new TrueTypeFont(font, antiAlias);
	}
	
}
