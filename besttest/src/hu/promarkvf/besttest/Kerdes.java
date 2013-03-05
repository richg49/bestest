package hu.promarkvf.besttest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Kerdes {

	private int _id;
	private String _kerdes;
	private byte[] _kep;
	private String _leiras1;
	private String _leiras2;
	private List<KategoriaSelected> _selectedKategoriak;
			
	public Kerdes() {
		super();
		this._id = 0;
		this._kerdes = "";
		this._kep = null;
		this._leiras1 = "";
		this._leiras2 = "";
		this._selectedKategoriak = null;
	}

	public Kerdes(int _id, String _kerdes, byte[] _kep, String _leiras1, String _leiras2) {
		super();
		this._id = _id;
		this._kerdes = _kerdes;
		this._kep = _kep;
		this._leiras1 = _leiras1;
		this._leiras2 = _leiras2;
		this._selectedKategoriak = null;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String get_kerdes() {
		return _kerdes;
	}

	public void set_kerdes(String _kerdes) {
		this._kerdes = _kerdes;
	}

	public byte[] get_kep() {
		return _kep;
	}

	public Bitmap get_kep_bitmap() {
		Bitmap newImage = null;
		if (this._kep != null) {
			ByteArrayInputStream imageStream = new ByteArrayInputStream(this._kep);
			Bitmap theImage = BitmapFactory.decodeStream(imageStream);
			newImage = Bitmap.createScaledBitmap(theImage, 150, 150, true);
		}
		return newImage;
	}

	public void set_kep(byte[] _kep) {
		if (_kep != null) {
			ByteArrayInputStream imageStream = new ByteArrayInputStream(_kep);
			Bitmap theImage = BitmapFactory.decodeStream(imageStream);
			int maxX = theImage.getWidth();
			int maxY = theImage.getHeight();
			// Bitmap newImage = Bitmap.createBitmap(300, 300, null);
			if (maxX != 300 || maxY != 300) {
				Bitmap newImage = Bitmap.createScaledBitmap(theImage, 300, 300, true);
//				int maxnX = newImage.getWidth();
//				int maxnY = newImage.getHeight();
				set_kep(newImage);
			}
			else {
				set_kep(theImage);
			}
		} else {
			this._kep = null;
		}
	}

	public void set_kep(Bitmap _kep) {
		if (_kep != null) {
			Bitmap bmp = _kep;
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.JPEG, 90, stream);
			this._kep = stream.toByteArray();
		} else {
			this._kep = null;
		}
	}

	public String get_leiras1() {
		return _leiras1;
	}

	public void set_leiras1(String _leiras1) {
		this._leiras1 = _leiras1;
	}

	public String get_leiras2() {
		return _leiras2;
	}

	public void set_leiras2(String _leiras2) {
		this._leiras2 = _leiras2;
	}

	public List<KategoriaSelected> get_SelectedKategoriak() {
		return _selectedKategoriak;
	}

	public List<Kategoria> get_Kategoriak() {
		List<Kategoria> ret = new ArrayList<Kategoria>();
		for ( int i = 0; i < this._selectedKategoriak.size(); ++i ) {
				ret.add(this._selectedKategoriak.get(i).get_kat());
			}
		return ret;
	}

	public void set_SelectedKategoriak(List<KategoriaSelected> _selectedKategoriak) {
		this._selectedKategoriak = _selectedKategoriak;
	}

}
