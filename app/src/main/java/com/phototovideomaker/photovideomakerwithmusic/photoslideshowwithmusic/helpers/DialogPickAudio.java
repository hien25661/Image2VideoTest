package com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.R;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.adapters.SimpleDividerItemDecoration;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ducnguyen on 7/9/16.
 */
public class DialogPickAudio extends Dialog {
    Context mContext;
    @Bind(R.id.btn_cancel)
    Button cancelButtom;
    @Bind(R.id.btn_ok)
    Button okButtom;
    @Bind(R.id.recycleView)
    RecyclerView recyclerView;
    AudioModel currentAudio;
    ArrayList<AudioModel> mListAudio;
    int currentPosition;


    public void enableButton(boolean isEnable) {
        okButtom.setEnabled(isEnable);
    }

    public ArrayList<AudioModel> getmListAudio() {
        return mListAudio;
    }

    public void setmListAudio(ArrayList<AudioModel> mListAudio) {
        this.mListAudio = mListAudio;
    }

    public DialogPickAudio(Context context) {
        super(context);
        this.mContext = context;
    }

    public DialogPickAudio(Context context, int themeResId) {
        super(context, themeResId);
    }

    public DialogPickAudio(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void audioPlayer(String path) {
        //set up MediaPlayer
        try {
            mp.reset();
            mp.setDataSource(path);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
    }

    MediaPlayer mp;

    @Override
    public void dismiss() {
        mp.stop();
        super.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mp = new MediaPlayer();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        this.setContentView(R.layout.dialog_pick_audio);
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ButterKnife.bind(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        okButtom.setEnabled(false);
        mListAudio = getAudioList();
        recyclerView.setAdapter(new AudioAdapter(mContext, mListAudio));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(mContext));

    }


    private ArrayList<AudioModel> getAudioList() {
        ArrayList<AudioModel> arrayList = new ArrayList<>();
        final Cursor mCursor = mContext.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DISPLAY_NAME
                        , MediaStore.Audio.Media.DATA
                        , MediaStore.Audio.Media.DURATION
                }, null, null,
                "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

        int count = mCursor.getCount();

        int i = 0;
        if (mCursor.moveToFirst()) {
            do {
                AudioModel audioModel = new AudioModel();
                audioModel.setTitle(mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                audioModel.setUriPath(mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                long duration = mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                audioModel.setDuration(duration);
                if (duration / 1000 > 5)
                    arrayList.add(audioModel);
                i++;
            } while (mCursor.moveToNext());
        }

        mCursor.close();

        return arrayList;
    }

    public AudioModel getCurrentAudio() {
        return currentAudio;
    }

    public void setCurrentAudio(AudioModel currentAudio) {
        this.currentAudio = currentAudio;
    }

    @OnClick(R.id.btn_cancel)
    public void cancel() {
        this.dismiss();
    }

    @OnClick(R.id.btn_ok)
    public void choose() {
        EventBusHelper.post(new AudioPicker());
        this.dismiss();
    }

    public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {
        private static final String TAG = "CustomAdapter";
        private Context context;
        private ArrayList<AudioModel> mDataSet;
        private SparseBooleanArray selectedItems;
        int previousPosition;

        // BEGIN_INCLUDE(recyclerViewSampleViewHolder)

        /**
         * Provide a reference to the type of views that you are using (custom ViewHolder)
         */
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView mTvTitle;
            private TextView mTvDuration;
            private LinearLayout mRelativeLayout;

            public ViewHolder(View v) {
                super(v);
                // Define click listener for the ViewHolder's View.
                mTvTitle = (TextView) v.findViewById(R.id.tvAudioName);
                mTvDuration = (TextView) v.findViewById(R.id.tvDuration);
                mRelativeLayout = (LinearLayout) v.findViewById(R.id.rootln);
                selectedItems = new SparseBooleanArray();
                v.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                // Save the selected positions to the SparseBooleanArray
                int tempPosition = previousPosition;
                previousPosition = position;
                if (selectedItems.get(position, false)) {
//                    selectedItems.delete(getAdapterPosition());
//                    mSelectionRelative.setSelected(false);
                } else {
                    selectedItems.clear();
                    selectedItems.put(position, true);
                    mRelativeLayout.setSelected(true);
                    notifyItemChanged(tempPosition);
                }
                EventBusHelper.post(new AudioModel(getAdapterPosition()));
            }
        }

        MediaPlayer mp;

        /**
         * Initialize the dataset of the Adapter.
         *
         * @param photoArrayList ArrayList<Photo> containing the data to populate views to be used by RecyclerView.
         */
        public AudioAdapter(Context context, ArrayList<AudioModel> photoArrayList) {
            this.mDataSet = photoArrayList;
            this.context = context;
            //set up MediaPlayer
        }

        // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view.
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_audio, viewGroup, false);
            return new ViewHolder(v);
        }

        // END_INCLUDE(recyclerViewOnCreateViewHolder)
        // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            Log.d(TAG, "Element " + position + " set.");
            // Get element from your dataset at this position and replace the contents of the view
            viewHolder.mTvTitle.setText(mDataSet.get(position).title);
            viewHolder.mTvDuration.setText(convertTime(mDataSet.get(position).duration));
            viewHolder.mRelativeLayout.setSelected(selectedItems.get(position, false));

        }

        public String convertTime(long totalSecs) {
            totalSecs = totalSecs / 1000;
            long hours = totalSecs / 3600;
            long minutes = (totalSecs % 3600) / 60;
            long seconds = totalSecs % 60;

            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }

        // END_INCLUDE(recyclerViewOnBindViewHolder)
        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataSet.size();
        }
    }

}
