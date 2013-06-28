package hu.promarkvf.besttest;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseUser extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "db_user.s3db";

	// user tábla
	public static final String TABLE_USER = "user";
	public static final String COLUMN_USER_ID = "id";
	public static final String COLUMN_USER_NEV = "name";
	public static final String COLUMN_USER_RNEV = "rname";
	public static final String COLUMN_USER_KEY = "key";

	private Context context;
	public static String sshKey;

	public DatabaseUser(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_USER + " (" + COLUMN_USER_ID + " INTEGER PRIMARY KEY , " + COLUMN_USER_NEV + " VARCHAR(80), " + COLUMN_USER_RNEV + " VARCHAR(10), " + COLUMN_USER_KEY + " VARCHAR(2048))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DatabaseHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		if ( oldVersion == 3 && newVersion == 4 ) {
			User user;
			db.execSQL("drop table if exists temp_user");
			db.execSQL("alter table user rename to temp_user");
			onCreate(db);
			db.execSQL("insert into user (id, name, rname) select id, name, rname from temp_user");
			db.execSQL("DROP table temp_user");
			Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " order by id", null);
			if ( cursor.moveToFirst() && cursor.getCount() > 0 ) {
				do {
					user = new User(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NEV)), cursor.getString(cursor.getColumnIndex(COLUMN_USER_RNEV)), cursor.getString(cursor.getColumnIndex(COLUMN_USER_KEY)), (long) cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
					sshKey = sslKeyGet();
					user.setKey(sshKey);
					ContentValues cv = new ContentValues();
					cv.put(COLUMN_USER_NEV, user.getName());
					cv.put(COLUMN_USER_RNEV, user.getRname());
					cv.put(COLUMN_USER_KEY, user.getKey());
					db.update(TABLE_USER, cv, COLUMN_USER_ID + "=?", new String[] { String.valueOf(user.getDbid()) });
				}
				while ( cursor.moveToNext() );
			}
			db.close();
		}
		else {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
			onCreate(db);
		}
	}

	public long AddUser(User user) {
		long lastID = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		sshKey = sslKeyGet();
		cv.put(COLUMN_USER_NEV, user.getName());
		cv.put(COLUMN_USER_RNEV, user.getRname());
		cv.put(COLUMN_USER_KEY, sshKey);

		lastID = db.insert(TABLE_USER, null, cv);
		user.setKey(sshKey);
		user.setDbid(lastID);
		MainActivity.sel_user = user;

		db.close();
		return lastID;
	}

	public List<User> getAllUser() {
		List<User> userList = new ArrayList<User>();
		User user;
		SQLiteDatabase db = this.getWritableDatabase();
		String ut = db.getPath();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " order by id", null);
		if ( cursor.moveToFirst() && cursor.getCount() > 0 ) {
			do {
				user = new User(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NEV)), cursor.getString(cursor.getColumnIndex(COLUMN_USER_RNEV)), cursor.getString(cursor.getColumnIndex(COLUMN_USER_KEY)), (long) cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
				userList.add(user);
			}
			while ( cursor.moveToNext() );
		}
		db.close();
		return userList;
	}

	public User getUserPos(int pos) {
		User user = new User();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " order by id limit ?,1", new String[] { String.valueOf(pos) });
		if ( cursor.moveToFirst() && cursor.getCount() > 0 ) {
			do {
				user = new User(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NEV)), cursor.getString(cursor.getColumnIndex(COLUMN_USER_RNEV)), cursor.getString(cursor.getColumnIndex(COLUMN_USER_KEY)), (long) cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
			}
			while ( cursor.moveToNext() );
		}
		db.close();
		return user;
	}

	public String[] getAllRname() {
		String[] ret;
		SQLiteDatabase db = this.getWritableDatabase();
		String ut = db.getPath();
		Cursor cursor = db.rawQuery("SELECT " + COLUMN_USER_NEV + " FROM " + TABLE_USER + " order by id", null);
		ret = new String[cursor.getCount()];
		if ( cursor.moveToFirst() && cursor.getCount() > 0 ) {
			ret = new String[cursor.getCount()];
			int i = 0;
			do {
				ret[i] = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NEV));
				i++;
			}
			while ( cursor.moveToNext() );
		}
		db.close();
		return ret;
	}

	private String sslKeyGet() {
		Ssl ssl = new Ssl();
		String rets = ssl.get_privateKey_string();
		return rets;
	}

	public int UpdateUser(User user) {
		int ret = -1;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_USER_NEV, user.getName());
		cv.put(COLUMN_USER_RNEV, user.getRname());
		cv.put(COLUMN_USER_KEY, user.getKey());
		ret = db.update(TABLE_USER, cv, COLUMN_USER_ID + "=?", new String[] { String.valueOf(user.getDbid()) });
		db.close();
		return ret;
	}

	public void DeleteUser(User user) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USER, COLUMN_USER_ID + "=?", new String[] { String.valueOf(user.getDbid()) });
		db.close();
	}
}
