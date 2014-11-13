package com.awaywater.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.awaywater.R;
import com.awaywater.kernel.GameControl;

public class GameActivity extends Activity {

	private static final String DEBUG = "##GameActivity##";

	private MazeView imageview;
	private GameControl gamecontrol;
	
	private TextView consoleUp, consoleDown;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.a_game);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		consoleUp = (TextView) findViewById(R.id.textView1);
		consoleDown = (TextView) findViewById(R.id.textView2);
		
		imageview = (MazeView) findViewById(R.id.maze);

		gamecontrol = new GameControl(imageview);
		imageview.setControl(gamecontrol);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		imageview.restart(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		imageview.pause(this);
	}

}
