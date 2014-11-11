package com.lucena.awaywater;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	Settings settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = new Settings(this);
		
		AppAccelerometer vista = new AppAccelerometer(this);
		setContentView(vista);
	}

}
