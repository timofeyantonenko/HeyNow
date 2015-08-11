package com.antonenkodev.heynow.heynow2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by root on 10.06.15.
 */
public class PlacesDbAdapter {

    private static final String TAG = "logs";

    public static final String KEY_ROWID = "_id";;

    public static final String KEY_CODE = "code";
    public static final String KEY_NAME = "name";
    public static final String KEY_LONG = "long";
    public static final String KEY_LAT = "lat";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "World";
    private static final String SQLITE_TABLE = "MyPlaces";
    private static final int DATABASE_VERSION = 10;

    private final Context mCtx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_CODE + "," +
                    KEY_NAME + "," +
                    KEY_LONG + " double," +
                    KEY_LAT + " double," +
                    " UNIQUE (" + KEY_CODE +"));";


    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            Log.w(TAG, DATABASE_CREATE);
            sqLiteDatabase.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public PlacesDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public PlacesDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createPlace(String code, String name,
                            double lon, double lat) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CODE, code);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_LONG, lon);
        initialValues.put(KEY_LAT, lat);

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public boolean deleteAllPlaces() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }
    public void deletePlace(String code) {
        Log.d(TAG,"love tag code + " + code);

        mDb.delete(SQLITE_TABLE, KEY_CODE + " = '" + code + "'", null);
    }


    public Cursor fetchPlacesByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_CODE, KEY_NAME, KEY_LONG, KEY_LAT},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_CODE, KEY_NAME, KEY_LONG, KEY_LAT},
                    KEY_CODE + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchAllPlaces() {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                        KEY_CODE, KEY_NAME, KEY_LONG, KEY_LAT},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public void insertSomePlaces() {

        createPlace("Red Square","Russia, Moscow",37.620139, 55.754194);
        createPlace("VDNH","Russia, Moscow",37.632222,55.829722);
        createPlace("Tret'yakovskaya gallery","Tret'yakov",37.620364, 55.741331);
        createPlace("Big-Ben","England, London",-0.124444, 	51.500833);
        createPlace("Eiffel","France, Paris",2.294444,48.85805);
        createPlace("Pyramid of Cheops","Egypt",29.979167,31.134167);
        createPlace("Torre pendente di Pisa","Italy, Pisa",10.396308,43.722983);
        createPlace("Stonehenge","England",-1.826411, 51.178861);
        createPlace("Akropolis","Grecce",23.726166,37.971421);
        createPlace("Sydney Opera House","Australia, Sydney",151.215264,-33.85680);
        createPlace("Statue of Liberty","USA, New York",-74.044583,40.689167);
        createPlace("Golden Gate Bridge","USA, San Francisco",-122.478333,37.8175);
        createPlace("Piazza San Marco","Italy, Venezia",12.338056,45.433889);
        createPlace("Colosseus","Italy, Roma",12.492269,41.890169);
        createPlace("Hollywood","USA, Los Angeles",-118.321389,34.133889);
        createPlace("Basilica Sancti Petri","Vatican", 12.453056,41.901944);
        createPlace("Niagara Falls","USA", -79.075833,43.078056);
        createPlace("Pyramid of Geeze","Egypt", 31.130425,29.975756);
        createPlace("Kölner Dom","Germany, Köln", 6.956667,50.941111);
        createPlace("de Dam","Nederland, Amsterdam", 4.893,52.373);
        createPlace("Notre-Dame de Paris","Fance, Paris", 2.35,48.852778);
        createPlace("Versailles","France,Versailles", 2.123162,48.804404);
        createPlace("Reichstag ","Germany, Berlin", 13.376111,52.518611);
        createPlace("Tower Bridge","England, London", -0.075,51.505556);
        createPlace("Estádio do Maracanã","Brazil, Rio de Janeiro", -43.230278,-22.912222);
        createPlace("Star Island (Miami)","USA, Miami",25.7773, -80.1502);
        createPlace("Camp Nou","Spain, Barcelona", 2.12283, 41.3808891);
        createPlace("Burj khalifa","UAE, Dubai", 55.274167,25.197222);
        createPlace("Ibiza","Illes Balears", 1.416667,38.983333);
        createPlace("Grand Prix Circuit","Australia, Melburn", 144.968333, -37.849722);
        createPlace("Silicon Valley","USA, San Francisco", -122.04,37.37);
        createPlace("Estadio Santiago Bernabéu","Spain, Madrid", -3.688333,40.452961);
        createPlace("Cristo Redentor","Brazil, Rio de Janeiro", -43.209583,-22.952778);
        createPlace("Taj Mahal","India, Agra", 78.042097,27.174931);
        createPlace("Empire State Building","USA, New York", -73.985833,40.747778);
        createPlace("Las Vegas Strip","USA,  Las Vegas", -115.172222,36.120833);



    }



}
