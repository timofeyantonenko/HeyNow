package com.antonenkodev.heynow.heynow2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.listener.OnRequestSocialPersonCompleteListener;
import com.github.gorbin.asne.core.persons.SocialPerson;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;

/**
 * Created by root on 20.05.15.
 */
public class SinglePostView extends Activity implements OnRequestSocialPersonCompleteListener, View.OnClickListener {
    private static final String NETWORK_ID = "NETWORK_ID";
    InterstitialAd mInterstitialAd;
    private SocialNetwork socialNetwork;
    private int networkId;
    String owner;
    String usrName;
    String socialURL;
    String photoURL;
    String bigPhotoURL;
    private Button openPage;
    ImageView currentPhoto;
    ImageView imgflag;
    TextView txtname;
    Intent intent;
    Context context;
    private static final String TAG = "logs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_post);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6861393331666165/7171321139");
        requestNewInterstitial();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s", socialURL)));
                Log.d(TAG, "love of insta request is..." + socialURL);
                startActivity(intent);
            }
        });

        openPage= (Button)findViewById(R.id.page);
        openPage.setOnClickListener(this);
        intent = getIntent();
        networkId =  intent.getIntExtra("networkId", 0);
        Log.d(TAG,"love tag "+networkId);
        //настраиваем запрос к соц сети на профиль
        owner = intent.getStringExtra("owner");
        context = getApplication();
        bigPhotoURL = intent.getStringExtra("bigPhotoURL");
        photoURL = intent.getStringExtra("photoURL");

        // Locate the TextViews in singleitemview.xml
        txtname = (TextView) findViewById(R.id.name);

        // Locate the ImageView in singleitemview.xml
        imgflag = (ImageView) findViewById(R.id.imageView);
        currentPhoto = (ImageView)findViewById(R.id.dwnImage);
        Picasso.with(this)
                .load(bigPhotoURL)
                .into(currentPhoto);


        socialNetwork = RegistrationFragment.mSocialNetworkManager.getSocialNetwork(networkId);
        socialNetwork.setOnRequestSocialPersonCompleteListener(this);
        socialNetwork.requestSocialPerson(owner);


    }

    @Override
    public void onRequestSocialPersonSuccess(int i, SocialPerson socialPerson) {
        Log.d(TAG,"wonder love is here"+" +id:"+socialPerson.id);
        txtname.setText(socialPerson.name);
        usrName=socialPerson.name;
        Log.d(TAG, "love of insta request on success before is"+socialPerson.name);
        //id.setText(socialPerson.id);
        String socialPersonString = socialPerson.toString();
        //String infoString = socialPersonString.substring(socialPersonString.indexOf("{")+1, socialPersonString.lastIndexOf("}"));
        //info.setText(infoString.replace(", ", "\n"));
        Log.d(TAG, "wonder love two is here");
        Picasso.with(this)
                .load(socialPerson.avatarURL)
                .into(imgflag);
        if(networkId==7){
            socialURL="http://instagram.com/_u/"+usrName;
            Log.d(TAG,"love of insta request before is..."+socialURL);
        }
        else {
            socialURL = "vkontakte://profile/"+owner;

        }
    }

    @Override
    public void onError(int i, String s, String s1, Object o) {
        Log.d(TAG,"wonder love fore is here"+intent.getIntExtra("networkId",0));
        Toast.makeText(this, "ERROR: " + s1, Toast.LENGTH_LONG).show();
        openPage.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.page:
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s", socialURL)));
                    Log.d(TAG, "love of insta request is..." + socialURL);
                    startActivity(intent);
                    break;
                }
        }
    }


    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("YOUR_DEVICE_HASH")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }


}
