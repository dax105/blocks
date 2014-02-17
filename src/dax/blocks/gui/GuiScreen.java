package dax.blocks.gui;

import dax.blocks.Base;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Font;

public abstract class GuiScreen {
	Base base;
	GuiScreen parent;
	Font f;

	public GuiScreen(Base base) {
        if (base.isIngame) {
        	objects.add(new GuiObjectRectangle(0, 0, base.width, base.height, 0xA0000000));
        }
		this.base = base;
		parent = null;
		f = base.font;
	}
	
	public GuiScreen(GuiScreen parent) {
        if (parent.base.isIngame) {
        	objects.add(new GuiObjectRectangle(0, 0, parent.base.width, parent.base.height, 0xA0000000));
        }
		this.base = parent.base;
		this.parent = parent;
		f = base.font;
	}
	
	ArrayList<GuiObject> objects = new ArrayList<GuiObject>();
	
	public void render() {
		for(GuiObject object : objects) {
			object.render();
		}
	}
	
	public void update() {
		for(GuiObject object : objects) {
			object.update();
		}	
		
		while (Mouse.next());
	}
	
	public void close() {
		if (parent != null) {
			base.openGuiScreen(parent);
		} else {
			base.closeGuiScreen();
		}
	}
	
	public abstract void buttonPress(GuiObjectButton button);
}
