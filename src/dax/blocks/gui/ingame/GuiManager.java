package dax.blocks.gui.ingame;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import dax.blocks.render.IOverlayRenderer;
import dax.blocks.render.ITickListener;
import dax.blocks.settings.Settings;

public class GuiManager implements ITickListener, IOverlayRenderer {
	GuiScreen currentGuiScreen;
	List<GuiScreen> screenList;
	boolean isScreenOpened = false;
	
	private static GuiManager _instance;
	public static GuiManager getInstance() {
		if(_instance == null) {
			_instance = new GuiManager();
		}
		
		return _instance;
	}
	
	private GuiManager() {
		this.screenList = new ArrayList<>();
	}
	
	public int getScreenWidth() {
		return Settings.getInstance().windowWidth.getValue();
	}
	
	public int getScreenHeight() {
		return Settings.getInstance().windowHeight.getValue();
	}

	public int registerNewScreen(GuiScreen screen) {
		if(!screenList.contains(screen)) {
			screenList.add(screen);
			return screenList.size() - 1;
		} else {
			return -1;
		}
	}
	
	public void setCurrentScreen(int s) {
		GuiScreen gScreen = this.screenList.get(s);
		if(gScreen != null) {
			this.currentGuiScreen = gScreen;
		}
	}
	
	public void openScreen() {
		if(currentGuiScreen != null && !this.isOpened()) {
			Mouse.setGrabbed(false);
			currentGuiScreen.onOpening();
			this.isScreenOpened = true;
		}
	}
	
	public void closeScreen() {
		if(currentGuiScreen != null && this.isOpened()) {
			Mouse.setGrabbed(true);
			currentGuiScreen.onClosing();
			this.isScreenOpened = false;
		}
	}
	
	public GuiScreen getCurrentScreen() {
		return this.currentGuiScreen;
	}
	
	public boolean isOpened() {
		return this.isScreenOpened;
	}
	
	public void checkMouseClosing() {
		int x = Mouse.getX();
		int y = this.getScreenHeight() - Mouse.getY();
		
		boolean left  = (x < this.currentGuiScreen.getX());
		boolean right = (x > (this.currentGuiScreen.getX() + this.currentGuiScreen.getWidth()));
		boolean top = (y < this.currentGuiScreen.getY());
		boolean bottom = (y > (this.currentGuiScreen.getY() + this.currentGuiScreen.getHeight()));
		
		if(left || right || top || bottom) {
			this.closeScreen();
		}
	}
	
	@Override
	public void onTick() {
		if(this.isOpened())
			this.currentGuiScreen.onTick();
	}

	@Override
	public void onRenderTick(float partialTickTime) {
		if(this.isOpened())
			this.currentGuiScreen.onRenderTick(partialTickTime);
	}
	
	@Override
	public void renderOverlay(float partialTickTime) {
		if(this.isOpened())
			this.currentGuiScreen.renderOverlay(partialTickTime);
	}

}
