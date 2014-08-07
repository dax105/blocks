package dax.blocks.gui;

import dax.blocks.FontManager;
import dax.blocks.Game;
import dax.blocks.settings.Settings;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Font;

public abstract class GuiScreen {

	protected Game game;
	protected GuiScreen parent;
	protected Font f;
	protected ArrayList<GuiObject> objects = new ArrayList<GuiObject>();
	
	public GuiScreen(Game game) {
		if(game.getWorldsManager().isInGame()) {
			this.objects.add(new GuiObjectRectangle(
						0, 0, 
						Settings.getInstance().windowWidth.getValue(), 
						Settings.getInstance().windowHeight.getValue(), 
						0xA0000000)
			);
		}
		this.game = game;
		this.parent = null;
		this.f = FontManager.getFont();
	}

	public GuiScreen(GuiScreen parent) {
		this(parent.game);
	}

	public void render() {
		for(GuiObject object : this.objects) {
			object.render();
		}
	}

	public void update() {
		for(GuiObject object : this.objects) {
			object.update();
		}

		while(Mouse.next()) {
			;
		}
	}

	public void close() {
		if(this.parent != null) {
			this.game.openGuiScreen(this.parent);
		} else {
			this.game.closeGuiScreen();
		}
	}
	
	public abstract void onOpening();
	
	public abstract void onClosing();

	public abstract void buttonPress(GuiObjectButton button);

	public abstract void sliderUpdate(GuiObjectSlider slider);
	
	public abstract void buttonChanged(GuiObjectChangingButton button, int line);
}
