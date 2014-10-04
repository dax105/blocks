package cz.dat.oots.gui.ingame;

import cz.dat.oots.render.ITickListener;
import cz.dat.oots.util.Rectangle;

public abstract class Control implements ITickListener {
	protected Rectangle rectangle;
	
	private int relativeX;
	private int relativeY;
	private GuiScreen screen;
	
	public Control(int relativeX, int relativeY, int width, int height, GuiScreen screen) {
		this.relativeX = relativeX;
		this.relativeY = relativeY;
		this.screen = screen;
		this.rectangle = new Rectangle(relativeX + screen.getX(), relativeY + screen.getY(),
				width, height);
	}
	
	public void setPosition(int relativeX, int relativeY) {
		this.rectangle.set(relativeX + screen.getX(), relativeY + screen.getY(),
				this.rectangle.getWidth(), this.rectangle.getHeight());
	}
	
	@Override
	public void onTick() {
		this.rectangle.getPosition().set(screen.getX() + relativeX,
				screen.getY() + relativeY);
	}
	
	@Override
	public void onRenderTick(float partialTickTime) {	
	}
	
	public void onParentOpened() {
	}

	public void onParentClosed() {
	}
	
	public abstract void render();
}
