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

        // Deletes the database so that it is always compatible
//        TODO: Remove when database is more stable
        context.deleteDatabase(DB_NAME);
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

//    ============================================================================================
//    Required Functions
//    ============================================================================================
//    Insert into DB
public void insertWine (Wine wine){
    List<String> columnNames = new ArrayList<String>();
    List<String> contents = new ArrayList<String>();
//        columnNames.add(TABLE_ROW_ID);
    columnNames.add(TABLE_ROW_NAME);
    columnNames.add(TABLE_ROW_BRAND);
//        columnNames.add(TABLE_ROW_RATING);
    columnNames.add(TABLE_ROW_COST);
    columnNames.add(TABLE_ROW_COLOR);

    // TODO: Cant set id (auto-incrementing)
//        contents.add(wine.id().toString());
    contents.add(wine.name());
    contents.add(wine.brand());
    // TODO: Rating field needs to be added
//        wine.rating();
    contents.add(Double.toString(wine.cost()));
    // TODO: How to convert enum into String?
    contents.add(wine.color().toString());
    insert_flex(columnNames, contents);
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

//    //    Find a specific record
//    public Cursor find (List<String> columnNames, List<String> contents){

//        String query = "SELECT " +
//                TABLE_ROW_ID   + ", " +
//                TABLE_ROW_NAME + ", " +
//                TABLE_ROW_BRAND  +
//                " from " +
//                TABLE_WINE + " WHERE " +
//                TABLE_ROW_NAME + " = '" + name + "';";
//
//        Cursor c = db.rawQuery(query, null);
//        return c;
//    }



//    ============================================================================================
//    Helper Functions
//    ============================================================================================

    public String verifyCols(List<String> columnNames){
        String contentCols = "";
        for(int i = 0; i < columnNames.size(); i++){
            // if(columnNames.get(i) in tableRowNames)
            contentCols += columnNames.get(i) + ", ";
        }
        contentCols = contentCols.trim();
        contentCols.substring(0, contentCols.length()-1);
        return contentCols;
    }
    public String verifyContents(List<String> contents){
        String contentVals = "";
        for(int i = 0; i < contents.size(); i++){
            contentVals += "'" + contents.get(i) + "'" + ", ";
        }
        contentVals = contentVals.trim();        
        contentVals.substring(0, contentVals.length()-1);
        return contentVals;
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

    //    Insert into DB
    public void insert (String name, String brand){
        String query = "INSERT INTO " + TABLE_WINE + " (" +
                TABLE_ROW_NAME + ", " +
                TABLE_ROW_BRAND + " ) " +
                "VALUES (" +
                "'" + name + "'" + ", " +
                "'" + brand + "'" + ");";
        // Print Statement
        Log.i("insert() = ", query);

        // Execute command
        db.execSQL(query);
    }

    //    Parse Flex command into DB
    public void parse_flex (String flexStr){

//        Log.i("parse_flex: ", flexStr);

        String[] arrString       = flexStr.split("//");
        List<String> columnNames = new ArrayList<String>();
        List<String> contents    = new ArrayList<String>();

        for(int i = 0; i < arrString.length; i++ ){
//            Log.i("arrString: ", arrString[i]);

            String[] subStr = arrString[i].trim().split(" ", 2);

//            Log.i("subStr[0]: ", subStr[0].trim());
//            Log.i("subStr[1]: ", subStr[1].trim());

            columnNames.add(subStr[0].trim());
            contents.add(subStr[1].trim());
        }
        Log.i("Names: ", columnNames.toString());
        Log.i("Contents: ", contents.toString());

        insert_flex(columnNames, contents);
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
        contentCols = contentCols.trim();
        contentVals = contentVals.trim();

        // Log.i("Names: ", contentCols.substring(0, contentCols.length()-1));
        // Log.i("Contents: ", contentVals.substring(0, contentVals.length()-1));


        query += contentCols.substring(0, contentCols.length()-1) + " ) "
                    + "VALUES ("
                    + contentVals.substring(0, contentVals.length()-1) + ");";

        // Print Statement
        Log.i("insert() = ", query);

        // Execute command
        db.execSQL(query);
    }


    public void checkCols(){
        Cursor dbCursor = db.query(TABLE_WINE,null,null,null,null,null,null);
        String[] columnNames = dbCursor.getColumnNames();
        String debug = "[";
        for(int i = 0; i < columnNames.length; i++ ) {
            debug += columnNames[i] + ", ";
        }
        debug += "]";

        Log.i("ColNames: ", debug);
    }


}
