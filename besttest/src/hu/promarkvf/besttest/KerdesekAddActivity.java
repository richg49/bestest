package hu.promarkvf.besttest;

import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class KerdesekAddActivity extends Activity {
	private static final int DATE_DUE_DIALOG_ID = 1;
	private EditText editkerdes;
	private EditText editleiras1;
	private EditText editleiras2;
	private Bitmap editkep;
	private Boolean ujkerdes = Boolean.FALSE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kerdesek_add);

        editkerdes = (EditText)this.findViewById(R.id.ujkerdes);
        editleiras1 = (EditText)this.findViewById(R.id.ujkerdesleiras1);
        editleiras2 = (EditText)this.findViewById(R.id.ujkerdesleiras2);
		Button btnOk = (Button)findViewById(R.id.btnUjkerdes);
		btnOk.setText(R.string.btnUjkerdes);

		ujkerdes = Boolean.TRUE;
		setTitle(R.string.title_activity_kerdesek_add);
		if(DataPreferences.kerdes != null) {
    		editkerdes.setText(DataPreferences.kerdes.get_kerdes());
    		editleiras1.setText(DataPreferences.kerdes.get_leiras1());
    		editleiras2.setText(DataPreferences.kerdes.get_leiras2());
//    		editkep.copyPixelsFromBuffer(DataPreferences.kerdes.get_kep());
    		btnOk.setText(R.string.btnModkat);
    		ujkerdes = Boolean.FALSE;
    		setTitle(R.string.title_activity_kategoria_mod);
    	}
        
		btnOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (ujkerdes) {
					DataPreferences.kerdes = new Kerdes(
							0,
							editkerdes.getText().toString(),
							null,
							editleiras1.getText().toString(),
							editleiras2.getText().toString()
					);
				} 
				else {
					DataPreferences.kerdes.set_kerdes(editkerdes.getText().toString());
					DataPreferences.kerdes.set_leiras1(editleiras1.getText().toString());
					DataPreferences.kerdes.set_leiras2(editleiras2.getText().toString());
				}
				finish();
			}
		});
		
		Button btnCancel = (Button)findViewById(R.id.btnCancelkerdes);
		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DataPreferences.kerdes = null;
				finish();
			}
		});

		Button btnCamera = (Button)findViewById(R.id.btnUjkepFoto);
		btnCamera.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent();
				myIntent.setClass(KerdesekAddActivity.this, CameraActivity.class);
				startActivity(myIntent);
			}
		});
	}

}
