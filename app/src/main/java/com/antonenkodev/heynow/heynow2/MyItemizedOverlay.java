package com.antonenkodev.heynow.heynow2;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import java.util.ArrayList;

/**
 * Created by root on 30.04.15.
 */
public class MyItemizedOverlay extends ItemizedOverlay {
    private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
    Context mContext;

    public MyItemizedOverlay(Drawable defaultMarker) {
        super(boundCenterBottom(defaultMarker));
    }

    @Override
    protected OverlayItem createItem(int i) {
        return mOverlays.get(i);
    }
    public void addOverlay(OverlayItem overlay) {
        mOverlays.add(overlay);
        populate();
    }

    @Override
    public int size() {
        return mOverlays.size();
    }
    public MyItemizedOverlay(Drawable defaultMarker, Context context) {
        super(boundCenterBottom(defaultMarker));
        mContext = context;
    }
    @Override
    protected boolean onTap(int index) {
        OverlayItem item = mOverlays.get(index);
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle(item.getTitle());
        dialog.setMessage(item.getSnippet());
        dialog.show();
        return true;
    }
}
