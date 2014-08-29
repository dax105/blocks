package cz.dat.oots.gui;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Font;

public class GuiObjectChangingButton extends GuiObjectButton {

	private String[] lines;
	int currentLine = 0;

	public GuiObjectChangingButton(int x1, int y1, int x2, int y2, Font font,
			String[] lines, int id, GuiScreen parent) {

		super(x1, y1, x2, y2, font, (lines == null || lines.length == 0) ? ""
				: lines[0], id, parent);

		if (lines == null || lines.length == 0) {
			throw new IllegalArgumentException();
		}

		this.lines = lines;
	}

	public GuiObjectChangingButton(int x1, int y1, int x2, int y2, Font font,
			String[] lines, int startingLine, int id, GuiScreen parent) {
		this(x1, y1, x2, y2, font, lines, id, parent);

		this.setLine(startingLine);
	}

	public int getCurrentLine() {
		return this.currentLine;
	}

	public String getCurrentLineString() {
		return this.lines[this.currentLine];
	}

	public int nextLine() {
		this.currentLine++;
		if(this.currentLine > (this.lines.length - 1)) {
			this.currentLine = 0;
		}

		this.text = this.lines[this.currentLine];
		return this.currentLine;
	}

	public int previousLine() {
		this.currentLine--;
		if(this.currentLine < 0) {
			this.currentLine = (this.lines.length - 1);
		}

		this.text = this.lines[this.currentLine];
		return this.currentLine;
	}

	public void setLine(int line) {
		if(line >= 0 && line <= (this.lines.length - 1)) {
			this.currentLine = line;
			this.text = this.lines[this.currentLine];
		}
	}

	@Override
	protected void onClick() {
		if(Mouse.getEventButton() == 0) {
			this.nextLine();
			this.parent.buttonChanged(this, this.currentLine);
		} else if(Mouse.getEventButton() == 1) {
			this.previousLine();
			this.parent.buttonChanged(this, this.currentLine);
		}
	}

}
