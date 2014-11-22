package com.awaywater.io;

import com.awaywater.kernel.World;
import com.awaywater.kernel.WorldFactory;

public final class Settings {

	private static World world;

	public Settings() {
		setWorld(WorldFactory.CITY);
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public World getWorld() {
		return world;
	}

}
