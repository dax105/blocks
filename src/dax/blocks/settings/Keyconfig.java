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
		ahead = new int[] { Keyboard.KEY_W, Keyboard.KEY_UP };
		back = new int[] { Keyboard.KEY_S, Keyboard.KEY_DOWN };
		left = new int[] { Keyboard.KEY_A, Keyboard.KEY_LEFT };
		right = new int[] { Keyboard.KEY_D, Keyboard.KEY_RIGHT };
		jump = new int[] { Keyboard.KEY_SPACE };
		boost = new int[] { Keyboard.KEY_LSHIFT, Keyboard.KEY_RSHIFT };
		crouch = new int[] { Keyboard.KEY_LCONTROL, Keyboard.KEY_RCONTROL };
		explosion = Keyboard.KEY_E;
		particleFirework = Keyboard.KEY_P;
		screenshot = Keyboard.KEY_F2;
		console = new int[] { Keyboard.KEY_C, Keyboard.KEY_GRAVE };
		fullscreen = Keyboard.KEY_F;
		exit = Keyboard.KEY_ESCAPE;
		zoom = Keyboard.KEY_Z;
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
