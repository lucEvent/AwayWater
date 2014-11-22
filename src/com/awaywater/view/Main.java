package com.awaywater.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.awaywater.R;
import com.awaywater.io.Settings;

public class Main extends Activity {

	private static final String DEBUG = "##Main##";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.a_main);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void startGame(View v) {
		Intent intent = new Intent(this, GameActivity.class);
		View background = findViewById(R.id.background);
		intent.putExtra("width", background.getMeasuredWidth());
		intent.putExtra("height", background.getMeasuredHeight());
		startActivity(intent);
	}

}
