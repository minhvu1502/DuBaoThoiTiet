package com.example.test.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.test.models.City;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class DBhelper extends SQLiteOpenHelper {
    private static final String DBname = "dbcity.db";
    private static final int VERSION = 1;
    private static final String TABLE_NAME = "City";
    private static final String ID = "_id";
    private static final String NAME = "name";
    private static final String LAT = "lat";
    private static final String LNG = "lng";
    private SQLiteDatabase myDB;

    public DBhelper(@Nullable Context context) {
        super(context, DBname, null, VERSION);
    }

    public static String getID() {
        return ID;
    }

    public static String getNAME() {
        return NAME;
    }

    public static String getLAT() {
        return LAT;
    }

    public static String getLNG() {
        return LNG;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryTB = "CREATE TABLE " + TABLE_NAME + "( " +
                ID + " INTEGER PRIMARY KEY, " +
                NAME + " TEXT NOT NULL, " +
                LAT + " TEXT NOT NULL, " +
                LNG + " TEXT NOT NULL )";
        db.execSQL(queryTB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDB() {
        myDB = getWritableDatabase();
    }

    public void closeDB() {
        if (myDB != null && myDB.isOpen()) {
            myDB.close();
        }
    }

    public long Insert(int id, String name, String lat, String lng) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, id);
        contentValues.put(NAME, name);
        contentValues.put(LAT, lat);
        contentValues.put(LNG, lng);
        return myDB.insert(TABLE_NAME, null, contentValues);
    }

    public long Update(int id, String name, String lat, String lng) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, id);
        contentValues.put(NAME, name);
        contentValues.put(LAT, lat);
        contentValues.put(LNG, lng);
        String where = ID + " = " + id;
        return myDB.update(TABLE_NAME, contentValues, where, null);
    }

    public long Delete(int id) {
        String where = ID + " = " + id;
        return myDB.delete(TABLE_NAME, where, null);
    }

    public Cursor getAllRecord() {
        String query = "SELECT * FROM " + TABLE_NAME;
        return myDB.rawQuery(query, null);
    }

    public ArrayList<City> getAllWords() {
        ArrayList<City> wordList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                City word = new City();
               word.setID(cursor.getInt(0));
               word.setCity_Name(cursor.getString(1));
               word.setLat(cursor.getString(2));
               word.setLng(cursor.getString(3));
                // Thêm vào danh sách.
                wordList.add(word);
            } while (cursor.moveToNext());
        }
        // return note list
        return wordList;
    }
}
