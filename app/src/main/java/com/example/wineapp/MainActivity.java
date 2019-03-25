package com.example.wineapp;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        WineListAdapter.OnWineListener,
        DetailFragment.OnDetailSelectListener
        {
    private static final String TAG = "MainActivity";
    private String[] data;

    // Fragments
    private DetailFragment detailFragment;

//==============================================
//    For Database layout
    // For all our buttons and edit text
    Button btnInsert;
    Button btnDelete;
    Button btnSelect;
    Button btnSearch;
    EditText editName;
    EditText editAge;
    EditText editDelete;
    EditText editSearch;
    // This is our DataManager instance
    DataManager dm;
    Button btnReturnToMain;
//==============================================

    Button btnDatabaseOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadMainLayout();

        dm = new DataManager(this);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnInsert:
                dm.insert(editName.getText().toString(), editAge.getText().toString());
                break;
            case R.id.btnSelect:
                showData(dm.selectAll());
                break;
            case R.id.btnSearch:
                showData(dm.searchName(editSearch.getText().toString()));
                break;
            case R.id.btnDelete:
                dm.delete(editDelete.getText().toString());
                break;
            case R.id.databaseOpen:
                loadDBLayout();
                break;
            case R.id.returnToMain:
                loadMainLayout();
                break;
            case R.id.showWineListButton:
                loadWineListLayout();
                break;
            case R.id.backToMainViewButton:
                loadMainLayout();
                break;
            case R.id.addWineButton:
                Log.w("MainActivity.onClick", "addWineButton NOT IMPLEMENTED!");
                Toast.makeText(this, "IMPLEMENT ME", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //    Output cursor contents to log
    public void showData (Cursor c) {
//        Moves cursor to next row
        while (c.moveToNext()){
//            Gets String at specified column
            Log.i(c.getString(1), c.getString(2));
        }
    }

    public void loadMainLayout(){
        setContentView(R.layout.activity_main);
        btnDatabaseOpen = findViewById(R.id.databaseOpen);
        btnDatabaseOpen.setOnClickListener(this);
    }

    public void loadDBLayout(){
        setContentView(R.layout.database_entry);
        // get a reference to the UI item
        btnInsert = findViewById(R.id.btnInsert);
        btnDelete = findViewById(R.id.btnDelete);
        btnSelect = findViewById(R.id.btnSelect);
        btnSearch = findViewById(R.id.btnSearch);
        btnReturnToMain = findViewById(R.id.returnToMain);

        editName   =  findViewById(R.id.editName);
        editAge    =  findViewById(R.id.editAge);
        editDelete =  findViewById(R.id.editDelete);
        editSearch =  findViewById(R.id.editSearch);

        // Register MainActivity as a listener
        btnSelect.setOnClickListener(this);
        btnInsert.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnReturnToMain.setOnClickListener(this);
    }

    public void loadWineListLayout() {
        setContentView(R.layout.wine_list);

        // *** wine list stuff ***
        RecyclerView wineList;
        RecyclerView.Adapter wineListAdapter;

        // create WineList recycle view
        wineList = findViewById(R.id.wineList);

        // improves performance if you know changes in content does not change layout size
        wineList.setHasFixedSize(true);

        wineList.setLayoutManager(new LinearLayoutManager(this));

        // TODO: get dataset from DB instead
        Random rand = new Random();
        data = new String[200];

        String[] name1 = new String[]{
                "Red",
                "Blue",
                "Crimson",
                "Pinot",
                "Cabernet",
                "Chateau",
                "Reisling",
                "Bordeaux",
                "Moscato",
                "Burgundy",
                "White",
                "New World",
                "Old World",
                "Chianti",
                "Merlot",
                "Malbec",
                "Sparkling",
                "Shiraz",
                "Zinfandel"
        };

        String[] name1a = new String[] {
                "Classic,",
                "Whopping",
                "Big",
                "The",
                "The Taco Bell",
                "Wet",
                "Poopy",
                "The German",
                "Shoe",
                "Grandpa",
                "Devil's"
        };

        String[] name2 = new String[]{
                "Noir",
                "Blanc",
                "",
                "Vintage",
                "Sauvignon",
                "Orange",
                "Magnolia",
                "Italia"
        };

        String[] name2a = new String[] {
                "Mac",
                "Philedelphia",
                "Slunch",
                "Buttpiss",
                "Megalomania",
                "Apocolypse",
                "Hammer",
                "Waffles",
                "Steele",
                "Special",
                "Orange",
                "Baseball",
                "Meat",
                "Garbage",
                "Loud Noises"
        };

        for (int i = 0; i < data.length; ++i) {
            int year = rand.nextInt(219) + 1800;
            double cost = 5 + Math.pow((2019 - year) / 3.0, 2);

            String color;
            switch (rand.nextInt(4)) {
                case 0:
                    color = "White";
                    break;
                case 1:
                    color = "Pink";
                    break;
                case 2:
                    color = "Amber";
                    break;
                default:
                    color = "Red";
                    break;
            }

            String cost_str = String.format(Locale.ENGLISH, "$%.2f", cost);

            String wine_name;
            if (rand.nextInt(100) == 0) {
                wine_name = name1a[rand.nextInt(name1a.length)];
            } else {
                wine_name = name1[rand.nextInt(name1.length)];
            }

            wine_name += " ";
            if (rand.nextInt(100) == 0) {
                wine_name += name2a[rand.nextInt(name2a.length)];
            } else {
                wine_name += name2[rand.nextInt(name2.length)];
            }

            data[i] = String.format(Locale.ENGLISH, "#%-4d %s \n\n  %d | %10s\t | %s", i + 1, wine_name, year, cost_str, color);
        }

        // need wine list adapter (class that feeds list view information)
        wineListAdapter = new WineListAdapter(data, this, this);
        wineList.setAdapter(wineListAdapter);
    }

    /* Implement OnWineClickListener interface. Takes position Int as argument.
     *
     */
    @Override
    public void onWineClick(int position) {
        String info = data[position];
        Log.d(TAG, "onWineClick: " + info);
        onDetailSelected(info);
    }

    /* Implement OnDetailSelectedListener for DetailFragment.
     *
     */
    @Override
    public void onDetailSelected(String info) {
        detailFragment = DetailFragment.newInstance(info);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.contentWindow, detailFragment, "DetailFragment")
                .addToBackStack(null)
                .commit();
    }
}
