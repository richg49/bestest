package hu.promarkvf.besttest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class AddUserActivity extends Activity implements OnClickListener {
	EditText textName;
	EditText textRName;
	User user;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_user);

		findViewsById();

		intent = getIntent();

		ParcelableUser parcelableuser = (ParcelableUser) intent.getParcelableExtra("user");
		user = parcelableuser.getUser();
		textName.setText(user.getName());
		textRName.setText(user.getRname());

		if ( user.getName() == "" ) {
			setTitle(R.string.title_activity_add_user);
		}
		else {
			setTitle(R.string.title_activity_user_mod);
		}

	}

	@Override
	public void onClick(View v) {
		switch ( v.getId() ) {
		// --- Rögzít
		case R.id.btnadd_user_ok:
			user.setName(textName.getText().toString());
			user.setRname(textRName.getText().toString());
			ParcelableUser parcelableUser = new ParcelableUser(user);
			intent.putExtra("user", parcelableUser);
			setResult(RESULT_OK, intent);
			finish();
			break;

		// --- Mégsem
		case R.id.btnadd_user_cancel:
			setResult(RESULT_CANCELED);
			finish();
			break;

		default:
			break;
		}
	}

	private void findViewsById() {
		textName = (EditText) findViewById(R.id.add_user_name);
		textRName = (EditText) findViewById(R.id.add_user_rname);
	}
}
