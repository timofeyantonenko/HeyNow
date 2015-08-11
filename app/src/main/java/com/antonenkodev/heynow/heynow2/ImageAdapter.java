package com.antonenkodev.heynow.heynow2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by root on 19.04.15.
 */
public class ImageAdapter extends ArrayAdapter<Posts> {
    private static final String TAG = "logs";
    ArrayList<Posts> postList;
    LayoutInflater vi;
    private int mItemHeight = 0;
    private int mNumColumns = 0;
    private RelativeLayout.LayoutParams mImageViewLayoutParams;
    int Resource;
    Context mContext;
    Context activity;
    ViewHolder holder;
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");

    public ImageAdapter(Context context, int resource, ArrayList<Posts> objects) {
        super(context, resource, objects);
        mContext=context;
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        postList = objects;
    }
    // set numcols
    public void setNumColumns(int numColumns) {
        mNumColumns = numColumns;
    }
    public int getNumColumns() {
        return mNumColumns;
    }

    // set photo item height
    public void setItemHeight(int height) {
        if (height == mItemHeight) {
            return;
        }
        mItemHeight = height;
        mImageViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mItemHeight);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        int p = position;
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
        holder.imageview.setTag((position));
        // new DownloadImageTask(holder.imageview).execute(postList.get(position).getImage());
        Log.d(TAG, "love in download "+convertView+" and "+parent);
        Log.d(TAG, "love in pozition: " + postList.get(position).getImage());
        Picasso.with(mContext)
                .load(postList.get(position).getImage())
                .placeholder(R.drawable.user)
                .error(R.drawable.temp_img)
                .resizeDimen(R.dimen.photo_width,R.dimen.photo_height)
                .into(holder.imageview);



        Log.d(TAG, holder.imageview.getClass().toString());

       // imageLoader.DisplayImage(postList.get(position).getImage(),holder.imageview);
        String strTime = formatter.format(new Date(Long.parseLong(postList.get(position).getCreated_time())*1000L));
        holder.tvUserName.setText(strTime);
        Log.d(TAG, "likes" + postList.get(position).getLikes());
        return v;
    }



    static class ViewHolder {
        public ImageView imageview;
        public TextView tvUserName;
    }

    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public Posts getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }






}
