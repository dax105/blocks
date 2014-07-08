package dax.blocks;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class GameUtil {

	public static boolean deleteDirectory(File directory) {
		if (directory.exists()) {
			File[] files = directory.listFiles();
			if (null != files) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteDirectory(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}
		return (directory.delete());
	}

	public static void screenshot() {
		GL11.glReadBuffer(GL11.GL_FRONT);
		int width = Display.getDisplayMode().getWidth();
		int height = Display.getDisplayMode().getHeight();
		int bpp = 4; // Assuming a 32-bit display with a byte each for red,
						// green, blue, and alpha.
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA,
				GL11.GL_UNSIGNED_BYTE, buffer);
	
		File dir = new File("screenshots");
	
		if (!dir.exists()) {
			dir.mkdir();
		}
	
		String filename = "screenshot";
	
		int num = 0;
	
		File file = new File(dir, filename + num + ".png");
	
		while (file.exists()) {
			num++;
			file = new File(dir, filename + num + ".png");
		}
	
		String format = "PNG";
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
	
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int i = (x + (width * y)) * bpp;
				int r = buffer.get(i) & 0xFF;
				int g = buffer.get(i + 1) & 0xFF;
				int b = buffer.get(i + 2) & 0xFF;
				image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16)
						| (g << 8) | b);
			}
		}
	
		try {
			ImageIO.write(image, format, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
}
