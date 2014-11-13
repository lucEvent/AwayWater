package com.awaywater.io;

import android.os.Vibrator;

import com.awaywater.kernel.World;
import com.awaywater.kernel.WorldFactory;

public final class Settings {

	public static Vibrator vibrator;

	private static World world;

	public Settings() {
		// vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
		setWorld();
	}

	private void setWorld() {
		world = WorldFactory.FOREST;
	}

	public World getWorld() {
		return world;
	}

}
