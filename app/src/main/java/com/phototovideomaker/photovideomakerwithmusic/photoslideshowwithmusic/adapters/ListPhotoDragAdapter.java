package com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.adapters;


import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.makeramen.dragsortadapter.DragSortAdapter;
import com.makeramen.dragsortadapter.NoForegroundShadowBuilder;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.R;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.models.Picture;

/**
 * Created by ducnguyen on 6/19/16.
 */

public class ListPhotoDragAdapter extends DragSortAdapter<ListPhotoDragAdapter.MainViewHolder> {

    public static final String TAG = ExampleAdapter.class.getSimpleName();
    public static final int ZOOM = 1;
    public static final int EDIT = 2;
    public static final int DELETE = 3;

    private final ArrayList<Picture> data;
    Context context;
    OnItemClickListener mOnItemClickListener;

    public ListPhotoDragAdapter(RecyclerView recyclerView, Context context, ArrayList<Picture> data) {
        super(recyclerView);
        this.data = data;
        this.context = context;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_list_image, parent, false);
        MainViewHolder holder = new MainViewHolder(this, view);
        view.setOnClickListener(holder);
        view.setOnLongClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, final int position) {
        int itemId = data.get(position).getId();
        int number = position + 1;
        Glide.with(context).load(data.get(position).getUrl())
                .signature(new StringSignature(System.currentTimeMillis() + ""))
                .into(holder.imvPhoto);
        // NOTE: check for getDraggingId() match to set an "invisible space" while dragging
        holder.container.setVisibility(getDraggingId() == itemId ? View.INVISIBLE : View.VISIBLE);
        holder.container.postInvalidate();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getPositionForId(long id) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId() == id)
                return i;
        }
        return 0;
    }

    @Override
    public boolean move(int fromPosition, int toPosition) {
        Log.e("move: " + fromPosition, " " + toPosition);
        Picture picture = new Picture(data.get(fromPosition).getId(), data.get(fromPosition).getUrl());
        data.remove(fromPosition);
        data.add(toPosition, picture);
        return true;
    }

    public class MainViewHolder extends DragSortAdapter.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {

        @Bind(R.id.container)
        RelativeLayout container;
        @Bind(R.id.imvPhoto)
        ImageView imvPhoto;
        @Bind(R.id.imvEdit)
        TextView imvEdit;
        @Bind(R.id.imvZoom)
        TextView imvZoom;
        @Bind(R.id.imvDel)
        TextView imvDel;

        public MainViewHolder(DragSortAdapter adapter, View itemView) {
            super(adapter, itemView);
            ButterKnife.bind(this, itemView);
            imvZoom.setOnClickListener(this);
            imvEdit.setOnClickListener(this);
            imvDel.setOnClickListener(this);
        }

        @Override
        public void onClick(@NonNull View v) {
            int position = getAdapterPosition();

            switch (v.getId()) {
                case R.id.imvZoom:
                    mOnItemClickListener.onItemClick(ZOOM, position);
                    break;
                case R.id.imvEdit:
                    mOnItemClickListener.onItemClick(EDIT, position);
                    break;
                case R.id.imvDel:
                    mOnItemClickListener.onItemClick(DELETE, position);
                    break;
            }
        }

        @Override
        public boolean onLongClick(@NonNull View v) {
            startDrag();
            return true;
        }

        @Override
        public View.DragShadowBuilder getShadowBuilder(View itemView, Point touchPoint) {
            return new NoForegroundShadowBuilder(itemView, touchPoint);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(int type, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }
}