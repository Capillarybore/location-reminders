package com.example.realshitmaybe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class databasehelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Entire.db";
    public static final String TABLE_NAME = "Task_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "DISC";
    public static final String COL_4 = "DATE";
    public static final String COL_5 = "MONTH";
    public static final String COL_6 = "YEAR";
    public static final String COL_7 = "HOURS";
    public static final String COL_8 = "MINUTES";
    public static final String COL_9 = "LATITUDE";
    public static final String COL_10 = "LONGITUDE";

    public databasehelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        //SQLiteDatabase db = this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT ,DISC TEXT, DATE INTEGER, MONTH INTEGER, YEAR INTEGER, HOURS INTEGER, MINUTES INTEGER, LATITUDE INTEGER, LONGITUDE INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean insertData(String NAME,String DISC, String DATE, String MONTH, String YEAR, String HOURS, String MINUTES, String LATITUDE, String LONGITUDE){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,NAME);
        contentValues.put(COL_3,DISC);
        contentValues.put(COL_4,DATE);
        contentValues.put(COL_5,MONTH);
        contentValues.put(COL_6,YEAR);
        contentValues.put(COL_7,HOURS);
        contentValues.put(COL_8,MINUTES);
        contentValues.put(COL_9,LATITUDE);
        contentValues.put(COL_10,LONGITUDE);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getSomeData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+" where ID="+id,null);
        return res;}


    public boolean UpdateData(String ID,String NAME,String DISC, String DATE, String MONTH, String YEAR, String HOURS, String MINUTES, String LATITUDE, String LONGITUDE) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, ID);
        contentValues.put(COL_2, NAME);
        contentValues.put(COL_3, DISC);
        contentValues.put(COL_4, DATE);
        contentValues.put(COL_5, MONTH);
        contentValues.put(COL_6, YEAR);
        contentValues.put(COL_7, HOURS);
        contentValues.put(COL_8, MINUTES);
        contentValues.put(COL_9, LATITUDE);
        contentValues.put(COL_10, LONGITUDE);
        db.update(TABLE_NAME, contentValues, "ID=?", new String[]{ID});
        return true;
    }

    public  Integer DeleteData(String ID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID=?", new String[] {ID});
    }



}