package hu.promarkvf.besttest;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	  private static final String DATABASE_NAME = "db1.s3db";
	  private static final int DATABASE_VERSION = 1;

	  // kategoria tábla
	  public static final String TABLE_KATEGORIA = "kategoria";
	  public static final String COLUMN_KATEGORIA_ID = "id";
	  public static final String COLUMN_KATEGORIA_NEV = "nev";
	  public static final String COLUMN_KATEGORIA_SZULO = "szulo";

	  // kerdes tábla
	  public static final String TABLE_KERDES = "kerdes";
	  public static final String COLUMN_KERDES_ID = "id";
	  public static final String COLUMN_KERDES_KERDES = "kerdes";
	  public static final String COLUMN_KERDES_KEP = "kep";
	  public static final String COLUMN_KERDES_LEIRAS1 = "leiras1";
	  public static final String COLUMN_KERDES_LEIRAS2 = "leiras2";

	  // kerdes tábla
	  public static final String TABLE_KATEGORIAKERDES = "kategoriakerdes";
	  public static final String COLUMN_KATEGORIAKERDES_ID = "id";
	  public static final String COLUMN_KATEGORIAKERDES_KATEGORIA = "kategoria";
	  public static final String COLUMN_KATEGORIAKERDES_KERDES = "kerdes";

	  public DatabaseHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE "+TABLE_KATEGORIA+" ("+
				COLUMN_KATEGORIA_ID+ " INTEGER PRIMARY KEY , "+
				COLUMN_KATEGORIA_NEV+ " VARCHAR(30), "+
				COLUMN_KATEGORIA_SZULO+ " INTEGER)");
		
		db.execSQL("CREATE TABLE "+TABLE_KERDES+" ("+
				COLUMN_KERDES_ID+ " INTEGER PRIMARY KEY , "+
				COLUMN_KERDES_KERDES+ " VARCHAR(30), "+
				COLUMN_KERDES_KEP+ " BLOB, "+
				COLUMN_KERDES_LEIRAS1+ " VARCHAR(50), "+
				COLUMN_KERDES_LEIRAS2+ " VARCHAR(50))");

		db.execSQL("CREATE TABLE "+TABLE_KATEGORIAKERDES+" ("+
				COLUMN_KATEGORIAKERDES_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
				COLUMN_KATEGORIAKERDES_KATEGORIA+" INTEGER NOT NULL , " +
				COLUMN_KATEGORIAKERDES_KERDES+" INTEGER NOT NULL );");
		
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(DatabaseHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_KATEGORIAKERDES);
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_KERDES);
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_KATEGORIA);
	    onCreate(db);
	  }
	  
	 public long AddKategoria(Kategoria kat) {
		long lastID = 0;
		SQLiteDatabase db= this.getWritableDatabase();
		ContentValues cv=new ContentValues();
		
		cv.put(COLUMN_KATEGORIA_NEV, kat.get_nev());
		cv.put(COLUMN_KATEGORIA_SZULO, kat.get_szulo());
		
		lastID = db.insert(TABLE_KATEGORIA, COLUMN_KATEGORIA_NEV, cv);
		
		db.close();
		return lastID;
	}

	public List<Kategoria> getAllKategoria() {
		List<Kategoria> kategoriaList = new ArrayList<Kategoria>();
		Kategoria kategoria;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_KATEGORIA, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst() && cursor.getCount() > 0) {
			do {
				kategoria = new Kategoria();
				kategoria.set_id(Integer.parseInt(cursor.getString(0)));
				kategoria.set_nev(cursor.getString(1));
				kategoria.set_szulo(Integer.parseInt(cursor.getString(2)));
				// Adding contact to list
				kategoriaList.add(kategoria);
			} while (cursor.moveToNext());
		}

		return kategoriaList;
	}

	 public int UpdateKategoria(Kategoria kat) {
		 SQLiteDatabase db=this.getWritableDatabase();
		 ContentValues cv=new ContentValues();
		 cv.put(COLUMN_KATEGORIA_NEV, kat.get_nev());
		 cv.put(COLUMN_KATEGORIA_SZULO, kat.get_szulo());
		 return db.update(TABLE_KATEGORIA, cv, COLUMN_KATEGORIA_ID+"=?", new String []{String.valueOf(kat.get_id())});
	 }
	 
	 public void DeleteKategoria(Kategoria kat) {
		 SQLiteDatabase db=this.getWritableDatabase();
		 db.delete(TABLE_KATEGORIA,COLUMN_KATEGORIA_ID+"=?", new String [] {String.valueOf(kat.get_id())});
		 db.close();
	 }
// kérdések
	 public long AddKerdes(Kerdes kerdes) {
		long lastID = 0;
		SQLiteDatabase db= this.getWritableDatabase();
		ContentValues cv=new ContentValues();
		
		cv.put(COLUMN_KERDES_KERDES, kerdes.get_kerdes());
		cv.put(COLUMN_KERDES_KEP, kerdes.get_kep());
		cv.put(COLUMN_KERDES_LEIRAS1, kerdes.get_leiras1());
		cv.put(COLUMN_KERDES_LEIRAS2, kerdes.get_leiras2());
		
		lastID = db.insert(TABLE_KERDES, COLUMN_KERDES_KERDES, cv);
		
		db.close();
		return lastID;
	}

	public List<Kerdes> getAllKerdes() {
		List<Kerdes> kerdesList = new ArrayList<Kerdes>();
		Kerdes kerdes;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT id, kerdes, kep, leiras1, leiras2 FROM " + TABLE_KERDES, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst() && cursor.getCount() > 0) {
			do {
				kerdes = new Kerdes();
				kerdes.set_id(Integer.parseInt(cursor.getString(0)));
				kerdes.set_kerdes(cursor.getString(1));
				kerdes.set_kep(cursor.getBlob(2));
				kerdes.set_leiras1(cursor.getString(3));
				kerdes.set_leiras2(cursor.getString(4));
				// Adding contact to list
				kerdesList.add(kerdes);
			} while (cursor.moveToNext());
		}

		return kerdesList;
	}

}
