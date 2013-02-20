package hu.promarkvf.besttest;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class KerdesekActivity extends ListActivity {

	List<Kerdes> kerdesList = new ArrayList<Kerdes>();
	Kerdes kerdes = new Kerdes();
	Boolean ujkerdes = Boolean.FALSE;
	AdapterView.AdapterContextMenuInfo info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		kerdesList = dbHelper.getAllKerdes();
		setListAdapter(new KerdesArrayAdapter(this, kerdesList));

		// Feliratkozás a hosszú lenyomás hatására előjövő menü kezelésére
		registerForContextMenu(getListView());
		if(kerdesList.size() == 10) {
			ujkerdes = Boolean.TRUE;
			DataPreferences.kerdes = null;
			Intent myIntent = new Intent();
			myIntent.setClass(KerdesekActivity.this, KerdesekAddActivity.class);
			startActivity(myIntent);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (DataPreferences.kerdes != null) {
			if (ujkerdes) {
// Új
				long lastID = 0;
				DatabaseHelper dbHelper = new DatabaseHelper(this);
				lastID = dbHelper.AddKerdes(DataPreferences.kerdes);
				DataPreferences.kerdes.set_id((int) lastID);
				((KerdesArrayAdapter) getListAdapter()).addItem(DataPreferences.kerdes);
				DataPreferences.kerdes = null;
				((KerdesArrayAdapter) getListAdapter()).notifyDataSetChanged();
			}
			else {
// Módosítás
				DatabaseHelper dbHelper = new DatabaseHelper(this);
//				dbHelper.UpdateKerdes(DataPreferences.kerdes);
				((KerdesArrayAdapter) getListAdapter()).modifyRow(info.position, DataPreferences.kerdes);
				DataPreferences.kerdes = null;
				((KerdesArrayAdapter) getListAdapter()).notifyDataSetChanged();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kerdesek, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v.equals(getListView())) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle(((Kerdes)getListAdapter().getItem(info.position)).get_kerdes());
			String[] menuItems = getResources().getStringArray(R.array.katmenu);
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int menuItemIndex = item.getItemId();

		switch (menuItemIndex) {
// törlés
		case 0: {
				DatabaseHelper dbHelper = new DatabaseHelper(this);
				Kerdes kat = (Kerdes) getListAdapter().getItem(info.position);
//				dbHelper.DeleteKerdes(kat);
				((KerdesArrayAdapter)getListAdapter()).deleteRow((Kerdes)getListAdapter().getItem(info.position));
				((KerdesArrayAdapter)getListAdapter()).notifyDataSetChanged();
			}
			break;
// új
		case 1: {
			ujkerdes = Boolean.TRUE;
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
