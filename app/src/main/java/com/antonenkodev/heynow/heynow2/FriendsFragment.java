package com.antonenkodev.heynow.heynow2;

/**
 * Created by root on 16.05.15.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.listener.OnRequestGetFriendsCompleteListener;
import com.github.gorbin.asne.core.persons.SocialPerson;

import java.util.ArrayList;

public class FriendsFragment extends Fragment implements OnRequestGetFriendsCompleteListener {

    private static final String NETWORK_ID = "NETWORK_ID";
    private static final String TAG = "logs";
    private ListView listView;

    public static FriendsFragment newInstannce(int id) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        args.putInt(NETWORK_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public FriendsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        int networkId = getArguments().containsKey(NETWORK_ID) ? getArguments().getInt(NETWORK_ID) : 0;

        View rootView = inflater.inflate(R.layout.friends_list_fragment, container, false);
        listView = (ListView) rootView.findViewById(R.id.list);

        SocialNetwork socialNetwork = RegistrationFragment.mSocialNetworkManager.getSocialNetwork(networkId);
        socialNetwork.setOnRequestGetFriendsCompleteListener(this);
        socialNetwork.requestGetFriends();
        Log.d(TAG, "love of token is... " + socialNetwork.getAccessToken().toString());
        RegistrationActivity.showProgress("Loading friends");

        return rootView;
    }

    @Override
    public void OnGetFriendsIdComplete(int id, String[] friendsID) {
        ((RegistrationActivity)getActivity()).getSupportActionBar().setTitle(friendsID.length + " Friends");
    }

    @Override
    public void OnGetFriendsComplete(int networkID, ArrayList<SocialPerson> socialPersons) {
        RegistrationActivity.hideProgress();
        FriendsListAdapter adapter = new FriendsListAdapter(getActivity(), socialPersons, networkID);
        listView.setAdapter(adapter);
    }

    @Override
    public void onError(int networkId, String requestID, String errorMessage, Object data) {
        RegistrationActivity.hideProgress();
        Toast.makeText(getActivity(), "ERROR: " + errorMessage, Toast.LENGTH_LONG).show();
    }
}