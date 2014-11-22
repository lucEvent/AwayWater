package com.awaywater.kernel.basic;

import com.awaywater.kernel.World;

import android.graphics.Bitmap;
import android.graphics.Paint;
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

	private void debug(Bitmap bitmap, World debug, Vector move) {
		int offset = 5;
		int left = position.x - offset;
		int right = position.x + sides.compX + offset;
		int top = position.y - offset;
		int bottom = position.y + sides.compY + offset;

		if (left < 0)
			left = 0;
		if (top < 0)
			top = 0;
		if (bottom > bitmap.getHeight())
			bottom = bitmap.getHeight();
		if (right > bitmap.getWidth())
			right = bitmap.getWidth();
		
		char[][] temp = new char[right+1][bottom+1];
		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < temp[0].length; j++) {
				temp[i][j] = '·';
			}
		}
		for (int i = left; i <= right; i++) {
			for (int j = top; j <= bottom; j++) {
				int color = bitmap.getPixel(i,j);
				char c;
				if (color == debug.WALL.getColor()) {
					c = 'W';
				} else if (color == debug.ROAD.getColor()) {
					c = 'R';
				} else if (color == debug.NOTREACHABLE.getColor()) {
					c = 'N';
				} else {
					c = 'X';
				}
				temp[i][j] = c;
			}
		}
		Point[] sq = new Point[4];
		sq[0] =new Point(position.x,position.y);
		sq[1]= new Point(position.x+sides.compX, position.y);
		sq[2]= new Point(position.x,position.y+sides.compY);
		sq[3]= new Point(position.x+sides.compX,position.y+sides.compY);
		for (int i = 0; i < sq.length; i++) {
			Point p = sq[i];
			temp[p.x][p.y] = 'O';
		}
		
		boolean b = false;
		StringBuilder result = new StringBuilder("##################### -- MAP -- #############################\n");
		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < temp[0].length; j++) {
				char t = temp[i][j];
				if (t != '·') {
					result.append(""+temp[i][j]);
					b = true;
				}
			}
			if (b) {
				result.append("\n");
				b = false;
			}
		}
		result.append("##################### -- DATA -- ############################\n");
		result.append("Map: [LEFT, TOP, RIGHT, BOTTOM] -> " +
				" [" + left + "," + top+ "," + right+ "," + bottom + "]\n");
		result.append("Square: [LEFT, TOP, RIGHT, BOTTOM] -> " +
				" [" + position.x + "," + position.y+ "," +
				(position.x + sides.compX)+ "," + (position.y + sides.compY) + "]\n");
		result.append("[Move.X, Move.Y] -> [" + (move.compX)+ "," + (move.compY) + "]\n");
		result.append("###################### -- END -- #############################\n");
		Log.d(DEBUG, result.toString());
	}

	@Override
	public Vector check(Bitmap map, int wallcolor, Vector movement, World debug) {
		Vector res = new Vector(0, 0);

		int cornerLTX = position.x + movement.compX;
		int cornerLTY = position.y + movement.compY;
		Log.d(DEBUG, "Corneres-> [" + cornerLTX + "," + cornerLTY
				+ "] ----- Movement-> [" + movement.compX + ","
				+ movement.compY + "]");
		if (movement.compX < 0) { // Check left
			Log.d(DEBUG, "Looking left");
			if (map.getPixel(cornerLTX, cornerLTY) == wallcolor
					|| map.getPixel(cornerLTX, cornerLTY + sides.compY) == wallcolor) {
				Log.d(DEBUG, "Breaking on the LEFT: ["+cornerLTX+","+cornerLTY+"] or ["+cornerLTX+","+(cornerLTY + sides.compY)+"]");
				res.compX = 1;
			}
		} else if (movement.compX > 0) { // Check right
			Log.d(DEBUG, "Looking right");
			if (map.getPixel(cornerLTX + sides.compX, cornerLTY) == wallcolor
					|| map.getPixel(cornerLTX + sides.compX, cornerLTY + sides.compY) == wallcolor) {
				Log.d(DEBUG, "Breaking on the RIGHT ["+(cornerLTX + sides.compX)+","+cornerLTY+"] or ["+(cornerLTX + sides.compX)+","+(cornerLTY + sides.compY)+"]");
				res.compX = -1;
			}
		}
		if (movement.compY < 0) {
			Log.d(DEBUG, "Looking TOP");
			if (map.getPixel(cornerLTX, cornerLTY) == wallcolor
					|| map.getPixel(cornerLTX + sides.compX, cornerLTY) == wallcolor) {
				Log.d(DEBUG, "Breaking on the TOP ["+cornerLTX+","+cornerLTY+"] or ["+(cornerLTX + sides.compX)+","+cornerLTY+"]");
				res.compY = 1;
			}
		} else if (movement.compY > 0) { // Check bottom
			Log.d(DEBUG, "Looking BOTTOM");
			if (map.getPixel(cornerLTX, cornerLTY + sides.compY) == wallcolor
					|| map.getPixel(cornerLTX + sides.compX, cornerLTY + sides.compY) == wallcolor) {
				Log.d(DEBUG, "Breaking on the BOTTOM ["+(cornerLTX)+","+( cornerLTY + sides.compY)+"] or ["+(cornerLTX + sides.compX)+","+( cornerLTY + sides.compY)+"]");
				res.compY = -1;
			}
		}

//		if (res.compX != 0 || res.compY != 0)
	//		debug(map, debug,movement);

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
