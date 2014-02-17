package dax.blocks.gui;

import dax.blocks.Base;

public class GuiScreenLoading extends GuiScreen {

	int width = 400;
	int height = 30;
	
	int overflow = 8;
	
	public GuiScreenLoading(Base base) {
		super(base);
		objects.add(new GuiObjectRectangle((base.width-width-overflow)/2, (base.height-height-overflow)/2, (base.width+width+overflow)/2, (base.height+height+overflow)/2, 0xA0000000));
		
		objects.add(new GuiObjectTitleBar((base.width-width)/2, (base.height-height)/2, (base.width+width)/2, ((base.height-height)/2)+30, this.f, "Loading..."));
	}

	@Override
	public void buttonPress(GuiObjectButton button) {
		
	}
}
