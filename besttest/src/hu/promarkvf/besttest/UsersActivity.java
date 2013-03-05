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

public class UsersActivity extends ListActivity {
	private static final int NEW_USER_ACTIVITY_ID = 1;
	private static final int MOD_USER_ACTIVITY_ID = 2;

	List<User> userList = new ArrayList<User>();
	AdapterView.AdapterContextMenuInfo info;
	int ret = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DatabaseUser dbuser = new DatabaseUser(this);
		userList = dbuser.getAllUser();
		setListAdapter(new UserArrayAdapter(this, userList));

		// Feliratkozás a hosszú lenyomás hatására előjövő menü kezelésére
		registerForContextMenu(getListView());

		if ( userList.size() == 0 ) {
			Intent myIntent = new Intent();
			myIntent.setClass(UsersActivity.this, AddUserActivity.class);
			startActivity(myIntent);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if ( v.equals(getListView()) ) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle( ( (User) getListAdapter().getItem(info.position) ).getName());
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

		switch ( menuItemIndex ) {
		// törlés
		case 0: {
			DatabaseUser dbUser = new DatabaseUser(this);
			User user = (User) getListAdapter().getItem(info.position);
			dbUser.DeleteUser(user);
			( (UserArrayAdapter) getListAdapter() ).deleteRow((User) getListAdapter().getItem(info.position));
			( (UserArrayAdapter) getListAdapter() ).notifyDataSetChanged();
		}
			break;
		// új
		case 1: {
			User newuser = new User();
			Intent myIntent = new Intent();
			myIntent.setClass(UsersActivity.this, AddUserActivity.class);
			ParcelableUser parcelableUser = new ParcelableUser(newuser);
			myIntent.putExtra("user", parcelableUser);
			startActivityForResult(myIntent, NEW_USER_ACTIVITY_ID);
		}
			break;
		// módosítás
		case 2: {
			User moduser = (User) getListAdapter().getItem(info.position);
			Intent myIntent = new Intent();
			myIntent.setClass(UsersActivity.this, AddUserActivity.class);
			ParcelableUser parcelableUser = new ParcelableUser(moduser);
			myIntent.putExtra("user", parcelableUser);
			startActivityForResult(myIntent, MOD_USER_ACTIVITY_ID);
		}
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch ( requestCode ) {
		case NEW_USER_ACTIVITY_ID:
			if ( resultCode == RESULT_OK ) {
				ParcelableUser parcelableuser = (ParcelableUser) data.getParcelableExtra("user");
				User user = parcelableuser.getUser();
				DatabaseUser dbuser = new DatabaseUser(this);
				dbuser.AddUser(user);
				((UserArrayAdapter) getListAdapter()).add(user);
				((UserArrayAdapter) getListAdapter()).notifyDataSetChanged();
			}
			break;
		case MOD_USER_ACTIVITY_ID:
			if ( resultCode == RESULT_OK ) {
				ParcelableUser parcelableuser = (ParcelableUser) data.getParcelableExtra("user");
				User user = parcelableuser.getUser();
				DatabaseUser dbuser = new DatabaseUser(this);
				dbuser.UpdateUser(user);
				((UserArrayAdapter) getListAdapter()).modifyRow(info.position, user);
				((UserArrayAdapter) getListAdapter()).notifyDataSetChanged();
			}
			break;
		}

	}

}
