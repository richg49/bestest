package hu.promarkvf.besttest;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UploadActivity extends Activity implements AdapterView.OnItemSelectedListener, OnClickListener {
	DatabaseUser dbUser;
	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);

		final Spinner spinneruser = (Spinner) findViewById(R.id.upload_user_spinner);

		spinneruser.setOnItemSelectedListener(this);
		dbUser = new DatabaseUser(this);
		String[] items = dbUser.getAllRname();
		ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);

		spinneruser.setAdapter(aa);
	}

	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		dbUser = new DatabaseUser(this);
		user = dbUser.getUserPos(position);
	}

	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public void onClick(View v) {
		final DatabaseHelper dbHelper = new DatabaseHelper(UploadActivity.this);
		String ret;

		switch ( v.getId() ) {
		case R.id.upload_button1:
			EditText editLeiras = (EditText) this.findViewById(R.id.upload_leiras_editText);

			Inforec info = new Inforec(null, user.getDbid(), user.getName(), user.getRname(), user.getKey(), editLeiras.getText().toString(), null);
			
			dbHelper.AddInfo(info);
			ret = dbHelper.DbUpload();
			Toast.makeText(this, ret, Toast.LENGTH_SHORT).show();

			break;

		// --- Kilépés
		case R.id.upload_button2:
			finish();
			break;

		default:
			break;
		}
		
	}
}
