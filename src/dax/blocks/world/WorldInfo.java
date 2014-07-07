package dax.blocks.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import dax.blocks.WorldsManager;

public class WorldInfo {
	private String worldName;
	private String worldDirectory;
	private String worldVersion;
	private int worldSeed;
	private float playerX;
	private float playerY;
	private float playerZ;
	private float playerTilt;
	private float playerHeading;
	
	public static WorldInfo constructFromFile(File f) throws FileNotFoundException {
		WorldInfo w = new WorldInfo(f.getParentFile().getName());
		w.setWorldDirectory(f.getParent());
		
		Scanner s = new Scanner(f);
		while (s.hasNextLine()) {
			String l = s.nextLine();
			String[] words = l.split(" ");

			if (words.length >= 2) {
				if (words[0].equals("seed")) {
					w.worldSeed = Integer.parseInt(words[1]);
				} else if (words[0].equals("playerx")) {
					w.playerX = Float.parseFloat(words[1]);
				} else if (words[0].equals("playery")) {
					w.playerY = Float.parseFloat(words[1]) + 1f;
				} else if (words[0].equals("playerz")) {
					w.playerZ = Float.parseFloat(words[1]);
				} else if (words[0].equals("playertilt")) {
					w.playerTilt = Float.parseFloat(words[1]);
				} else if (words[0].equals("playerheading")) {
					w.playerHeading = Float.parseFloat(words[1]);
				}
			}

		}
		
		s.close();	
		return w;
	}
	
	
	public void saveWorldInfo() {
		try {
			File dir = new File(WorldsManager.SAVES_DIR, this.worldName);

			if (!dir.exists()) {
				dir.mkdir();
			}

			File file = new File(dir, "world" + ".txt");

			PrintWriter pw = new PrintWriter(file);

			pw.println("version " + this.worldVersion);

			pw.println("seed " + this.worldSeed);

			pw.println("playerx " + this.playerX);
			pw.println("playery " + this.playerY);
			pw.println("playerz " + this.playerZ);

			pw.println("playertilt " + this.playerTilt);
			pw.println("playerheading " + this.playerHeading);

			pw.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	public String getWorldDirectory() {
		return worldDirectory;
	}

	public void setWorldDirectory(String worldDirectory) {
		this.worldDirectory = worldDirectory;
	}

	public String getWorldVersion() {
		return worldVersion;
	}

	public void setWorldVersion(String worldVersion) {
		this.worldVersion = worldVersion;
	}

	public int getWorldSeed() {
		return worldSeed;
	}

	public void setWorldSeed(int worldSeed) {
		this.worldSeed = worldSeed;
	}

	public float getPlayerX() {
		return playerX;
	}

	public void setPlayerX(float playerX) {
		this.playerX = playerX;
	}

	public float getPlayerY() {
		return playerY;
	}

	public void setPlayerY(float playerY) {
		this.playerY = playerY;
	}

	public float getPlayerZ() {
		return playerZ;
	}

	public void setPlayerZ(float playerZ) {
		this.playerZ = playerZ;
	}

	public float getPlayerTilt() {
		return playerTilt;
	}

	public void setPlayerTilt(float playerTilt) {
		this.playerTilt = playerTilt;
	}

	public float getPlayerHeading() {
		return playerHeading;
	}

	public void setPlayerHeading(float playerHeading) {
		this.playerHeading = playerHeading;
	}

	public WorldInfo(String worldName) {
		this.worldName = worldName;
	}
}
