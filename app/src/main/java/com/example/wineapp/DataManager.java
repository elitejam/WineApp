package com.example.wineapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private SQLiteDatabase db;
    //    Row and Table Names used outside
    public static final String TABLE_ROW_ID         = "_id";
    public static final String TABLE_ROW_ID_DEF     = "integer primary key autoincrement not null";    
    public static final String TABLE_ROW_NAME       = "name";
    public static final String TABLE_ROW_NAME_DEF   = "text not null";    
    public static final String TABLE_ROW_BRAND      = "brand";
    public static final String TABLE_ROW_BRAND_DEF  = "text";
    public static final String TABLE_ROW_RATING     = "rating";
    public static final String TABLE_ROW_RATING_DEF = "real";
    public static final String TABLE_ROW_COST       = "cost";
    public static final String TABLE_ROW_COST_DEF   = "real";
    public static final String TABLE_ROW_COLOR      = "color";
    public static final String TABLE_ROW_COLOR_DEF  = "text";

    //    Row and Table used inside
    private static final String DB_NAME = "wine_list_db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_WINE = "wine_list";

    public List<String> tableRowNames;
    public List<String> tableRowProps;

// ===================================================
// Constructors
// ===================================================

    public DataManager (Context context){

        tableRowNames = new ArrayList<String>();
        tableRowProps = new ArrayList<String>();
        tableRowNames.add(TABLE_ROW_ID);
        tableRowProps.add(TABLE_ROW_ID_DEF);
        tableRowNames.add(TABLE_ROW_NAME);
        tableRowProps.add(TABLE_ROW_NAME_DEF);
        tableRowNames.add(TABLE_ROW_BRAND);
        tableRowProps.add(TABLE_ROW_BRAND_DEF);
        tableRowNames.add(TABLE_ROW_RATING);
        tableRowProps.add(TABLE_ROW_RATING_DEF);
        tableRowNames.add(TABLE_ROW_COST);
        tableRowProps.add(TABLE_ROW_COST_DEF);
        tableRowNames.add(TABLE_ROW_COLOR);
        tableRowProps.add(TABLE_ROW_COLOR_DEF);
        Log.i("Column size: ", Integer.toString(tableRowNames.size()));

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
                    + TABLE_WINE   + " ( ";

            String columns = "";

            for(int i = 0; i < tableRowNames.size(); i++){
                columns += tableRowNames.get(i) + " " + tableRowProps.get(i) + ",";
            }
            columns = columns.substring(0, columns.length()-1);
            columns += ");";
            newTableQuery += columns;
            Log.i("Create Table: ", newTableQuery);

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
        String query = "INSERT INTO " + TABLE_WINE + " (" +
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
    //    Insert into DB
    public void insert_flex (List<String> columnNames, List<String> contents){
        String query = "INSERT INTO " + TABLE_WINE + " ( ";

        String contentCols = "";
        String contentVals = "";
        for(int i = 0; i < columnNames.size(); i++){
            contentCols += columnNames.get(i) + ", ";
            contentVals += "'" + contents.get(i) + "'" + ", ";
        }

        query += contentCols.substring(0, contentCols.length()-1) + " ) "
                    + "VALUES (" 
                    + contentVals.substring(0, contentVals.length()-1) + ");";
                    
        // Print Statement
        Log.i("insert() = ", query);

        // Execute command
        db.execSQL(query);
    }

    //    Deletes an entry
    public void delete(String name){
        String query = "DELETE FROM " + TABLE_WINE +
                " WHERE " + TABLE_ROW_NAME +
                " = '" + name + "';";

        Log.i("delete(); = ", query);
        db.execSQL(query);
    }

    //    Get all the records
    public Cursor selectAll() {
        Cursor c = db.rawQuery("SELECT *" + " from " +
                TABLE_WINE, null);
        return c;
    }
    //    Find a specific record
    public Cursor searchName (String name){
        String query = "SELECT " +
                TABLE_ROW_ID   + ", " +
                TABLE_ROW_NAME + ", " +
                TABLE_ROW_BRAND  +
                " from " +
                TABLE_WINE + " WHERE " +
                TABLE_ROW_NAME + " = '" + name + "';";

        Cursor c = db.rawQuery(query, null);
        return c;
    }
}
