package cz.dat.oots.console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.lwjgl.input.Keyboard;

import cz.dat.oots.FontManager;
import cz.dat.oots.Game;

public class Console {

	public static final int MAX_LINES = 100;
	public static final int MAX_MEMORY = 100;

	public List<String> lines = new ArrayList<String>();
	public List<String> memory = new ArrayList<String>();

	public CommandManager manager;
	private Game game;
	public String currentCommand = "";
	public int memPos = 0;
	public boolean memUsed = false;
	public int displayOffset = 0;
	private Date now = new Date();
	private String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789._-!,=%()[]{}<>#&@* ";

	public void println(String ln) {
		this.out(ln);
	}

	public Console(Game game) {
		this.game = game;
		this.manager = new CommandManager(this);
	}

	public void clearInput() {
		this.currentCommand = "";
	}

	public void out(String text) {
		this.now = new Date();
		System.out.println("[INFO] " + this.now + ": " + text);
		this.lines.add(text);
		if(this.lines.size() > Console.MAX_LINES) {
			this.lines.remove(0);
		}
	}

	public void addToMemory(String text) {
		this.memory.add(text);
		if(this.memory.size() > Console.MAX_MEMORY) {
			this.memory.remove(0);
		}
	}

	public void memUp() {
		if(this.memUsed) {
			if(this.memPos - 1 >= 0) {
				this.memPos--;
				this.setCommandFromMem();
			}
		} else if(this.memory.size() > 0) {
			this.memUsed = true;
			this.memPos = this.memory.size() - 1;
			this.setCommandFromMem();
		}
	}

	public void memDown() {
		if(this.memUsed) {
			if(this.memPos + 1 < this.memory.size()) {
				this.memPos++;
				this.setCommandFromMem();
			} else {
				this.memUsed = false;
				this.clearInput();
			}
		}
	}

	public void setCommandFromMem() {
		this.currentCommand = this.memory.get(this.memPos);
	}

	public void offsetDown() {
		this.displayOffset -= 1;
	}

	public void offsetUp() {
		this.displayOffset += 1;
	}

	public float getTranslation() {
		return -FontManager.getFont().getLineHeight() * this.displayOffset;
	}

	public void charTyped(char c, int key) {
		if(this.characters.contains("" + c)) {
			this.currentCommand += c;
			this.displayOffset = 0;
		}

		if(Keyboard.KEY_UP == key) {

			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
					|| Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
				this.offsetUp();
			} else {
				this.memUp();
			}

		}

		if(Keyboard.KEY_DOWN == key) {
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
					|| Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
				this.offsetDown();
			} else {
				this.memDown();
			}
		}

		if(Keyboard.KEY_BACK == key && this.currentCommand.length() > 0) {
			this.currentCommand = this.currentCommand.substring(0,
					this.currentCommand.length() - 1);
		}

		if(Keyboard.KEY_RETURN == key && this.currentCommand != "") {
			this.executeCommand();
		}
	}

	public void executeCommand() {
		this.memUsed = false;
		this.addToMemory(this.currentCommand);
		this.out("> " + this.currentCommand);
		String[] words = this.currentCommand.split(" ");
		String[] args = Arrays.copyOfRange(words, 1, words.length);
		this.clearInput();

		if(words.length > 0) {
			Command command;
			command = this.manager.getCommand(words[0].toLowerCase());
			if(command != null) {
				command.execute(args);
			} else {
				if(!words[0].equalsIgnoreCase("")) {
					this.out("Unknown command \"" + words[0] + "\"");
				} else {
					this.out("");
				}
			}
		}
	}

	public Game getGame() {
		return this.game;
	}
}
