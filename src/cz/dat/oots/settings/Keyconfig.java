package cz.dat.oots.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
	public static int toggleNoClip;

	private File configFile;
	private List<IKeyconfigLoader> loaders;

	public Keyconfig() {
		this.loaders = new ArrayList<IKeyconfigLoader>();
	}

	public void addKeyconfigLoader(IKeyconfigLoader loader) {
		this.loaders.add(loader);
	}

	public void load(File configFile) {
		this.configFile = configFile;

		if(!configFile.exists()) {

			Keyconfig.ahead = new int[] { Keyboard.KEY_W, Keyboard.KEY_UP };
			Keyconfig.back = new int[] { Keyboard.KEY_S, Keyboard.KEY_DOWN };
			Keyconfig.left = new int[] { Keyboard.KEY_A, Keyboard.KEY_LEFT };
			Keyconfig.right = new int[] { Keyboard.KEY_D, Keyboard.KEY_RIGHT };
			Keyconfig.jump = new int[] { Keyboard.KEY_SPACE };
			Keyconfig.boost = new int[] { Keyboard.KEY_LSHIFT,
					Keyboard.KEY_RSHIFT };
			Keyconfig.crouch = new int[] { Keyboard.KEY_LCONTROL,
					Keyboard.KEY_RCONTROL };
			Keyconfig.explosion = Keyboard.KEY_E;
			Keyconfig.particleFirework = Keyboard.KEY_P;
			Keyconfig.screenshot = Keyboard.KEY_F2;
			Keyconfig.console = new int[] { Keyboard.KEY_C, Keyboard.KEY_GRAVE };
			Keyconfig.fullscreen = Keyboard.KEY_F;
			Keyconfig.exit = Keyboard.KEY_ESCAPE;
			Keyconfig.zoom = Keyboard.KEY_Z;
			Keyconfig.toggleNoClip = Keyboard.KEY_N;

			try {
				this.save();
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else {
			Scanner s;
			try {
				s = new Scanner(configFile);
				while(s.hasNextLine()) {
					String l = s.nextLine();
					this.a(l);
				}

				s.close();

				for(IKeyconfigLoader l : this.loaders) {
					l.load(configFile);
				}
			} catch(FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public void save() throws IOException {
		if(!this.configFile.exists()) {
			this.configFile.createNewFile();
		}

		PrintWriter pw = new PrintWriter(this.configFile);
		this.b(pw);
		pw.close();

		for(IKeyconfigLoader l : this.loaders) {
			l.save(this.configFile);
		}
	}

	// The secret I will take to the grave
	private void a(String line) {
		String[] p = line.split(" ");

		switch(p[0]) {
		case "ahead":
			Keyconfig.ahead = this.d(p[1]);
			break;
		case "back":
			Keyconfig.back = this.d(p[1]);
			break;
		case "left":
			Keyconfig.left = this.d(p[1]);
			break;
		case "right":
			Keyconfig.right = this.d(p[1]);
			break;
		case "jump":
			Keyconfig.jump = this.d(p[1]);
			break;
		case "boost":
			Keyconfig.boost = this.d(p[1]);
			break;
		case "crouch":
			Keyconfig.crouch = this.d(p[1]);
			break;
		case "console":
			Keyconfig.console = this.d(p[1]);
			break;
		case "explosion":
			Keyconfig.explosion = Integer.parseInt(p[1]);
			break;
		case "particleFirework":
			Keyconfig.particleFirework = Integer.parseInt(p[1]);
			break;
		case "screenshot":
			Keyconfig.screenshot = Integer.parseInt(p[1]);
			break;
		case "fullscreen":
			Keyconfig.fullscreen = Integer.parseInt(p[1]);
			break;
		case "exit":
			Keyconfig.exit = Integer.parseInt(p[1]);
			break;
		case "zoom":
			Keyconfig.zoom = Integer.parseInt(p[1]);
			break;
		case "toggleNoClip":
			Keyconfig.toggleNoClip = Integer.parseInt(p[1]);
			break;
		}
	}

	private void b(PrintWriter pw) {
		pw.println("ahead " + this.c(Keyconfig.ahead));
		pw.println("back " + this.c(Keyconfig.back));
		pw.println("left " + this.c(Keyconfig.left));
		pw.println("right " + this.c(Keyconfig.right));
		pw.println("jump " + this.c(Keyconfig.jump));
		pw.println("boost " + this.c(Keyconfig.boost));
		pw.println("crouch " + this.c(Keyconfig.crouch));
		pw.println("console " + this.c(Keyconfig.console));
		pw.println("explosion " + Keyconfig.explosion);
		pw.println("particleFirework " + Keyconfig.particleFirework);
		pw.println("screenshot " + Keyconfig.screenshot);
		pw.println("fullscreen " + Keyconfig.fullscreen);
		pw.println("exit " + Keyconfig.exit);
		pw.println("zoom " + Keyconfig.zoom);
		pw.println("toggleNoClip " + Keyconfig.toggleNoClip);
	}

	private String c(int[] k) {
		StringBuilder sb = new StringBuilder();
		for(int i : k) {
			sb.append(i);
			sb.append(",");
		}

		String ret = sb.toString();
		return ret.substring(0, ret.length() - 1);
	}

	private int[] d(String s) {
		String[] p = s.split(",");
		int[] r = new int[p.length];
		for(int i = 0; i < p.length; i++) {
			r[i] = Integer.parseInt(p[i]);
		}

		return r;
	}

	// Don't ever, EVER, try to do something with these bloody methods

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
