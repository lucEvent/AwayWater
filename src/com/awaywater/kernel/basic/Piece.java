package com.awaywater.kernel.basic;

import com.awaywater.kernel.World;

import android.graphics.Bitmap;

public abstract class Piece {

	public Point position;

	public Piece(Point position) {
		this.position = position;
	}

	public Piece(int X, int Y) {
		this.position = new Point(X, Y);
	}

	public abstract Vector check(Bitmap map, int wallcolor, Vector movement, World debug);

	public void move(Vector movement) {
		position.x += movement.compX;
		position.y += movement.compY;
	}
}
