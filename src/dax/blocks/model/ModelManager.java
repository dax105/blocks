package dax.blocks.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

import org.newdawn.slick.util.ResourceLoader;

public class ModelManager {

	private final int BUFFER = 1024;
	private Map<String, Model> models;
	private static ModelManager instance;

	public static ModelManager getInstance() {
		if(ModelManager.instance == null) {
			ModelManager.instance = new ModelManager();
		}
		
		return ModelManager.instance;
	}
	
	private ModelManager() {
		this.models = new HashMap<>();
	}
	
	public void load() {
		this.models.put("char", this.loadModel("dax/blocks/res/models/char.zip"));
		
		for(Model m : this.models.values())
			m.generateDisplayList();
	}
	
	public Model getModel(String name) {
		return models.get(name);
	}
	
	private Model loadModel(String path) {
		try {
			Model m = new Model();
			Map<String, byte[]> streams = new HashMap<String, byte[]>();
			InputStream in = ResourceLoader.getResourceAsStream(path);
			ZipInputStream zin = new ZipInputStream(in);
			ZipEntry e;	
			
			while((e = zin.getNextEntry()) != null) {
				ByteArrayOutputStream bout = new ByteArrayOutputStream();

				byte[] data = new byte[this.BUFFER];
				
				int count;
				while((count = zin.read(data, 0, this.BUFFER)) != -1) {
				    bout.write(data, 0, count);
				}
				
				streams.put(e.getName(), bout.toByteArray());
				zin.closeEntry();
			}

			zin.close();

			int maxl = 0;

			for(Entry<String, byte[]> en : streams.entrySet()) {
				String key = en.getKey();
				key = key.replace("layer", "");
				key = key.replace(".png", "");
				int l = Integer.parseInt(key);

				if(l > maxl) {
					maxl = l;
				}

			}

			for(int i = 0; i <= maxl; i++) {
				BufferedImage img;
				
				InputStream bain = new ByteArrayInputStream(streams.get("layer" + i + ".png")); 
				
				img = ImageIO.read(bain);
				
				if(i == 0) {
					m.setWidth(img.getWidth() + 1);
					m.setHeight(img.getHeight() + 1);
					m.setDepth(maxl + 1);
					m.createArray();
				}

				for(int x = 0; x < m.getWidth() - 1; x++) {
					for(int y = 0; y < m.getHeight() - 1; y++) {

						int col = img.getRGB(x, y);

						Color color = new Color(col, true);

						if(color.getAlpha() > 0) {
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
}
