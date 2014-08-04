package dax.blocks.gui.ingame;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.input.Mouse;
import dax.blocks.render.IRenderable;

public class GuiManager implements IRenderable {
	GuiScreen currentGuiScreen;
	List<GuiScreen> screenList;
	boolean isScreenOpened = false;
	
	public GuiManager() {
		this.screenList = new ArrayList<>();
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
	
	public boolean isOpened() {
		return this.isScreenOpened;
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
	public void renderWorld(float partialTickTime) {
		if(this.isOpened())
			this.currentGuiScreen.renderWorld(partialTickTime);
	}

	@Override
	public void renderGui(float partialTickTime) {
		if(this.isOpened())
			this.currentGuiScreen.renderGui(partialTickTime);
	}

}
