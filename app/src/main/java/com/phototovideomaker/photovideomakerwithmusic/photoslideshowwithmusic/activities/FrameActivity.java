package com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.R;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.adapters.EmotionAdapter;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.adapters.FrameAdapter;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.adapters.SimpleDividerItemDecoration;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.utils.FileGLUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class FrameActivity extends BaseActivity {
    String urlPhoto;
    int index;
    FrameView frameView;
    @Bind(R.id.rlContainer)
    RelativeLayout rlContainer;
    @Bind(R.id.recycleView)
    RecyclerView recyclerView;
    ArrayList<String> frameList = new ArrayList<>();

    @Override
    public int setContentViewId() {
        return R.layout.activity_frame;
    }

    @Override
    public void initView() {
        urlPhoto = getIntent().getStringExtra("uri");
        index = getIntent().getIntExtra("position", 0);
        Log.e("initView: ", " " + urlPhoto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Frame");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        frameView = new FrameView(this);
        rlContainer.addView(frameView);
        Glide.with(this).load(urlPhoto)
                .asBitmap()
                .signature(new StringSignature("" + System.currentTimeMillis()))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        frameView.setFinalBitmap(resource);
                        frameView.invalidate();
                    }
                });
    }

    @Override
    public void initData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showLoading();
            }


            @Override
            protected Void doInBackground(Void... voids) {
                frameList = FileGLUtils.listAssetFiles(FrameActivity.this, "frame");
                frameList.add(0, null);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                dismissLoading();
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(FrameActivity.this, LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(new FrameAdapter(FrameActivity.this, frameList));
                recyclerView.addItemDecoration(new SimpleDividerItemDecoration(FrameActivity.this));
            }
        }.execute();
    }

    @Subscribe
    public void onItemClick(EmotionClick emotionClick) {
        Log.e("onItemClick: ", "" + emotionClick.position);
        if (emotionClick.position != 0) {
            showLoading();
            Glide.with(this).load(frameList.get(emotionClick.position))
                    .asBitmap()
                    .signature(new StringSignature("" + System.currentTimeMillis()))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            dismissLoading();
                            frameView.setFrameBitmap(resource);
                            frameView.invalidate();
                        }
                    });

        } else {
            frameView.setFrameBitmap(null);
            frameView.invalidate();
        }
    }

    @OnClick(R.id.tvSave)
    public void saveBitmap() {
        new AsyncTask<Void, Void, Void>() {
            Bitmap saveBitmap;
            String saveFile;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showLoading();
                saveBitmap = frameView.exportFinal();
            }

            @Override
            protected Void doInBackground(Void... params) {
                saveFile = EditPhotoScreen.storeImage(FrameActivity.this, saveBitmap);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                super.onPostExecute(aVoid);
                dismissLoading();
                Intent mIntent = new Intent();
                Log.e("onPostExecute: ", " " + saveFile);
                mIntent.putExtra("uri", saveFile);
                mIntent.putExtra("position", index);
                setResult(RESULT_OK, mIntent);
                finish();
            }
        }.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
