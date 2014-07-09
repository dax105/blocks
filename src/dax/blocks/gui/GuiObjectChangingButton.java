package dax.blocks.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
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

		setLine(startingLine);
	}

	public int getCurrentLine() {
		return currentLine;
	}

	public String getCurrentLineString() {
		return lines[currentLine];
	}

	public int nextLine() {
		currentLine++;
		if (currentLine > (lines.length - 1)) {
			currentLine = 0;
		}

		this.text = lines[currentLine];
		return currentLine;
	}

	public int previousLine() {
		currentLine--;
		if (currentLine < 0) {
			currentLine = (lines.length - 1);
		}

		this.text = lines[currentLine];
		return currentLine;
	}

	public void setLine(int line) {
		if (line >= 0 && line <= (lines.length - 1)) {
			this.currentLine = line;
			this.text = lines[currentLine];
		}
	}

	@Override
	protected void onClick() {
		if (Mouse.getEventButton() == 0) {
			this.nextLine();
			parent.buttonChanged(this, currentLine);
		} else if (Mouse.getEventButton() == 1) {
			this.previousLine();
			parent.buttonChanged(this, currentLine);
		}
	}

}
