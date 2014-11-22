package com.awaywater.harware;

import android.content.Context;

public class Vibrator {

	private android.os.Vibrator vibrator;

	public Vibrator(Context context) {
		vibrator = (android.os.Vibrator) context
				.getSystemService(context.VIBRATOR_SERVICE);
	}

	public void vibrate(long[] pattern, int times) {
		vibrator.vibrate(pattern, times);
	}

}
