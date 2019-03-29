package com.example.wineapp;

import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AddWineFragment.OnAddWineListener {
    private static final String TAG = "MainActivity";
    private String[] data;

//==============================================
//    For Database layout
    // For all our buttons and edit text
    Button btnInsert;
    Button btnDelete;
    Button btnSelect;
    Button btnSearch;
    Button btnDebug;
    EditText editName;
    EditText editAge;
    EditText editDelete;
    EditText editSearch;
    EditText debugField;
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

        // TODO: delete this when not needed anymore
        Wine testWine = new Wine(0, "Wet Garbage", "Yellowtail", Wine.Color.RED, 5.22, "Concord");
        Log.i("Wine test", testWine.toString());
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
            case R.id.btnDebug:
                dm.checkCols();
//                showData(dm.checkCols());
//                dm.parse_flex(debugField.getText().toString());
//                dm.parse_flex("name Chateau Berliquet 2015 // brand Bordeaux Red Blends // rating 4.5 // cost 39.99 // color Red");
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
//                Toast.makeText(this, "IMPLEMENT ME", Toast.LENGTH_SHORT).show();
                onAddWine();
                break;
            case R.id.wineList:
                // we get this view when someone clicks on a wine list entry
                loadWineDetailLayout((int)v.getTag());
                break;
            case R.id.wineDetailBackToList:
                unloadWineDetailLayout();
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
        btnDebug = findViewById(R.id.btnDebug);
        btnReturnToMain = findViewById(R.id.returnToMain);

        editName   =  findViewById(R.id.editName);
        editAge    =  findViewById(R.id.editAge);
        editDelete =  findViewById(R.id.editDelete);
        editSearch =  findViewById(R.id.editSearch);
        debugField   =  findViewById(R.id.debugField);

        // Register MainActivity as a listener
        btnSelect.setOnClickListener(this);
        btnInsert.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnDebug.setOnClickListener(this);
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

    public void loadWineDetailLayout(int wine_id) {
        // don't just call setContentView because we don't want to destroy the wineList
        FrameLayout wine_list_layout = findViewById(R.id.wineListFrameLayout);
        View detail_view = getLayoutInflater().inflate(R.layout.wine_detail, wine_list_layout);
        detail_view.setElevation(10000);

        // disable all widgets underneath detail view
        this.setViewEnable(findViewById(R.id.mainContentLayout), false);
        this.setViewEnable(findViewById(R.id.backToMainViewButton), false);

        String info = data[wine_id];
        Log.d(TAG, "onWineClick: " + info);

        TextView wine_details = findViewById(R.id.info);
        wine_details.setText(info);
    }

    public void unloadWineDetailLayout() {
        FrameLayout wine_frame_layout = findViewById(R.id.wineListFrameLayout);
        wine_frame_layout.removeView(findViewById(R.id.wineDetailContents));

        // re-enable views that were underneath the frame layout
        this.setViewEnable(findViewById(R.id.mainContentLayout), true);
        this.setViewEnable(findViewById(R.id.backToMainViewButton), true);
    }

    private void setViewEnable(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewEnable(child, enabled);
            }
        }
    }

    /* Implement OnAddWineListener for AddWineFragment.
     *
     */
    @Override
    public void onAddWine() {
        AddWineFragment addWineFragment = AddWineFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(
                R.anim.enter_from_bottom,
                R.anim.exit_from_top,
                R.anim.enter_from_bottom,
                R.anim.exit_from_top
        );
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.wineListContentWindow, addWineFragment, "AddWineFragment").commit();

    }
}
