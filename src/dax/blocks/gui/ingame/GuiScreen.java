package dax.blocks.gui.ingame;

import dax.blocks.render.IRenderable;

public abstract class GuiScreen implements IRenderable {

	private int x, y, width, height;
	private float backColorR, backColorG, backColorB, backColorA;
	private GuiManager guiManager;
	
	public GuiScreen(int x, int y, int width, int height, GuiManager guiManager) {
		this(x, y, width, height, 1, 1, 1, 0.5f, guiManager);
	}
	
	public GuiScreen(int x, int y, int width, int height, float r, float g, float b, float a, GuiManager guiManager) {
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
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
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
