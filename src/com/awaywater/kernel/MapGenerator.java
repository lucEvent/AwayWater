package com.awaywater.kernel;

import java.util.Random;

import com.awaywater.kernel.basic.Map;
import com.awaywater.kernel.basic.TypeofGround;

import android.util.Log;

public class MapGenerator implements Runnable {

	private static final String DEBUG = "##MapGenerator##";

	private static final int NORTH = 0;
	private static final int EAST = 1;
	private static final int SOUTH = 2;
	private static final int WEST = 3;

	// Variables for running
	private Random rand;
	public boolean isGenerated;

	// Variables for generating map
	private Map map;
	
	public MapGenerator() {
		rand = new Random();
		isGenerated = true;
	}

	// Function that generates a map maze of width x height dimension
	// Note that map maze isnt generated at returning of this function,
	// you must comprove public isGenerated variable
	public void generate(TypeofGround[][] maze, int width, int height, int start) {
		Log.d(DEBUG, "En generate para llamar al thread");
		
		map = new Map();
		map.map = maze;
		map.width = width;
		map.height = height;
		map.end = -1;
		if (start <= 0) {
			map.start = rand.nextInt(height - 2) + 1;
		} else
			map.start = start;

		isGenerated = false;
		new Thread(this).start();
	}

	// Functions that changes goOn function behavior
	public void defineTypeofMap(int type/** Need some parameters **/) {
		// Needs to be implemented
		Log.d(DEBUG, "defineTypeofMap");
	}

	// WARNING: Do not call this function
	// You must call generate function to run this class
	@Override
	public void run() {
		while (map.end < 0) {
			Log.d(DEBUG, "Run starts");
			// Initialize to Road (false)
			for (int j = 0; j < map.height; j++) {
				for (int i = 0; i < map.width; i++) {
					map.map[i][j] = TypeofGround.NOTVISITED;
				}
			}
			// Initialize bounds
			for (int i = 0; i < map.width; ++i) {
				map.map[i][0] = TypeofGround.WALL;
				map.map[i][map.height - 1] = TypeofGround.WALL;
			}
			for (int j = 0; j < map.height; ++j) {
				map.map[0][j] = TypeofGround.WALL;
				map.map[map.width - 1][j] = TypeofGround.WALL;
			}
			// Initialize entrance
			int startx = 1;
			int starty = map.start;
			map.map[0][starty] = TypeofGround.ROAD;
			map.map[startx][starty] = TypeofGround.ROAD;
			// Begining recursivity
			advancePosition(startx, starty, WEST);
		}
		isGenerated = true;
		Log.d(DEBUG, "Run Ends");
	}

	private void advancePosition(int posx, int posy, int directionFrom) {
		TypeofGround next;
		if (directionFrom != NORTH) {
			// Going to North if possible
			if (posy > 1) {
				next = map.map[posx][posy - 1];
				if (next == TypeofGround.NOTVISITED) {
					if (goOn()) {
						map.map[posx][posy - 1] = TypeofGround.ROAD;
						advancePosition(posx, posy - 1, SOUTH);
					} else {
						map.map[posx][posy - 1] = TypeofGround.WALL;
					}
				}
			}
		}
		if (directionFrom != EAST) {
			// Going to East if possible
			if (posx + 2 == map.width) {
				if (map.end == -1) {
					map.map[posx + 1][posy] = TypeofGround.ROAD;
					map.end = posy;
				}
			} else if (posx + 1 < map.width) {
				next = map.map[posx + 1][posy];
				if (next == TypeofGround.NOTVISITED) {
					if (goOn()) {
						map.map[posx + 1][posy] = TypeofGround.ROAD;
						advancePosition(posx + 1, posy, WEST);
					} else {
						map.map[posx + 1][posy] = TypeofGround.WALL;
					}
				}
			}
		}
		if (directionFrom != SOUTH) {
			// Going to South if possible
			if (posy + 2 < map.height) {
				next = map.map[posx][posy + 1];
				if (next == TypeofGround.NOTVISITED) {
					if (goOn()) {
						map.map[posx][posy + 1] = TypeofGround.ROAD;
						advancePosition(posx, posy + 1, NORTH);
					} else {
						map.map[posx][posy + 1] = TypeofGround.WALL;
					}
				}
			}
		}

		if (directionFrom != WEST) {
			// Going to West if possible
			if (posx - 1 > 0) {
				next = map.map[posx - 1][posy];
				if (next == TypeofGround.NOTVISITED) {
					if (goOn()) {
						map.map[posx - 1][posy] = TypeofGround.ROAD;
						advancePosition(posx - 1, posy, EAST);
					} else {
						map.map[posx - 1][posy] = TypeofGround.WALL;
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
	
	public Map getMap() {
		return map;
	}

}
