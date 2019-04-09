package com.example.wineapp;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        WineDetailFragment.OnDetailSelectListener,
        AddWineFragment.OnAddWineListener
{
    private static final String TAG = "MainActivity";
    private List<Wine> data;
    private RecyclerView.Adapter wineListAdapter;

//==============================================
//    For Database layout
    // For all our buttons and edit text
    Button btnInsert;
    Button btnDelete;
    Button btnSelect;
    Button btnSearch;
    Button btnDebug;
    Button btnUpdate;
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
        loadWineListLayout();
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnInsert:
                dm.insert(editName.getText().toString(), editAge.getText().toString());
                break;
            case R.id.btnSelect:
                dm.printWineList(dm.selectAll());
                break;
            case R.id.btnSearch:
                HashMap<String, String> wineRack = new HashMap<>();
                wineRack.put(DataManager.TABLE_ROW_NAME, "Wet Garbage");
                wineRack.put(DataManager.TABLE_ROW_COLOR, (Wine.Color.RED).toString());

                final List<Wine> wineList = dm.find(wineRack);
                dm.printWineList(wineList);
                break;
            case R.id.btnDebug:
                Wine testWine = new Wine(1, "Wet Garbage", "Yellowtail", Wine.Color.RED, 5.22, "Concord", 9.0);
                Wine newWine = dm.insertWine(testWine);
                Log.i("newWine: ", newWine.toString());
//                dm.checkCols();
//                showData(dm.checkCols());
//                dm.parse_flex(debugField.getText().toString());
//                dm.parse_flex("name Chateau Berliquet 2015 // brand Bordeaux Red Blends // rating 4.5 // cost 39.99 // color Red");
                break;
            case R.id.btnUpdate:
                HashMap<String, String> wineChange = new HashMap<>();
                wineChange.put(DataManager.TABLE_ROW_NAME, "Dry Gaarboge");
                wineChange.put(DataManager.TABLE_ROW_COLOR, (Wine.Color.AMBER).toString());
                boolean success = dm.update(1, wineChange);
                Log.i("Completed", Boolean.toString(success));
                break;
            case R.id.btnDelete:
                Log.i("delete: ", Boolean.toString(dm.delete(Integer.parseInt(editDelete.getText().toString()))));
                break;
            case R.id.databaseOpen:
                loadDBLayout();
                break;
            case R.id.returnToMain:
                loadMainLayout();
                break;
            case R.id.showWineListButton:
//                Layout will always be in view.
//                loadWineListLayout();
                break;
            case R.id.backToMainViewButton:
                loadMainLayout();
                break;
            case R.id.wineDetailDeleteButton:
                // get wine object
                final Wine w = (Wine) v.getTag();

                // prompt for delete
                final Context c = this;
                final FragmentManager fm = this.getSupportFragmentManager();
                final WineListAdapter wineListAdapter = (WineListAdapter) this.wineListAdapter;

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Delete this wine?");

                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // pop wine detail fragment off back-stack
                                fm.popBackStack("WineDetail", FragmentManager.POP_BACK_STACK_INCLUSIVE);

                                // notify user
                                Toast.makeText(
                                        c,
                                        "Wine #" + w.id() + ": \"" + w.name() + "\" removed.",
                                        Toast.LENGTH_LONG
                                ).show();

                                // delete wine from database
                                dm.delete(w.id());

                                // update the recycler view dataset
                                wineListAdapter.refresh();
                            }
                        }
                );

                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                break;
            case R.id.addWineButton:
                Log.w("MainActivity.onClick", "addWineButton NOT IMPLEMENTED!");
                onAddWine();
                break;
        }
    }

    //    Output cursor contents to log
    public void showData (Cursor c) {
//        Moves cursor to next row
        while (c.moveToNext()){
//            Gets String at specified column
            Log.i(c.getString(0), c.getString(1) + ", " + c.getString(2) + ", "
                 + c.getString(3) + ", " + c.getString(4) + ", " + c.getString(5));
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
        btnUpdate = findViewById(R.id.btnUpdate);
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
        btnUpdate.setOnClickListener(this);
        btnReturnToMain.setOnClickListener(this);
    }

    public void loadWineListLayout() {
        setContentView(R.layout.wine_list);

        // *** wine list stuff ***
        RecyclerView wineList;

        // create WineList recycle view
        wineList = findViewById(R.id.wineList);

        // improves performance if you know changes in content does not change layout size
        wineList.setHasFixedSize(true);

        wineList.setLayoutManager(new LinearLayoutManagerScrollEnable(this));

        // reload wine dataset
        this.data = this.dm.selectAll();
        this.dm.printWineList((this.data));
        // need wine list adapter (class that feeds list view information)
        this.wineListAdapter = new WineListAdapter(data, this.dm, this);
        wineList.setAdapter(wineListAdapter);
    }


    /* Implement OnDetailSelectedListener for WineDetailFragment.
     *
     */
    @Override
    public void onDetailSelected(int wine_id) {
        Wine info = this.data.get(wine_id);
        WineDetailFragment detailFragment = WineDetailFragment.newInstance(info);
        getSupportFragmentManager().beginTransaction()
            .add(R.id.wineListContentWindow, detailFragment, "DetailFragment")
            .addToBackStack("WineDetail")
            .commit();

        Log.d(TAG, "onWineClick: " + info);
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
