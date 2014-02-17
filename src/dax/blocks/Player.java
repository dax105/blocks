package dax.blocks;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import dax.blocks.world.World;

public class Player {

	World world;

	public static final float PLAYER_HEIGHT = 1.7f;
	public static final float EYES_HEIGHT = 1.6f;
	public static final float PLAYER_SIZE = 0.5f;

	byte selectedBlockID = 3;

	double posX = 0.0D;
	double posZ = 0.0D;
	double posY = 32D;

	double lastPosY = 0D;

	public int lookingAtX = -1;
	public int lookingAtY = -1;
	public int lookingAtZ = -1;

	int placesAtX = -1;
	int placesAtY = -1;
	int placesAtZ = -1;

	public boolean hasSelected = false;

	float heading = 140.0F;
	float tilt = -10.0F;

	boolean reload = false;

	public Player(World world) {
		this.world = world;
	}

	private void updateLookingAt() {
		float reach = 16.0F;

		float xn = (float) posX;
		float yn = (float) posY + PLAYER_HEIGHT;
		float zn = (float) posZ;

		float xl;
		float yl;
		float zl;

		float yChange = (float) Math.cos((-tilt + 90) / 180 * Math.PI);
		float ymult = (float) Math.sin((-tilt + 90) / 180 * Math.PI);

		float xChange = (float) (Math.cos((-heading + 90) / 180 * Math.PI) * ymult);
		float zChange = (float) (-Math.sin((-heading + 90) / 180 * Math.PI) * ymult);

		for (float f = 0; f <= reach; f += 0.01f) {
			xl = xn;
			yl = yn;
			zl = zn;

			xn = (float) (posX + f * xChange);
			yn = (float) (posY + EYES_HEIGHT + f * yChange);
			zn = (float) (posZ + f * zChange);

			if (world.getBlock((int) Math.floor(xn), (int) Math.floor(yn), (int) Math.floor(zn)) > 0) {
				lookingAtX = (int) Math.floor(xn);
				lookingAtY = (int) Math.floor(yn);
				lookingAtZ = (int) Math.floor(zn);

				placesAtX = (int) xl;
				placesAtY = (int) yl;
				placesAtZ = (int) zl;
				hasSelected = true;
				return;
			}

			hasSelected = false;

		}
	}

	public void setSelectedBlock(int id) {
		selectedBlockID = (byte) id;
	}

	public void update(float delta) {
		int multi = 1;

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			multi = 5;
		}

		double move = 0.0D;
		double moveStrafe = 0.0D;
		double moveY = 0.0D;

		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			move -= 0.005D * multi;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			move += 0.005D * multi;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			moveStrafe -= 0.005D * multi;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			moveStrafe += 0.005D * multi;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			moveY += 0.005D * multi * delta;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			moveY -= 0.005D * multi * delta;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_1) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD1)) {
			setSelectedBlock(1);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_2) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)) {
			setSelectedBlock(2);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_3) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD3)) {
			setSelectedBlock(3);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_4) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4)) {
			setSelectedBlock(4);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_5) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD5)) {
			setSelectedBlock(5);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_6) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6)) {
			setSelectedBlock(6);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_7) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD7)) {
			setSelectedBlock(7);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_8) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8)) {
			setSelectedBlock(8);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_9) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD9)) {
			setSelectedBlock(9);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_0) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD0)) {
			setSelectedBlock(10);
		}

		if (Mouse.isGrabbed()) {
			float mouseDX = Mouse.getDX() * 0.8f * 0.16f;
			float mouseDY = Mouse.getDY() * 0.8f * 0.16f;
			heading += mouseDX;
			tilt += mouseDY;
		}

		while (heading <= -180) {
			heading += 360;
		}
		while (heading > 180) {
			heading -= 360;
		}

		if (tilt < -90) {
			tilt = -90;
		}

		if (tilt > 90) {
			tilt = 90;
		}

		if (move != 0.0D || moveStrafe != 0.0D || moveY != 0) {
			double toMoveZ = (posZ + Math.cos(-heading / 180 * Math.PI) * delta * move) + (Math.cos((-heading + 90) / 180 * Math.PI) * delta * moveStrafe);
			double toMoveX = (posX + Math.sin(-heading / 180 * Math.PI) * delta * move) + (Math.sin((-heading + 90) / 180 * Math.PI) * delta * moveStrafe);
			double toMoveY = posY + moveY;
			
			posX = toMoveX;
			posY = toMoveY;
			posZ = toMoveZ;
		}

		while (Mouse.next()) {
			if (Mouse.getEventButtonState()) {
				if (Mouse.getEventButton() == 0) {
					if (hasSelected) {
						world.setBlock(lookingAtX, lookingAtY, lookingAtZ, (byte) 0);
					}
				}
				if (Mouse.getEventButton() == 1) {
					if (hasSelected && (lookingAtX != placesAtX || lookingAtY != placesAtY || lookingAtZ != placesAtZ)) {
						world.setBlock(placesAtX, placesAtY, placesAtZ, selectedBlockID);
					}
				}
			}
		}

		updateLookingAt();

	}

}
