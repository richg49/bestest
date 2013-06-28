package hu.promarkvf.besttest;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class UserSelActivity extends Activity implements AdapterView.OnItemSelectedListener {
	String[] items;
	DatabaseUser dbUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_sel);

		final Spinner spinneruser = (Spinner) findViewById(R.id.UserSelspinner);
		spinneruser.setOnItemSelectedListener(this);
		dbUser = new DatabaseUser(this);
		items = dbUser.getAllRname();
		ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);

		spinneruser.setAdapter(aa);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		dbUser = new DatabaseUser(this);
		MainActivity.sel_user = dbUser.getUserPos(position);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	public void onClick(View v) {
		finish();
	}
}
