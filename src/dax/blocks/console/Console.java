package dax.blocks.console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.lwjgl.input.Keyboard;

import dax.blocks.Game;

public class Console {

	public static final int MAX_LINES = 100;
	public static final int MAX_MEMORY = 100;
	
	public List<String> lines = new ArrayList<String>();
	public List<String> memory = new ArrayList<String>();
	
	public CommandManager manager = new CommandManager();
	public String currentCommand = "";
	public int memPos = 0;
	public boolean memUsed = false;
	
	public int displayOffset = 0;
	
	private Date now = new Date();
	
	private String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789._-!,=%()[]{}<>#&@* ";
	
	public void clearInput() {
		this.currentCommand = "";
	}
	
	public void out(String text) {
		now = new Date();
		System.out.println("[INFO] " + now + ": " + text);
		lines.add(text);
		if (lines.size() > MAX_LINES) {
			lines.remove(0);
		}
	}
	
	public void addToMemory(String text) {
		memory.add(text);
		if (memory.size() > MAX_MEMORY) {
			memory.remove(0);
		}
	}
	
	public void memUp() {
		if (memUsed) {
			if (memPos - 1 >= 0) {
				memPos--;
				setCommandFromMem();
			}
		} else if (memory.size() > 0) {
			memUsed = true;
			memPos = memory.size()-1;
			setCommandFromMem();
		}
	}
	
	public void memDown() {
		if (memUsed) {
			if (memPos + 1 < memory.size()) {
				memPos++;
				setCommandFromMem();
			} else {
				memUsed = false;
				clearInput();
			}
		}
	}
	
	public void setCommandFromMem() {
		currentCommand = memory.get(memPos);
	}
	
	public void offsetDown() {
		displayOffset -= 1;
	}
	
	public void offsetUp() {
		displayOffset += 1;
	}
	
	public float getTranslation() {
		return -Game.getInstance().font.getLineHeight() * displayOffset;
	}
	
	public void charTyped(char c, int key) {
		if (characters.contains("" + c)) {
			currentCommand += c;
			displayOffset = 0;
		}
		
		if (Keyboard.KEY_UP == key) {
			
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
				offsetUp();
			} else {
				memUp();
			}

		}
		
		if (Keyboard.KEY_DOWN == key) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
				offsetDown();
			} else {
				memDown();
			}
		}
		
		if (Keyboard.KEY_BACK == key && currentCommand.length() > 0) {
			currentCommand = currentCommand.substring(0, currentCommand.length() - 1);
		}
		
		if (Keyboard.KEY_RETURN == key && currentCommand != "") {
			executeCommand();
		}
	}
	
	public void executeCommand() {
		memUsed = false;
		addToMemory(currentCommand);
		out("> " + currentCommand);
		String[] words = currentCommand.split(" ");
		String[] args = Arrays.copyOfRange(words, 1, words.length);
		clearInput();
		
		if (words.length > 0) {
			Command command;
			command = manager.getCommand(words[0].toLowerCase());
			if (command != null) {
				command.execute(args);
			} else {
				if (!words[0].equalsIgnoreCase("")) {
					out("Unknown command \"" + words[0] + "\"");
				} else {
					out("");
				}
			}
		}
	}

}
