package dax.blocks.gui;

import dax.blocks.Base;

public class GuiScreenMenu extends GuiScreen {

    int width = 400;
    int height = 142;

    int overflow = 8;

    public GuiScreenMenu(Base base) {
        super(base);
        objects.add(new GuiObjectRectangle((base.width - width - overflow) / 2, (base.height - height - overflow) / 2, (base.width + width + overflow) / 2, (base.height + height + overflow) / 2, 0xA0000000));

        objects.add(new GuiObjectTitleBar((base.width - width) / 2, (base.height - height) / 2, (base.width + width) / 2, ((base.height - height) / 2) + 30, this.f, "Menu"));

        objects.add(new GuiObjectButton((base.width - width) / 2, (base.height - height) / 2 + 34, (base.width + width) / 2, ((base.height - height) / 2) + 58, this.f, "Back to game", 0, this));
        objects.add(new GuiObjectButton((base.width - width) / 2, (base.height - height) / 2 + 62, (base.width + width) / 2, ((base.height - height) / 2) + 86, this.f, "Regenerate world", 1, this));
        objects.add(new GuiObjectButton((base.width - width) / 2, (base.height - height) / 2 + 90, (base.width + width) / 2, ((base.height - height) / 2) + 114, this.f, "Options", 2, this));
        objects.add(new GuiObjectButton((base.width - width) / 2, (base.height - height) / 2 + 118, (base.width + width) / 2, ((base.height - height) / 2) + 142, this.f, "Quit to title", 3, this));
    }

    @Override
    public void buttonPress(GuiObjectButton button) {
        if (button.id == 0) {
            base.closeGuiScreen();
        } else if (button.id == 1) {
            base.displayLoadingScreen();
        	base.makeNewWorld();
        } else if (button.id == 2) {
            base.openGuiScreen(new GuiScreenSettings(this));
        } else if (button.id == 3) {
            base.isIngame = false;
            base.openGuiScreen(new GuiScreenMainMenu(base));
        }
        
        
    }

}
