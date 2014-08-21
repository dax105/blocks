package dax.blocks.gui.ingame;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;

import dax.blocks.render.IOverlayRenderer;
import dax.blocks.render.ITickListener;
import dax.blocks.util.Coord2D;
import dax.blocks.util.CoordUtil;
import dax.blocks.util.GLHelper;

public abstract class GuiScreen implements ITickListener, IOverlayRenderer {

	protected int x, y, width, height;
	protected Color backColor;
	protected GuiManager guiManager;
	protected boolean isInCenter = false;
	protected List<Control> controls;

	public GuiScreen(int x, int y, int width, int height, GuiManager guiManager) {
		this(x, y, width, height, 1, 1, 1, 0.5f, guiManager);
	}

	public GuiScreen(int width, int height, GuiManager guiManager) {
		this(width, height, 1, 1, 1, 0.5f, guiManager);
	}

	public GuiScreen(int width, int height, float r, float g, float b, float a,
			GuiManager guiManager) {
		this(0, 0, width, height, r, g, b, a, guiManager);
		
		this.isInCenter = true;
		this.updateCenteredPosition(this.guiManager.getScreenWidth(), this.guiManager.getScreenHeight());
	}

	public GuiScreen(int x, int y, int width, int height, float r, float g,
			float b, float a, GuiManager guiManager) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.backColor = new Color(r, g, b, a);
		this.guiManager = guiManager;
		this.controls = new ArrayList<Control>();
	}

	public abstract void onOpening();

	public abstract void onClosing();
	
	public void addControl(Control c) {
		this.controls.add(c);
	}
	
	public void removeControl(Control c) {
		this.controls.remove(c);
	}
	
	public List<Control> getControls() {
		return this.controls;
	}
	
	@Override
	public void onTick() {
		for(Control c : this.controls) {
			c.onTick();
		}
	}
	
	@Override
	public void onRenderTick(float ptt) {
		for(Control c : this.controls) {
			c.onRenderTick(ptt);
		}
	}
	
	@Override
	public void renderOverlay(float partialTickTime) {
		GLHelper.drawRectangle(this.backColor.r, this.backColor.g,
				this.backColor.b, this.backColor.a, this.x, this.x + this.width,
				this.y, this.y + this.height);
		
		for(Control c : this.controls) {
			c.render();
		}
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
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setColor(float r, float g, float b) {
		this.backColor = new Color(r, g, b, 1f);
	}
	
	public void setColor(int color) {
		this.backColor = new Color(color);
	}
	
	public Color getColor() {
		return this.backColor;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
}
