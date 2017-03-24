package com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.R;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.activities.EmotionClick;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.activities.ProductImageView;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.helpers.EventBusHelper;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.helpers.ScreenHelper;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.utils.FileUtils;

import java.util.ArrayList;

/**
 * Created by ducnguyen on 7/3/16.
 */

public class EmotionAdapter extends RecyclerView.Adapter<EmotionAdapter.ViewHolder> {
    private static final String TAG = "EmotionAdapter";
    private Context context;
    private ArrayList<String> mDataSet;

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ProductImageView mImageView;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBusHelper.post(new EmotionClick(getAdapterPosition()));
                }
            });
            mImageView = (ProductImageView) v.findViewById(R.id.mImageView);
        }

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param photoArrayList ArrayList<Photo> containing the data to populate views to be used by RecyclerView.
     */
    public EmotionAdapter(Context context, ArrayList<String> photoArrayList) {
        this.mDataSet = photoArrayList;
        this.context = context;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_emotion, viewGroup, false);
        return new ViewHolder(v);
    }

    // END_INCLUDE(recyclerViewOnCreateViewHolder)
    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

//        ImageLoader.getInstance().displayImage("file://" + mDataSet.get(position).getUrlPage(), viewHolder.getmImageView());
        Glide.with(context).load(mDataSet.get(position))
                .override(50,50)
                .into(viewHolder.mImageView);
    }

    // END_INCLUDE(recyclerViewOnBindViewHolder)
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
