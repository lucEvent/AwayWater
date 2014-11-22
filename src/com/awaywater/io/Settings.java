package com.awaywater.io;

import com.awaywater.kernel.World;
import com.awaywater.kernel.WorldFactory;

public final class Settings {

	private static World world;

	public Settings() {
		setWorld();
	}

	private void setWorld() {
		world = WorldFactory.FOREST;
	}

	public World getWorld() {
		return world;
	}

}
