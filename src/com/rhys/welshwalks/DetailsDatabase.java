package com.rhys.welshwalks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DetailsDatabase {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_DISTANCE = "total_distance";
	public static final String KEY_WALKS = "total_walks";
	
	public static final String DATABASE_NAME = "DetailsDB";
	public static final String DATABASE_TABLE = "Stats";
	public static final int DATABASE_VERSION = 1;
	
	private DbHelper helper;
	private final Context context;
	private SQLiteDatabase database;
	
	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_DISTANCE + " TEXT NOT NULL, " +
					KEY_WALKS + " TEXT NOT NULL);"
			);
			ContentValues cv = new ContentValues();
			cv.put(KEY_DISTANCE, "0");
			cv.put(KEY_WALKS, "0");
			db.insert(DATABASE_TABLE, null, cv);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
		
	}
	
	public DetailsDatabase(Context c) {
		context = c;
	}
	
	public DetailsDatabase open() throws SQLException {
		helper = new DbHelper(context);
		database = helper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		helper.close();
	}
	
	public void updateEntry(String dwt, String now) {
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_DISTANCE, dwt);
		cvUpdate.put(KEY_WALKS, now);
		database.update(DATABASE_TABLE, cvUpdate, KEY_ROWID + "=1", null);
	}

	public String getDis() {
		String[] cols = new String[]{ KEY_ROWID, KEY_DISTANCE, KEY_WALKS};
		Cursor c = database.query(DATABASE_TABLE, cols, KEY_ROWID + "=1", null, null, null, null);
		if(c != null) {
			String result = "";
			int iDistance = c.getColumnIndex(KEY_DISTANCE);
			c.moveToFirst();
			result = result + c.getString(iDistance);
			return result;
		}
		return null;
	}
	
	public String getWalks() {
		String[] cols = new String[]{ KEY_ROWID, KEY_DISTANCE, KEY_WALKS};
		Cursor c = database.query(DATABASE_TABLE, cols, KEY_ROWID + "=1", null, null, null, null);
		if(c != null) {
			String result = "";
			int iWalks = c.getColumnIndex(KEY_WALKS);
			c.moveToFirst();
			result = result + c.getString(iWalks);
			return result;
		}
		return null;
	}
}
