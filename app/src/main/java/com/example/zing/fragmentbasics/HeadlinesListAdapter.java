package com.example.zing.fragmentbasics;

import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Zing on 2016/9/5.
 */

public class HeadlinesListAdapter extends BaseAdapter {
    private HeadlinesFragment.OnHeadlineSelectedListener activity;
    private ImageLoader mImageLoader;
    private ArrayList<User> mUserArrayList;


    public HeadlinesListAdapter(HeadlinesFragment.OnHeadlineSelectedListener activity) {
        this.activity = activity;
        this.mImageLoader = AppController.getImageLoader();
    }

    @Override
    public int getCount() {
        return mUserArrayList == null ? 0 : mUserArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mUserArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.listviewrow_headlines, parent, false);
            holder = new Holder();
            holder.contentWrap = (View) convertView.findViewById(R.id.click_area);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            holder.headline = (TextView) convertView.findViewById(R.id.headline);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final User user = (User) getItem(position);

        // "cancelRequest" is for stopping loading image
        // when scrolling a new item
        if (holder.imageContainer != null) {
            holder.imageContainer.cancelRequest();
            holder.imageContainer = null;
        }

        // Set click listener so that we can access to more information of the item
        holder.contentWrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onUserSelected(user);
            }
        });
        holder.headline.setText(user.getName());
        holder.imageContainer = mImageLoader.get(user.getimageUrl(), ImageLoader.getImageListener(holder.imageView, 0, 0));
        return convertView;
    }

    public void swapData(ArrayList<User> data) {
        mUserArrayList = data;
        notifyDataSetChanged();
    }

    class Holder {
        View contentWrap;
        ImageView imageView;
        TextView headline;
        ImageLoader.ImageContainer imageContainer;
    }
}
