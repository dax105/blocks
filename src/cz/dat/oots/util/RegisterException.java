package cz.dat.oots.util;

public class RegisterException extends Exception {

	private static final long serialVersionUID = -6278217466183667900L;
	private int causeID;

	public RegisterException(int id) {
		super("Can't register block at ID " + id
				+ ", because this ID is already registered!");
		this.causeID = id;
	}

	public RegisterException(String name) {
		super("Can't register block " + name
				+ ", because this name is already registered!");
	}

	public int getIDCaused() {
		return this.causeID;
	}
}
