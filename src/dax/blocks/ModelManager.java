package dax.blocks;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

import org.newdawn.slick.util.ResourceLoader;

public class ModelManager {

	public static final int BUFFER = 1024;
	
	public static Model character;

	public static Model loadModel(String path) {
		try {
			Model m = new Model();

			Map<String, byte[]> streams = new HashMap<String, byte[]>();

			InputStream in = ResourceLoader.getResourceAsStream(path);

			ZipInputStream zin = new ZipInputStream(in);

			// ZipFile zipFile = new ZipFile();
			// Enumeration<? extends ZipEntry> entries = zipFile.entries();

			List<ZipEntry> entries = new ArrayList<ZipEntry>();

			ZipEntry e;

			
			//byte[] data = new byte[BUFFER];

			
			
			while ((e = zin.getNextEntry()) != null) {
				ByteArrayOutputStream bout = new ByteArrayOutputStream();

				byte[] data = new byte[BUFFER];
				
				int count;
				while ((count = zin.read(data, 0, BUFFER)) != -1) {
				    bout.write(data, 0, count);
				}
				
				streams.put(e.getName(), bout.toByteArray());
				zin.closeEntry();
			}


			zin.close();



			int maxl = 0;

			for (Entry<String, byte[]> en : streams.entrySet()) {
				String key = en.getKey();
				key = key.replace("layer", "");
				key = key.replace(".png", "");
				int l = Integer.parseInt(key);

				if (l > maxl) {
					maxl = l;
				}

			}

			for (int i = 0; i <= maxl; i++) {
				BufferedImage img;
				
				InputStream bain = new ByteArrayInputStream(streams.get("layer" + i + ".png")); 
				
				img = ImageIO.read(bain);
				//Game.console.out(streams.get("layer" + i + ".png").length);

				if (i == 0) {
					m.width = img.getWidth() + 1;
					m.height = img.getHeight() + 1;
					m.depth = maxl + 1;
					m.createArray();
				}

				for (int x = 0; x < m.width-1; x++) {
					for (int y = 0; y < m.height-1; y++) {

						int col = img.getRGB(x, y);

						Color color = new Color(col, true);

						if (color.getAlpha() > 0) {
							m.voxels[img.getWidth() - x][img.getHeight() - y][i] = new Voxel(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
						}
					}
				}

			}


			return m;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void load() {
		character = loadModel("dax/blocks/res/models/head.zip");
		character.generateDisplayList();
	}

}
