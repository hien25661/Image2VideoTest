package com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.R;

/**
 * Created by ducnguyen on 6/19/16.
 */

public class ListPhotoAdapter extends RecyclerView.Adapter<ListPhotoAdapter.ViewHolder> {
    private ArrayList<String> mListUrl;
    private Context mContext;
    private int mSelectionPos = 0;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        @Bind(R.id.imvPhoto)
        public ImageView mImvPhoto;
        @Bind(R.id.imvZoom)
        public ImageView mImvZoom;
        @Bind(R.id.imvEdit)
        public ImageView mImvEdit;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListPhotoAdapter(Context mContext, ArrayList<String> mListUrl) {
        this.mContext = mContext;
        this.mListUrl = mListUrl;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListPhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_list_image, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        int count = position + 1;
        Glide.with(mContext).load(mListUrl.get(position))
                .signature(new StringSignature("" + System.currentTimeMillis()))
                .into(holder.mImvPhoto);
        //holder.mImageView.setImageURI(mOrderInformationObjects.get(position).getmPhotoList()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mListUrl.size();
    }
}