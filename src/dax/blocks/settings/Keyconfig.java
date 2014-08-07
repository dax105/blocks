package dax.blocks.settings;

import java.io.File;

import org.lwjgl.input.Keyboard;

public class Keyconfig {
	public static int[] ahead;
	public static int[] back;
	public static int[] left;
	public static int[] right;
	
	public static int[] jump;
	public static int[] boost;
	public static int[] crouch;
	
	public static int explosion;
	public static int particleFirework;
	
	public static int screenshot;
	public static int[] console;
	public static int fullscreen;
	public static int exit;
	
	public static int zoom;
	
	public static void load() {
		Keyconfig.ahead = new int[] { Keyboard.KEY_W, Keyboard.KEY_UP };
		Keyconfig.back = new int[] { Keyboard.KEY_S, Keyboard.KEY_DOWN };
		Keyconfig.left = new int[] { Keyboard.KEY_A, Keyboard.KEY_LEFT };
		Keyconfig.right = new int[] { Keyboard.KEY_D, Keyboard.KEY_RIGHT };
		Keyconfig.jump = new int[] { Keyboard.KEY_SPACE };
		Keyconfig.boost = new int[] { Keyboard.KEY_LSHIFT, Keyboard.KEY_RSHIFT };
		Keyconfig.crouch = new int[] { Keyboard.KEY_LCONTROL, Keyboard.KEY_RCONTROL };
		Keyconfig.explosion = Keyboard.KEY_E;
		Keyconfig.particleFirework = Keyboard.KEY_P;
		Keyconfig.screenshot = Keyboard.KEY_F2;
		Keyconfig.console = new int[] { Keyboard.KEY_C, Keyboard.KEY_GRAVE };
		Keyconfig.fullscreen = Keyboard.KEY_F;
		Keyconfig.exit = Keyboard.KEY_ESCAPE;
		Keyconfig.zoom = Keyboard.KEY_Z;
	}
	
	public static void save(File config) {
		//TODO: saving keyconfig
	}
	
	public static boolean isDown(int[] keys) {
		for(int k : keys) {
			if(Keyboard.isKeyDown(k))
				return true;
		}
		
		return false;
	}
	
	public static boolean isDownEvent(int[] keys) {
		for(int k : keys) {
			if(Keyboard.getEventKey() == k)
				return true;
		}
		
		return false;
	}
}
