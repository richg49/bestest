package hu.promarkvf.besttest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class KerdesekAddActivity extends Activity {
	private static final int CAMERA_ACTIVITY_ID = 1;
	private static final int FILE_BROWSER_ACTIVITY_ID = 2;
	private EditText editkerdes;
	private EditText editleiras1;
	private EditText editleiras2;
	// private Bitmap editkep;
	private Boolean ujkerdes = Boolean.FALSE;
	private ImageView kPhoto;
	private List<Kategoria> kategorialist = new ArrayList<Kategoria>();
	private String[] kategoriak;
	private int[] kategoriaid;
	private boolean[] kategoriasel;
	private Bitmap theImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kerdesek_add);

		kPhoto = (ImageView) findViewById(R.id.imageViewkerdes);
		editkerdes = (EditText) this.findViewById(R.id.ujkerdes);
		editleiras1 = (EditText) this.findViewById(R.id.ujkerdesleiras1);
		editleiras2 = (EditText) this.findViewById(R.id.ujkerdesleiras2);
		Button btnOk = (Button) findViewById(R.id.btnUjkerdes);

		final MultiSelectSpinner spinnerkat = (MultiSelectSpinner) findViewById(R.id.spinnerkategoria);

		if ( DataPreferences.kerdes != null ) {
			editkerdes.setText(DataPreferences.kerdes.get_kerdes());
			editleiras1.setText(DataPreferences.kerdes.get_leiras1());
			editleiras2.setText(DataPreferences.kerdes.get_leiras2());
			btnOk.setText(R.string.btnModkat);
			theImage = DataPreferences.kerdes.get_kep_bitmap();
			if ( theImage != null ) {
				kPhoto.setImageBitmap(theImage);
				// DataPreferences.kep = theImage;
			}
			ujkerdes = Boolean.FALSE;
			setTitle(R.string.title_activity_kerdesek_mod);
			kategoriaid = new int[DataPreferences.kerdes.get_SelectedKategoriak().size()];
			kategoriak = new String[DataPreferences.kerdes.get_SelectedKategoriak().size()];
			kategoriasel = new boolean[DataPreferences.kerdes.get_SelectedKategoriak().size()];
			for ( int i = 0; i < DataPreferences.kerdes.get_SelectedKategoriak().size(); i++ ) {
				kategorialist.add(DataPreferences.kerdes.get_SelectedKategoriak().get(i).get_kat());
				kategoriaid[i] = ( (KategoriaSelected) DataPreferences.kerdes.get_SelectedKategoriak().get(i) ).get_id();
				kategoriak[i] = ( (KategoriaSelected) DataPreferences.kerdes.get_SelectedKategoriak().get(i) ).get_nev();
				kategoriasel[i] = ( (KategoriaSelected) DataPreferences.kerdes.get_SelectedKategoriak().get(i) ).get_selectedJel();
			}
			spinnerkat.setItems(kategoriak);
			spinnerkat.setSelectionB(kategoriasel);
		}
		else {
			ujkerdes = Boolean.TRUE;
			setTitle(R.string.title_activity_kerdesek_add);
			btnOk.setText(R.string.btnUjkerdes);
			DatabaseHelper dbHelper = new DatabaseHelper(this);
			kategorialist = dbHelper.getAllKategoria();
			kategoriaid = new int[kategorialist.size()];
			kategoriak = new String[kategorialist.size()];
			for ( int i = 0; i < kategorialist.size(); i++ ) {
				kategoriaid[i] = ( (Kategoria) kategorialist.get(i) ).get_id();
				kategoriak[i] = ( (Kategoria) kategorialist.get(i) ).get_nev();
			}
			spinnerkat.setItems(kategoriak);
		}

		// Rögzít
		btnOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if ( ujkerdes ) {
					DataPreferences.kerdes = new Kerdes(0, editkerdes.getText().toString(), null, editleiras1.getText().toString(), editleiras2.getText().toString());
					DataPreferences.kerdes.set_kep(theImage);
				}
				else {
					DataPreferences.kerdes.set_kerdes(editkerdes.getText().toString());
					DataPreferences.kerdes.set_leiras1(editleiras1.getText().toString());
					DataPreferences.kerdes.set_leiras2(editleiras2.getText().toString());
					DataPreferences.kerdes.set_kep(theImage);

				}
				kategoriasel = spinnerkat.getSelectedboolean();
				List<KategoriaSelected> katselList = new ArrayList<KategoriaSelected>();
				for ( int i = 0; i < kategorialist.size(); i++ ) {
					KategoriaSelected katsel = new KategoriaSelected(kategorialist.get(i), kategoriasel[i]);
					katselList.add(katsel);
				}
				DataPreferences.kerdes.set_SelectedKategoriak(katselList);

				finish();
			}
		});

		Button btnCancel = (Button) findViewById(R.id.btnCancelkerdes);
		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DataPreferences.kategoria = null;
				// DataPreferences.kep = null;
				DataPreferences.kerdes = null;
				finish();
			}
		});

		Button btnCamera = (Button) findViewById(R.id.btnUjkepFoto);
		btnCamera.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent();
				myIntent.setClass(KerdesekAddActivity.this, CameraActivity.class);
				// startActivity(myIntent);
				startActivityForResult(myIntent, CAMERA_ACTIVITY_ID);
			}
		});

		Button btnkepfeltolt = (Button) findViewById(R.id.btnUjkepFile);
		btnkepfeltolt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setAction(Intent.ACTION_GET_CONTENT);
				String imgMimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpg");
				intent.setType(imgMimeType);
				startActivityForResult(intent, FILE_BROWSER_ACTIVITY_ID);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// if ( DataPreferences.kep != null ) {
		// kPhoto.setImageBitmap(Bitmap.createScaledBitmap(DataPreferences.kep,
		// 150, 150, true));
		// }
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch ( requestCode ) {
		case FILE_BROWSER_ACTIVITY_ID:
			if ( resultCode == RESULT_OK ) {
				Uri currFileURI = data.getData();
				String srcPath = currFileURI.getPath();
				File afile = new File(srcPath);
				if(!afile.isFile()){
					srcPath = getPath(currFileURI);
					afile = new File(srcPath);
				}
				Bitmap myBitmap = BitmapFactory.decodeFile(afile.getAbsolutePath());
				if ( myBitmap != null ) {
					kPhoto.setImageBitmap(Bitmap.createScaledBitmap(myBitmap, 150, 150, true));
					theImage = Bitmap.createScaledBitmap(myBitmap, 300, 300, true);
				}
				else {
					Toast.makeText(this, getString(R.string.nemkepfile), Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case CAMERA_ACTIVITY_ID:
			if ( resultCode == RESULT_OK ) {
				Bitmap bp = (Bitmap) data.getExtras().get("bitmap");
				if ( bp != null ) {
					theImage = bp;
					kPhoto.setImageBitmap(Bitmap.createScaledBitmap(bp, 150, 150, true));
				}
			}
			break;
		}

	}

	private String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
}
