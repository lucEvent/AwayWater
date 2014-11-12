package com.awaywater.view;

import com.awaywater.io.Settings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class AppAccelerometer extends LinearLayout implements
		SensorEventListener, OnSeekBarChangeListener {

	private static final int WRAPC = LayoutParams.WRAP_CONTENT;
	private static final int MATCHP = LayoutParams.MATCH_PARENT;

	private Maze image;
	private SeekBar barR, barG, barB, barS;
	private TextView numR, numG, numB, numS;
	private TextView textView;

	public AppAccelerometer(Context context) {
		super(context);
		// Atributos del layout Padre(this)
		this.setOrientation(LinearLayout.VERTICAL);

		checkAccelerometer(context);

		// Imagen
		image = new Maze(context, textView);
		image.setLayoutParams(new LayoutParams(MATCHP, WRAPC, 1));
		// Falta decir qué será la imagen
		// image.setBackgroundColor(Color.BLUE);
		this.addView(image, 0);

		// Layout de settings
		LinearLayout settings = new LinearLayout(context);
		settings.setOrientation(LinearLayout.VERTICAL);
		settings.setLayoutParams(new LayoutParams(MATCHP, WRAPC));
		// Adding R G B S
		LinearLayout barraR = new LinearLayout(context);
		LinearLayout barraG = new LinearLayout(context);
		LinearLayout barraB = new LinearLayout(context);
		LinearLayout barraS = new LinearLayout(context);
		barraR.setOrientation(LinearLayout.HORIZONTAL);
		barraG.setOrientation(LinearLayout.HORIZONTAL);
		barraB.setOrientation(LinearLayout.HORIZONTAL);
		barraS.setOrientation(LinearLayout.HORIZONTAL);
		TextView colR = new TextView(context);
		TextView colG = new TextView(context);
		TextView colB = new TextView(context);
		TextView colS = new TextView(context);
		LayoutParams pars1 = new LayoutParams(WRAPC, WRAPC, 1);
		colR.setLayoutParams(pars1);
		colG.setLayoutParams(pars1);
		colB.setLayoutParams(pars1);
		colS.setLayoutParams(pars1);
		colR.setText("R");
		colG.setText("G");
		colB.setText("B");
		colS.setText("S");

		barR = new SeekBar(context);
		barG = new SeekBar(context);
		barB = new SeekBar(context);
		barS = new SeekBar(context);
		LayoutParams pars2 = new LayoutParams(WRAPC, WRAPC, 8);
		barR.setLayoutParams(pars2);
		barG.setLayoutParams(pars2);
		barB.setLayoutParams(pars2);
		barS.setLayoutParams(pars2);
		barR.setMax(255);
		barG.setMax(255);
		barB.setMax(255);
		barS.setMax(100);
		barR.setProgress(0);
		barG.setProgress(0);
		barB.setProgress(0);
		barS.setProgress(20);
		barR.setOnSeekBarChangeListener(this);
		barG.setOnSeekBarChangeListener(this);
		barB.setOnSeekBarChangeListener(this);
		barS.setOnSeekBarChangeListener(this);

		numR = new TextView(context);
		numG = new TextView(context);
		numB = new TextView(context);
		numS = new TextView(context);
		numR.setLayoutParams(pars1);
		numG.setLayoutParams(pars1);
		numB.setLayoutParams(pars1);
		numS.setLayoutParams(pars1);
		numR.setText("0");
		numG.setText("0");
		numB.setText("0");
		numS.setText("20");

		barraR.addView(colR, 0);
		barraR.addView(barR, 1);
		barraR.addView(numR, 2);
		barraG.addView(colG, 0);
		barraG.addView(barG, 1);
		barraG.addView(numG, 2);
		barraB.addView(colB, 0);
		barraB.addView(barB, 1);
		barraB.addView(numB, 2);
		barraS.addView(colS, 0);
		barraS.addView(barS, 1);
		barraS.addView(numS, 2);

		settings.addView(barraR, 0);
		settings.addView(barraG, 1);
		settings.addView(barraB, 2);
		settings.addView(barraS, 3);

		this.addView(settings, 1);
		this.addView(textView, 2);
	}

	private void checkAccelerometer(Context context) {
		textView = new TextView(context);
		SensorManager manager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() == 0) {
			textView.setText("No hay un acelerómetro instalado");
		} else {
			Sensor accelerometer = manager.getSensorList(
					Sensor.TYPE_ACCELEROMETER).get(0);
			if (!manager.registerListener(this, accelerometer,
					SensorManager.SENSOR_DELAY_GAME)) {
				textView.setText("No se ha podido registrar el sensor listener");
			} else {
				textView.setText("SI se ha podido registrar el sensor listener");
			}
		}
	}

	// ////////////////////////////////////////////////////////
	// ////////////////**METODOS SEEKBAR**/////////////////////
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (seekBar == barR) {
			image.changeRed(progress);
			numR.setText("" + progress);
		} else if (seekBar == barG) {
			image.changeGreen(progress);
			numG.setText("" + progress);
		} else if (seekBar == barB) {
			image.changeBlue(progress);
			numB.setText("" + progress);
		} else if (seekBar == barS) {
			image.changeSize(progress);
			numS.setText("" + progress);
		}

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	// //////////////////////////////////////////////////////

	// ////////////////**METODOS ACCELEROMETRO**//////////////
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// Para cuando cambia la precision del accelerometro

	}

	@Override
	public void onSensorChanged(SensorEvent se) {
		image.movePosition(se.values[0], se.values[1]);
	}

	// ///////////////////////////////////////////////////////

	// Clase que pintara la pantalla
	private class Maze extends ImageView {

		private int r, g, b;
		private float x, y, xAnt, yAnt;
		private int radix;

		private int width, height;

		private Paint color;

		private TextView tv;

		Maze(Context context, TextView tv) {
			super(context);
			this.tv = tv;
			width = context.getResources().getDisplayMetrics().widthPixels;
			height = this.getBottom();

			x = width / 2;
			y = x;
			xAnt = x;
			yAnt = y;
			r = 0;
			g = 0;
			b = 0;
			radix = 20;

			color = new Paint();
			color.setStyle(Paint.Style.FILL_AND_STROKE);
			;
			color.setARGB(250, r, g, b);

		}

		private long[] vibseq = { 250, 150 };

		@Override
		protected void onDraw(Canvas can) {
			super.onDraw(can);
			can.drawCircle(x, y, radix, color);
			if (touchBoundsX) {
				if (!crashBoundsX) {
					Settings.vibrator.vibrate(vibseq, 1);
					crashBoundsX = true;
				}
			} else {
				crashBoundsX = false;
			}
			if (touchBoundsY) {
				if (!crashBoundsY) {
					Settings.vibrator.vibrate(vibseq, 1);
					crashBoundsY = true;
				}
			} else {
				crashBoundsY = false;
			}
		}

		public void changeRed(int red) {
			r = red;
			color.setARGB(250, r, g, b);
		}

		public void changeGreen(int green) {
			g = green;
			color.setARGB(250, r, g, b);
		}

		public void changeBlue(int blue) {
			b = blue;
			color.setARGB(250, r, g, b);
		}

		public void changeSize(int size) {
			radix = size;
		}

		public void movePosition(float posx, float posy) {
			x = crashBoundsX(x - posx, radix, width - radix);
			y = crashBoundsY(y + posy, radix, this.getBottom() - radix);
			invalidate();
		}

		private boolean touchBoundsX = false;
		private boolean touchBoundsY = false;
		private boolean crashBoundsX = false;
		private boolean crashBoundsY = false;

		private float crashBoundsX(float pos, int limitUp, int limitDown) {
			if (pos < limitUp) {
				touchBoundsX = true;
				return limitUp;
			} else if (pos > limitDown) {
				touchBoundsX = true;
				return limitDown;
			}
			touchBoundsX = false;
			return pos;
		}

		private float crashBoundsY(float pos, int limitUp, int limitDown) {
			if (pos < limitUp) {
				touchBoundsY = true;
				return limitUp;
			} else if (pos > limitDown) {
				touchBoundsY = true;
				return limitDown;
			}
			touchBoundsY = false;
			return pos;
		}
	}

}
