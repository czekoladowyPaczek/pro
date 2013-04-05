package eu.boiled.chainreaction;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.google.analytics.tracking.android.EasyTracker;

import eu.boiled.chainreaction.async.DataRequestFactory;
import eu.boiled.chainreaction.async.WebAsync;
import eu.boiled.chainreaction.async.WebHelper;
import eu.boiled.chainreaction.helpers.UserData;
import eu.boiled.chainreaction.surfaceview.GLRenderer;
import eu.boiled.chainreaction.surfaceview.GameLogic;
import eu.boiled.chainreaction.surfaceview.MyGlSurfaceView;

public class GameActivity extends Activity{
	public static final String LOG = "GameActivity";
	public static final String GAME_LEVEL = "eu.boiled.GameActivity.gamelevel";
	private MyGlSurfaceView gameSurface;
	private GLRenderer mRenderer;
	private GameLogic logic;
	private int currentLevel;
	private Handler gameHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888); 
		setContentView(R.layout.activity_game);
		gameSurface = (MyGlSurfaceView) findViewById(R.id.gameSurface);
		mRenderer = new GLRenderer();
		logic = new GameLogic(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
		currentLevel = 1;
		logic.setLevel(1);
		mRenderer.setGameLogic(logic);
		gameSurface.setRenderer(mRenderer);
		gameHandler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				Dialog dialog = new Dialog(GameActivity.this);
				switch(msg.what){
				case 1:
					gameSurface.onPause();
					dialog.setTitle("przegrales");
					dialog.show();
					logic.setLevel(1);
					gameSurface.onResume();
					break;
				case 2:
					gameSurface.onPause();
					UserData.getInstance().saveUserPoints(msg.arg1);
					if(WebHelper.isOnline(GameActivity.this)){
						new WebAsync().execute(DataRequestFactory.sendScore());
					}
					dialog.setTitle("nowy lewel");
					dialog.show();
					logic.setLevel(++currentLevel);
					gameSurface.onResume();
					break;
				}
			}
		};
		logic.setHandler(gameHandler);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		opts.inDither = false;
		opts.inScaled = false;
		logic.setBackground(BitmapFactory.decodeResource(getResources(), R.drawable.back4, opts));
		
	}

	@Override
	protected void onPause() {
		Log.e(LOG, "onPause");
		super.onPause();
	}
	
	@Override
	protected void onResume(){
		Log.e(LOG, "onResume");
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}

	public void pauseGame(View v){
		Log.e(LOG, "pauseGame view");
		pauseGame();
	}
	
	private void restartGame(){
		Log.e(LOG, "restartGame");
		logic.setLevel(currentLevel);
		gameSurface.onResume();
	}
	
	
	private void pauseGame(){
		Log.e(LOG, "pauseGame");
		gameSurface.onPause();
		showDialog();
	}
	
	
	private void resumeGame(){
		Log.e(LOG, "resumeGame");
		gameSurface.onResume();
	}
	
	private void exitGame(){
		Log.e(LOG, "exitGame");
		finish();
	}
	
	@Override
	public void onBackPressed() {
		Log.e(LOG, "onBackPressed");
		pauseGame();
	}
	
	private void showDialog(){
		Log.e(LOG, "onCreateDialog");
		final Dialog dialog = new Dialog(GameActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_pause);
		
		Button resume = (Button) dialog.findViewById(R.id.button_resume);
		resume.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		
		Button restart = (Button) dialog.findViewById(R.id.button_reastart);
		restart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				restartGame();
				dialog.dismiss();
			}
		});
		
		Button exit = (Button) dialog.findViewById(R.id.button_exit);
		exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				exitGame();
				
			}
		});

		dialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				resumeGame();
			}
		});
		dialog.show();
	}
}




