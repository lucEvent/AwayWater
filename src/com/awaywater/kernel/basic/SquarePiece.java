package com.awaywater.kernel.basic;

import android.graphics.Bitmap;
import android.util.Log;

public class SquarePiece extends Piece {

	private static final String DEBUG = "##SquarePiece##";

	public Vector sides;

	public SquarePiece(Point position, Vector sides) {
		super(position);
		this.sides = sides;
	}

	public SquarePiece(int X, int Y, Vector sides) {
		super(X, Y);
		this.sides = sides;
	}

	@Override
	public Vector check(Bitmap map, int wallcolor, Vector movement) {
		Vector res = new Vector(0, 0);

		int cornerLTX = position.x + movement.compX;
		int cornerLTY = position.x + movement.compX;

		if (movement.compX < 0) { // Check left
			Log.d(DEBUG, "Looking left");
			if (map.getPixel(cornerLTX, cornerLTY) == wallcolor
					|| map.getPixel(cornerLTX, cornerLTY + sides.compY) == wallcolor) {
				Log.d(DEBUG, "Breaking on the corner LEFT TOP or LEFT BOTTOM");
				res.compX = 1;
			}
		} else { // Check right
			Log.d(DEBUG, "Looking right");
			if (map.getPixel(cornerLTX + sides.compX, cornerLTY) == wallcolor
					|| map.getPixel(cornerLTX + sides.compX, cornerLTY
							+ sides.compY) == wallcolor) {
				Log.d(DEBUG, "Breaking on the corner RIGHT TOP or RIGHT BOTTOM");
				res.compX = -1;
			}
		}
		if (movement.compY < 0) {
			Log.d(DEBUG, "Looking TOP");
			if (map.getPixel(cornerLTX, cornerLTY) == wallcolor
					|| map.getPixel(cornerLTX + sides.compX, cornerLTY) == wallcolor) {
				Log.d(DEBUG, "Breaking on the corner LEFT TOP or RIGHT TOP");
				res.compY = 1;
			}
		} else { // Check bottom
			Log.d(DEBUG, "Looking BOTTOM");
			if (map.getPixel(cornerLTX, cornerLTY + sides.compY) == wallcolor
					|| map.getPixel(cornerLTX + sides.compX, cornerLTY
							+ sides.compY) == wallcolor) {
				Log.d(DEBUG,
						"Breaking on the corner RIGHT BOTTOM or LEFT BOTTOM");
				res.compY = -1;
			}
		}
		return res;
	}

	public int getLeft() {
		return position.x;
	}

	public int getRight() {
		return position.x + sides.compX;
	}

	public int getTop() {
		return position.y;
	}

	public int getBottom() {
		return position.y + sides.compY;
	}

}
