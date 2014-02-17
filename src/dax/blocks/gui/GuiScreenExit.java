package dax.blocks.gui;

public class GuiScreenExit extends GuiScreen {
	
	int width = 400;
	int height = 58;
	
	int overflow = 8;
	
	public GuiScreenExit(GuiScreen parent) {
		super(parent);
		objects.add(new GuiObjectRectangle((base.width-width-overflow)/2, (base.height-height-overflow)/2, (base.width+width+overflow)/2, (base.height+height+overflow)/2, 0xA0000000));
		
		objects.add(new GuiObjectTitleBar((base.width-width)/2, (base.height-height)/2, (base.width+width)/2, ((base.height-height)/2)+30, this.f, "Do you really want to exit?"));
		
		objects.add(new GuiObjectButton((base.width-width)/2, (base.height+height)/2-24, (base.width)/2, ((base.height+height)/2), this.f, "No", 0, this));
		objects.add(new GuiObjectButton((base.width+8)/2, (base.height+height)/2-24, (base.width+width)/2, ((base.height+height)/2), this.f, "Yes", 1, this));
	}

	@Override
	public void buttonPress(GuiObjectButton button) {
		if (button.id == 0) {
			close();
		}
		
		if (button.id == 1) {
			System.exit(0);
		}
	}

}
