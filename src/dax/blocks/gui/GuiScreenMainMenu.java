package dax.blocks.gui;

import dax.blocks.Base;

public class GuiScreenMainMenu extends GuiScreen {

    int width = 400;
    int height = 114;

    int overflow = 8;
    
    public GuiScreenMainMenu(Base base) {
        super(base);
        objects.add(new GuiObjectRectangle((base.width - width - overflow) / 2, (base.height - height - overflow) / 2, (base.width + width + overflow) / 2, (base.height + height + overflow) / 2, 0xA0000000));

        objects.add(new GuiObjectTitleBar((base.width - width) / 2, (base.height - height) / 2, (base.width + width) / 2, ((base.height - height) / 2) + 30, this.f, "Main menu"));

        objects.add(new GuiObjectButton((base.width - width) / 2, (base.height - height) / 2 + 34, (base.width + width) / 2, ((base.height - height) / 2) + 58, this.f, "Create new world", 0, this));
        objects.add(new GuiObjectButton((base.width - width) / 2, (base.height - height) / 2 + 62, (base.width + width) / 2, ((base.height - height) / 2) + 86, this.f, "Options", 1, this));
        objects.add(new GuiObjectButton((base.width - width) / 2, (base.height - height) / 2 + 90, (base.width + width) / 2, ((base.height - height) / 2) + 114, this.f, "Exit", 2, this));
    }

    @Override
    public void buttonPress(GuiObjectButton button) {
        if (button.id == 0) {
            base.displayLoadingScreen();
        	base.makeNewWorld();
        } else if (button.id == 1) {
            base.openGuiScreen(new GuiScreenSettings(this));
        } else if (button.id == 2) {
            base.openGuiScreen(new GuiScreenExit(this));
        }
    }

}
