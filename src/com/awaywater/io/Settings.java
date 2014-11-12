package com.awaywater.io;

import android.content.Context;
import android.os.Vibrator;


public final class Settings {

	public static Vibrator vibrator;
	
	
	public Settings(Context context) {
		vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
	
	}

}
