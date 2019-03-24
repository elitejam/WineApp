package com.example.wineapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataManager {
    private SQLiteDatabase db;
    //    Row and Table Names used outside
    public static final String TABLE_ROW_ID        = "_id";
    public static final String TABLE_ROW_ID_DEF    = "integer primary key autoincrement not null";    
    public static final String TABLE_ROW_NAME      = "name";
    public static final String TABLE_ROW_NAME_DEF  = "text not null";    
    public static final String TABLE_ROW_BRAND     = "age";
    public static final String TABLE_ROW_BRAND_DEF = "text not null";    

    //    Row and Table used inside
    private static final String DB_NAME = "address_book_db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_N_AND_A = "names_and_addresses";

// ===================================================
// Constructors
// ===================================================

    //    Q: What is a Context Object
    public DataManager (Context context){
//        Create an instance of the SQLiteHelper
        CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
        db = helper.getWritableDatabase();
    }
    private class CustomSQLiteOpenHelper extends SQLiteOpenHelper {
        public CustomSQLiteOpenHelper(Context context){
            super(context, DB_NAME, null, DB_VERSION);
        }
        // Replaces the Override command in the parent class SQLiteOpenHelper
        @Override
        public void onCreate(SQLiteDatabase db) {
            // Create a table
            String newTableQuery = "create table "
                    + TABLE_N_AND_A   + " ( "
                    + TABLE_ROW_ID    + " " + TABLE_ROW_ID_DEF    + ","
                    + TABLE_ROW_NAME  + " " + TABLE_ROW_NAME_DEF  + ","
                    + TABLE_ROW_BRAND + " " + TABLE_ROW_BRAND_DEF + ");";

            db.execSQL(newTableQuery);
        }
        // Used when upgrading versions
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            // Do nothing
        }
    }

    //    Insert into DB
    public void insert (String name, String age){
        String query = "INSERT INTO " + TABLE_N_AND_A + " (" +
                TABLE_ROW_NAME + ", " +
                TABLE_ROW_BRAND + " ) " +
                "VALUES (" +
                "'" + name + "'" + ", " +
                "'" + age + "'" + ");";
        // Print Statement
        Log.i("insert() = ", query);

        // Execute command
        db.execSQL(query);
    }
    //    Deletes an entry
    public void delete(String name){
        String query = "DELETE FROM " + TABLE_N_AND_A +
                " WHERE " + TABLE_ROW_NAME +
                " = '" + name + "';";

        Log.i("delete(); = ", query);
        db.execSQL(query);
    }
    //    Get all the records
    public Cursor selectAll() {
        Cursor c = db.rawQuery("SELECT *" + " from " +
                TABLE_N_AND_A, null);
        return c;
    }
    //    Find a specific record
    public Cursor searchName (String name){
        String query = "SELECT " +
                TABLE_ROW_ID   + ", " +
                TABLE_ROW_NAME + ", " +
                TABLE_ROW_BRAND  +
                " from " +
                TABLE_N_AND_A + " WHERE " +
                TABLE_ROW_NAME + " = '" + name + "';";

        Cursor c = db.rawQuery(query, null);
        return c;
    }
}
