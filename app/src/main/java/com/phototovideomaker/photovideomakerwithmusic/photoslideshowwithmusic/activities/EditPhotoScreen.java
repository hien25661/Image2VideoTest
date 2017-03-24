package com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.github.florent37.viewanimator.ViewAnimator;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.R;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.adapters.EmotionAdapter;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.adapters.SimpleDividerItemDecoration;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.utils.FileGLUtils;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.OnClick;
import info.hoang8f.android.segmented.SegmentedGroup;

public class EditPhotoScreen extends BaseActivity implements View.OnTouchListener, RadioGroup.OnCheckedChangeListener {
    @Bind(R.id.recycleView)
    RecyclerView recyclerView;
    int position = -1;
    @Bind(R.id.tvNext)
    TextView tvSave;
    ArrayList<String> emotionString = new ArrayList<>();
    ArrayList<String> frameList = new ArrayList<>();
    public static final String TAG = "EditPhoto";
    public static final String ASSET_FOLDER = "file:///android_asset/";
    Bitmap rootBm;
    int bmWidth, bmHeight;
    @Bind(R.id.rlContainer)
    RelativeLayout rlContainer;
    @Bind(R.id.ln_textSize)
    RelativeLayout ln_textSize;
    @Bind(R.id.lnColor)
    LinearLayout lnColor;
    DrawView drawView;
    @Bind(R.id.segmentedGroup)
    SegmentedGroup segmentedGroup;
    @Bind(R.id.sbSizeText)
    SeekBar mSeekbar;
    Animation anim, animDown;

    @Override
    public int setContentViewId() {
        return R.layout.activity_edit_photo_screen;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Photo Edit");
        anim = AnimationUtils.loadAnimation(this, R.anim.anim_bottom_up);
        animDown = AnimationUtils.loadAnimation(this, R.anim.anim_top_down);
        mSeekbar.setMax(20);
        mSeekbar.setProgress(10);
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (drawView != null) {
                    drawView.getPaint().setStrokeWidth(i == 0 ? 1 : i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mMoveDetector = new MoveGestureDetector(this,
                new MoveListener());
        drawView = new DrawView(this);
        rlContainer.addView(drawView);
        segmentedGroup.setOnCheckedChangeListener(this);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showLoading();
            }

            @Override
            protected Void doInBackground(Void... params) {
                Intent mIntent = getIntent();
                position = mIntent.getIntExtra("position", -1);
                String uri = mIntent.getStringExtra("uri");
                if (uri == null) {
                    uri = Uri.parse("R.mipmap.ic_launcher").toString();

                }

                try {

                    rootBm = Glide.with(EditPhotoScreen.this).load(uri).asBitmap().signature(new StringSignature(System.currentTimeMillis()+"")).into(-1, -1).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    rootBm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    rootBm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                }
                bmWidth = rootBm.getWidth();
                bmHeight = rootBm.getHeight();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                dismissLoading();
                drawView.setFinalBitmap(rootBm);
                drawView.setOnTouchListener(EditPhotoScreen.this);
                drawView.invalidate();
            }
        }.execute();

//        Bitmap temp = BitmapFactory.decodeFile(uri);
//        Log.e(TAG, "initView: "+temp.getHeight()+" "+temp.getWidth() );

    }

