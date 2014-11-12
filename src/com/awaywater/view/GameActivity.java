package com.awaywater.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.awaywater.R;

public class GameActivity extends Activity {

	private MazeView imageview;
	
	private TextView consoleUp, consoleDown;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_game);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		consoleUp = (TextView) findViewById(R.id.textView1);
		consoleDown = (TextView) findViewById(R.id.textView2);
		
		
		imageview = (MazeView) findViewById(R.id.maze);
		imageview.initialize(this, consoleUp, consoleDown);
		
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	//	control.getMaze().restart(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
//		control.getMaze().pause(this);
	}

}
