package dax.blocks.gui.ingame;

import dax.blocks.render.IRenderable;
import dax.blocks.util.Coord2D;
import dax.blocks.util.CoordUtil;
import dax.blocks.util.GLHelper;

public abstract class GuiScreen implements IRenderable {

	private int x, y, width, height;
	private float backColorR, backColorG, backColorB, backColorA;
	private IngameGuiManager guiManager;
	private boolean isInCenter = false;

	public GuiScreen(int x, int y, int width, int height, IngameGuiManager guiManager) {
		this(x, y, width, height, 1, 1, 1, 0.5f, guiManager);
	}

	public GuiScreen(int width, int height, IngameGuiManager guiManager) {
		this(width, height, 1, 1, 1, 0.5f, guiManager);
	}

	public GuiScreen(int width, int height, float r, float g, float b, float a,
			IngameGuiManager guiManager) {
		this(0, 0, width, height, r, g, b, a, guiManager);
		this.isInCenter = true;
		this.updateCenteredPosition(guiManager.getScreenWidth(), guiManager.getScreenHeight());
	}

	public GuiScreen(int x, int y, int width, int height, float r, float g,
			float b, float a, IngameGuiManager guiManager) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.backColorR = r;
		this.backColorG = g;
		this.backColorB = b;
		this.backColorA = a;
		this.guiManager = guiManager;
	}

	public abstract void onOpening();

	public abstract void onClosing();

	@Override
	public void onTick() {

	}

	@Override
	public void onRenderTick(float partialTickTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void renderWorld(float partialTickTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void renderGui(float partialTickTime) {
		GLHelper.drawRectangle(this.backColorR, this.backColorG,
				this.backColorB, this.backColorA, this.x, this.x + this.width,
				this.y, this.y + this.height);
	}

	public boolean isCentered() {
		return this.isInCenter;
	}

	public void toggleCentered() {
		this.isInCenter = !this.isInCenter;
	}

	public void updateCenteredPosition(int width, int height) {
		if (this.isCentered()) {
			Coord2D c = CoordUtil.getCenteredRectanglePosition(this.width, this.height,
					width, height);
			this.setPosition(c.x, c.y);
		}
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public float getBackColorR() {
		return backColorR;
	}

	public void setBackColorR(float backColorR) {
		this.backColorR = backColorR;
	}

	public float getBackColorG() {
		return backColorG;
	}

	public void setBackColorG(float backColorG) {
		this.backColorG = backColorG;
	}

	public float getBackColorB() {
		return backColorB;
	}

	public void setBackColorB(float backColorB) {
		this.backColorB = backColorB;
	}

	public float getBackColorA() {
		return backColorA;
	}

	public void setBackColorA(float backColorA) {
		this.backColorA = backColorA;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
