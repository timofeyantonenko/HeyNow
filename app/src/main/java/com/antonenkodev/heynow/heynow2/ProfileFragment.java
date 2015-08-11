package com.antonenkodev.heynow.heynow2;

/**
 * Created by root on 16.05.15.
 */

        import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.listener.OnRequestSocialPersonCompleteListener;
import com.github.gorbin.asne.core.persons.SocialPerson;
import com.github.gorbin.asne.instagram.InstagramSocialNetwork;
import com.github.gorbin.asne.vk.VkSocialNetwork;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment implements OnRequestSocialPersonCompleteListener {
    private static final String TAG = "logs";
    private String message = "Enjoy this!:";
    private String link = "market://details?id=com.antonenkodev.heynow.heynow";

    private SharedPreferences sPref;
    private String SAVED_INSTA_TOKEN= "settings";
    private String MY_PREF= "settings";
    CurrentPlaceDate appState;

    private static final String NETWORK_ID = "NETWORK_ID";
    private SocialNetwork socialNetwork;
    private int networkId;
    private ImageView photo;
    private TextView name;
    //private Button share;
    private Button logout;
    private RelativeLayout frame;

    public static ProfileFragment newInstannce(int id) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(NETWORK_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("HeyNow");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        appState = ((CurrentPlaceDate)getActivity().getApplicationContext());
        networkId = getArguments().containsKey(NETWORK_ID) ? getArguments().getInt(NETWORK_ID) : 0;
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Profile");

        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);

        frame = (RelativeLayout) rootView.findViewById(R.id.frame);
        photo = (ImageView) rootView.findViewById(R.id.imageView);
        name = (TextView) rootView.findViewById(R.id.name);
        //share = (Button) rootView.findViewById(R.id.share);
        //share.setOnClickListener(shareClick);
        logout = (Button) rootView.findViewById(R.id.logout);
        logout.setOnClickListener(logoutClick);
        colorProfile(networkId);

        socialNetwork = RegistrationFragment.mSocialNetworkManager.getSocialNetwork(networkId);
        socialNetwork.setOnRequestCurrentPersonCompleteListener(this);
        if(!(appState.getInsta_token()==null&&appState.getInsta_token()!=""))
        socialNetwork.requestCurrentPerson();

        MainActivity.showProgress("Loading social person");
        return rootView;
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main2, menu);
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                socialNetwork.logout();
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    @Override
    public void onRequestSocialPersonSuccess(int i, SocialPerson socialPerson) {
        MainActivity.hideProgress();

        Log.d(TAG, "wonder love fore is here" + networkId);
        name.setText(socialPerson.name);
        //id.setText(socialPerson.id);
        //String socialPersonString = socialPerson.toString();
        //String infoString = socialPersonString.substring(socialPersonString.indexOf("{")+1, socialPersonString.lastIndexOf("}"));
        //info.setText(infoString.replace(", ", "\n"));
        Picasso.with(getActivity())
                .load(socialPerson.avatarURL)
                .into(photo);
    }

    @Override
    public void onError(int networkId, String requestID, String errorMessage, Object data) {
        MainActivity.hideProgress();
        Toast.makeText(getActivity(), "ERROR: " + errorMessage, Toast.LENGTH_LONG).show();
    }



    private View.OnClickListener logoutClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            socialNetwork.logout();
            sPref = getActivity().getSharedPreferences(MY_PREF, getActivity().MODE_PRIVATE);
            SharedPreferences.Editor ed = sPref.edit();
            ed.putString(SAVED_INSTA_TOKEN, null);
            appState.setInsta_token(null);
            ed.commit();
            RegistrationFragment profile = new RegistrationFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .addToBackStack("profile")
                    .replace(R.id.container, profile)
                    .commit();
            //getActivity().getSupportFragmentManager().popBackStack();
        }
    };

    /*private View.OnClickListener shareClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            link = "market://details?id=com.antonenkodev.heynow.heynow";
            AlertDialog.Builder ad = alertDialogInit("Would you like to post Link:", link);
            ad.setPositiveButton("Post link", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Bundle postParams = new Bundle();
                    postParams.putString(SocialNetwork.BUNDLE_LINK, link);
                    socialNetwork.requestPostLink(postParams, message, postingComplete);
                }
            });
            ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.cancel();
                }
            });
            ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    dialog.cancel();
                }
            });
            ad.create().show();
        }
    };*/

   /* private OnPostingCompleteListener postingComplete = new OnPostingCompleteListener() {
        @Override
        public void onPostSuccessfully(int socialNetworkID) {
            Toast.makeText(getActivity(), "Sent", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {
            Toast.makeText(getActivity(), "Error while sending: " + errorMessage, Toast.LENGTH_LONG).show();
        }
    };*/

    private void colorProfile(int networkId){
        int color = getResources().getColor(R.color.dark);
        int image = R.drawable.user;
        switch (networkId) {
            case VkSocialNetwork.ID:
                color = getResources().getColor(R.color.vk);
                image = R.drawable.vk_user;
                break;
            case InstagramSocialNetwork.ID:
                color = getResources().getColor(R.color.insta);
                image = R.drawable.vk_user;
                break;
        }
        frame.setBackgroundColor(color);
        name.setTextColor(color);
        //share.setBackgroundColor(color);
        logout.setBackgroundColor(color);
        photo.setBackgroundColor(color);
        photo.setImageResource(image);
    }

    private AlertDialog.Builder alertDialogInit(String title, String message){
        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setCancelable(true);
        return ad;
    }


}