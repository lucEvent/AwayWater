package com.awaywater.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.awaywater.R;

public class Main extends Activity {


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
