package dax.blocks.gui;

public class GuiManager {

	private static GuiManager _instance;
	
	public static GuiManager getInstance() {
		if (_instance != null) {
			_instance = new GuiManager();
		}
		return _instance;
	}
	
	// We will actually need a list to support animations and all that funny
	// stuff :D This will do for now tho.
	private GuiScreen screen = null;

	public void open(GuiScreen screen) {
		this.screen = screen;
	}

	public void close() {
		if (screen.parent != null) {
			screen = screen.parent;
		}
	}

	public void closeAll() {
		this.screen = null;
	}

	public GuiScreen getScreen() {
		return this.screen;
	}

	public boolean hasOpen() {
		return this.screen != null;
	}

}