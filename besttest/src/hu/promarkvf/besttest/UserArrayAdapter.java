package hu.promarkvf.besttest;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UserArrayAdapter extends ArrayAdapter<User> {
	private final Context context;
	private final List<User> values;

	public UserArrayAdapter(Context context, List<User> userList) {
		super(context, R.layout.activity_users, userList);
		this.context = context;
		this.values = userList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.activity_users, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.user);
		textView.setText(values.get(position).getRname() + "  "+values.get(position).getName());

		return rowView;
	}

	// beszúrás
	public void addItem(User user) {
		values.add(user);
	}

	public int getCount() {
		return values.size();
	}

	// Egye elem törlése
	public void deleteRow(User user) {
		if ( values.contains(user) ) {
			values.remove(user);
		}
	}

	// Egye elem módosítása
	public void modifyRow(int location, User newuser) {
		values.set(location, newuser);
	}
}
