package com.antonenkodev.heynow.heynow2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.model.LatLng;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by root on 02.06.15.
 */
public class MainActivity extends ActionBarActivity implements LocationListener, MapActivity.onMapEventListener, FragmentManager.OnBackStackChangedListener {

    //чтобы сделать главной активити**********
    private static ProgressDialog pd;
    static Context context;
    public static final String SOCIAL_NETWORK_TAG = "SocialIntegrationMain.SOCIAL_NETWORK_TAG";


    /*@Override
    protected void onStart() {
        super.onStart();
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        }
    }*/


    //***************************************************************************
    //**************************************************************************

    private final String TAG = "logs";

    private PlacesDbAdapter dbHelper;

    //for global variables
    CurrentPlaceDate appState;

    //final Context context = this;

    //map variables
    private long min_time;
    private long max_time;
    private double coordX;
    private double coordY;
    private double myCoordX;
    private double myCoordY;
    private int photosAmount=100;
    private int radius;

    //for instagram token
    SharedPreferences sPref;
    final String SAVED_INSTA_TOKEN = "settings";
    final String MY_PREF = "settings";


    //nawigation menu
    private Drawer.Result drawerResult = null;
    //toolbar
    Toolbar toolbar;
    //fragments for nawigation menu
    GalleryFragment galleryFragment;
    InstaVkSlideFragment instaVkSlideFragment;
    MapDateSlideFragment mapDateSlideFragment;
    SettingsFragment settingsFragment;
    PlaceListViewFragment placeListViewFragment;
    RegistrationFragment registrationFragment;
    FragmentTransaction fTrans;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //убирает заголовок
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        appState = ((CurrentPlaceDate)getApplicationContext());
        dbHelper = new PlacesDbAdapter(this);
        dbHelper.open();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        initDriwer();
        mapSettings();

        //for main******************
        context=this;
        getSupportFragmentManager().addOnBackStackChangedListener(this);

        sPref = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        String savedText = sPref.getString(SAVED_INSTA_TOKEN, null);
        Log.d(TAG,"love of token is " + savedText);
        //if (savedInstanceState == null&&savedText==null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container,new RegistrationFragment())
                    .commit();
        //}
        //************************************************************
        //perenoshu v otvet


        Log.d(TAG, "love start");

            Log.d(TAG, "Text love loaded: " + savedText);
            appState.setInsta_token(savedText);
            appState.setMaxRadius(sPref.getInt("maxRadius", 1000));
            appState.setRadius(sPref.getInt("defoultRadius", 1000));
            Log.d(TAG, "radius love loaded: " + String.valueOf(sPref.getInt("defoultRadius", 100)));
        // проверяем, первый ли раз открывается программа
        boolean hasReg = sPref.getBoolean("hasReg", false);

