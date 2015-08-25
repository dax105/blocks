package cz.dat.oots.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import cz.dat.oots.settings.ObjectType;

public class GameUtil {

	public static boolean deleteDirectory(File directory) {
		if(directory.exists()) {
			File[] files = directory.listFiles();
			if(files != null) {
				for(int i = 0; i < files.length; i++) {
					if(files[i].isDirectory()) {
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
		final int width = Display.getDisplayMode().getWidth();
		final int height = Display.getDisplayMode().getHeight();
		final int bpp = 4;
		final ByteBuffer buffer = BufferUtils.createByteBuffer(width * height
				* bpp);
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA,
				GL11.GL_UNSIGNED_BYTE, buffer);

		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				File dir = new File("screenshots");

				if(!dir.exists()) {
					dir.mkdir();
				}

				String filename = "screenshot";
				DateFormat dateFormat = new SimpleDateFormat("yyMMdd-HHmmss");
				Calendar calendar = Calendar.getInstance();
				String date = dateFormat.format(calendar.getTime());
				File file = new File(dir, filename + date + ".png");
				;

				for(int num = 0; file.exists(); num++) {
					file = new File(dir, filename + date + "-" + num + ".png");
				}

				String format = "PNG";
				BufferedImage image = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_RGB);

				for(int x = 0; x < width; x++) {
					for(int y = 0; y < height; y++) {
						int i = (x + (width * y)) * bpp;
						int r = buffer.get(i) & 0xFF;
						int g = buffer.get(i + 1) & 0xFF;
						int b = buffer.get(i + 2) & 0xFF;
						image.setRGB(x, height - (y + 1), (0xFF << 24)
								| (r << 16) | (g << 8) | b);
					}
				}

				try {
					ImageIO.write(image, format, file);
				} catch(IOException ex) {
					ex.printStackTrace();
				}
			}
		}, 0);

	}

	public static String readFileAsString(InputStream in) throws Exception {
		StringBuilder source = new StringBuilder();

		Exception exception = null;

		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

			Exception innerExc = null;
			try {
				String line;
				while((line = reader.readLine()) != null)
					source.append(line).append('\n');
			} catch(Exception exc) {
				exception = exc;
			} finally {
				try {
					reader.close();
				} catch(Exception exc) {
					if(innerExc == null)
						innerExc = exc;
					else
						exc.printStackTrace();
				}
			}

			if(innerExc != null)
				throw innerExc;
		} catch(Exception exc) {
			exception = exc;
		} finally {
			try {
				in.close();
			} catch(Exception exc) {
				if(exception == null)
					exception = exc;
				else
					exc.printStackTrace();
			}

			if(exception != null)
				throw exception;
		}

		return source.toString();
	}

	public static String objectTypeAsString(ObjectType type) {
		switch(type) {
		case BOOLEAN:
			return "[bit]";
		case FLOAT:
			return "[flt]";
		case INTEGER:
			return "[int]";
		case STRING:
		default:
			return "[str]";
		}
	}

	public static ObjectType stringAsObjectType(String type) {
		switch(type) {
		case "[bit]":
			return ObjectType.BOOLEAN;
		case "[flt]":
			return ObjectType.FLOAT;
		case "[int]":
			return ObjectType.INTEGER;
		case "[str]":
		default:
			return ObjectType.STRING;
		}
	}
}
