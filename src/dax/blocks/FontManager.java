package dax.blocks;

import java.awt.Font;
import java.io.InputStream;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

public class FontManager {

	public static TrueTypeFont text;
	
	public static void load() {
		FontManager.text = FontManager.loadFont(ResourceLoader.getResourceAsStream("dax/blocks/res/fonts/roboto.ttf"), Font.PLAIN, 12, true);
	}
	
	public static TrueTypeFont loadFont(InputStream in, int style, int size, boolean antiAlias) {
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, in);
			font = font.deriveFont(style, size);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FontManager.loadFont(font, antiAlias);
	}
	
	public static TrueTypeFont loadFont(String name, int style, int size, boolean antiAlias) {
		Font font = new Font(name, style, size);
		return FontManager.loadFont(font, antiAlias);
	}
	
	public static TrueTypeFont loadFont(Font font, boolean antiAlias) {
		return new TrueTypeFont(font, antiAlias);
	}
	
}
