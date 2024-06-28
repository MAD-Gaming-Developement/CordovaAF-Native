package mgd.cordovatools.afnative.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import mgd.cordovatools.afnative.AppConfig;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "config.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "config";
    private static final String COLUMN_VERSION = "dbversion";
    private static final String COLUMN_LINK = "dblink";
    private static final String COLUMN_POLICY = "policylink";
    private static final String COLUMN_STATUS = "dbstatus";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_VERSION + " INTEGER DEFAULT 0, " +
                COLUMN_LINK + " TEXT DEFAULT '', " +
                COLUMN_POLICY + " TEXT DEFAULT '', " +
                COLUMN_STATUS + " INTEGER DEFAULT 0)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public int getDbVersion() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_VERSION + " FROM " + TABLE_NAME, null);
        int version = 0;
        if (cursor.moveToFirst()) {
            version = cursor.getInt(0);
        }
        cursor.close();
        return version;
    }

    public void updateData(int version, String link, String policylink, int status) {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_VERSION, version);
            contentValues.put(COLUMN_LINK, link);
            contentValues.put(COLUMN_POLICY, policylink);
            contentValues.put(COLUMN_STATUS, status);
            db.update(TABLE_NAME, contentValues, null, null);
            Log.d(AppConfig.LOG_TAG, "Local DB Updated.");
        }
        catch (SQLException error)
        {
            Log.e(AppConfig.LOG_TAG, "Error updating DB: " + error.getMessage());
        }

    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
