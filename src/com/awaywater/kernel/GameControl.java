package com.awaywater.kernel;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;

import com.awaywater.io.Settings;
import com.awaywater.kernel.basic.Point;
import com.awaywater.kernel.basic.SquarePiece;
import com.awaywater.kernel.basic.TypeofGround;
import com.awaywater.kernel.basic.Vector;
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

	public void getMaze(int width, int height) {
		this.width = width;
		this.height = height;
		generateMapFlag = true;
		new Thread(this).start();
	}

	public World getActualWorld() {
		return settings.getWorld();
	}

	public void setWorld(World world) {
		settings.setWorld(world);
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

		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		int widthMap = WorldFactory.LEVEL_WIDTH[level++];
		int heightMap = (widthMap * height) / width;

		TypeofGround[][] map = new TypeofGround[widthMap][heightMap];
		generator.defineTypeofMap(0);
		generator.generate(map, widthMap, heightMap, 0);
		while (!generator.isGenerated) {
			Log.d(DEBUG, "Esperando en el while");
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				Log.d(DEBUG, "InterruptedException: " + e.toString());
			}
		}

		World world = settings.getWorld();
		int pixelsX = width / map.length;
		int pixelsY = height / map[0].length;

		
		int stopW = pixelsX*map.length;
		int stopH = pixelsY*map[0].length;
		
		for (int i = 0; i < stopW; i +=pixelsX) {
			for (int j = 0; j < stopH;j+=pixelsY) {
				int left = i;
				int right = i + pixelsX;
				int top = j;
				int bottom = j + pixelsY;
				
				Rect rec = new Rect(left, top, right, bottom);
				
				switch (map[i/pixelsX][j/pixelsY]) {
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

		int startSquare = generator.getMap().start;
		SquarePiece p = new SquarePiece(new Point(0, startSquare*pixelsY), new Vector(pixelsX-1, pixelsY-1));
		generator.getMap().piece = p;
		generator.getMap().bitmap = bitmap;
		generator.getMap().world = world;

		handler.post(new Runnable() {
			@Override
			public void run() {
				network.message(Network.RESULT_OK, REQUEST_MAP,
						generator.getMap());
			}
		});
		Log.d(DEBUG, "Generated map");
	}
}
