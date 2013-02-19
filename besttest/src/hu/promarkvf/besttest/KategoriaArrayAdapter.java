package hu.promarkvf.besttest;

import java.util.List;

import hu.promarkvf.besttest.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class KategoriaArrayAdapter extends ArrayAdapter<Kategoria> {
	private final Context context;
	private final List<Kategoria> values;

	public KategoriaArrayAdapter(Context context, List<Kategoria> kategoriaList) {
		super(context, R.layout.activity_kategoriak, kategoriaList);
		this.context = context;
		this.values = kategoriaList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.activity_kategoriak, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		textView.setText(values.get(position).get_nev());

		return rowView;
	}

	// beszúrás
	public void addItem(Kategoria kat) {
		values.add(kat);
    }

    public int getCount() {
        return values.size();
    }

   // Egye elem törlése
    public void deleteRow(Kategoria kat) {
        if(values.contains(kat)) {
        	values.remove(kat);
        }
    }

    // Egye elem módosítása
    public void modifyRow(int location, Kategoria newkat) {
      	values.set(location, newkat);
    }
}
