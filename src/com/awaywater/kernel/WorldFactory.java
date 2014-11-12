package com.awaywater.kernel;

import android.graphics.Color;
import android.graphics.Paint;

public final class WorldFactory {

	public static int[] LEVEL_WIDTH = { 15, 17, 19, 20, 21, 22, 23, 25, 25, 26,
			27, 27, 28, 28, 29, 29, 30, 30, 31, 31 };

	public static World FOREST;
	public static World CITY;
	public static World MOUNTAIN;
	public static World GRAVEYARD;

	public static Paint BALL;

	public WorldFactory() {

		// Initializing BALL
		BALL = initializeBall();
		// Initializing FOREST
		FOREST = initializeForest();
		// Initializing CITY
		CITY = initializeCity();
		// Initialize MOUNTAIN
		MOUNTAIN = initializeMountain();
		// Initialize GRAVEYARD
		GRAVEYARD = initializeGraveyard();

		// SOON MORE WORLDS...
	}

	private Paint initializeBall() {
		Paint res = new Paint(/* flags */);
		res.setStyle(Paint.Style.FILL);
		res.setColor(Color.BLACK);

		return res;
	}

	private World initializeForest() {
		World res = new World();

		res.ROAD = new Paint(/* flags */);
		res.ROAD.setStyle(Paint.Style.FILL);
		res.ROAD.setARGB(255, 205, 102, 29);

		res.WALL = new Paint(/* flags */);
		res.WALL.setStyle(Paint.Style.FILL);
		res.WALL.setARGB(255, 34, 139, 34);

		res.NOTREACHABLE = new Paint(/* flags */);
		res.NOTREACHABLE.setStyle(Paint.Style.FILL);
		res.NOTREACHABLE.setARGB(255, 47, 79, 47);

		return res;
	}

	private World initializeCity() {
		// TODO Auto-generated method stub
		return null;
	}

	private World initializeMountain() {
		// TODO Auto-generated method stub
		return null;
	}

	private World initializeGraveyard() {
		// TODO Auto-generated method stub
		return null;
	}
}
