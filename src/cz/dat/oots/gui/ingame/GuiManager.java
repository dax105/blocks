package cz.dat.oots.gui.ingame;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import cz.dat.oots.Game;
import cz.dat.oots.render.ITickListener;

public class GuiManager implements ITickListener {
	GuiScreen currentGuiScreen;
	List<GuiScreen> screenList;
	boolean isScreenOpened = false;
	private Game game;

	public GuiManager(Game game) {
		this.screenList = new ArrayList<>();
		this.game = game;
	}

	public int getScreenWidth() {
		return Display.getWidth();
	}

	public int getScreenHeight() {
		return Display.getHeight();
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
			this.game.getOverlayManager().addOverlay(this.currentGuiScreen);
		}
	}

	public void closeScreen() {
		if(this.currentGuiScreen != null && this.isOpened()) {
			Mouse.setGrabbed(true);
			this.game.getOverlayManager().removeOverlay(this.currentGuiScreen);
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

		boolean left = (x < this.currentGuiScreen.getX());
		boolean right = (x > (this.currentGuiScreen.getX() + this.currentGuiScreen
				.getWidth()));
		boolean top = (y < this.currentGuiScreen.getY());
		boolean bottom = (y > (this.currentGuiScreen.getY() + this.currentGuiScreen
				.getHeight()));

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
