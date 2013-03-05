package hu.promarkvf.besttest;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class KategoriakActivity extends ListActivity {

	List<Kategoria> kategoriaList = new ArrayList<Kategoria>();
	Kategoria kategoria = new Kategoria();
	Boolean ujkat = Boolean.FALSE;
	AdapterView.AdapterContextMenuInfo info;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		kategoriaList = dbHelper.getAllKategoria();
		setListAdapter(new KategoriaArrayAdapter(this, kategoriaList));

		// Feliratkozás a hosszú lenyomás hatására előjövő menü kezelésére
		registerForContextMenu(getListView());
		if (kategoriaList.size() == 0) {
			ujkat = Boolean.TRUE;
			DataPreferences.kategoria = null;
//			DataPreferences.kep = null;
			DataPreferences.kerdes = null;
			Intent myIntent = new Intent();
			myIntent.setClass(KategoriakActivity.this, KategoriaAddActivity.class);
			startActivity(myIntent);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (DataPreferences.kategoria != null) {
			if (ujkat) {
				// Új
				long lastID = 0;
				DatabaseHelper dbHelper = new DatabaseHelper(this);
				lastID = dbHelper.AddKategoria(DataPreferences.kategoria);
				DataPreferences.kategoria.set_id((int) lastID);
				((KategoriaArrayAdapter) getListAdapter()).addItem(DataPreferences.kategoria);
				DataPreferences.kategoria = null;
//				DataPreferences.kep = null;
				DataPreferences.kerdes = null;
				((KategoriaArrayAdapter) getListAdapter()).notifyDataSetChanged();
			} else {
				// Módosítás
				DatabaseHelper dbHelper = new DatabaseHelper(this);
				dbHelper.UpdateKategoria(DataPreferences.kategoria);
				((KategoriaArrayAdapter) getListAdapter()).modifyRow(info.position, DataPreferences.kategoria);
				DataPreferences.kategoria = null;
//				DataPreferences.kep = null;
				DataPreferences.kerdes = null;
				((KategoriaArrayAdapter) getListAdapter()).notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v.equals(getListView())) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle(((Kategoria) getListAdapter().getItem(info.position)).get_nev());
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
			Kategoria kat = (Kategoria) getListAdapter().getItem(info.position);
			dbHelper.DeleteKategoria(kat);
			((KategoriaArrayAdapter) getListAdapter()).deleteRow((Kategoria) getListAdapter().getItem(info.position));
			((KategoriaArrayAdapter) getListAdapter()).notifyDataSetChanged();
		}
			break;
		// új
		case 1: {
			ujkat = Boolean.TRUE;
			DataPreferences.kategoria = null;
//			DataPreferences.kep = null;
			DataPreferences.kerdes = null;
			Intent myIntent = new Intent();
			myIntent.setClass(KategoriakActivity.this, KategoriaAddActivity.class);
			startActivity(myIntent);
		}
			break;
		// módosítás
		case 2: {
			ujkat = Boolean.FALSE;
			DataPreferences.kategoria = (Kategoria) getListAdapter().getItem(info.position);
			Intent myIntent = new Intent();
			myIntent.setClass(KategoriakActivity.this, KategoriaAddActivity.class);
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
		Kategoria selectedValue = (Kategoria) getListAdapter().getItem(position);
		Toast.makeText(this, selectedValue.get_nev(), Toast.LENGTH_SHORT).show();

	}

}
