package com.antonenkodev.heynow.heynow2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.SocialNetworkManager;
import com.github.gorbin.asne.core.listener.OnLoginCompleteListener;
import com.github.gorbin.asne.instagram.InstagramSocialNetwork;
import com.github.gorbin.asne.vk.VkSocialNetwork;
import com.vk.sdk.VKScope;

import java.util.List;

/**
 * Created by root on 16.05.15.
 */
public class RegistrationFragment  extends Fragment implements SocialNetworkManager.OnInitializationCompleteListener, OnLoginCompleteListener {
    public static SocialNetworkManager mSocialNetworkManager;
    /**
     * SocialNetwork Ids in ASNE:
     * 1 - Twitter
     * 2 - LinkedIn
     * 3 - Google Plus
     * 4 - Facebook
     * 5 - Vkontakte
     * 6 - Odnoklassniki
     * 7 - Instagram
     */
    SharedPreferences sPref;
    //for global variables
    CurrentPlaceDate appState;

    final String MY_PREF = "settings";
    final String SAVED_INSTA_TOKEN = "settings";
    //private Button vk;
    private Button insta;
    private Button skipReg;
    //private Button twitter;
    private  String token;
    private static final String TAG = "logs";
    public RegistrationFragment() {
    }
    android.support.v7.app.ActionBar actionBar;


    /*@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBar= ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.registration_fragment, container, false);
        Log.d(TAG,"love in this place");

        appState = ((CurrentPlaceDate)getActivity().getApplicationContext());
        //((RegistrationActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);


        // init buttons and set Listener
        //vk = (Button) rootView.findViewById(R.id.vk);
        //vk.setOnClickListener(loginClick);
        insta = (Button) rootView.findViewById(R.id.insta);
        insta.setOnClickListener(loginClick);
        skipReg = (Button)rootView.findViewById(R.id.skip);
        skipReg.setOnClickListener(loginClick);
        //twitter = (Button)rootView.findViewById(R.id.twitter);
        //twitter.setOnClickListener(loginClick);

        //Get Keys for initiate SocialNetworks
        String VK_KEY = getActivity().getString(R.string.vk_app_id);
        String INSTAGRAM_CLIENT_KEY = getActivity().getString(R.string.insta_app_id);
        String INSTAGRAM_CLIENT_SECRET = getActivity().getString(R.string.insta_client_secret);
        //String TWITTER_CONSUMER_KEY = getActivity().getString(R.string.twitter_consumer_key);
        //String TWITTER_CONSUMER_SECRET = getActivity().getString(R.string.twitter_consumer_secret);
        //String TWITTER_CALLBACK_URL = "oauth://ASNE";
        /* String OK_APP_ID = getActivity().getString(R.string.ok_app_id);
        String OK_PUBLIC_KEY = getActivity().getString(R.string.ok_public_key);
        String OK_SECRET_KEY = getActivity().getString(R.string.ok_secret_key);*/


        String[] vkScope = new String[] {
                VKScope.FRIENDS,
                VKScope.WALL,
                VKScope.PHOTOS,
                VKScope.NOHTTPS,
                VKScope.STATUS,

        };
            String instagramScope = "likes+comments";

        //Use manager to manage SocialNetworks
        //ayayayaay поменял регистрацию на мэйн
        mSocialNetworkManager = (SocialNetworkManager) getFragmentManager().findFragmentByTag(MainActivity.SOCIAL_NETWORK_TAG);

