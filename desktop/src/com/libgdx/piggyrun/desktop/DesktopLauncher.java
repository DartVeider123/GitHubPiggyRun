package com.libgdx.piggyrun.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.libgdx.piggyrun.PiggyRunMain;

public class DesktopLauncher {
	public static void main (String[] arg) {
		System.setProperty("user.name","\\xD0\\x98\\xD0\\xB2\\xD0\\xB0\\xD0\\xBD");
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 840;
		config.height = 540;
		new LwjglApplication(new PiggyRunMain(), config);
	}
}
