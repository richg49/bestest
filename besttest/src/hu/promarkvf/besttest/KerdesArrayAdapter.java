package hu.promarkvf.besttest;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class KerdesArrayAdapter extends ArrayAdapter<Kerdes> {
	private final Context context;
	private final List<Kerdes> values;

	public KerdesArrayAdapter(Context context, List<Kerdes> kerdesList) {
		super(context, R.layout.activity_kerdesek, kerdesList);
		this.context = context;
		this.values = kerdesList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.activity_kerdesek, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.kerdes);
		textView.setText(values.get(position).get_kerdes());

		return rowView;
	}

	// beszúrás
	public void addItem(Kerdes kerdes) {
		values.add(kerdes);
    }

    @Override
	public int getCount() {
        return values.size();
    }

   // Egye elem törlése
    public void deleteRow(Kerdes kerdes) {
        if(values.contains(kerdes)) {
        	values.remove(kerdes);
        }
    }

    // Egye elem módosítása
    public void modifyRow(int location, Kerdes newkerdes) {
      	values.set(location, newkerdes);
    }
}
