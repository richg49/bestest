package hu.promarkvf.besttest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		final Button btnn_k = (Button) findViewById(R.id.button1);
		final Button btnk_n = (Button) findViewById(R.id.button2);
		final Button btnjb = (Button) findViewById(R.id.button3);
		final Button btnlist = (Button) findViewById(R.id.button4);
		final Button btnbeall = (Button) findViewById(R.id.button5);
		final Button btnexit = (Button) findViewById(R.id.button6);

		// --- Kezeles inditas
		btnbeall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent myIntent =new Intent();
				myIntent.setClass(MainActivity.this, BeallitasActivity.class);
				myIntent.putExtra("MyValue", "OK");
				startActivity(myIntent);
			}
		});

		// --- Kilépés
		btnexit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	Handler mHideHandler = new Handler();

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		loadPref();
	}

	private void loadPref(){
		SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
//		JSONUlr = mySharedPreferences.getString("@string/prefJSONUlr","http://10.1.6.11/animiwebservice/");
//		login_user = mySharedPreferences.getString("Azonosito","");
	}
}
