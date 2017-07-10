package com.example.jswebstage1.contactjsweb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Jsweb Stage1 on 10/07/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;
    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_HOME = "home";
    public static final String KEY_ROW_ID = "_id";
    public static final String PROJECTION[] = {
            KEY_ROW_ID,
            KEY_NAME,
            KEY_ADDRESS,
            KEY_MOBILE,
            KEY_HOME
    };
    /* The table and database names */
    public static final String TABLE_NAME = "mycontacts";
    public static final String DATABASE_NAME = "contactDB";
    //private static String DB_PATH;

    private final static String DBPATH =
            "/data/data/com.example.jswebstage1.contactjsweb/databases/";

    /*SQL code for creating the table*/
    private static final String TABLE_CREATE=
            "create table "+ TABLE_NAME + "("+ KEY_ROW_ID
                    +" integer primary key autoincrement,"+
                    KEY_NAME +" text not null,"+
                    KEY_ADDRESS + " text not null,"+
                    KEY_MOBILE + " text,"+
                    KEY_HOME + " text)";

    public DBHelper(Context ctx){
        super(ctx, DATABASE_NAME , null, 1);
    }

    public void createRow(String name, String address, String mobile, String home)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_ADDRESS, address);
        initialValues.put(KEY_MOBILE, mobile);
        initialValues.put(KEY_HOME, home);
        db.insert(TABLE_NAME, null, initialValues);
    }

    public void deleteRow(long rowId){
        db.delete(TABLE_NAME, KEY_ROW_ID+"="+rowId,null);
    }

    public boolean updateRow (long rowId, String name, String address, String mobile, String home){
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_ADDRESS, address);
        args.put(KEY_MOBILE, mobile);
        args.put(KEY_HOME, home);
        return db.update(TABLE_NAME, args, KEY_ROW_ID +"="+ rowId, null)>0;
    }

    public Cursor fetchRow(long rowId) throws SQLException {

        Cursor result = db.query(TABLE_NAME, PROJECTION,
                KEY_ROW_ID + "=" + rowId, null, null, null, null);
        if ((result.getCount() == 0) || !result.isNull(0)) {
            throw new SQLException("No note matching ID: " + rowId);
        }
        return result;
    }

    public Cursor fetchAllRows(){
        return db.query(TABLE_NAME, PROJECTION,
                null, null, null, null, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table contacts " +
                        "(id integer primary key, name text,email text, phone text, home text"
        );
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        return res;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }
}