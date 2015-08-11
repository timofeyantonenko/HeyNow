package com.antonenkodev.heynow.heynow2;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by root on 10.05.15.
 */
public class GalleryFragment extends Fragment {
    public static String TAG = "logs";
    ProgressBar pb;
    ProgressBar pb1;
    ArrayList<Posts> postsList;
    Parcelable state;
    GridView gridView;
    ImageAdapter imageAdapter;
    private int mPhotoSize, mPhotoSpacing;
    private double coordX;

    long date_increment;//432000;
    long max_time;
    long min_time;
    private double coordY;
    private int photosAmount=100;
    private int radius=100;
    Intent intent;
    private FragmentActivity myContext;
    CurrentPlaceDate appState;



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myContext=(FragmentActivity) activity;
        appState = ((CurrentPlaceDate)myContext.getApplicationContext());

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "GalleryFragment love onCreate");
        this.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "GalleryFragment love onCreateView");
       // super.onCreate(savedInstanceState);

        Log.d(TAG,"GaleryActivity love onCreateView");
        View v = inflater.inflate(R.layout.activity_photos, null);
        pb = (ProgressBar)v.findViewById(R.id.progressBar);
        pb.setVisibility(View.GONE);
        pb1 = (ProgressBar)v.findViewById(R.id.progressBar);
        pb1.setVisibility(View.GONE);
        // get the photo size and spacing
        mPhotoSize = getResources().getDimensionPixelSize(R.dimen.photo_size);
        mPhotoSpacing = getResources().getDimensionPixelSize(R.dimen.photo_spacing);
        //определим listview для вывода фото
        //list = (ListView)findViewById(R.id.list);
        gridView = (GridView)v.findViewById(R.id.gridview);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                Posts post = (Posts) parent.getItemAtPosition(position);
                String name = post.getUserName();
                String imageURL = post.getImage();
                String imgSt = post.getStandartIm();
                int networkId = post.getNetwork();
                intent = new Intent(getActivity(), SinglePostView.class);
                intent.putExtra("owner", name);
                intent.putExtra("photoURL", imageURL);
                intent.putExtra("networkId",networkId);
                intent.putExtra("bigPhotoURL",imgSt);
                startActivity(intent);
                Log.d(TAG, "love for clickGallery is... " + imgSt);
            }
        });
        //для вывода фото
        postsList = new ArrayList<Posts>();
        imageAdapter = new ImageAdapter(myContext.getApplicationContext(),R.layout.row,postsList);
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (imageAdapter.getNumColumns() == 0) {
                    final int numColumns = (int) Math.floor(gridView.getWidth() / (mPhotoSize + mPhotoSpacing));
                    if (numColumns > 0) {
                        final int columnWidth = (gridView.getWidth() / numColumns) - mPhotoSpacing;
                        imageAdapter.setNumColumns(numColumns);
                        imageAdapter.setItemHeight(columnWidth);

                    }
                }
            }
        });
        return  v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "GalleryFragment love onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        Log.d(TAG, "love is sent to fragment " + coordX);
        coordX = appState.getCoordX();
        coordY = appState.getCoordY();
        Log.d(TAG, "love is sent to fragment" + coordY);
        radius = appState.getRadius();


        //настроим запрос для инсты
        // fot starting of loop
        appState.setInsta(true);
        appState.setVk(true);
        // set min max time
        //insta
        appState.setILmin_time(appState.getMin_time());
        appState.setImax_time(appState.getMax_time());
        if (appState.getILmin_time() + appState.getI_date_incr() > appState.getMax_time()) {
            appState.setILmax_time(appState.getMax_time());
        } else {
            appState.setILmax_time(appState.getILmin_time() + appState.getI_date_incr());
        }
        //vk
        appState.setVLmin_time(appState.getMin_time());
        appState.setVmax_time(appState.getMax_time());
        if (appState.getVLmin_time() + appState.getV_date_incr() > appState.getMax_time()){
            appState.setVLmax_time(appState.getMax_time());
            } else {
        appState.setVLmax_time(appState.getVLmin_time() + appState.getV_date_incr());
             }
        new ParseTaskInsta().execute();
        new ParseTaskVK().execute();

    }


    private class ParseTaskVK extends AsyncTask<Void,Void,ArrayList<String>> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        ArrayList<String> resultJsons = new ArrayList<String>();
        String resultJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            // получаем данные с внешнего ресурса
            String answerJson;
            Log.d(TAG, "GalleryFragment love Instabegin");
            // получаем данные с внешнего ресурса
            while (appState.isVk()) {
                if (appState.getVLmax_time()>appState.getVmax_time()) {
                    appState.setVLmax_time(appState.getVmax_time());
                }
                answerJson="";
            try {

                String s = "https://api.vk.com/method/photos.search?"
                        + "lat=" + Double.toString(appState.getCoordY())
                        + "&long=" + Double.toString(appState.getCoordX())
                        + "&count=" + String.valueOf(photosAmount)
                        + "&radius=" + String.valueOf(appState.getRadius())
                        + "&start_time=" + Long.toString(appState.getVLmin_time())
                        + "&end_time=" + Long.toString(appState.getVLmax_time());
                URL url = new URL(s);
                Log.d(TAG,"lova adress VK one is..."+s);

                appState.setVLmin_time(appState.getVLmax_time());
                appState.setVLmax_time(appState.getVLmax_time() + appState.getV_date_incr());
                if (appState.getVLmin_time() >= appState.getVmax_time()) {
                    Log.d(TAG, "tuta ya");
                    //break insta;
                    appState.setVk(false);
                }

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();
                answerJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
                resultJsons.add(answerJson);
            }


            return resultJsons;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strJsons) {
            super.onPostExecute(strJsons);
            pb.setVisibility(View.GONE);
            // выводим целиком полученную json-строку

            Log.d(TAG, strJsons.toString());
            int j = 1;
            String strJson;
            for(j=1; j<=strJsons.size();j++) {
                strJson = strJsons.get(strJsons.size() - j);

                JSONObject dataJsonObj = null;

                try {
                    dataJsonObj = new JSONObject(strJson);

                    JSONArray posts = dataJsonObj.getJSONArray("response");

                    // 2. перебираем и выводим ссыдки на картинки для каждого события
                    for (int i = 1; i < posts.length(); i++) {
                        Posts postView = new Posts();
                        JSONObject post = posts.getJSONObject(i);
                        String img = post.getString("src_big");
                        String usr = String.valueOf(post.getInt("owner_id"));
                        String created_time = String.valueOf(post.getInt("created"));


                        Log.d(TAG, "username: " + usr);
                        Log.d(TAG, "love image_url: " + img);
                        postView.setImage(img);
                        postView.setCreated_time(created_time);
                        postView.setStandartIm(img);
                        postView.setUserName(usr);
                        postView.setNetwork(5);
                        postsList.add(postView);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "love one");
                //ImageAdapter imageAdapter = new ImageAdapter(myContext.getApplicationContext(),R.layout.row,postsList);
                Log.d(TAG, "love two");
                // list.setAdapter(adapter);
                gridView.setAdapter(imageAdapter);
                adjustGridView();
            }
        }
    }
    private class ParseTaskInsta extends AsyncTask<Void,Void,ArrayList<String>>{
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        ArrayList<String> resultJsons = new ArrayList<String>();
        String resultJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb1.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            String answerJson;
            Log.d(TAG, "GalleryFragment love Instabegin");
            // получаем данные с внешнего ресурса
            while (appState.isInsta()) {
                if (appState.getILmax_time()>appState.getImax_time()) {
                    appState.setILmax_time(appState.getImax_time());
                    Log.d(TAG, "lova adress one is time3 " + max_time);
                }

                answerJson="";

            try {
                Log.d(TAG,"lova, agga "+ appState.getILmin_time());
                    String s = "https://api.instagram.com/v1/media/search?"
                            + "lat=" + Double.toString(appState.getCoordY())
                            + "&lng=" + Double.toString(appState.getCoordX())
                            + "6&distance=" + String.valueOf(appState.getRadius())
                            + "&min_timestamp=" + Long.toString(appState.getILmin_time())
                            + "&max_timestamp=" + Long.toString(appState.getILmax_time())
                            + "&access_token=" + appState.getInsta_token(); //+"&access_token=1721084755.1f0d434.cdf54fb7c0a5421b8fbfec635c6d518d";
                URL url = new URL(s);
                Log.d(TAG, "lova adress one is..." + s);
                appState.setILmin_time(appState.getILmax_time());
                appState.setILmax_time(appState.getILmax_time() + appState.getI_date_incr());
                if (appState.getILmin_time() >= appState.getImax_time()) {
                    Log.d(TAG, "tuta ya");
                    //break insta;
                    appState.setInsta(false);
                }

                urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();
                answerJson = buffer.toString();

                        } catch (Exception e) {
                            e.printStackTrace();
                          }
                resultJsons.add(answerJson);
            }


            return resultJsons;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strJsons) {
            super.onPostExecute(strJsons);
            pb1.setVisibility(View.GONE);
            // выводим целиком полученную json-строку

            Log.d(TAG, strJsons.toString());
            int j = 1;
            String strJson;
            for(j=1; j<=strJsons.size();j++){
                    strJson = strJsons.get(strJsons.size()-j);

                JSONObject dataJsonObj = null;

                try {
                    dataJsonObj = new JSONObject(strJson);
                    JSONArray posts = dataJsonObj.getJSONArray("data");

                    // 2. перебираем и выводим ссыдки на картинки для каждого события
                    for (int i = 0; i < posts.length(); i++) {
                        Posts postView = new Posts();
                        JSONObject post = posts.getJSONObject(i);

                        JSONObject image = post.getJSONObject("images");
                        JSONObject lowres = image.getJSONObject("low_resolution");
                        JSONObject standard_resolution = image.getJSONObject("standard_resolution");
                        String imgSt = standard_resolution.getString("url");
                        String img = lowres.getString("url");
                        String created_time = post.getString("created_time");

                        JSONObject user = post.getJSONObject("user");
                        String usr = user.getString("id");

                        JSONObject like = post.getJSONObject("likes");
                        int likes = like.getInt("count");

                        Log.d(TAG, "username: " + usr);
                        Log.d(TAG, "image_url: " + img);
                        postView.setStandartIm(imgSt);
                        postView.setImage(img);
                        postView.setLikes(likes);
                        postView.setUserName(usr);
                        postView.setCreated_time(created_time);
                        postView.setNetwork(7);
                        postsList.add(postView);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ImageAdapter imageAdapter = new ImageAdapter(myContext.getApplicationContext(), R.layout.row, postsList);
                Log.d(TAG, "love two");
                // list.setAdapter(adapter);
                gridView.setAdapter(imageAdapter);
                adjustGridView();
            }
        }
    }
    private void adjustGridView() {
        gridView.setNumColumns(3);
    }
}
