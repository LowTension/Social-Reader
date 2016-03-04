package com.destiner.social_reader.model.cache;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.destiner.social_reader.model.structs.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles work with articles database. Every class can create an instance of this class to work
 * with SQLite table. Assigns unique IDs to each article to store them in the order they were added
 * (and therefore retrieved from server).
 */
public class ArticleOpenHelper extends SQLiteOpenHelper {
    private final SharedPreferences preferences;
    // Name of SharedPreferences that store information about this database
    private static final String PREFERENCES_NAME = "article_database_info";
    private static final String PREFERENCES_KEY_ID = "id";
    // Table constants
    private static final String TABLE_NAME = "events";
    private static final int TABLE_VERSION = 1;

    // Key constants
    private static final String KEY_ID = "id";
    private static final String KEY_TEXT = "text";

    public ArticleOpenHelper(Context context) {
        super(context, TABLE_NAME, null, TABLE_VERSION);
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE =
                "CREATE TABLE "
                        + TABLE_NAME
                        + "("
                        + KEY_ID + " INTEGER PRIMARY KEY,"
                        + KEY_TEXT + " TEXT"
                        + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Adds all articles in list to the database.
     * @param articles list of articles
     */
    public void addAll(List<Article> articles) {
        SQLiteDatabase db = getWritableDatabase();
        for (Article article : articles) {
            ContentValues values = getValues(article, getNextId());
            db.insert(TABLE_NAME, null, values);
        }
    }

    /**
     * Deletes specified article from database if it is exists.
     * @param article article
     */
    public void delete(Article article) {
        SQLiteDatabase db = getWritableDatabase();
        String deletionRule = KEY_TEXT + " LIKE ?";
        String[] ruleArguments = new String[] {article.getText()};
        db.delete(TABLE_NAME, deletionRule, ruleArguments);
    }

    /**
     * Fetches list of articles from database in specified range.
     * @param count how many articles required
     * @param offset position of first article in range
     * @return list of articles
     */
    public List<Article> get(int count, int offset) {
        SQLiteDatabase db = getReadableDatabase();
        String queryText =
                String.format("SELECT %s FROM %s ORDER BY %s LIMIT %d OFFSET %d;",
                        KEY_TEXT,
                        TABLE_NAME,
                        KEY_TEXT,
                        count,
                        offset);
        Cursor cursor = db.rawQuery(queryText, null);
        return getFromCursor(cursor);
    }

    /**
     * Returns number of articles currently stored in database.
     * @return number of articles
     */
    public int getCount() {
        SQLiteDatabase db = getReadableDatabase();
        String queryText =
                String.format("SELECT * FROM %s;",
                        TABLE_NAME);
        Cursor cursor = db.rawQuery(queryText, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    private ContentValues getValues(Article article, long id) {
        ContentValues values = new ContentValues();
        // Put all data as values
        values.put(KEY_ID, id);
        values.put(KEY_TEXT, article.getText());
        return values;
    }

    private long getNextId() {
        long id = preferences.getLong(PREFERENCES_KEY_ID, 0);
        id++;
        preferences.edit().putLong(PREFERENCES_KEY_ID, id).apply();
        return id;
    }

    private List<Article> getFromCursor(Cursor cursor) {
        List<Article> articles = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            String text = cursor.getString(cursor.getColumnIndex(KEY_TEXT));
            Article article = new Article(text);
            articles.add(article);
        }
        cursor.close();
        return articles;
    }
}
