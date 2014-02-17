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
		if (game.isIngame) {
			objects.add(new GuiObjectRectangle(0, 0, game.width, game.height, 0xA0000000));
		}
		this.game = game;
		parent = null;
		f = game.font;
	}

	public GuiScreen(GuiScreen parent) {
		if (parent.game.isIngame) {
			objects.add(new GuiObjectRectangle(0, 0, parent.game.width, parent.game.height, 0xA0000000));
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

	public abstract void buttonPress(GuiObjectButton button);
}
