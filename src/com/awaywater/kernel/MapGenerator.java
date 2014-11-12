package com.awaywater.kernel;

import java.util.Random;

import android.util.Log;

public class MapGenerator implements Runnable {

	// Constants
	private static final int NORTH = 0;
	private static final int EAST = 1;
	private static final int SOUTH = 2;
	private static final int WEST = 3;

	// Variables for running
	private Thread thread;
	private Random rand;
	public boolean isGenerated;

	// Variables for generating map
	private TypeofGround[][] map;
	private int width;
	private int height;
	private int start;
	private int finaly;

	// Constructor call
	public MapGenerator() {
		Log.d("##MapGenerator##",
				"constructor->		" + System.currentTimeMillis());
		thread = new Thread(this);
		rand = new Random();
		isGenerated = true;
	}

	// Function that generates a map maze of width x height dimension
	// Note that map maze isnt generated at returning of this function,
	// you must comprove public isGenerated variable
	public void generate(TypeofGround[][] maze, int width, int height, int start) {
		Log.d("##MapGenerator##",
				"generate()->			" + System.currentTimeMillis());
		map = maze;
		this.width = width;
		this.height = height;
		finaly = -1;
		if (start <= 0) {
			this.start = rand.nextInt(height - 2) + 1;
		} else
			this.start = start;

		isGenerated = false;
		thread.start();
	}

	// Functions that changes goOn function behavior
	public void defineTypeofMap(int type/** Need some parameters **/
	) {
		// Needs to be implemented
		Log.d("##MapGenerator##",
				"definetypeofview()->	" + System.currentTimeMillis());
	}

	// WARNING: Do not call this function
	// You must call generate function to run this class
	@Override
	public void run() {
		while (finaly < 0) {
			Log.d("##MapGenerator##",
					"RunStarts->			" + System.currentTimeMillis());
			// Initialize to Road (false)
			for (int j = 0; j < height; j++) {
				for (int i = 0; i < width; i++) {
					map[i][j] = TypeofGround.NOTVISITED;
				}
			}
			// Initialize bounds
			for (int i = 0; i < width; ++i) {
				map[i][0] = TypeofGround.WALL;
				map[i][height - 1] = TypeofGround.WALL;
			}
			for (int j = 0; j < height; ++j) {
				map[0][j] = TypeofGround.WALL;
				map[width - 1][j] = TypeofGround.WALL;
			}
			// Initialize entrance
			int startx = 1;
			int starty = start;
			map[0][starty] = TypeofGround.ROAD;
			map[startx][starty] = TypeofGround.ROAD;
			// Begining recursivity
			advancePosition(startx, starty, WEST);
		}
		isGenerated = true;
		Log.d("##MapGenerator##", "RunEnds->" + System.currentTimeMillis());
	}

	private void advancePosition(int posx, int posy, int directionFrom) {
		TypeofGround next;
		if (directionFrom != NORTH) {
			// Going to North if possible
			if (posy > 1) {
				next = map[posx][posy - 1];
				if (next == TypeofGround.NOTVISITED) {
					if (goOn()) {
						map[posx][posy - 1] = TypeofGround.ROAD;
						advancePosition(posx, posy - 1, SOUTH);
					} else {
						map[posx][posy - 1] = TypeofGround.WALL;
					}
				}
			}
		}
		if (directionFrom != EAST) {
			// Going to East if possible
			if (posx + 2 == width) {
				if (finaly == -1) {
					map[posx + 1][posy] = TypeofGround.ROAD;
					finaly = posy;
				}
			} else if (posx + 1 < width) {
				next = map[posx + 1][posy];
				if (next == TypeofGround.NOTVISITED) {
					if (goOn()) {
						map[posx + 1][posy] = TypeofGround.ROAD;
						advancePosition(posx + 1, posy, WEST);
					} else {
						map[posx + 1][posy] = TypeofGround.WALL;
					}
				}
			}
		}
		if (directionFrom != SOUTH) {
			// Going to South if possible
			if (posy + 2 < height) {
				next = map[posx][posy + 1];
				if (next == TypeofGround.NOTVISITED) {
					if (goOn()) {
						map[posx][posy + 1] = TypeofGround.ROAD;
						advancePosition(posx, posy + 1, NORTH);
					} else {
						map[posx][posy + 1] = TypeofGround.WALL;
					}
				}
			}
		}

		if (directionFrom != WEST) {
			// Going to West if possible
			if (posx - 1 > 0) {
				next = map[posx - 1][posy];
				if (next == TypeofGround.NOTVISITED) {
					if (goOn()) {
						map[posx - 1][posy] = TypeofGround.ROAD;
						advancePosition(posx - 1, posy, EAST);
					} else {
						map[posx - 1][posy] = TypeofGround.WALL;
					}
				}
			}
		}
	}

	private boolean goOn() {
		switch (rand.nextInt(6)) {
		case 0:
			return rand.nextBoolean() && rand.nextBoolean();
		case 1:
			return rand.nextBoolean();
		case 3:
			return (rand.nextBoolean() || rand.nextBoolean())
					&& (rand.nextBoolean() || rand.nextBoolean());
		case 2:
			return rand.nextBoolean() || rand.nextBoolean()
					|| rand.nextBoolean();
		default:
			return rand.nextBoolean() || rand.nextBoolean();
		}
	}

	public int getStartSquare() {
		return start;
	}

	public int getFinalSquare() {
		return finaly;
	}

}
