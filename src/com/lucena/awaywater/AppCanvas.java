package com.lucena.awaywater;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.view.View.OnTouchListener;

public class AppCanvas {

	public class Vista extends LinearLayout implements OnSeekBarChangeListener {

		private static final int WRAPC = LayoutParams.WRAP_CONTENT;
		private static final int MATCHP = LayoutParams.MATCH_PARENT;

		private Maze image;
		private SeekBar barR, barG, barB, barS;
		private TextView numR, numG, numB, numS;
		private TextView textView;

		public Vista(Context context) {
			super(context);
			// Atributos del layout Padre(this)
			this.setOrientation(LinearLayout.VERTICAL);
			textView = new TextView(context);

			// Imagen
			image = new Maze(context);
			image.setLayoutParams(new LayoutParams(MATCHP, WRAPC, 1));
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
			barS.setProgress(10);
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
			numS.setText("10");

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

			textView.setLayoutParams(new LayoutParams(MATCHP, WRAPC));
			this.addView(textView, 2);
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
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
	}

	// Clase que pintara la pantalla
	final class Maze extends ImageView implements OnTouchListener {

		private int r, g, b, radix;
		float downx, downy, upx, upy;

		private Bitmap bitmap;
		private Canvas canvas;
		private Paint color;

		Maze(Context context) {
			super(context);

			downx = 0;
			downy = 0;
			upx = 0;
			upy = 0;
			r = 0;
			g = 0;
			b = 0;
			radix = 10;

			color = new Paint();
			color.setStyle(Paint.Style.STROKE);
			color.setARGB(250, r, g, b);
			color.setStrokeWidth(radix);
			color.setStrokeJoin(Paint.Join.ROUND);
			color.setStrokeCap(Paint.Cap.ROUND);
		}
		
		private boolean start = true;
		
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			
			if (start) {
				int width = this.getWidth();
				int height = this.getHeight();
				
				bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);

				this.canvas = new Canvas(bitmap);
				
				Matrix matrix = new Matrix();
				this.canvas.drawBitmap(bitmap, matrix, color);

				this.setImageBitmap(bitmap);
				this.setOnTouchListener(this);		
				
				start = false;
			}
		}

		public void changeRed(int red) {
			r = red;
			color.setARGB(250, red, g, b);
		}

		public void changeGreen(int green) {
			g = green;
			color.setARGB(250, r, green, b);
		}

		public void changeBlue(int blue) {
			b = blue;
			color.setARGB(250, r, g, blue);
		}

		public void changeSize(int size) {
			radix = size;
			color.setStrokeWidth(size);
		}

		// //////////////////////////////////////////////////////
		// //////////////**METODOS ONTOUCH**/////////////////////
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int action = event.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				downx = event.getX();
				downy = event.getY();
				canvas.drawLine(downx, downy, downx+1, downy+1, color);
				break;
			case MotionEvent.ACTION_MOVE:
				upx = event.getX();
				upy = event.getY();
				canvas.drawLine(downx, downy, upx, upy, color);
				this.invalidate();
				downx = upx;
				downy = upy;
				break;
			case MotionEvent.ACTION_UP:
				upx = event.getX();
				upy = event.getY();
				canvas.drawLine(downx, downy, upx, upy, color);
				this.invalidate();
				break;
			}
			return true;
		}
		// //////////////////////////////////////////////////////

	}
}