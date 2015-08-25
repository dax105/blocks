package cz.dat.oots.gui;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import cz.dat.oots.Game;
import cz.dat.oots.TextureManager;
import cz.dat.oots.util.GLHelper;

public class GuiScreenSplash extends GuiScreen {

	private Texture splash;
	private float x, y;

	public GuiScreenSplash(Game game) {
		super(game);

		this.splash = TextureManager
				.loadTex("cz/dat/oots/res/textures/lovescreen.png");
		this.x = (Display.getWidth() / 2) - (this.splash.getImageWidth() / 2);
		this.y = (Display.getHeight() / 2) - (this.splash.getImageHeight() / 2);
	}

	@Override
	public void render() {
		GL11.glClearColor(1, 1, 1, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GLHelper.drawTexture(this.splash, x, y);
	}

	@Override
	public void onOpening() {
	}

	@Override
	public void onClosing() {
	}

	@Override
	public void buttonPress(GuiObjectButton button) {
	}

	@Override
	public void sliderUpdate(GuiObjectSlider slider) {
	}

	@Override
	public void buttonChanged(GuiObjectChangingButton button, int line) {
	}

}
