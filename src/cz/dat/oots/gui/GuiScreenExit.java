package cz.dat.oots.gui;

import org.lwjgl.opengl.Display;

public class GuiScreenExit extends GuiScreen {

	private int width = 400;
	private int height = 58;
	private int overflow = 8;

	public GuiScreenExit(GuiScreen parent) {
		super(parent);
		/*this.objects.add(new GuiObjectRectangle(
				(Display.getWidth() - width - overflow) / 2, (Display
						.getHeight() - height - overflow) / 2, (Display
						.getWidth() + width + overflow) / 2, (Display
						.getHeight() + height + overflow) / 2, 0xA0000000));*/

		this.objects.add(new GuiObjectTitleBar(
				(Display.getWidth() - width) / 2,
				(Display.getHeight() - height) / 2,
				(Display.getWidth() + width) / 2,
				((Display.getHeight() - height) / 2) + 30, this.f,
				"Do you really want to exit?"));

		this.objects.add(new GuiObjectButton((Display.getWidth() - width) / 2,
				(Display.getHeight() + height) / 2 - 24,
				(Display.getWidth()) / 2, ((Display.getHeight() + height) / 2),
				this.f, "No", 0, this));

		this.objects.add(new GuiObjectButton((Display.getWidth() + 8) / 2,
				(Display.getHeight() + height) / 2 - 24,
				(Display.getWidth() + width) / 2,
				((Display.getHeight() + height) / 2), this.f, "Yes", 1, this));
	}

	@Override
	public void buttonPress(GuiObjectButton button) {
		if(button.id == 0) {
			this.close();
		}

		if(button.id == 1) {
			this.game.exit();
		}
	}

	@Override
	public void sliderUpdate(GuiObjectSlider slider) {

	}

	@Override
	public void buttonChanged(GuiObjectChangingButton button, int line) {

	}

	@Override
	public void onClosing() {
	}

	@Override
	public void onOpening() {
	}
}
