package com.awaywater.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.awaywater.io.Settings;
import com.awaywater.kernel.GameControl;
import com.awaywater.kernel.World;
import com.awaywater.kernel.WorldFactory;
import com.awaywater.net.Network;

public class MazeView extends ImageView implements OnTouchListener,
		SensorEventListener, Network {

	private static final String DEBUG = "##MazeView##";

	public World actualWorld;
	public boolean moving;

	private boolean crashBounds;

	private GameControl control;

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

	private long[] vibseq = { 150, 150 };

	private float accX = 0;
	private float accY = 0;

	@Override
	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);

		int lf, tp, rg, bt;

		Log.d(DEBUG, "xAcc: " + accX + "  yAcc: " + accY);
		lf = left - (int) (accX + 0.5);
		tp = top + (int) (accY + 0.5);
		rg = right - (int) (accX + 0.5);
		bt = bottom + (int) (accY + 0.5);

		int color = actualWorld.WALL.getColor();

		// Comprovem que el limit de la bola no toca pared (per coordenades)
		if (lf < 0) {
			Log.d(DEBUG, "Tocando banda izquierda");
			return;
		}
		if (rg >= getWidth()) {
			// Canvi de mazeview i level
			Log.d(DEBUG, "Tocando banda derecha");
			return;
		}

		// debug
		deb = false;
		// if (!deb) return;
		if (lf - 2 >= 0 && deb) {
			deb = false;
			for (int i = tp - 5; i < bt + 5; ++i) {
				for (int j = lf - 2; j < rg + 2; ++j) {
					char c;
					if (actualWorld.ROAD.getColor() == background.getPixel(j, i)) {
						c = 'R';
					} else if (actualWorld.WALL.getColor() == background
							.getPixel(j, i)) {
						c = 'W';
					} else if (actualWorld.NOTREACHABLE.getColor() == background
							.getPixel(j, i)) {
						c = 'N';
					} else {
						c = '*';
					}
					System.out.print(" " + c);
				}
				System.out.println(" ");
			}
		}
		// Cheching
		if (lf == left && top == tp) {
			Log.d(DEBUG, "Se queda en el checking");
			return;
		}
		Log.d(DEBUG, "l:" + lf + "  t:" + tp + "  rg:" + rg + "  bt:" + bt);
		Log.d(DEBUG, "left:" + left + "  top:" + top + "  right:" + right
				+ "  bottom:" + bottom);
		if (lf < left) { // Check left
			Log.d(DEBUG, "Looking left");
			for (int i = tp; i <= bt; ++i) {
				if (background.getPixel(lf, i) == color) {
					Log.d(DEBUG, "Breaking on x:" + lf + "  y:" + i);
					lf = left;
					rg = right;
					break;
				}
			}
		} else { // Check right
			Log.d(DEBUG, "Looking right");
			for (int i = tp; i <= bt; ++i) {
				if (background.getPixel(rg, i) == color) {
					Log.d(DEBUG, "Breaking on x:" + rg + "  y:" + i);
					lf = left;
					rg = right;
					break;
				}
			}
		}
		if (tp < top) {
			// Check top
			Log.d(DEBUG, "Looking TOP");
			for (int i = lf; i <= rg; ++i) {
				if (background.getPixel(i, tp) == color) {
					tp = top;
					bt = bottom;
					break;
				}
			}
		} else { // Check bottom
			Log.d(DEBUG, "Looking BOTTOM");
			for (int i = lf; i <= rg; ++i) {
				if (background.getPixel(i, bt) == color) {
					tp = top;
					bt = bottom;
					break;
				}
			}
		}
		Log.d(DEBUG, "l:" + lf + "  t:" + tp + "  rg:" + rg + "  bt:" + bt);
		Log.d(DEBUG, "left:" + left + "  top:" + top + "  right:" + right
				+ "  bottom:" + bottom);
		Log.d(DEBUG, "###################################################");
		if (lf == left && top == tp) {
			Log.d(DEBUG, "Acabando en el segundo checking");
			return;
		}

		canvas.drawRect(left, top, right, bottom, actualWorld.ROAD);
		left = lf;
		top = tp;
		right = rg;
		bottom = bt;
		canvas.drawRect(left, top, right, bottom, WorldFactory.BALL);

		if (crashBounds) {
			Settings.vibrator.vibrate(vibseq, 1);
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
			if (count%100 != 1) {
				return;
			}
			accX = event.values[0];
			accY = event.values[1];
			invalidate();
		}
	}

	boolean deb = true;

	int left, top, right, bottom;

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

	private Bitmap background;

	@Override
	public void message(int resultCode, int requestCode, final Object result) {
		Log.d(DEBUG, "Receiving");
		if (resultCode == Network.RESULT_OK) {
			if (requestCode == GameControl.REQUEST_MAP) {
				Log.d(DEBUG, "Setting");
				background = (Bitmap) result;
				setBackground(new BitmapDrawable((Bitmap) result));
				invalidate();
				moving = true;
			}
		}

	}

}
