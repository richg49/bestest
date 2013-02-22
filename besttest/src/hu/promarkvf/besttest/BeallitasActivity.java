package hu.promarkvf.besttest;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BeallitasActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_beallitas);

		final Button btnurit = (Button) findViewById(R.id.b_button1);
		final Button btnkategoriak = (Button) findViewById(R.id.b_button2);
		final Button btntesztek = (Button) findViewById(R.id.b_button3);
		final Button btnlista = (Button) findViewById(R.id.b_button4);
		final Button btntbeall = (Button) findViewById(R.id.b_button5);
		final Button btnexit = (Button) findViewById(R.id.b_button6);

		// --- Kategóriák módosítása
		btnkategoriak.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent myIntent = new Intent();
				myIntent.setClass(BeallitasActivity.this, KategoriakActivity.class);
				myIntent.putExtra("MyValue", "OK");
				startActivity(myIntent);
			}
		});

		// --- Kérdések módosítása
		btntesztek.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent myIntent = new Intent();
				myIntent.setClass(BeallitasActivity.this, KerdesekActivity.class);
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

}
