/**
 * 
 */
package hu.promarkvf.besttest;

/**
 * @author Laci
 * 
 */
public class Kerdes_hiv implements Comparable<Kerdes_hiv> {
	private int sort;
	private int[] kerdes_id;

	/**
	 * Egy activity kérdés adatai: kérdés ID + 3 válasz ID
	 */
	public Kerdes_hiv() {
		sort = 0;
		kerdes_id = new int[] { 0, 0, 0, 0 };
	}

	public int getSort() {
		return sort;
	}

	public int getKerdes_id(int id) {
		if ( id >= 0 && id < kerdes_id.length ) {
			return kerdes_id[id];
		}
		else {
			return -1;
		}
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public void setKerdes_id(int id, int kerdes_id0) {
		if ( id >= 0 && id < kerdes_id.length ) {
			this.kerdes_id[id] = kerdes_id0;
		}
	}

	@Override
	public int compareTo(Kerdes_hiv another) {
		int comp = 0;
		if ( this.sort > another.sort ) {
			comp = 1;
		}
		else {
			if ( this.sort < another.sort ) {
				comp = -1;
			}
			else {
				comp = 0;
			}
		}
		return comp;

	}

}
