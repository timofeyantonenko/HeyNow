package com.antonenkodev.heynow.heynow2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Fragment implements LocationListener, SeekBar.OnSeekBarChangeListener {

    private FragmentActivity myContext;
    //интент для запуска активности с фото
    CurrentPlaceDate appState;
    //координаты маркера
    private double coordX;
    private double coordY;
    private double myCoordX;
    private double myCoordY;
    //время отсчета
    private long max_time;

    private int radius=100;

    private boolean atFirst=true;
    //фрагмент для карты
    SupportMapFragment mapFragment;
    GoogleMap map;
    final String TAG = "myLogs";
    private Marker marker;
    //кнопки для запуска активности с фото
    private TextView mTextValue;

    Circle circle;
    CircleOptions circleOptions;


    @Override
    public void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);
    }

    public interface onMapEventListener {
        public void mapCoordEvent(double x, double y);
        public void mapRadiusEvent(int r);
    }
    onMapEventListener mapEventListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myContext=(FragmentActivity) activity;
        try {
            mapEventListener = (onMapEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_layout,null);
        Log.d(TAG,"MapActivity love onCreatrView");
        final SeekBar seekbar = (SeekBar)v.findViewById(R.id.seekBar);
        seekbar.setMax(appState.getMaxRadius());
        seekbar.setOnSeekBarChangeListener(this);
        mTextValue = (TextView)v.findViewById(R.id.textView2);
        mTextValue.setText("0");
        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getBaseContext());

        // Showing status
        if(status!= ConnectionResult.SUCCESS){ // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getActivity(), requestCode);
            dialog.show();

        }else {
            mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                    .findFragmentById(R.id.map);
            if(mapFragment!=null)
                Log.d(TAG,"love is not null");
            else
                Log.d(TAG,"love is null");
            map = mapFragment.getMap();
            // Enabling MyLocation Layer of Google Map
            map.setMyLocationEnabled(true);
            UiSettings settings = map.getUiSettings();
            settings.setZoomControlsEnabled(true);
            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
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

        }
        init();
        return  v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (marker!=null){
            LatLng latLng = new LatLng(appState.getCoordY(), appState.getCoordX());
            // Showing the current location in Google Map
             map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            // Zoom in the Google Map
             map.animateCamera(CameraUpdateFactory.zoomTo(15));


        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "MapActivity love onCreate");
        appState = ((CurrentPlaceDate)myContext.getApplicationContext());
//        setRetainInstance(true);

    }
    @Override
    public void onLocationChanged(Location location) {


        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        if(atFirst) {
            Log.d(TAG,"love at first is true");
            // Creating a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            // Zoom in the Google Map
            map.animateCamera(CameraUpdateFactory.zoomTo(15));
            coordX=latLng.longitude;
            coordY=latLng.latitude;
            myCoordX=latLng.longitude;
            myCoordY=latLng.latitude;
            // Showing the current location in Google Map
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            // Zoom in the Google Map
            map.animateCamera(CameraUpdateFactory.zoomTo(15));
            atFirst=false;
        }
        Log.d(TAG,"love at first is false");
        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(appState.getCoordY(), appState.getCoordX());
        if(appState.getCoordX()==myCoordX&&appState.getCoordY()==myCoordY)
        marker = map.addMarker(new MarkerOptions().position(new LatLng(appState.getCoordY(), appState.getCoordX()))
                .title("Here is You!").snippet("Additional text"));
        else
            marker = map.addMarker(new MarkerOptions().position(new LatLng(appState.getCoordY(), appState.getCoordX()))
                    .title("Searching point!").snippet("previos"));
        // Showing the current location in Google Map
       // map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
       // map.animateCamera(CameraUpdateFactory.zoomTo(15));
        // Instantiates a new CircleOptions object and defines the center and radius
        circleOptions = new CircleOptions()
                .center(new LatLng(appState.getCoordY(), appState.getCoordX()))
                .radius(0).fillColor(0x4000BFFF)  //default
                .strokeColor(Color.BLUE)
                .strokeWidth(5); // In meters

// Get back the mutable Circle
        circle = map.addCircle(circleOptions);
        // Setting latitude and longitude in the TextView tv_location
        Log.d(TAG,"Latitude:" +  latitude  + ", Longitude:"+ longitude );

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    private void init() {
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                Log.d(TAG, "onMapClick: " + latLng.latitude + "," + latLng.longitude);

            }
        });

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
                Log.d(TAG, "onMapLongClick: " + latLng.latitude + "," + latLng.longitude);
                if (marker != null) {
                    marker.remove();
                }
                marker = map.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude))
                        .title("Searching point").snippet("current"));
                // Instantiates a new CircleOptions object and defines the center and radius
                /*circleOptionsMarker = new CircleOptions()
                        .center(new LatLng(latLng.latitude,latLng.longitude))
                        .radius(radius).fillColor(0x4000BFFF)  //default
                        .strokeColor(Color.BLUE)
                        .strokeWidth(5); // In meters*/
                circle.setCenter(new LatLng(latLng.latitude, latLng.longitude));
// Get back the mutable Circle
               /* circleMarker = map.addCircle(circleOptionsMarker);
                if (circle != null) {
                    circle.remove();
                }
                circle = circleMarker;*/
                appState.setCoordX(latLng.longitude);
                appState.setCoordY(latLng.latitude);


            }
        });
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition camera) {
                Log.d(TAG, "onCameraChange: " + camera.target.latitude + "," + camera.target.longitude);
            }
        });
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        mTextValue.setText(String.valueOf(seekBar.getProgress()));

        circle.setRadius(i);
        radius = i;

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mapEventListener.mapRadiusEvent(radius);
        appState.setRadius(radius);
    }



}