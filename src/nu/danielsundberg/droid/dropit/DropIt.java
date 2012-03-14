package nu.danielsundberg.droid.dropit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.EditText;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class DropIt extends AndroidApplication {
	
	private DropItEngine engine;
	private PowerManager.WakeLock wl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
		Intent intent = getIntent();
		Log.d("Intent", "action:"+intent.getAction()+" data:"+intent.getDataString());
		engine = new DropItEngine(this);
		initialize(engine,false);
		AlertDialog alert = new AlertDialog.Builder(this).create();
		final EditText input = new EditText(this);
		input.setText(Intent.ACTION_SEND.equals(intent.getAction())?
				intent.getExtras().get(Intent.EXTRA_STREAM)!=null?
						intent.getExtras().get(Intent.EXTRA_STREAM).toString():
							intent.getExtras().get(Intent.EXTRA_TEXT).toString():
			"http://www.danielsundberg.nu/images/dlogosmall.jpg");
		alert.setView(input);
		alert.setIcon(getResources().getDrawable(R.drawable.icon));
		alert.setMessage("Enter url and DropIT");
		alert.setButton("Drop!", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString().trim();
				engine.setUrlToLoad(value);
			}
		});
		alert.show();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		wl.release();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		wl.acquire();
	}
	
	

}