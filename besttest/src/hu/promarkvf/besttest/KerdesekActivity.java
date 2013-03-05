package hu.promarkvf.besttest;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class KerdesekActivity extends ListActivity {

	List<Kerdes> kerdesList = new ArrayList<Kerdes>();
//	Kerdes kerdes = new Kerdes();
	Boolean ujkerdes = Boolean.FALSE;
	AdapterView.AdapterContextMenuInfo info;
	int ret = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		kerdesList = dbHelper.getAllKerdes();
		setListAdapter(new KerdesArrayAdapter(this, kerdesList));

		// Feliratkozás a hosszú lenyomás hatására előjövő menü kezelésére
		registerForContextMenu(getListView());
		if ( kerdesList.size() == 0 ) {
			ujkerdes = Boolean.TRUE;
			DataPreferences.kategoria = null;
			// DataPreferences.kep = null;
			DataPreferences.kerdes = null;
			Intent myIntent = new Intent();
			myIntent.setClass(KerdesekActivity.this, KerdesekAddActivity.class);
			startActivity(myIntent);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if ( DataPreferences.kerdes != null && ( info != null || ujkerdes ) ) {
			DatabaseHelper dbHelper = new DatabaseHelper(this);
			if ( ujkerdes ) {
				// Új
				long lastID = 0;
				lastID = dbHelper.AddKerdes(DataPreferences.kerdes);
				DataPreferences.kerdes.set_id((int) lastID);
				( (KerdesArrayAdapter) getListAdapter() ).addItem(DataPreferences.kerdes);
			}
			else {
				// Módosítás
				ret = dbHelper.UpdateKerdes(DataPreferences.kerdes);
				if ( ret > 0 ) {
					if ( info != null ) {
						( (KerdesArrayAdapter) getListAdapter() ).modifyRow(info.position, DataPreferences.kerdes);
					}
				}
				else {
					Toast.makeText(this, getString(R.string.DBError_1) + String.valueOf(ret), Toast.LENGTH_SHORT).show();
				}
			}
			DataPreferences.kategoria = null;
			// DataPreferences.kep = null;
			DataPreferences.kerdes = null;
			( (KerdesArrayAdapter) getListAdapter() ).notifyDataSetChanged();
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if ( v.equals(getListView()) ) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle( ( (Kerdes) getListAdapter().getItem(info.position) ).get_kerdes());
			String[] menuItems = getResources().getStringArray(R.array.katmenu);
			for ( int i = 0; i < menuItems.length; i++ ) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int menuItemIndex = item.getItemId();
		AlertDialog.Builder alertDialogBuilder;
		AlertDialog alertDialog;

		switch ( menuItemIndex ) {
		// törlés
		case 0: {
			alertDialogBuilder = new AlertDialog.Builder(KerdesekActivity.this);
			alertDialogBuilder.setTitle( (CharSequence) ((Kerdes) getListAdapter().getItem(info.position) ).get_kerdes());
			alertDialogBuilder.setMessage(getString(R.string.kerdesQ));
			alertDialogBuilder.setCancelable(false);
			alertDialogBuilder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					DatabaseHelper dbHelper = new DatabaseHelper(KerdesekActivity.this);
					Kerdes kat = (Kerdes) getListAdapter().getItem(info.position);
					dbHelper.DeleteKerdes(kat);
					( (KerdesArrayAdapter) getListAdapter() ).deleteRow((Kerdes) getListAdapter().getItem(info.position));
					( (KerdesArrayAdapter) getListAdapter() ).notifyDataSetChanged();
				}
			});
			alertDialogBuilder.setNegativeButton(getString(R.string.no), null);
			alertDialog = alertDialogBuilder.create();
			alertDialog.show();
	}
			break;
		// új
		case 1: {
			ujkerdes = Boolean.TRUE;
			DataPreferences.kategoria = null;
			// DataPreferences.kep = null;
			DataPreferences.kerdes = null;
			Intent myIntent = new Intent();
			myIntent.setClass(KerdesekActivity.this, KerdesekAddActivity.class);
			startActivity(myIntent);
		}
			break;
		// módosítás
		case 2: {
			ujkerdes = Boolean.FALSE;
			DataPreferences.kerdes = (Kerdes) getListAdapter().getItem(info.position);
			Intent myIntent = new Intent();
			myIntent.setClass(KerdesekActivity.this, KerdesekAddActivity.class);
			startActivity(myIntent);
		}
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// get selected items
		Kerdes selectedValue = (Kerdes) getListAdapter().getItem(position);
		Toast.makeText(this, selectedValue.get_kerdes(), Toast.LENGTH_SHORT).show();
	}

}
