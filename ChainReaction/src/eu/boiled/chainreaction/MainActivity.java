package eu.boiled.chainreaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.Session;
import com.google.analytics.tracking.android.EasyTracker;

import eu.boiled.chainreaction.async.WebHelper;
import eu.boiled.chainreaction.helpers.FacebookExecutor;
import eu.boiled.chainreaction.helpers.UserData;

public class MainActivity extends Activity {
	public static final String LOG = "MainActivity";
	public static final String prefsName = "eu.boiled.ChainReactions.prefsNamezzzz";
	public static final String resumeLevel = "eu.boiled.ChainReactions.resumeLevel";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initializeStatics();
		Log.e(LOG, "onCreate");
	}
	
	private void initializeStatics(){
		UserData.initialize(getApplicationContext());
	}
	
	public void newGame(View view){
		Intent intent = new Intent(MainActivity.this, GameActivity.class);
		intent.putExtra(GameActivity.GAME_LEVEL, 0);
		startActivity(intent);
	}
	public void resumeGame(View view){
		Intent intent = new Intent(MainActivity.this, GameActivity.class);
		int level = getSharedPreferences(prefsName, 0).getInt(resumeLevel, 0);
		intent.putExtra(GameActivity.GAME_LEVEL, level);
		startActivity(intent);
	}
	public void showRank(View view){
		if(!WebHelper.isOnline(this)){
			createAlertDialog();
		} else{
			Intent intent = new Intent(MainActivity.this, RankActivity.class);
			startActivity(intent);
		}
	}
	
	private void exitApp(){
		finish();
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

	@Override
	protected void onPause() {
		Log.e(LOG, "onPause");
		super.onPause();
	}


	@Override
	protected void onResume() {
		Log.e(LOG, "onResume");
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		final Dialog dialog = new Dialog(MainActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_exit);
		
		Button exit = (Button) dialog.findViewById(R.id.button_exit);
		exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				exitApp();
			}
		});
		
		Button cancel = (Button) dialog.findViewById(R.id.button_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	public void fbInvite(View view){
		new FacebookExecutor(this).execute();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Session.DEFAULT_AUTHORIZE_ACTIVITY_CODE) {
			Session.getActiveSession().onActivityResult(this, requestCode,
					resultCode, data);
		}
	}
	

	private void createAlertDialog(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		
		alertDialog.setTitle(R.string.noInternetTitle);
		alertDialog.setMessage(getResources().getString(R.string.noInternetMessage));
		alertDialog.setIcon(R.drawable.fail);
		alertDialog.setPositiveButton(android.R.string.yes, new Dialog.OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
				
			}
		});
		alertDialog.setNegativeButton(android.R.string.no, new Dialog.OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Toast.makeText(MainActivity.this, R.string.noInternetTitle, Toast.LENGTH_SHORT).show();
			}
		});
		
		AlertDialog dialog = alertDialog.create();
		dialog.show();
	}
	
	
}