        //Check if manager exist
        if (mSocialNetworkManager == null) {
            mSocialNetworkManager = new SocialNetworkManager();

            //Init and add to manager VkSocialNetwork
            VkSocialNetwork vkNetwork = new VkSocialNetwork(this, VK_KEY, vkScope);
            mSocialNetworkManager.addSocialNetwork(vkNetwork);

            //Init and add to manager InstaSocialNetwork
            InstagramSocialNetwork instagramNetwork = new InstagramSocialNetwork(this, INSTAGRAM_CLIENT_KEY, INSTAGRAM_CLIENT_SECRET,"http://localhost",instagramScope);
            mSocialNetworkManager.addSocialNetwork(instagramNetwork);


            // permissions for twitter in developer twitter console
            //TwitterSocialNetwork twNetwork = new TwitterSocialNetwork(this, TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET,TWITTER_CALLBACK_URL);
            //mSocialNetworkManager.addSocialNetwork(twNetwork);
            //Initiate every network from mSocialNetworkManager
            //ayayayayayayayaya change from regist to main
            getFragmentManager().beginTransaction().add(mSocialNetworkManager, MainActivity.SOCIAL_NETWORK_TAG).commit();
            mSocialNetworkManager.setOnInitializationCompleteListener(this);


            //my block*************************>>>>>>>>>>>>>>>
            if(!mSocialNetworkManager.getInitializedSocialNetworks().isEmpty()) {
                List<SocialNetwork> socialNetworks = mSocialNetworkManager.getInitializedSocialNetworks();
                for (SocialNetwork socialNetwork : socialNetworks) {
                    socialNetwork.setOnLoginCompleteListener(this);


                }
            }
            //finish it>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


        } else {
            //if manager exist - get and setup login only for initialized SocialNetworks
            if(!mSocialNetworkManager.getInitializedSocialNetworks().isEmpty()) {
                List<SocialNetwork> socialNetworks = mSocialNetworkManager.getInitializedSocialNetworks();
                for (SocialNetwork socialNetwork : socialNetworks) {
                    socialNetwork.setOnLoginCompleteListener(this);

                }
            }
        }

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //инициализирую сети >>>>>>>>>>>>>>
        sPref = getActivity().getSharedPreferences(MY_PREF, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_INSTA_TOKEN, "");
        token = "";
        appState.setInsta_token("");
        ed.commit();
        SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(7);
        initSocialNetwork(socialNetwork);
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        //смотрю, заходить ли в фрагмент регистрации>>>>>>>>>>>>>>>>>>>>>
        if((appState.getInsta_token()==""&&appState.isFirstReg())||((appState.getInsta_token()!=""&&appState.getInsta_token()!=null))){
            appState.setFirstReg(false);
            GalleryFragment profile = new GalleryFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .addToBackStack("profile")
                    .replace(R.id.container, profile)
                    .commit();
        }
        return rootView;
    }

    private void initSocialNetwork(SocialNetwork socialNetwork){
        if(socialNetwork.isConnected()){
            switch (socialNetwork.getID()){
                /*case VkSocialNetwork.ID:
                    vk.setText("Show VK profile");
                    break;*/
                case InstagramSocialNetwork.ID:
                    insta.setText("Show Instagram profile");
                    break;
                /*case TwitterSocialNetwork.ID:
                    twitter.setText("Show Twitter profile");
                    break;*/
            }
            if(socialNetwork.getID()==InstagramSocialNetwork.ID){
                Log.d(TAG,"love in this place two");
               // if(getActivity()!=null) {
                    sPref = getActivity().getSharedPreferences(MY_PREF, getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putString(SAVED_INSTA_TOKEN, socialNetwork.getAccessToken().token);
                    token = socialNetwork.getAccessToken().token;
                    appState.setInsta_token(socialNetwork.getAccessToken().token);
                    ed.commit();

                GalleryFragment profile = new GalleryFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack("profile")
                        .replace(R.id.container, profile)
                        .commit();
                //}
            }
            //startActivity(new Intent(this.getActivity(), MainActivity.class));
        }
    }
    @Override
    public void onSocialNetworkManagerInitialized() {
        //when init SocialNetworks - get and setup login only for initialized SocialNetworks
        for (SocialNetwork socialNetwork : mSocialNetworkManager.getInitializedSocialNetworks()) {
            socialNetwork.setOnLoginCompleteListener(this);
            if(appState.getInsta_token()==null||appState.getInsta_token()=="")
            initSocialNetwork(socialNetwork);
        }
    }

    //Login listener

    private View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int networkId = 0;
            switch (view.getId()){
                /*case R.id.vk:
                    networkId = VkSocialNetwork.ID;
                    break;*/
                case R.id.insta:
                    networkId = InstagramSocialNetwork.ID;
                    Log.d(TAG,"love insta: "+networkId);
                    break;
                case R.id.skip:
                    networkId = -1;
                    break;

                /*case R.id.twitter:
                    networkId = TwitterSocialNetwork.ID;
                    Log.d(TAG,"love insta: "+networkId);
                    break;*/
            }
            if(networkId!=-1){
                SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);
                    if(!socialNetwork.isConnected()) {
                         if(networkId != 0) {
                             socialNetwork.requestLogin();
                            MainActivity.showProgress("Loading social person");
                         } else {
                            Toast.makeText(getActivity(), "Wrong networkId", Toast.LENGTH_LONG).show();
                                }
                    } else {
                            // startProfile(socialNetwork.getID());
                            }
            } else {
                sPref = getActivity().getSharedPreferences(MY_PREF, getActivity().MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(SAVED_INSTA_TOKEN, "");
                token = "";
                appState.setInsta_token("");
                ed.commit();
                SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(5);
                initSocialNetwork(socialNetwork);
                GalleryFragment profile = new GalleryFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack("profile")
                        .replace(R.id.container, profile)
                        .commit();

            }
        }
    };

    @Override
    public void onLoginSuccess(int networkId) {
        //ayayayaya
        SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);
        if(!socialNetwork.isConnected()) {
            if (networkId != 0) {
                socialNetwork.requestLogin();

                initSocialNetwork(socialNetwork);
            }
        }
        initSocialNetwork(socialNetwork);
        MainActivity.hideProgress();
        appState.setInsta_token(token);

    }

    @Override
    public void onError(int networkId, String requestID, String errorMessage, Object data) {
        ///ayayaay
        MainActivity.hideProgress();
        Toast.makeText(getActivity(), "ERROR: " + errorMessage, Toast.LENGTH_LONG).show();
        Log.d(TAG, "love: " + errorMessage);
    }

    private void startProfile(int networkId){
        ProfileFragment profile = ProfileFragment.newInstannce(networkId);
        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack("profile")
                .replace(R.id.container, profile)
                .commit();
    }
}
