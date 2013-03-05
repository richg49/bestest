package hu.promarkvf.besttest;

public class Kerdes_nev extends Kerdes {
	private byte[][] s_kep_t;

	public Kerdes_nev() {
		super();
		byte[] s_kep = null;
		s_kep_t[0] = s_kep;
		s_kep_t[1] = s_kep;
		s_kep_t[2] = s_kep;
	}

	/**
	 * @param int i, byte[] s_kep
	 */
	public Kerdes_nev(int i, byte[] s_kep_t) {
		super();
		if ( i >= 0 && i <= 2 ) {
			this.s_kep_t[i] = s_kep_t;
		}
	}

	public Kerdes_nev(int _id, String _kerdes, byte[] _kep, String _leiras1, String _leiras2) {
		super(_id, _kerdes, _kep, _leiras1, _leiras2);
	}

}
