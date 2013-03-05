package hu.promarkvf.besttest;

public class Kategoria {

	int _id;
	String _nev;
	int _szulo;
	
	public Kategoria(int _id, String _nev, int _szulo) {
		this._id = _id;
		this._nev = _nev;
		this._szulo = _szulo;
	}

	public Kategoria(int _id, String _nev) {
		this._id = _id;
		this._nev = _nev;
		this._szulo = 0;
	}

	public Kategoria() {
		this._id = 0;
		this._nev = "";
		this._szulo = 0;
	}

	public Kategoria get_kat() {
		return this;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String get_nev() {
		return _nev;
	}

	public void set_nev(String _nev) {
		this._nev = _nev;
	}

	public int get_szulo() {
		return _szulo;
	}

	public void set_szulo(int _szulo) {
		this._szulo = _szulo;
	}
}
