package hu.promarkvf.besttest;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class NevNevActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nev_nev);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nev_nev, menu);
		return true;
	}

}
