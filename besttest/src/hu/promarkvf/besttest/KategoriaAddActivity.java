package hu.promarkvf.besttest;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class KategoriaAddActivity extends Activity {
	private static final int DATE_DUE_DIALOG_ID = 1;
	private EditText editKatNev;
	private EditText editKatSzulo;
	private Boolean ujkat = Boolean.FALSE;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kategoria_add);
		
        editKatNev = (EditText)this.findViewById(R.id.katNev);
        editKatSzulo = (EditText)this.findViewById(R.id.katSzulo);
		Button btnOk = (Button)findViewById(R.id.btnUjkat);
		btnOk.setText(R.string.btnUjkat);

		ujkat = Boolean.TRUE;
		setTitle(R.string.title_activity_kategoria_add);
		if(DataPreferences.kategoria != null) {
    		editKatNev.setText(DataPreferences.kategoria.get_nev());
    		editKatSzulo.setText(String.valueOf(DataPreferences.kategoria.get_szulo()));
    		btnOk.setText(R.string.btnModkat);
    		ujkat = Boolean.FALSE;
    		setTitle(R.string.title_activity_kategoria_mod);
    	}
        
		btnOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int szulo;
				try {
					szulo = Integer.parseInt(editKatSzulo.getText().toString());
				} catch (NumberFormatException e) {
					szulo = 0;
				}
				if (ujkat) {
					DataPreferences.kategoria = new Kategoria(
							0,
							editKatNev.getText().toString(),
							szulo
					);
				} 
				else {
					DataPreferences.kategoria.set_nev(editKatNev.getText().toString());
					DataPreferences.kategoria.set_szulo(szulo);
				}
		
				finish();
			}
		});
		
		Button btnCancel = (Button)findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DataPreferences.kategoria = null;
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kategoria_add, menu);
		return true;
	}

}
