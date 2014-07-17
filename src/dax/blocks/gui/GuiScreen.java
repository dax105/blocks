package dax.blocks.gui;

import dax.blocks.Game;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Font;

public abstract class GuiScreen {
	Game game;
	GuiScreen parent;
	Font f;
	
	public GuiScreen(Game game) {
		if (game.ingame) {
			objects.add(new GuiObjectRectangle(0, 0, Game.settings.windowWidth.getValue(), Game.settings.windowHeight.getValue(), 0xA0000000));
		}
		this.game = game;
		parent = null;
		f = game.font;
	}

	public GuiScreen(GuiScreen parent) {
		if (parent.game.ingame) {
			objects.add(new GuiObjectRectangle(0, 0, Game.settings.windowWidth.getValue(), Game.settings.windowHeight.getValue(), 0xA0000000));
		}
		this.game = parent.game;
		this.parent = parent;
		f = game.font;
	}

	ArrayList<GuiObject> objects = new ArrayList<GuiObject>();

	public void render() {
		for (GuiObject object : objects) {
			object.render();
		}
	}

	public void update() {
		for (GuiObject object : objects) {
			object.update();
		}

		while (Mouse.next()) {
			;
		}
	}

	public void close() {
		if (parent != null) {
			game.openGuiScreen(parent);
		} else {
			game.closeGuiScreen();
		}
	}
	
	public abstract void onOpening();
	
	public abstract void onClosing();

	public abstract void buttonPress(GuiObjectButton button);

	public abstract void sliderUpdate(GuiObjectSlider slider);
	
	public abstract void buttonChanged(GuiObjectChangingButton button, int line);
}