        /*if (!hasReg) {
            // выводим нужную активность
            SharedPreferences.Editor e = sPref.edit();
            e.putBoolean("hasVisited", true);
            e.commit(); // не забудьте подтвердить изменения
        }
        if(savedText!=null||!savedText.equals("")) {
            galleryFragment = new GalleryFragment();
            fTrans = getSupportFragmentManager().beginTransaction();
            fTrans.add(R.id.container, galleryFragment);
            Log.d(TAG, "love start two");
            fTrans.commit();
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();

    }



    @Override
      public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            settingsFragment = new SettingsFragment();
                            fTrans = getSupportFragmentManager().beginTransaction();
                            fTrans.replace(R.id.container, settingsFragment);
                            fTrans.commit();
            return true;
        }
        if (id == R.id.action_search) {
            callSaveDialog();
            return true;
        }
        if (id == R.id.action_show) {
            callShowDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void callShowDialog() {
        galleryFragment = new GalleryFragment();
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.container, galleryFragment);
        fTrans.commit();
    }

    private void callSaveDialog() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                Editable countryCode =userInput.getText();
                                dbHelper.createPlace(String.valueOf(countryCode),
                                        getAddress(appState.getCoordY(), appState.getCoordX())
                                        , appState.getCoordX()
                                        , appState.getCoordY());
                                if(placeListViewFragment!=null)
                                placeListViewFragment.displayListView();
                                Toast.makeText(getApplicationContext(),
                                        countryCode, Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    //настраиваем тулбар
    public void initDriwer(){
        drawerResult= new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_newspaper_o),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_google_map).withIcon(FontAwesome.Icon.faw_paper_plane),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_puzzle_piece),
                        new SectionDrawerItem().withName(R.string.drawer_item_settings),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_data_base).withIcon(FontAwesome.Icon.faw_globe),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_social).withIcon(FontAwesome.Icon.faw_user_plus),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_rate_us).withIcon(FontAwesome.Icon.faw_heart)
                )
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Скрываем клавиатуру при открытии Navigation Drawer
                        InputMethodManager inputMethodManager = (InputMethodManager) MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                    }
                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    // Обработка клика
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (id == 0) {
                            galleryFragment = new GalleryFragment();
                            fTrans = getSupportFragmentManager().beginTransaction();
                            fTrans.replace(R.id.container, galleryFragment);
                            fTrans.commit();
                        }
                        if (id == 2) {
                            instaVkSlideFragment = new InstaVkSlideFragment();
                            fTrans = getSupportFragmentManager().beginTransaction();
                            fTrans.replace(R.id.container, instaVkSlideFragment);
                            fTrans.commit();
                        }
                        if (id == 1) {
                            mapDateSlideFragment = new MapDateSlideFragment();
                            fTrans = getSupportFragmentManager().beginTransaction();
                            fTrans.replace(R.id.container, mapDateSlideFragment);
                            fTrans.commit();
                        }

                        if (id == 4) {
                            placeListViewFragment = new PlaceListViewFragment();
                            fTrans = getSupportFragmentManager().beginTransaction();
                            fTrans.replace(R.id.container, placeListViewFragment);
                            fTrans.commit();
                        }
                        if (id == 5) {
                            if (appState.getInsta_token() == null || appState.getInsta_token() == "") {
                                registrationFragment = new RegistrationFragment();
                                fTrans = getSupportFragmentManager().beginTransaction();
                                fTrans.replace(R.id.container, registrationFragment);
                                fTrans.commit();
                            } else {
                                startProfile(7);

                            }
                        }
                        if (id == 6) {
                            rateApp();
                        }

                        if (drawerItem instanceof Badgeable)

                        {
                            Badgeable badgeable = (Badgeable) drawerItem;
                            if (badgeable.getBadge() != null) {
                                // учтите, не делайте так, если ваш бейдж содержит символ "+"
                                try {
                                    int badge = Integer.valueOf(badgeable.getBadge());
                                    if (badge > 0) {
                                        drawerResult.updateBadge(String.valueOf(badge - 1), position);
                                    }
                                } catch (Exception e) {
                                    Log.d("test", "Не нажимайте на бейдж, содержащий плюс! :)");
                                }
                            }
                        }
                    }
                })
                .build();
    }

    public void rateApp() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("market://details?id=com.antonenkodev.heynow.heynow2"));
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        // Закрываем Navigation Drawer по нажатию системной кнопки "Назад" если он открыт
        if (drawerResult.isDrawerOpen()) {
            drawerResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void mapSettings(){
        // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);

        if(location!=null){
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(provider, 20000, 0, this);
        //настроим запрос
        if (appState.getMax_time()==0) {
            max_time = System.currentTimeMillis() / 1000L;
            min_time = max_time - 10000;
            Log.d(TAG,"love is tutochki");

            appState.setMax_time(max_time);
            appState.setMin_time(min_time);
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);
        //take the position
        // set my Coords
        myCoordX = latLng.longitude;
        myCoordY = latLng.latitude;
        coordX = latLng.longitude;
        coordY = latLng.latitude;
        Log.d(TAG,"my lova adres is" + getAddress(myCoordY,myCoordX));
        radius=appState.getRadius();
        //если врервые
        Log.d(TAG, "love with state before, firstGallery:" + appState.isFirstGallery());
        if (appState.isFirstGallery()){
            appState.setCoordX(myCoordX);
            appState.setCoordY(myCoordY);
            appState.setFirstGallery(false);

        }
        Log.d(TAG, "love with state, coordX:" + appState.getCoordX());
        Log.d(TAG, "love with state, firstGallery:" + appState.isFirstGallery());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void mapCoordEvent(double x, double y) {
        coordX=x;
        coordY=y;
    }

    @Override
    public void mapRadiusEvent(int r) {
        radius = r;
    }
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new DateAndPlace.TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DateAndPlace.DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public void showTimeEndPickerDialog(View v) {
        DialogFragment newFragment = new DateAndPlace.TimeEndPickerFragment();
        newFragment.show(getSupportFragmentManager(), "timeEndPicker");
    }
    public void showDateEndPickerDialog(View v) {
        DialogFragment newFragment = new DateAndPlace.DateEndPickerFragment();
        newFragment.show(getSupportFragmentManager(), "dateEndPicker");
    }
    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getLocality()).append(", ");
                result.append(address.getCountryName());
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }
//********************************************************
    //for main ************************************
    @Override
    public void onBackStackChanged() {

    }

    protected static void showProgress(String message) {
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage(message);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
    }

    protected static void hideProgress() {
        pd.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Fragment fragment = getSupportFragmentManager().findFragmentByTag(SOCIAL_NETWORK_TAG);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
    //**************************************
      //**************************
    private void startProfile(int networkId){
        ProfileFragment profile = ProfileFragment.newInstannce(networkId);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack("profile")
                .replace(R.id.container, profile)
                .commit();
    }

}
