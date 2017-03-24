package com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.activities;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.OnClick;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.R;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.adapters.ListPhotoDragAdapter;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.adapters.SimpleDividerItemDecoration;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.helpers.DialogImageView;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.helpers.PhotoHelper;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.models.ImageManager;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.models.Picture;

import nl.changer.polypicker.Config;
import nl.changer.polypicker.ImagePickerActivity;

public class CreateVideoActivity extends BaseActivity implements ListPhotoDragAdapter.OnItemClickListener {
    public static final int INTENT_REQUEST_GET_IMAGES = 222;

    @Bind(R.id.recycleView)
    RecyclerView mRecyclerView;
    ListPhotoDragAdapter listPhotoDragAdapter;
    @Bind(R.id.fab_convert)
    ImageView fab_convert;
    FFmpeg ffmpeg;
    ProgressDialog progressDialog;
    public static final String FOLDER_NAME = "ImageToVideo";
    public File folderParent;
    String uriVideo;
    String commandFFmpeg;
    ImageManager imageManager;
    @Bind(R.id.spnSecond)
    Spinner spnSecond;
    SpinnerAdapter spinnerAdapter;
//    String[] secondList = {"1","2","3","4","5"};
    int defaultSecond = 3;
    @Override
    public int setContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pick your Photo");
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.second_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSecond.setAdapter(adapter);
        spnSecond.setSelection(2);
        spnSecond.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                defaultSecond =i+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        imageManager = new ImageManager();
        String jsonData = getIntent().getStringExtra("data");
        imageManager.getObjectFromString(jsonData);
        listPhotoDragAdapter = new ListPhotoDragAdapter(mRecyclerView, this, imageManager.getUrlList());
        listPhotoDragAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(listPhotoDragAdapter);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ffmpeg = FFmpeg.getInstance(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        folderParent = new File(Environment.getExternalStorageDirectory(), FOLDER_NAME);
        folderParent.mkdir();

    }

    @Override
    public void initData() {
        loadFFMpegBinary();

    }

