package com.example.wineapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
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
        tableRowNames.add(TABLE_ROW_ID);            // 0
        tableRowProps.add(TABLE_ROW_ID_DEF);        // 0
        tableRowNames.add(TABLE_ROW_NAME);          // 1
        tableRowProps.add(TABLE_ROW_NAME_DEF);      // 1
        tableRowNames.add(TABLE_ROW_BRAND);         // 2
        tableRowProps.add(TABLE_ROW_BRAND_DEF);     // 2
        tableRowNames.add(TABLE_ROW_RATING);        // 3
        tableRowProps.add(TABLE_ROW_RATING_DEF);    // 3
        tableRowNames.add(TABLE_ROW_COST);          // 4
        tableRowProps.add(TABLE_ROW_COST_DEF);      // 4
        tableRowNames.add(TABLE_ROW_COLOR);         // 5
        tableRowProps.add(TABLE_ROW_COLOR_DEF);     // 5
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
public Wine insertWine (Wine wine){
    HashMap<String, String> content = new HashMap<>();
    content.put(TABLE_ROW_NAME, wine.name());
    content.put(TABLE_ROW_BRAND, wine.brand());
    content.put(TABLE_ROW_RATING, Double.toString(wine.rating()));
    content.put(TABLE_ROW_COST, Double.toString(wine.cost()));
    content.put(TABLE_ROW_COLOR, wine.color().toString());

    insert_flex(content);

    wine.id(getLastId());
    return wine;
}

    //    Deletes an entry and returns true if there was an entry and now it is deleted
    public boolean delete(int id){
        int numAffected = db.delete(TABLE_WINE, TABLE_ROW_ID + "=?",
                            new String[]{Integer.toString(id)});
        return numAffected > 0 ? true : false;
    }

    //    Get all the records
    public List<Wine> selectAll() {
        Cursor c = db.rawQuery("SELECT *" + " from " +
                TABLE_WINE, null);
        List<Wine> wineList;
        wineList = cursorToWineList(c);
        return wineList;
    }

    //    Find a specific record
    public List<Wine> find (HashMap<String, String> contents){
        // List<String> columnNames, List<String> contents
        String query = "SELECT * from " + TABLE_WINE + " WHERE ";

        String contentCols = "";
        String andOp = "' AND ";
        int strLen = andOp.length()-1;
        for(HashMap.Entry<String, String> entry : contents.entrySet()){
            contentCols += entry.getKey() + " = '" + entry.getValue() + andOp;
        }

        contentCols = contentCols.substring(0, contentCols.length()-strLen);

        query += contentCols + ";";

        Cursor c = db.rawQuery(query, null);
        return cursorToWineList(c);
    }
    // Updates an existing Wine entry
    public boolean update(int id, HashMap<String, String> propMap){
        ContentValues values = new ContentValues();
        for(HashMap.Entry<String, String> entry : propMap.entrySet()){
            values.put(entry.getKey(), entry.getValue());
        }

        int numAffected = db.update(TABLE_WINE, values, TABLE_ROW_ID + "=?",
                            new String[]{Integer.toString(id)});

        return numAffected > 0 ? true : false;
    }

//    Takes full cursors and makes wine objects with it
    public List<Wine> cursorToWineList (Cursor c){
        List<Wine> wineList = new ArrayList<>();
        while (c.moveToNext()){
//            Gets String at specified column
//            TODO: Add grapetype once that is included in DB
            Wine wine = new Wine(c.getInt(0), c.getString(1), c.getString(2), Wine.Color.valueOf(c.getString(5)),c.getDouble(4), "", c.getDouble(3));
            wineList.add(wine);
        }
        return wineList;
    }

    public void printWineList(List<Wine> wineList){
        String str = "";
        for(int i = 0; i < wineList.size(); i++){
            str += wineList.get(i).toString();
        }
        Log.i("Wine List: ", str);
    }
//    ============================================================================================
//    Helper Functions
//    ============================================================================================

    public int getLastId(){
        String query = "SELECT last_insert_rowid()";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        return c.getInt(0);
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

    //    Insert into DB
    public void insert_flex (HashMap<String, String> contents){
        String query = "INSERT INTO " + TABLE_WINE + " ( ";

        String contentCols = "";
        String contentVals = "";

        for(HashMap.Entry<String, String> entry : contents.entrySet()){
            contentCols += entry.getKey() + ", ";
            contentVals += "'" + entry.getValue() + "'" + ", ";
        }

        contentCols = contentCols.trim();
        contentVals = contentVals.trim();

        query += contentCols.substring(0, contentCols.length()-1) + " ) "
                    + "VALUES ("
                    + contentVals.substring(0, contentVals.length()-1) + ");";

        // Print Statement
        Log.i("insert() = ", query);

        // Execute command
        db.execSQL(query);
    }

    // Prints the Columns that exist in the database
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
