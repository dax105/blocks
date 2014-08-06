package dax.blocks.gui;

import org.lwjgl.input.Mouse;

public class GuiManager {

	private static GuiManager _instance;

	public static GuiManager getInstance() {
		if (_instance == null) {
			_instance = new GuiManager();
		}
		return _instance;
	}

	// I guess that we'll have some sort of "screen stacks"
	// One for "Main GUI" eg. pause menu
	// One for "HUD" eg. the overlay (lives, hotbar and all that stuff)
	// And one for "Ingame GUI" eg. chests
	// You'll be able to put screen on top of each stack
	// They'll also be drawn in priority: HUD-INGAME-MAIN
	// Only the first screen on each stack will be drawn!
	// However there can be more *animating* screen at once (eg. opening and
	// closing at the same time, producing "switch" animation)
	// And you'll always control only the topmost priority stack open

	// We will actually need a list to support animations and all that funny
	// stuff :D This will do for now tho.
	private GuiScreen screen = null;

	public void open(GuiScreen screen) {
		this.screen = screen;
		screen.onOpening();
		Mouse.setGrabbed(false);
	}

	public void close() {
		if (hasOpen()) {
			this.screen.onClosing();
		}
		
		if (this.screen.parent != null) {
			this.screen = this.screen.parent;
		} else {
			Mouse.setGrabbed(true);
		}
	}

	public void closeAll() {
		if (hasOpen()) {
			this.screen.onClosing();
			this.screen = null;
			Mouse.setGrabbed(true);
		}
	}

	public GuiScreen getScreen() {
		return this.screen;
	}

	public boolean hasOpen() {
		return this.screen != null;
	}

	public void update() {
		this.screen.update();
	}

	public void render() {
		this.screen.render();
	}

}