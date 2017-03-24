package com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by ducnguyen on 6/19/16.
 */
public class ListImageAdapter extends BaseAdapter {
    ArrayList<String> mListUrl = new ArrayList<>();
    Context context;

    public ListImageAdapter(ArrayList<String> mListUrl, Context context) {
        this.mListUrl = mListUrl;
        this.context = context;
    }


    @Override
    public int getCount() {
        return mListUrl.size();
    }

    @Override
    public Object getItem(int position) {
        return mListUrl.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RecordHolder holder = new RecordHolder();
//        if (convertView == null) {
//            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//            convertView = inflater.inflate(layoutResourceId, parent, false);
//            holder.imageItem = (ImageView) convertView
//                    .findViewById(R.id.item_image);
//            convertView.setTag(holder);
//        } else {
//            holder = (RecordHolder) convertView.getTag();
//        }
//        Photo photo = data.get(position);
//        // Picasso.with(context).load(photo.getUrlPage()).into(holder.imageItem);
////		imageLoader.displayImage("file://" + photo.getUrlPage(),
////				holder.imageItem,disPlayoption);
//        Glide.with(context).load(photo.getUrlPage()).signature(new StringSignature(System.currentTimeMillis() + "")).into(holder.imageItem);
        return convertView;
    }

    static class RecordHolder {
        ImageView imageItem;
    }
}
