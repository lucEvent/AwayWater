package com.awaywater.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.awaywater.harware.Vibrator;
import com.awaywater.kernel.GameControl;
import com.awaywater.kernel.basic.Map;
import com.awaywater.kernel.basic.Vector;
import com.awaywater.net.Network;

public class MazeView extends ImageView implements OnTouchListener,
		SensorEventListener, Network {

	private static final String DEBUG = "##MazeView##";
	private static final long[] vibseq = { 150, 150 };

	private Map map;
	private GameControl control;
	private Vibrator vibrator;

	private boolean moving;
	private float accX, accY;
	private Vector move;

	public MazeView(Context context) {
		super(context);
		initialize();
	}

	public MazeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	private void initialize() {
		this.accX = this.accY = 0;
		this.moving = false;
		this.move = new Vector(0, 0);
	}

	public void setUp(Context context, GameControl control, int width,
			int height) {
		Log.d(DEBUG, "[WIDHT HEIGTH] -> [" + width + "," + height + "]");
		control.getMaze(width, height);
		this.vibrator = new Vibrator(context);
		this.control = control;
		checkAccelerometer(context);
		setOnTouchListener(this);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// super.onDraw(canvas);
		move.compX = -(int) (accX + 0.5);
		move.compY = (int) (accY + 0.5);

		if (move.compX == 0 && move.compY == 0) {// Si no se ha movido
			paintPiece(canvas);
			return;
		}

		int left = map.piece.getLeft() + move.compX;
		int right = map.piece.getRight() + move.compX;
		int top = map.piece.getTop() + move.compY;
		int bottom = map.piece.getBottom() + move.compY;

		Log.d(DEBUG, "[MoveX MoveY] ->" + "[" + move.compX + "," + move.compY
				+ "]");
		Log.d(DEBUG, "[LEFT RIGHT TOP BOTTOM] ->" + "[" + left + "," + right
				+ "," + top + "," + bottom + "]");
		// Si llega a los bordes
		if (left < 0) {
			Log.d(DEBUG, "TOCA POR LA IZQUIERDA");
			map.piece.position.x = 0;
			paintPiece(canvas);
			return;
		}
		if (right > map.bitmap.getWidth() - 1) {
			Log.d(DEBUG, "TOCA POR LA DERECHA");
			map.piece.position.x = map.bitmap.getWidth() - 1
					- map.piece.sides.compX;
			paintPiece(canvas);

			moving = false;
			control.getMaze(map.bitmap.getWidth(), map.bitmap.getHeight());
			return;
		}
		if (top < 0) {
			Log.d(DEBUG, "TOCA POR ARRIBA");
			map.piece.position.y = 0;
			paintPiece(canvas);
			return;
		}
		if (bottom > map.bitmap.getHeight() - 1) {
			Log.d(DEBUG, "TOCA POR ABAJO");
			map.piece.position.y = map.bitmap.getHeight() - 1
					- map.piece.sides.compY;
			paintPiece(canvas);
			return;
		}

		// Se calcula la nueva posicion
		Vector readjust = null;
		boolean crashBounds = false;
		while (true) {
			readjust = map.piece.check(map.bitmap, map.world.WALL.getColor(),
					move, map.world);
			if (readjust.compX == 0 && readjust.compY == 0) {
				break;
			} else {
				Log.d(DEBUG, "Readjusting");
				crashBounds = true;
				move.compX += readjust.compX;
				move.compY += readjust.compY;
			}
		}

		map.piece.move(move);
		paintPiece(canvas);

		if (crashBounds) {
			vibrator.vibrate(vibseq, 1);
		}
	}

	private void paintPiece(Canvas canvas) {
		if (map == null) {
			return;
		}
		// Se pinta en su nueva posicion
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.FILL);

		canvas.drawRect(map.piece.getLeft(), map.piece.getTop(),
				map.piece.getRight(), map.piece.getBottom(), paint);
	}

	// //////////////////////////////////////////////////////
	// //////////////**METODOS ONTOUCH**/////////////////////
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			// pausar/reanudar juego
			Log.d(DEBUG, "onTouch UP");
			moving = !moving;
		}
		return true;
	}

	// //////////////////////////////////////////////////////
	// ///////////**METODOS SENSOR LISTENER**////////////////
	@Override
	public void onAccuracyChanged(Sensor sen, int acc) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (moving) {
			accX = event.values[0];
			accY = event.values[1];
			invalidate();
		}
	}

	private void checkAccelerometer(Context context) {
		SensorManager manager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() == 0) {
			Log.d(DEBUG, "No hay un acelerómetro instalado");
		} else {
			Sensor accelerometer = manager.getSensorList(
					Sensor.TYPE_ACCELEROMETER).get(0);
			if (!manager.registerListener(this, accelerometer,
					SensorManager.SENSOR_DELAY_GAME)) {
				Log.d(DEBUG, "No se ha podido registrar el sensor listener");
			}
		}
	}

	// //////////////////////////////////////////////////////

	public void restart(Context context) {
		checkAccelerometer(context);
	}

	public void pause(Context context) {
		SensorManager manager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		manager.unregisterListener(this);
	}

	@Override
	public void message(int resultCode, int requestCode, final Object result) {
		if (resultCode == Network.RESULT_OK) {
			if (requestCode == GameControl.REQUEST_MAP) {
				map = (Map) result;
				setBackground(new BitmapDrawable(map.bitmap));
				invalidate();
				moving = true;
			}
		}

	}

}