    private void loadFFMpegBinary() {
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    showSingleDialog("Your device unsupport");
                }
            });
        } catch (FFmpegNotSupportedException e) {
            showSingleDialog("Your device unsupport");
        }
    }

    private void execFFmpegBinary(final String[] command) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    Log.e("onFailure: ", " " + s);
                }

                @Override
                public void onSuccess(String s) {
                    Log.e("onSuccess: ", " " + s);

                }

                @Override
                public void onProgress(String s) {
                    progressDialog.setMessage("Processing: " + s);
                }

                @Override
                public void onStart() {
//                    outputLayout.removeAllViews();
//
//                    Log.d(TAG, "Started command : ffmpeg " + command);
//                    progressDialog.setMessage("Processing...");
                }

                @Override
                public void onFinish() {
//                    Log.d(TAG, "Finished command : ffmpeg " + command);
                    progressDialog.dismiss();

                    Intent mIntent = new Intent(CreateVideoActivity.this, AddSoundActivity.class);
                    mIntent.putExtra("videolink", uriVideo);
                    startActivityForResult(mIntent, 123);

                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // do nothing for now
        }
    }

    public static final String PNG_SUFFIX = ".jpg";

    @OnClick(R.id.fab_convert)
    public void runExecute() {
        new AsyncTask<Void, Void, Void>() {
            String[] fullCommand;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.show();
                progressDialog.setMessage("Loading...");
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    FileUtils.cleanDirectory(folderParent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                copyAllFile();
                uriVideo = createFolderVideo();
                String folderPicture = folderParent.getAbsolutePath() + "/" + "image%03d" + PNG_SUFFIX;
                Log.e("folderPicture: ", "  " + folderPicture);
                commandFFmpeg = "-framerate 2 -i " + folderPicture + " -c:v libx264 -r 30 -pix_fmt yuv420p " + uriVideo;
//        commandFFmpeg = "-framerate 1/3 -s 1280x720 -i " + folderPicture + " -c:v libx264 -r 30 -crf 25 -pix_fmt yuv420p " + uriVideo;
//        String[] fullCommand = new String[]{"-framerate","1/3","-i",folderPicture,"-c:v","libx264","-pix_fmt","yuv420p","-r","25",uriVideo};
                fullCommand = new String[]{"-framerate", "1/"+defaultSecond, "-i", folderPicture, "-preset", "ultrafast", "-r", "30", "-pix_fmt", "yuv420p", uriVideo};

//                fullCommand = commandFFmpeg.split(" ");

//        execFFmpegBinary(commandFFmpeg);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                execFFmpegBinary(fullCommand);

            }
        }.execute();


    }

    public String createFolderVideo() {
        File folderVideo = new File(Environment.getExternalStorageDirectory(), "VideoList");
        folderVideo.mkdir();
        return new File(folderVideo, System.currentTimeMillis() + ".mp4").getAbsolutePath();
    }

    public static String formatFileName(int number) {
        return "image" + String.format("%03d", number) + PNG_SUFFIX;
    }

    public void copyAllFile() {
        for (int i = 0; i < imageManager.getUrlList().size(); i++) {
            Bitmap photo = null;
            photo = BitmapFactory.decodeFile(imageManager.getUrlList().get(i).getUrl());

//            File fileCopy = new File(folderParent, formatFileName(i + 1));

            if (photo != null) {
//                Log.e("copyAlphotolFile: ", );
                Bitmap finalBitmap = null;
                if (photo.getWidth() > photo.getHeight()) {
                    finalBitmap = PhotoHelper.scaleBitmap(photo, false);
                } else {
                    finalBitmap = PhotoHelper.scaleBitmap(photo, true);
                }
                PhotoHelper.storeImageCamera(this, finalBitmap, folderParent, formatFileName(i + 1));
                photo.recycle();
                finalBitmap.recycle();

            }
//            try {
//                FileUtils.copyFile(new File(ImageManager.getUrlList().get(i).getUrl()), fileCopy);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

//    public void genNewFile(Bitmap mBitmap)
//    {
//        int width = mBitmap.getWidth();
//        int heigh = mBitmap.getHeight();
//        if (width < heigh)
//        {
//
//        }
//    }

    public void showSingleDialog(String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(content)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onItemClick(int type, int position) {
        switch (type) {
            case ListPhotoDragAdapter.ZOOM:
                new DialogImageView(this, imageManager.getUrlList().get(position).getUrl()).show();
                break;
            case ListPhotoDragAdapter.EDIT:
                Intent mIntent = new Intent(this, EditPhotoScreen.class);
                mIntent.putExtra("uri", imageManager.getUrlList().get(position).getUrl());
                mIntent.putExtra("position", position);
                startActivityForResult(mIntent, 1122);
                break;
            case ListPhotoDragAdapter.DELETE:
                imageManager.getUrlList().remove(position);
                listPhotoDragAdapter.notifyDataSetChanged();
                break;
        }
    }

    @OnClick(R.id.fab_addmore)
    public void addMoreImage() {
        getImages();
    }

    private void getImages() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        Config config = new Config.Builder()
                .setTabBackgroundColor(R.color.white)    // set tab background color. Default white.
                .setTabSelectionIndicatorColor(R.color.blue)
                .setCameraButtonColor(R.color.green)
                .build();
        ImagePickerActivity.setConfig(config);
        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case INTENT_REQUEST_GET_IMAGES:
                if (resultCode == Activity.RESULT_OK) {
                    if (requestCode == INTENT_REQUEST_GET_IMAGES) {
                        Parcelable[] parcelableUris = intent.getParcelableArrayExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

                        if (parcelableUris == null) {
                            return;
                        }

                        // Java doesn't allow array casting, this is a little hack
                        Uri[] uris = new Uri[parcelableUris.length];
                        System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);

                        if (uris != null) {
                            for (int i = 0; i < uris.length; i++) {
                                Log.i("show uri", " uri: " + uris[i]);
//                        mMedia.add(uri);
                                Picture mPicture = new Picture(imageManager.getUrlList().size() + i, uris[i].toString());
                                imageManager.addUrl(mPicture);
                                listPhotoDragAdapter.notifyDataSetChanged();
                            }

//                    showMedia();
                        }

                    }
                }
                break;
            case 1122:
                if (resultCode == Activity.RESULT_OK) {
                    String newUri = intent.getStringExtra("uri");
                    int position = intent.getIntExtra("position", -1);
                    if (position != -1) {
                        imageManager.getUrlList().set(position, new Picture(position, newUri));
                        listPhotoDragAdapter.notifyDataSetChanged();
                    }
                }
                break;
            default:
                if (resultCode == RESULT_OK) finish();
                break;
        }

    }

}
