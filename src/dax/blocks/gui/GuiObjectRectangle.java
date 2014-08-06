package dax.blocks.gui;

public class GuiObjectRectangle extends GuiObject {

	private int x1;
	private int y1;
	private int x2;
	private int y2;

	private int color;

	public GuiObjectRectangle(int x1, int y1, int x2, int y2, int color) {
		this.color = color;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	@Override
	public void render() {
		this.drawRect(this.x1, this.y1, this.x2, this.y2, this.color);
	}

	@Override
	public void update() {

	}

}
