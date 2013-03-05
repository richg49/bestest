package hu.promarkvf.besttest;

public class KategoriaSelected extends Kategoria {

	private boolean _selectedJel;

	public KategoriaSelected(int _id, String _nev, int _szulo, boolean _selectedJel) {
		super(_id, _nev, _szulo);
		this._selectedJel = _selectedJel;
	}

	public KategoriaSelected(int _id, String _nev) {
		super(_id, _nev);
		this._selectedJel = Boolean.FALSE;
	}

	public KategoriaSelected(Kategoria _kat, boolean _selectedJel) {
		super(_kat.get_id(), _kat.get_nev(), _kat.get_szulo());
		this._selectedJel = _selectedJel;
	}

	public KategoriaSelected() {
		this._selectedJel = Boolean.FALSE;
	}

	public Boolean get_selectedJel() {
		return _selectedJel;
	}

	public void set_selectedJel(Boolean _selectedJel) {
		this._selectedJel = _selectedJel;
	}

	public void set_selectedJel(int _selectedJel) {
		if ( _selectedJel > 0 ) {
			this._selectedJel = Boolean.TRUE;
		}
		else {
			this._selectedJel = Boolean.FALSE;
		}
	}

}
