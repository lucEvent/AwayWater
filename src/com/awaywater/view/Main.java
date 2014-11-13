package com.awaywater.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.awaywater.R;
import com.awaywater.io.Settings;

public class Main extends Activity {

	private static final String DEBUG = "##Main##";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_main);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void startGame(View v) {
		startActivity(new Intent(this, GameActivity.class));
	}

}