    public void closeAndShow(boolean isRecycleView) {
        if (isRecycleView) {
//            ViewAnimator.animate(recyclerView).translationY(-recyclerView.getHeight(), 0).duration(200).start();
//            ViewAnimator.animate(ln_textSize).translationY(0, ln_textSize.getHeight()).duration(200).start();
            recyclerView.startAnimation(anim);
            ln_textSize.startAnimation(animDown);
            ln_textSize.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);


        } else {
//            ViewAnimator.animate(ln_textSize).translationY(-ln_textSize.getHeight(),0).duration(200).start();
//            ViewAnimator.animate(recyclerView).translationY(0,recyclerView.getHeight()).duration(200).start();
            ln_textSize.startAnimation(anim);
            recyclerView.startAnimation(animDown);
            recyclerView.setVisibility(View.INVISIBLE);
            ln_textSize.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.btnEmoticon:
                closeAndShow(true);
                drawView.setDrawText(false);
//                Toast.makeText(EditPhotoScreen.this, "Emoticon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnText:
                drawView.replaceFinalBitmap();
                closeAndShow(false);
                drawView.setDrawText(true);
//                Toast.makeText(EditPhotoScreen.this, "Text", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private class MoveListener extends
            MoveGestureDetector.SimpleOnMoveGestureListener {
        @Override
        public boolean onMove(MoveGestureDetector detector) {
            PointF d = detector.getFocusDelta();
            drawView.setDeltaX(drawView.getDeltaX() + d.x);
            drawView.setDeltaY(drawView.getDeltaY() + d.y);
            drawView.invalidate();
            return true;
        }
    }

    @Override
    public void initData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                emotionString = FileGLUtils.listAssetFiles(EditPhotoScreen.this, "sticker");

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);


                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(EditPhotoScreen.this, LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(new EmotionAdapter(EditPhotoScreen.this, emotionString));
                recyclerView.addItemDecoration(new SimpleDividerItemDecoration(EditPhotoScreen.this));
            }
        }.execute();

    }

    /**
     * Create a File for saving an image or video
     */
    public static File getOutputMediaFile(Context context) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + context.getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        File mediaFile;
        String mImageName = "MI_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public static String storeImage(Context context, Bitmap image) {
        File pictureFile = getOutputMediaFile(context);
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
        return pictureFile.getAbsolutePath();
    }

    @OnClick(R.id.tvNext)
    public void Save() {
        new AsyncTask<Void, Void, Void>() {
            Bitmap saveBitmap;
            ProgressDialog progressDialog;
            String saveFile;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(EditPhotoScreen.this);
                progressDialog.show();
                progressDialog.setMessage("Loading");
                saveBitmap = drawView.getExportBitmap();
            }

            @Override
            protected Void doInBackground(Void... params) {
                saveFile = storeImage(EditPhotoScreen.this, saveBitmap);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                Intent mIntent = new Intent(EditPhotoScreen.this, FrameActivity.class);
                Log.e("onPostExecute: ", " " + saveFile);
                mIntent.putExtra("uri", saveFile);
                mIntent.putExtra("position", position);
                startActivityForResult(mIntent, 111);
//                setResult(RESULT_OK, mIntent);
//                finish();
            }
        }.execute();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent mIntent = new Intent();
            mIntent.putExtra("uri", data.getStringExtra("uri"));
            mIntent.putExtra("position", data.getIntExtra("position", 0));
            setResult(RESULT_OK, mIntent);
            finish();
        }
    }

    public void getBitmap(Bitmap stamp) {
        Canvas mCanvas = new Canvas(rootBm);
    }

    @Subscribe
    public void onItemClick(EmotionClick emotionClick) {
        Log.e("onItemClick: ", " " + emotionClick.position);
        showLoading();
        Glide.with(this).load(emotionString.get(emotionClick.position))
                .asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                dismissLoading();
                drawView.setMaskBitmap(resource);

            }
        });
    }

    //    float startX, startY;
//    float lastX, lastY;
    private MoveGestureDetector mMoveDetector;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!drawView.isDrawText) {
            mMoveDetector.onTouchEvent(event);
        } else {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    drawView.invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    drawView.invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    drawView.invalidate();
                    break;
            }
        }

        return true;
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        drawView.setUp(false);
        drawView.getmPath().reset();
        drawView.getmPath().moveTo(x, y);
        mX = x;
        mY = y;
    }

    boolean isLineTo;

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            isLineTo = true;
            drawView.getmPath().quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        } else {
//            drawView.getmPath().lineTo(mX, mY);
        }

    }

    private void touch_up() {
        drawView.setUp(true);

        drawView.getmPath().lineTo(mX, mY);

        // commit the path to our offscreen
//        mCanvas.drawPath(mPath, mPaint);
        drawView.commitToFinalBitmap();
        // kill this so we don't double draw
        drawView.clearPath();
    }

    @OnClick(R.id.lnColor)
    public void onClickChooseColor() {
        showDialogChooseColor();
    }

    public void showDialogChooseColor() {
        int colorInit = lnColor.getDrawingCacheBackgroundColor();
        int newColor = colorInit & 0xFF000000;
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .initialColor(0xFFFFFFFF)
                .lightnessSliderOnly()
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
//                        Toast.makeText(EditPhotoScreen.this, "onColorSelected: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        lnColor.setBackgroundColor(selectedColor);
                        drawView.getPaint().setColor(selectedColor);
// Toast.makeText(EditPhotoScreen.this, "setPositiveButton: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }


}
