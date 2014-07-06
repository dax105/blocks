package dax.blocks.world;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import org.xerial.snappy.Snappy;

import dax.blocks.Coord2D;
import dax.blocks.Game;
import dax.blocks.world.chunk.Chunk;

public class SaveManager {
	World world;
	ChunkProvider provider;
	String name;
	
	public static final int WORLD_VERSION = 1;
	
	private void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { 
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    
	}
	
	public void tryToLoadWorld() {

		try {
			File dir = new File("saves", name);
			
			if (!dir.exists()) {
				dir.mkdir();
			}	
			
			if(dir.exists() && !provider.loadingWorld) {
				deleteFolder(dir);
				return;
			}

			File file = new File(dir, "world" + ".txt");

			if (!file.exists()) {
				Game.console.out("World save not found!");
				return;
			}

			Scanner s = new Scanner(file);

			float x = 0;
			float y = 200;
			float z = 0;

			while (s.hasNextLine()) {
				String l = s.nextLine();
				String[] words = l.split(" ");

				if (words.length >= 2) {
					if (words[0].equals("seed")) {
						this.provider.seed = Integer.parseInt(words[1]);
					} else if (words[0].equals("playerx")) {
						x = Float.parseFloat(words[1]);
					} else if (words[0].equals("playery")) {
						y = Float.parseFloat(words[1]) + 1f;
					} else if (words[0].equals("playerz")) {
						z = Float.parseFloat(words[1]);
					} else if (words[0].equals("playertilt")) {
						this.world.player.tilt = Float.parseFloat(words[1]);
					} else if (words[0].equals("playerheading")) {
						this.world.player.heading = Float.parseFloat(words[1]);
					}
				}

			}

			this.world.player.setPos(x, y, z);

			s.close();

			Game.console.out("World info sucessfully loaded!");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public SaveManager(ChunkProvider provider, String saveName) {
		this.provider = provider;
		this.world = provider.world;
		this.name = saveName;
	}
	
	public boolean isChunkSaved(int cx, int cz) {
		File dir = new File("saves", name);

		if (!dir.exists()) {
			dir.mkdir();
		}

		File file = new File(dir, "x" + cx + "z" + cz + ".ccf");

		return file.exists();
	}

	public void saveAll() {
		Game.getInstance().displayLoadingScreen("Saving...");
		Iterator<Entry<Coord2D, Chunk>> it = provider.loadedChunks.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Coord2D, Chunk> pairs = it.next();
			Chunk c = (Chunk) pairs.getValue();

			c.deleteAllRenderChunks();

			saveChunk(c);
		}

		saveProviderInfo();
		Game.getInstance().closeGuiScreen();
	}

	public void saveProviderInfo() {
		try {
			File dir = new File("saves", name);

			if (!dir.exists()) {
				dir.mkdir();
			}

			File file = new File(dir, "world" + ".txt");

			PrintWriter pw = new PrintWriter(file);

			pw.println("version " + WORLD_VERSION);

			pw.println("seed " + this.provider.seed);

			pw.println("playerx " + this.world.player.posX);
			pw.println("playery " + this.world.player.posY);
			pw.println("playerz " + this.world.player.posZ);

			pw.println("playertilt " + this.world.player.tilt);
			pw.println("playerheading " + this.world.player.heading);

			pw.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Chunk loadChunk(int cx, int cz) {
		File dir = new File("saves", name);
		File file = new File(dir, "x" + cx + "z" + cz + ".ccf");
		byte[] fileData = new byte[(int) file.length()];
		try {
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			dis.readFully(fileData);
			dis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Chunk c = new Chunk(cx, cz, world);
		try {
			c.blocks.put(Snappy.uncompressShortArray(fileData));
		} catch (IOException e) {
			e.printStackTrace();
		}

		c.changed = true;

		return c;
	}

	public void saveChunk(Chunk c) {
		if (!c.changed) {
			return;
		}

		try {
			File dir = new File("saves", name);

			if (!dir.exists()) {
				dir.mkdir();
			}

			File file = new File(dir, "x" + c.x + "z" + c.z + ".ccf");
			FileOutputStream stream = new FileOutputStream(file);
			try {
				stream.write(Snappy.compress(c.blocks.array()));
			} finally {
				stream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
