package hu.promarkvf.besttest;

import java.io.ByteArrayOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import android.graphics.Bitmap;

public class Kerdes {
	

	int _id;
	String _kerdes;
	byte[] _kep;
	String _leiras1;
	String _leiras2;

	public Kerdes() {
		super();
		this._id = 0;
		this._kerdes = "";
		this._kep = null;
		this._leiras1 = "";
		this._leiras2 = "";
	}

	public Kerdes(int _id, String _kerdes, byte[] _kep, String _leiras1, String _leiras2) {
		super();
		this._id = _id;
		this._kerdes = _kerdes;
		this._kep = _kep;
		this._leiras1 = _leiras1;
		this._leiras2 = _leiras2;
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

	@SuppressWarnings("null")
	public Bitmap get_kep_bitmap() {
		Bitmap bmp = null;
		Buffer stream = ByteBuffer.wrap(this._kep);
		bmp.copyPixelsFromBuffer(stream);
		return bmp;
	}

	public void set_kep(byte[] _kep) {
		this._kep = _kep;
	}

	public void set_kep(Bitmap _kep) {
		Bitmap bmp = _kep;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 90, stream);
		this._kep = stream.toByteArray();
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

}
