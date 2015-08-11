package com.antonenkodev.heynow.heynow2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by root on 09.06.15.
 */
public class SettingsFragment extends Fragment {

    public static String MY_PREF = "settings";
    SharedPreferences sPref;
    SharedPreferences.Editor editor;

    private int defoultRadius;
    private int maximumRadius;

    private TextView maxRadius;
    private TextView mDefoutlRadius;

    private EditText getMaximumRadius;
    private EditText getDefoutlRadius;

    private Button btnApply;

    private FragmentActivity myContext;
    CurrentPlaceDate appState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_fragment,null);

        mDefoutlRadius = (TextView)v.findViewById(R.id.defoultRadius);
        maxRadius = (TextView)v.findViewById(R.id.radiusIn);

        getMaximumRadius = (EditText)v.findViewById(R.id.input_max_radius);
        getDefoutlRadius = (EditText)v.findViewById(R.id.defoultRadiusInput);
        getMaximumRadius.setText(String.valueOf(appState.getMaxRadius()));
        getDefoutlRadius.setText(String.valueOf(appState.getRadius()));

        btnApply = (Button)v.findViewById(R.id.btnApply);

        // создаем обработчик нажатия
        View.OnClickListener oclBtnApl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Меняем текст в TextView (tvOut)
                sPref = getActivity().getSharedPreferences(MY_PREF,getActivity().MODE_PRIVATE);
                editor = sPref.edit();

                maximumRadius = Integer.parseInt(getMaximumRadius.getText().toString());
                defoultRadius = Integer.parseInt(getDefoutlRadius.getText().toString());
                appState.setRadius(defoultRadius);
                appState.setMaxRadius(maximumRadius);
                Log.d("logs", "love about setting " + appState.getRadius());
                Log.d("logs", "love about setting max "+ appState.getMaxRadius());
                editor.putInt("defoultRadius", defoultRadius);
                editor.putInt("maxRadius", maximumRadius);
                editor.commit();

            }
        };

        btnApply.setOnClickListener(oclBtnApl);


        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myContext=(FragmentActivity)activity;
        appState = ((CurrentPlaceDate)myContext.getApplicationContext());
        Log.d("logs","love sandman");

    }
}
