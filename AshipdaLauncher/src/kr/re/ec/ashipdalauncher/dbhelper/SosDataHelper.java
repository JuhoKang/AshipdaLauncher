package kr.re.ec.ashipdalauncher.dbhelper;

import java.util.ArrayList;
import java.util.List;

import kr.re.ec.ashipdalauncher.constants.SosConstants;
import kr.re.ec.ashipdalauncher.constants.SosConstants.SosFrame;
import kr.re.ec.ashipdalauncher.objects.Sos;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract.Columns;

//dbopenhelper
public class SosDataHelper extends SQLiteOpenHelper {

	public SosDataHelper(Context context) {
		super(context, SosFrame.DB_NAME, null, SosFrame.DB_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		/*
		 * String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
		 * + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_PH_NO
		 * + " TEXT" + ")"; db.execSQL(CREATE_CONTACTS_TABLE);
		 */
		String CREATE_SosS_TABLE = "CREATE TABLE " + SosFrame.TABLE_NAME
				+ "(" + SosFrame.COLUMN_NAME_SOS_ID + " INTEGER PRIMARY KEY,"
				+ SosFrame.COLUMN_NAME_SOS_NAME + " TEXT,"
				+ SosFrame.COLUMN_NAME_SOS_PHONENUM + " TEXT" + ")";
		db.execSQL(CREATE_SosS_TABLE);

	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + SosFrame.TABLE_NAME);

		// Create tables again
		onCreate(db);
	}

	/**
	 * CRUD �Լ�
	 */

	// add new Sos
	public void addSos(Sos Sos) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(SosFrame.COLUMN_NAME_SOS_NAME, Sos.getName()); // name
		values.put(SosFrame.COLUMN_NAME_SOS_PHONENUM, Sos.getPhoneNumber()); // url

		// Inserting Row
		db.insert(SosFrame.TABLE_NAME, null, values);
		db.close(); // Closing database connection
	}

	// get Sos by id
	public Sos getSos(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		/*
		 * Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
		 * KEY_NAME, KEY_PH_NO }, KEY_ID + "=?", new String[] {
		 * String.valueOf(id) }, null, null, null, null);
		 */
		Cursor cursor = db.query(SosFrame.TABLE_NAME, new String[] {
				SosFrame.COLUMN_NAME_SOS_ID, SosFrame.COLUMN_NAME_SOS_NAME,
				SosFrame.COLUMN_NAME_SOS_PHONENUM },
				SosFrame.COLUMN_NAME_SOS_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Sos Sos = new Sos(Integer.parseInt(cursor.getString(0)),cursor.getString(1),
				cursor.getString(2));
		// return contact
		return Sos;
	}

	// get all Sos information
	public List<Sos> getAllSos() {
		List<Sos> SosList = new ArrayList<Sos>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + SosFrame.TABLE_NAME;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Sos Sos = new Sos();
				Sos.setId(Integer.parseInt(cursor.getString(0)));
				Sos.setName(cursor.getString(1));
				Sos.setPhoneNumber(cursor.getString(2));
				// Adding contact to list
				SosList.add(Sos);
			} while (cursor.moveToNext());
		}

		// return contact list
		return SosList;
	}

	// get all Soss by name
	public ArrayList<String> getAllSosName() {
		ArrayList<String> SosList = new ArrayList<String>();

		String selectQuery = "SELECT * FROM " + SosFrame.TABLE_NAME;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				String SosName = new String();
				SosName = cursor.getString(1);
				// Adding to list
				SosList.add(SosName);
			} while (cursor.moveToNext());
		}

		return SosList;
	}

	// update Sos
	public int updateSos(Sos Sos) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(SosFrame.COLUMN_NAME_SOS_NAME, Sos.getName());
		values.put(SosFrame.COLUMN_NAME_SOS_PHONENUM, Sos.getPhoneNumber());

		// updating row
		return db.update(SosFrame.TABLE_NAME, values,
				SosFrame.COLUMN_NAME_SOS_ID + " = ?",
				new String[] { String.valueOf(Sos.getId()) });
	}

	// delete Sos
	public void deleteSos(Sos Sos) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(SosFrame.TABLE_NAME, SosFrame.COLUMN_NAME_SOS_ID + " = ?",
				new String[] { String.valueOf(Sos.getId()) });
		db.close();
	}

	// how many Soss
	public int getSossCount() {
		String countQuery = "SELECT  * FROM " + SosFrame.TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}
	
	// get one video by name
		public Sos getSosWithName(String name) {
			SQLiteDatabase db = this.getReadableDatabase();
			/*
			 * Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
			 * KEY_NAME, KEY_PH_NO }, KEY_ID + "=?", new String[] {
			 * String.valueOf(id) }, null, null, null, null);
			 */
			Cursor cursor = db.query(SosFrame.TABLE_NAME, new String[] {
					SosFrame.COLUMN_NAME_SOS_ID, SosFrame.COLUMN_NAME_SOS_NAME,
					SosFrame.COLUMN_NAME_SOS_PHONENUM },
					SosFrame.COLUMN_NAME_SOS_NAME + "=?",
					new String[] { String.valueOf(name) }, null, null, null);
			if (cursor != null)
				cursor.moveToFirst();

			Sos sos = new Sos(Integer.parseInt(cursor.getString(0)),
					cursor.getString(1), cursor.getString(2));
			// return contact
			return sos;
		}

}