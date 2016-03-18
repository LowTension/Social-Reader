package com.destiner.social_reader.model.source_manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.destiner.social_reader.model.structs.source.Source;

import org.joda.time.DateTime;

/**
 * Handles work with sources database. Every class can create an instance of this class to work
 * with SQLite table.
 */
public class SourceDBOpenHelper extends SQLiteOpenHelper {
    // Table constants
    private static final String TABLE_NAME = "sources";
    private static final int TABLE_VERSION = 1;

    // Key constants
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";

    public SourceDBOpenHelper(Context context) {
        super(context, TABLE_NAME, null, TABLE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE =
                "CREATE TABLE "
                        + TABLE_NAME
                        + "("
                        + KEY_ID + " TEXT PRIMARY KEY,"
                        + KEY_DATE + " INTEGER"
                        + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Inserts information about given source. If the information already exists,
     * @param source source to be inserted
     * @param dateTime first post date
     */
    public void insert(Source source, DateTime dateTime) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = getValues(source, dateTime);
        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    /**
     * Retrieves date of first post for given source.
     * @param source source
     * @return milliseconds of date if entry is found; -1 otherwise
     */
    public long getDate(Source source) {
        SQLiteDatabase db = getReadableDatabase();
        String queryText =
                String.format("SELECT %s FROM %s WHERE %s=\"%s\";",
                        KEY_DATE,
                        TABLE_NAME,
                        KEY_ID,
                        source.getStringId()
                );
        Cursor cursor = db.rawQuery(queryText, null);
        return getFirstDate(cursor);
    }

    private long getFirstDate(Cursor cursor) {
        cursor.moveToFirst();
        // If nothing is found
        if (cursor.isAfterLast()) {
            return -1;
        }
        long date = cursor.getLong(cursor.getColumnIndex(KEY_DATE));
        cursor.close();
        return date;
    }

    private ContentValues getValues(Source source, DateTime dateTime) {
        ContentValues values = new ContentValues();
        String id = source.getStringId();
        long date = dateTime.getMillis();
        values.put(KEY_ID, id);
        values.put(KEY_DATE, date);
        return values;
    }
}
