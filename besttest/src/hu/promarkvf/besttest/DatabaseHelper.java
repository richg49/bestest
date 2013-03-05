package hu.promarkvf.besttest;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
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

	// kategoriakerdes tábla
	public static final String TABLE_KATEGORIAKERDES = "kategoriakerdes";
	public static final String COLUMN_KATEGORIAKERDES_ID = "id";
	public static final String COLUMN_KATEGORIAKERDES_KATEGORIA = "kategoria";
	public static final String COLUMN_KATEGORIAKERDES_KERDES = "kerdes";

	// db info tábla
	public static final String TABLE_INFO = "info";
	public static final String COLUMN_INFO_ID = "id";
	public static final String COLUMN_INFO_CREATE_USER_ID = "user_id";
	public static final String COLUMN_INFO_CREATE_USER_NEV = "name";
	public static final String COLUMN_INFO_CREATE_USER_RNEV = "rname";
	public static final String COLUMN_INFO_CREATE_USER_KEY = "key";
	public static final String COLUMN_INFO_LEIRAS = "leiras";
	public static final String COLUMN_INFO_DATUM = "datum";

	private Context context;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_KATEGORIA + " (" + COLUMN_KATEGORIA_ID + " INTEGER PRIMARY KEY , " + COLUMN_KATEGORIA_NEV + " VARCHAR(30), " + COLUMN_KATEGORIA_SZULO + " INTEGER)");

		db.execSQL("CREATE TABLE " + TABLE_KERDES + " (" + COLUMN_KERDES_ID + " INTEGER PRIMARY KEY , " + COLUMN_KERDES_KERDES + " VARCHAR(30), " + COLUMN_KERDES_KEP + " BLOB, " + COLUMN_KERDES_LEIRAS1 + " VARCHAR(50), " + COLUMN_KERDES_LEIRAS2 + " VARCHAR(50))");

		db.execSQL("CREATE TABLE " + TABLE_KATEGORIAKERDES + " (" + COLUMN_KATEGORIAKERDES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_KATEGORIAKERDES_KATEGORIA + " INTEGER NOT NULL , " + COLUMN_KATEGORIAKERDES_KERDES + " INTEGER NOT NULL );");

		db.execSQL("CREATE TABLE IF NOT EXISTS" + TABLE_INFO + " (" + COLUMN_INFO_ID + " INTEGER PRIMARY KEY , " + COLUMN_INFO_CREATE_USER_NEV + " VARCHAR(80), " + COLUMN_INFO_CREATE_USER_RNEV + " VARCHAR(10), " + COLUMN_INFO_CREATE_USER_KEY + " VARCHAR(2048))" + COLUMN_INFO_LEIRAS + " VARCHAR(50)" + COLUMN_INFO_DATUM + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DatabaseHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_KATEGORIAKERDES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_KERDES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_KATEGORIA);
		onCreate(db);
	}

	// Kategoria
	public long AddKategoria(Kategoria kat) {
		long lastID = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();

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
		String ut = db.getPath();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_KATEGORIA + " order by id", null);
		// looping through all rows and adding to list
		if ( cursor.moveToFirst() && cursor.getCount() > 0 ) {
			do {
				kategoria = new Kategoria();
				kategoria.set_id(Integer.parseInt(cursor.getString(0)));
				kategoria.set_nev(cursor.getString(1));
				kategoria.set_szulo(Integer.parseInt(cursor.getString(2)));
				// Adding contact to list
				kategoriaList.add(kategoria);
			}
			while ( cursor.moveToNext() );
		}
		db.close();
		return kategoriaList;
	}

	public List<KategoriaSelected> getAllKategoria(int kerdesID) {
		KategoriaSelected kategoria;
		SQLiteDatabase db = this.getWritableDatabase();
		// Cursor cursor = db.rawQuery("SELECT a.*, b.id FROM " +
		// TABLE_KATEGORIA + " as a left join " + TABLE_KATEGORIAKERDES +
		// " as b on (a.id = b.kategoria) WHERE b.kerdes =? or b.kerdes is null order by a.id",
		// new String[] { String.valueOf(kerdesID) });
		Cursor cursorkat = db.rawQuery("SELECT a.* FROM " + TABLE_KATEGORIA + " as a order by a.id", null);
		Cursor cursor = db.rawQuery("SELECT b.kategoria FROM " + TABLE_KATEGORIA + " as a left join " + TABLE_KATEGORIAKERDES + " as b on (a.id = b.kategoria) WHERE b.kerdes = ? order by b.kategoria", new String[] { String.valueOf(kerdesID) });
		List<KategoriaSelected> kategoriaList = new ArrayList<KategoriaSelected>(cursorkat.getCount());
		cursor.moveToFirst();
		boolean cursorOK = true;
		if ( cursorkat.moveToFirst() && cursorkat.getCount() > 0 ) {
			do {
				kategoria = new KategoriaSelected();
				kategoria.set_id(Integer.parseInt(cursorkat.getString(0)));
				kategoria.set_nev(cursorkat.getString(1));
				kategoria.set_szulo(Integer.parseInt(cursorkat.getString(2)));
				if ( cursorOK && ( cursor.getCount() > 0 ) && !cursor.isNull(0) && !cursorkat.isNull(0) && ( cursor.getInt(0) == cursorkat.getInt(0) ) ) {
					kategoria.set_selectedJel(1);
					cursorOK = cursor.moveToNext();
				}
				else {
					kategoria.set_selectedJel(0);
				}
				kategoriaList.add(kategoria);
			}
			while ( cursorkat.moveToNext() );
		}
		db.close();
		return kategoriaList;
	}

	public int UpdateKategoria(Kategoria kat) {
		int ret = -1;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_KATEGORIA_NEV, kat.get_nev());
		cv.put(COLUMN_KATEGORIA_SZULO, kat.get_szulo());
		ret = db.update(TABLE_KATEGORIA, cv, COLUMN_KATEGORIA_ID + "=?", new String[] { String.valueOf(kat.get_id()) });
		db.close();
		return ret;
	}

	public void DeleteKategoria(Kategoria kat) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_KATEGORIA, COLUMN_KATEGORIA_ID + "=?", new String[] { String.valueOf(kat.get_id()) });
		db.close();
	}

	// kérdések
	public long AddKerdes(Kerdes kerdes) {
		long lastID = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();

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
		if ( cursor.moveToFirst() && cursor.getCount() > 0 ) {
			do {
				kerdes = new Kerdes();
				kerdes.set_id(Integer.parseInt(cursor.getString(0)));
				kerdes.set_kerdes(cursor.getString(1));
				kerdes.set_kep(cursor.getBlob(2));
				kerdes.set_leiras1(cursor.getString(3));
				kerdes.set_leiras2(cursor.getString(4));
				kerdes.set_SelectedKategoriak(getAllKategoria(cursor.getInt(0)));
				kerdesList.add(kerdes);
			}
			while ( cursor.moveToNext() );
		}
		db.close();
		return kerdesList;
	}

	public int UpdateKerdes(Kerdes kerdes) {
		int ret = -1;
		SQLiteDatabase db = this.getWritableDatabase();
		if ( db.isOpen() ) {
			try {
				db.beginTransaction();
				ContentValues cv = new ContentValues();
				ContentValues cvk = new ContentValues();
				cv.put(COLUMN_KERDES_KERDES, kerdes.get_kerdes());
				cv.put(COLUMN_KERDES_KEP, kerdes.get_kep());
				cv.put(COLUMN_KERDES_LEIRAS1, kerdes.get_leiras1());
				cv.put(COLUMN_KERDES_LEIRAS2, kerdes.get_leiras2());
				ret = db.update(TABLE_KERDES, cv, COLUMN_KERDES_ID + "=?", new String[] { String.valueOf(kerdes.get_id()) });
				int deleted = db.delete(TABLE_KATEGORIAKERDES, COLUMN_KATEGORIAKERDES_KERDES + "=?", new String[] { String.valueOf(kerdes.get_id()) });
				List<KategoriaSelected> selkat = kerdes.get_SelectedKategoriak();
				for ( int i = 0; i < selkat.size(); i++ ) {
					if ( selkat.get(i).get_selectedJel() ) {
						cvk.put(COLUMN_KATEGORIAKERDES_KATEGORIA, selkat.get(i).get_id());
						cvk.put(COLUMN_KATEGORIAKERDES_KERDES, kerdes.get_id());
						db.insert(TABLE_KATEGORIAKERDES, null, cvk);
					}
				}
				db.setTransactionSuccessful();
			}
			finally {
				db.endTransaction();
				db.close();
			}
		}
		else {
			ret = -2;
		}
		return ret;
	}

	public void DeleteKerdes(Kerdes kerdes) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_KERDES, COLUMN_KERDES_ID + "=?", new String[] { String.valueOf(kerdes.get_id()) });
		db.close();
	}

	/*
	 * A kérdés és kérdés_kategória tábla kiürítése
	 */
	public String KerdesUrit() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_KERDES);
		db.execSQL("DELETE FROM " + TABLE_KATEGORIAKERDES);
		db.close();
		return context.getResources().getString(R.string.deleteOK);
	}

	/*
	 * Egy kérdés szövege
	 */
	public String getKerdesId(int id) {
		String ret = "";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT id, kerdes FROM " + TABLE_KERDES + " where id = ?", new String[] { String.valueOf(id) });
		if ( cursor.moveToFirst() && cursor.getCount() > 0 ) {
			do {
				ret = cursor.getString(1);
			}
			while ( cursor.moveToNext() );
		}
		db.close();
		return ret;
	}

	/*
	 * Egy kérdés képe
	 */
	public Bitmap getBitmapKerdesId(int id) {
		Bitmap ret = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT id, kep FROM " + TABLE_KERDES + " where id = ?", new String[] { String.valueOf(id) });
		if ( cursor.moveToFirst() && cursor.getCount() > 0 ) {
			do {
				byte[] skep = cursor.getBlob(1);
				if ( skep != null ) {
					ByteArrayInputStream imageStream = new ByteArrayInputStream(skep);
					Bitmap theImage = BitmapFactory.decodeStream(imageStream);
					ret = Bitmap.createScaledBitmap(theImage, MainActivity.image_w, MainActivity.image_h, true);
				}
			}
			while ( cursor.moveToNext() );
		}
		db.close();
		return ret;
	}

	// Kategóriakéerdes
	public void DeleteKategoriaKerdes(Kerdes kerdes) {
		SQLiteDatabase db = this.getWritableDatabase();
		int deleted = db.delete(TABLE_KATEGORIAKERDES, COLUMN_KATEGORIAKERDES_KERDES + "=?", new String[] { String.valueOf(kerdes.get_id()) });
		db.close();
	}

	public void DeleteKategoriaKerdes(Kategoria kat) {
		SQLiteDatabase db = this.getWritableDatabase();
		int deleted = db.delete(TABLE_KATEGORIAKERDES, COLUMN_KATEGORIAKERDES_KATEGORIA + "=?", new String[] { String.valueOf(kat.get_id()) });
		db.close();
	}

	public String DbBackup() {
		String ret = context.getResources().getString(R.string.backupEr);

		File file = new File(Environment.getExternalStorageDirectory(), "/dbbackup");
		SQLiteDatabase db = this.getWritableDatabase();
		String srcPath = db.getPath();
		String destPath = file + "/dbbackup.s3db";
		db.close();

		InputStream inStream = null;
		OutputStream outStream = null;

		try {
			if ( !file.exists() ) {
				if ( !file.mkdirs() ) {
					Log.e("BestTestLog :: ", "Problem creating dbbackup folder");
				}
			}

			File afile = new File(srcPath);
			File bfile = new File(destPath);
			inStream = new FileInputStream(afile);
			outStream = new FileOutputStream(bfile);
			byte[] buffer = new byte[1024];
			int length;
			// copy the file content in bytes
			while ( ( length = inStream.read(buffer) ) > 0 ) {
				outStream.write(buffer, 0, length);
			}
			inStream.close();
			outStream.close();

			ret = context.getResources().getString(R.string.backupOK) + "\n" + destPath;
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}

		return ret;
	}

	public String DbRestor() {
		String ret = context.getResources().getString(R.string.restorEr);

		SQLiteDatabase db = this.getWritableDatabase();
		String srcPath = db.getPath();
		String destPath = Environment.getExternalStorageDirectory() + "/dbbackup/dbrestor.s3db";
		db.close();

		InputStream inStream = null;
		OutputStream outStream = null;

		try {

			File afile = new File(srcPath);
			File bfile = new File(destPath);

			inStream = new FileInputStream(afile);
			outStream = new FileOutputStream(bfile);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ( ( length = inStream.read(buffer) ) > 0 ) {

				outStream.write(buffer, 0, length);

			}

			inStream.close();
			outStream.close();

			ret = context.getResources().getString(R.string.restorOK);

		}
		catch ( IOException e ) {
			e.printStackTrace();
		}

		return ret;
	}

	public String DbAlap() {
		String ret = context.getResources().getString(R.string.dbalapEr);
		SQLiteDatabase db = this.getWritableDatabase();
		String srcPath = db.getPath();
		try {
			InputStream assetsDB = context.getAssets().open("db_fruits_hu.s3db");
			OutputStream dbOut = new FileOutputStream(srcPath);

			byte[] buffer = new byte[1024];
			int length;
			while ( ( length = assetsDB.read(buffer) ) > 0 ) {
				dbOut.write(buffer, 0, length);
			}

			dbOut.flush();
			dbOut.close();
			assetsDB.close();
			ret = context.getResources().getString(R.string.dbalapOK);
		}
		catch ( FileNotFoundException e ) {
			e.printStackTrace();
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
		return ret;

	}

	/**
	 * Kérdés név osztály feltöltése az adatbázisból @param Kategoria Ha 0 akkor
	 * mind
	 */
	public void Kerdes_nev_tolt(int kategoria) {
		Kerdes_nev kerdes_nev = new Kerdes_nev();
	}

	/**
	 * Egy vagy több kategóriához tartozó kérdés ID vissza adása
	 */
	public List<Integer> getKerdesIDs(int... kat) {
		List<Integer> kerdesList = null;
		Cursor cursor;
		SQLiteDatabase db = this.getReadableDatabase();
		if ( kat.length == 0 ) {
			cursor = db.rawQuery("SELECT a.id FROM " + TABLE_KERDES + " as a", null);
		}
		else {
			cursor = db.rawQuery("SELECT a.id FROM " + TABLE_KERDES + " as a  left join kategoriakerdes as b on a.id = b.kerdes where b.kategoria in ( ? )", new String[] { "1" });
		}

		if ( cursor.moveToFirst() && cursor.getCount() > 0 ) {
			kerdesList = new ArrayList<Integer>(cursor.getCount());
			do {
				kerdesList.add(cursor.getInt(0));
			}
			while ( cursor.moveToNext() );
		}
		db.close();
		return kerdesList;
	}

	public List<Kerdes_hiv> Kerdes_hiv_tolt(int db, int kategoriID) {
		int rv = 0;
		List<Kerdes_hiv> ret = new ArrayList<Kerdes_hiv>();
		List<Integer> kerdesekID = new ArrayList<Integer>();
		kerdesekID = this.getKerdesIDs();
		int kerdesekIDsize = kerdesekID != null ? kerdesekID.size() : 0;
		List<Integer> sID = new ArrayList<Integer>(kerdesekIDsize);
		if ( kerdesekIDsize > 4 ) {
			for ( int i = 0; i < db && i < kerdesekIDsize; i++ ) {
				Kerdes_hiv kh = new Kerdes_hiv();
				sID.clear();
				sID.addAll(kerdesekID);
				// kivenni az aktuális kérdést és eltenni
				sID.remove(kerdesekID.get(i));
				kh.setKerdes_id(0, kerdesekID.get(i));
				// roszz válaszok
				for ( int j = 1; j <= 3; j++ ) {
					rv = (int) Math.floor(Math.random() * 100000 % sID.size());
					kh.setKerdes_id(j, sID.get(rv));
					sID.remove(rv);
				}
				// sort
				rv = (int) Math.floor(Math.random() * 100000 % kerdesekIDsize);
				kh.setSort(rv);

				ret.add(kh);

			}
			Collections.sort(ret);
		}
		return ret;
	}

	/*
	 * DB upload server
	 */
	public String DbUpload() {
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;
		DataInputStream inputStream = null;
		int serverResponseCode = -1;
		String serverResponseMessage = "";
		
		SQLiteDatabase db = this.getWritableDatabase();
		String pathToOurFile = db.getPath();
		db.close();

		String urlServer = "http://ip.promarkvf.hu/ip/upload.php";
//		String urlServer = "http://10.1.6.52/upload.php";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;

		try {
			FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile));

			URL url = new URL(urlServer);
			connection = (HttpURLConnection) url.openConnection();

			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			// Enable POST method
			connection.setRequestMethod("POST");

			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
//			outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + MainActivity.sel_user.getRname()+pathToOurFile + "\"" + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" +  MainActivity.sel_user.getRname() +".s3db" + "\"" + lineEnd);
			outputStream.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// Read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while ( bytesRead > 0 ) {
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			inputStream = new DataInputStream(connection.getInputStream());

			// Responses from the server (code and message)
			serverResponseCode = connection.getResponseCode();
			serverResponseMessage = connection.getResponseMessage();

			fileInputStream.close();
			outputStream.flush();
			outputStream.close();
		}
		catch ( Exception ex ) {
			// Exception handling
		}
		return serverResponseMessage;
	}

	public long AddInfo(Inforec info) {
		long ret = -1;
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INFO);
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_INFO + " (" + COLUMN_INFO_ID + " INTEGER PRIMARY KEY , " + COLUMN_INFO_CREATE_USER_ID + " INTEGER, " + COLUMN_INFO_CREATE_USER_NEV + " VARCHAR(80), " + COLUMN_INFO_CREATE_USER_RNEV + " VARCHAR(10), " + COLUMN_INFO_CREATE_USER_KEY + " VARCHAR(2048), " + COLUMN_INFO_LEIRAS + " VARCHAR(50), " + COLUMN_INFO_DATUM + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL)");

		ContentValues cv = new ContentValues();

		cv.put(COLUMN_INFO_CREATE_USER_ID, info.getUser_id());
		cv.put(COLUMN_INFO_CREATE_USER_KEY, info.getKey());
		cv.put(COLUMN_INFO_CREATE_USER_NEV, info.getName());
		cv.put(COLUMN_INFO_CREATE_USER_RNEV, info.getRname());
		cv.put(COLUMN_INFO_LEIRAS, info.getLeiras());

		ret = db.insert(TABLE_INFO, null, cv);

		db.close();
		return ret;
	}
}
