package cz.dat.oots;

import java.awt.Font;
import java.io.InputStream;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

public class FontManager {

	private static FontManager _instance;
	public TrueTypeFont text;

	public static FontManager getInstance() {
		if(_instance == null) {
			_instance = new FontManager();
		}
		
		return _instance;
	}
	
	public static TrueTypeFont getFont() {
		return getInstance().text;
	}
	
	private FontManager() {
		
	}
	
	
	public void load() {
		this.text = this.loadFont(ResourceLoader.getResourceAsStream("cz/dat/oots/res/fonts/roboto.ttf"), Font.PLAIN, 12, true);
	}
	
	public TrueTypeFont loadFont(InputStream in, int style, int size, boolean antiAlias) {
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, in);
			font = font.deriveFont(style, size);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.loadFont(font, antiAlias);
	}
	
	public TrueTypeFont loadFont(String name, int style, int size, boolean antiAlias) {
		Font font = new Font(name, style, size);
		return this.loadFont(font, antiAlias);
	}
	
	public TrueTypeFont loadFont(Font font, boolean antiAlias) {
		return new TrueTypeFont(font, antiAlias);
	}
	
}
