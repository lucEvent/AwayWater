package com.awaywater.view;

import android.content.Context;
import android.graphics.Canvas;
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
import com.awaywater.kernel.World;
import com.awaywater.kernel.basic.Map;
import com.awaywater.kernel.basic.Vector;
import com.awaywater.net.Network;

public class MazeView extends ImageView implements OnTouchListener,
		SensorEventListener, Network {

	private static final String DEBUG = "##MazeView##";

	public World actualWorld;

	public boolean moving;
	private boolean crashBounds;

	private GameControl control;

	
	private Vibrator vibrator;

	private long[] vibseq = { 150, 150 };

	private float accX = 0;
	private float accY = 0;

	private Map map;

	public MazeView(Context context) {
		super(context);
		initialize(context);
	}

	public MazeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	private void initialize(Context context) {
		moving = false;
		crashBounds = false;
		checkAccelerometer(context);
		vibrator = new Vibrator(context);
		setOnTouchListener(this);
	}

	public void setControl(GameControl control) {
		this.control = control;
		this.actualWorld = control.getActualWorld();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.d(DEBUG, "On measure");
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		this.setMeasuredDimension(width, height);
		if (!control.hasDimension()) {
			control.getMaze(width, height);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.d(DEBUG, "Drawing");
		// super.onDraw(canvas);
		Vector move = new Vector((int) (accX + 0.5), (int) (accY + 0.5));

		if (move.compX == 0 && move.compY == 0) {// Si no se ha movido
			return;
		}

		// Se llega a los bordes
		if (map.piece.getLeft() + move.compX == 0) {
			// Toca por la izquierda
			return;
		}
		if (map.piece.getRight() + move.compX == map.width) {
			// Toca por la derecha
			return;
		}
		if (map.piece.getTop() + move.compY == 0) {
			// Toca por arriba
			return;
		}
		if (map.piece.getBottom() + move.compY == map.height) {
			// Toca por abajo
			return;
		}

		// Se calcula la nueva posicion
		Vector readjust = null;
		while (true) {
			readjust = map.piece.check(map.bitmap, actualWorld.WALL.getColor(),
					move);
			if (readjust.compX == 0 && readjust.compY == 0) {
				break;
			} else {
				crashBounds = true;
				move.compX -= readjust.compX;
				move.compY -= readjust.compY;
			}
		}

		if (move.compX == 0 && move.compY == 0) {// Si no se ha movido
			return;
		}

		// Se borra de su antigua posicion
		canvas.drawRect(map.piece.getLeft(), map.piece.getTop(),
				map.piece.getRight(), map.piece.getBottom(), actualWorld.ROAD);

		map.piece.move(move);
		// Se pinta en su nueva posicion
		canvas.drawRect(map.piece.getLeft(), map.piece.getTop(),
				map.piece.getRight(), map.piece.getBottom(), actualWorld.ROAD);

		if (crashBounds) {
			vibrator.vibrate(vibseq, 1);
			crashBounds = false;
		}
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

	int count = 0;

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (moving) {
			count++;
			if (count % 100 != 1) {
				return;
			}
			Log.d(DEBUG, "Invalidating");
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
		Log.d(DEBUG, "Receiving");
		if (resultCode == Network.RESULT_OK) {
			if (requestCode == GameControl.REQUEST_MAP) {
				Log.d(DEBUG, "Setting");
				map = (Map) result;
				setBackground(new BitmapDrawable(map.bitmap));
				invalidate();
				moving = true;
			}
		}

	}

}
