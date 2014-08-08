package dax.blocks.gui.ingame;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import dax.blocks.Game;
import dax.blocks.render.ITickListener;
import dax.blocks.settings.Settings;

public class GuiManager implements ITickListener {
	GuiScreen currentGuiScreen;
	List<GuiScreen> screenList;
	boolean isScreenOpened = false;

	private static GuiManager instance;	
	public static GuiManager getInstance() {
		if(GuiManager.instance == null) {
			GuiManager.instance = new GuiManager();
		}
		
		return GuiManager.instance;
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
		if(!this.screenList.contains(screen)) {
			this.screenList.add(screen);
			return this.screenList.size() - 1;
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
		if(this.currentGuiScreen != null && !this.isOpened()) {
			Mouse.setGrabbed(false);
			this.currentGuiScreen.onOpening();
			this.isScreenOpened = true;
			Game.getInstance().getOverlayManager().addOverlay(this.currentGuiScreen);
		}
	}
	
	public void closeScreen() {
		if(this.currentGuiScreen != null && this.isOpened()) {
			Mouse.setGrabbed(true);
			Game.getInstance().getOverlayManager().removeOverlay(this.currentGuiScreen);
			this.currentGuiScreen.onClosing();
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

}
