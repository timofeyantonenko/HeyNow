package com.antonenkodev.heynow.heynow2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by root on 19.04.15.
 */
public class PostsAdapter extends ArrayAdapter<Posts> {
    private static final String TAG = "logs";
    ArrayList<Posts> postList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;
    ImageLoader imageLoader;

    public PostsAdapter(Context context, int resource, ArrayList<Posts> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        postList = objects;
        imageLoader = new ImageLoader(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.imageview = (ImageView) v.findViewById(R.id.ivImage);
            holder.tvUserName = (TextView) v.findViewById(R.id.userName);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.imageview.setImageResource(R.drawable.temp_img);
       // new DownloadImageTask(holder.imageview).execute(postList.get(position).getImage());
        Log.d(TAG,"love in download");
        Log.d(TAG,"love in pozition: "+postList.get(position).getImage());

            Log.d(TAG,holder.imageview.getClass().toString());

        imageLoader.DisplayImage(postList.get(position).getImage(), holder.imageview);
        holder.tvUserName.setText(postList.get(position).getCreated_time());
        Log.d(TAG, "love in download time "+ postList.get(position).getCreated_time());
        return v;
    }


    static class ViewHolder {
        public ImageView imageview;
        public TextView tvUserName;
    }


}
