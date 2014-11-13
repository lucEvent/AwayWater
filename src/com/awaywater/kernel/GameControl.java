package com.awaywater.kernel;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;

import com.awaywater.io.Settings;
import com.awaywater.net.Network;

public class GameControl implements Runnable {

	private static final String DEBUG = "##GameControl##";

	public static final int REQUEST_MAP = 0;

	public Settings settings;

	private WorldFactory worldfactory;
	private MapGenerator generator;
	private int level;

	private Network network;

	/** THREAD VARIABLES **/
	public boolean isRunning;
	private boolean generateMapFlag;

	/** GAME COMPOUNTS **/
	private TypeofGround[][] mapMaze;
	private int width;
	private int height;

	public GameControl(Network network) {
		this.worldfactory = new WorldFactory();
		this.generator = new MapGenerator();
		this.level = 0;
		this.network = network;
		generateMapFlag = false;
		this.settings = new Settings();
		this.width = 0;
		this.height = 0;
		this.handler = new Handler();
	}

	public boolean hasDimension() {
		return width != 0;
	}

	public void getMaze(int width, int height) {
		this.width = width;
		this.height = height;
		generateMapFlag = true;
		new Thread(this).start();
	}

	public World getActualWorld() {
		return settings.getWorld();
	}

	@Override
	public void run() {
		if (generateMapFlag) {
			generateMap();
		}
	}

	private Handler handler;

	private void generateMap() {
		Log.d(DEBUG, "Generating map");
		generateMapFlag = false;

		final Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		// Matrix matrix = new Matrix();

		int widthMap = WorldFactory.LEVEL_WIDTH[level++];
		int heightMap = (widthMap * height) / width;

		mapMaze = new TypeofGround[widthMap][heightMap];
		generator.defineTypeofMap(0);
		generator.generate(mapMaze, widthMap, heightMap, 0);
		while (!generator.isGenerated) {
			Log.d(DEBUG, "Esperando en el while");
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				Log.d(DEBUG, "InterruptedException: " + e.toString());
			}
		}

		TypeofGround[][] map = mapMaze;

		int startSquare = generator.getStartSquare();
		World world = settings.getWorld();
		int pixelsX = width / map.length;
		int pixelsY = height / map[0].length;

		int drawOnX = (width % map.length) / 2;
		int drawOnY = (height % map[0].length) / 2;

		// int accumulator = 1; //for improvement
		for (int i = 0; i < map.length; ++i) {
			for (int j = 0; j < map[0].length; ++j) {
				Rect rec = new Rect(i * pixelsX + drawOnX, j * pixelsY
						+ drawOnY, (i + 1) * pixelsX + drawOnX, (j + 1)
						* pixelsY + drawOnY);
				switch (map[i][j]) {
				case WALL:
					canvas.drawRect(rec, world.WALL);
					break;
				case ROAD:
					canvas.drawRect(rec, world.ROAD);
					break;
				case NOTVISITED:
					canvas.drawRect(rec, world.NOTREACHABLE);
				}
			}
		}

		// Drawing the ball (Soon wont be a ball)
		int l, u, r, d;
		l = drawOnX + 1;
		u = startSquare * pixelsY + drawOnY;
		r = l + pixelsX;
		d = u + pixelsY;

		canvas.drawRect(l, u, r, d, WorldFactory.BALL);

		handler.post(new Runnable() {
			@Override
			public void run() {
					network.message(Network.RESULT_OK, REQUEST_MAP, result);
			}
		});
		Log.d(DEBUG, "Generated map");
	}
}
