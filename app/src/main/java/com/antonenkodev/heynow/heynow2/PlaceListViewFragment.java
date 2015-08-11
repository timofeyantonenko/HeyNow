package com.antonenkodev.heynow.heynow2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by root on 10.06.15.
 */
public class PlaceListViewFragment extends Fragment {
    private PlacesDbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;

    private static final String MY_SETTINGS = "settings";

    //for global variables
    CurrentPlaceDate appState;
    private FragmentActivity myContext;

    View v;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myContext=(FragmentActivity)activity;
        appState = ((CurrentPlaceDate)myContext.getApplicationContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.db_list_fragment,null);
        dbHelper = new PlacesDbAdapter(this.getActivity());
        dbHelper.open();
        SharedPreferences sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        // проверяем, первый ли раз открывается программа
        boolean hasVisited = sp.getBoolean("hasVisited", false);

        if (!hasVisited) {
            // delete data

            dbHelper.deleteAllPlaces();
            //Add some data
            dbHelper.insertSomePlaces();

            SharedPreferences.Editor e = sp.edit();
            e.putBoolean("hasVisited", true);
            e.commit(); // не забудьте подтвердить изменения
        }



        //Generate ListView from SQLite Database
        displayListView();

        return v;
    }

    public void displayListView() {
        Cursor cursor = dbHelper.fetchAllPlaces();
        // The desired columns to be bound
        final String[] columns = new String[] {
                PlacesDbAdapter.KEY_CODE,
                PlacesDbAdapter.KEY_NAME,
                PlacesDbAdapter.KEY_LONG,
                PlacesDbAdapter.KEY_LAT
        };
        // the XML defined views which the data will be bound to
        final int[] to = new int[] {
                R.id.code,
                R.id.name,
                R.id.lon,
                R.id.lat,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this.getActivity(), R.layout.place_info,
                cursor,
                columns,
                to,
                0);
        ListView listView = (ListView)v.findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                appState.setCoordX(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("long"))));
                appState.setCoordY(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("lat"))));

                // Get the state's capital from this row in the database.
                String countryCode =
                        cursor.getString(cursor.getColumnIndexOrThrow("long"));
                countryCode = countryCode + " \n " + cursor.getString(cursor.getColumnIndexOrThrow("lat"));
                Toast.makeText(getActivity().getApplicationContext(),
                        countryCode, Toast.LENGTH_SHORT).show();

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> listView, View view,
                                           final int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(myContext);
                View promptsView = li.inflate(R.layout.delete_prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        myContext);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);


                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //deleting
                                        Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                                        String countryCode =
                                                cursor.getString(cursor.getColumnIndexOrThrow("code"));
                                        dbHelper.deletePlace(countryCode);
                                        displayListView();

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                return true;
            }
        });

        EditText myFilter = (EditText)v.findViewById(R.id.myFilter);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchPlacesByName(constraint.toString());
            }
        });
    }


}
