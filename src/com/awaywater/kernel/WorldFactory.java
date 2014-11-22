package com.awaywater.kernel;

import android.graphics.Color;
import android.graphics.Paint;

public final class WorldFactory {

	public static int[] LEVEL_WIDTH = { 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 25, 25, 26, 26,
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
		World res = new World();

		res.ROAD = new Paint(/* flags */);
		res.ROAD.setStyle(Paint.Style.FILL);
		res.ROAD.setARGB(255, 120, 120, 120);

		res.WALL = new Paint(/* flags */);
		res.WALL.setStyle(Paint.Style.FILL);
		res.WALL.setARGB(255, 207, 207, 207);

		res.NOTREACHABLE = new Paint(/* flags */);
		res.NOTREACHABLE.setStyle(Paint.Style.FILL);
		res.NOTREACHABLE.setARGB(255, 64, 148, 35);

		return res;
	}

	private World initializeMountain() {
		World res = new World();

		res.ROAD = new Paint(/* flags */);
		res.ROAD.setStyle(Paint.Style.FILL);
		res.ROAD.setARGB(255, 247, 187, 84);

		res.WALL = new Paint(/* flags */);
		res.WALL.setStyle(Paint.Style.FILL);
		res.WALL.setARGB(255, 34, 105, 15);

		res.NOTREACHABLE = new Paint(/* flags */);
		res.NOTREACHABLE.setStyle(Paint.Style.FILL);
		res.NOTREACHABLE.setARGB(255, 179, 79, 12);

		return res;
	}

	private World initializeGraveyard() {
		World res = new World();

		res.ROAD = new Paint(/* flags */);
		res.ROAD.setStyle(Paint.Style.FILL);
		res.ROAD.setARGB(255, 0, 160, 0);

		res.WALL = new Paint(/* flags */);
		res.WALL.setStyle(Paint.Style.FILL);
		res.WALL.setARGB(255, 115, 115, 115);

		res.NOTREACHABLE = new Paint(/* flags */);
		res.NOTREACHABLE.setStyle(Paint.Style.FILL);
		res.NOTREACHABLE.setARGB(255, 43, 43, 43);

		return res;
	}
}
