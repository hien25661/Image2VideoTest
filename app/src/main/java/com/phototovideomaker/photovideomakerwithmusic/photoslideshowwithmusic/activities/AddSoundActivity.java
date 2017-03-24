package com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import butterknife.Bind;
import butterknife.OnClick;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.R;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.helpers.AudioModel;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.helpers.AudioPicker;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.helpers.DialogPickAudio;

import org.apache.commons.io.FileUtils;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class AddSoundActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.videoView)
    VideoView videoView;
    @Bind(R.id.btn_add_audio)
    Button btnAddAudio;
    private MediaController mediaControls;
    String audioFilePath = "";
    ProgressDialog progressDialog;
    String videoUri;
    @Bind(R.id.edt_input)
    EditText edtInputName;
    @Bind(R.id.btnDone)
    ImageView btnDone;
    boolean isAddSound;

    @OnClick(R.id.btnDone)
    public void clickDone() {
        if (isAddSound) {
            try {
                File aFile = new File(fileNew);
                String parenFile = aFile.getParent();
                String newName = edtInputName.getText().toString() + "_" + System.currentTimeMillis() + ".mp4";
                File newFile = new File(parenFile, newName);
                try {
                    FileUtils.moveFile(aFile, newFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MediaScannerConnection.scanFile(
                        this, new String[]{newFile.getAbsolutePath()},
                        new String[]{"audio/mp3"},
                        new MediaScannerConnection.MediaScannerConnectionClient() {
                            public void onMediaScannerConnected() {
                            }

                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
                File folderParent = new File(Environment.getExternalStorageDirectory(), CreateVideoActivity.FOLDER_NAME);
                folderParent.mkdir();
                try {
                    FileUtils.cleanDirectory(folderParent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(AddSoundActivity.this, "Success !!! Watch your video at WatchVideo screen.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }catch (Exception e)
            {
                Toast.makeText(AddSoundActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm")
                    .setMessage("Do you want save video without sound?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            File aFile = new File(videoUri);
                            String parenFile = aFile.getParent();
                            String newName = edtInputName.getText().toString() + "_" + System.currentTimeMillis() + ".mp4";
                            File newFile = new File(parenFile, newName);
                            try {
                                FileUtils.moveFile(aFile, newFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            MediaScannerConnection.scanFile(
                                    AddSoundActivity.this, new String[]{newFile.getAbsolutePath()},
                                    new String[]{"audio/mp3", "*/*"},
                                    new MediaScannerConnection.MediaScannerConnectionClient() {
                                        public void onMediaScannerConnected() {
                                        }

                                        public void onScanCompleted(String path, Uri uri) {
                                        }
                                    });
                            File folderParent = new File(Environment.getExternalStorageDirectory(), CreateVideoActivity.FOLDER_NAME);
                            folderParent.mkdir();
                            try {
                                FileUtils.cleanDirectory(folderParent);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(AddSoundActivity.this, "Success !!! Watch your video at WatchVideo screen.", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    @Bind(R.id.adView)
    AdView adView;

    @Override
    public int setContentViewId() {
        return R.layout.activity_list_video;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        try {
//            File mFile = new File(videoUri);
//            mFile.delete();
//        } catch (Exception e) {
//
//        }
    }

    @Override
    public void initView() {

        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    String currentPath;

    @Subscribe
    public void itemChoose(AudioModel audioModel) {
        currentPath = pickDialog.getmListAudio().get(audioModel.position).getUriPath();
        pickDialog.enableButton(true);
        pickDialog.audioPlayer(currentPath);
        Log.e("itemChoose: ", "  " + pickDialog.getmListAudio().get(audioModel.position).getUriPath());
    }

    @Subscribe
    public void onAudioPicker(AudioPicker audioPicker) {
        Log.e("onAudioPicker: ", "click");
        fileNew = createFolderVideo();

//                String commandFFmpeg = "-i "+videoUri+" -i "+ URLEncoder.encode(path,"utf-8")+" -c:v libx264 -c:a libvorbis -shortest "+fileNew;
//                -map 0:1 -map 1:1 -c copy
        String[] fullCommand = new String[]{"-i", videoUri, "-i", currentPath, "-vcodec", "copy", "-acodec", "aac","-preset", "ultrafast", "-shortest", fileNew};
//                String[] fullCommand = new String[]{"-i", videoUri, "-i", path, "-c:v","libx264","-c:a","libvorbis", "-shortest", fileNew};
        execFFmpegBinary(fullCommand);
    }

    @Override
    public void initData() {
        videoUri = getIntent().getStringExtra("videolink");
        boolean onlyView = getIntent().getBooleanExtra("onlyview", false);
        if (onlyView) {
            btnAddAudio.setVisibility(View.GONE);
            toolbar.setVisibility(View.INVISIBLE);
        }
        pickDialog = new DialogPickAudio(this);

        if (mediaControls == null) {
            mediaControls = new MediaController(this);
        }

        videoView.setMediaController(mediaControls);
        if (videoUri != null) {
            videoView.setVideoURI(Uri.parse(videoUri));
            videoView.start();
        }
    }

    DialogPickAudio pickDialog;

    @OnClick(R.id.btn_add_audio)
    public void openDialogSelect() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.INTERNAL_CONTENT_URI);
//        startActivityForResult(Intent.createChooser(intent, "Select audio"), 111);
        pickDialog.show();

    }

    public String createFolderVideo() {
        File folderVideo = new File(Environment.getExternalStorageDirectory(), "VideoList");
        folderVideo.mkdir();
        return new File(folderVideo, System.currentTimeMillis() + ".mp4").getAbsolutePath();
    }

    String fileNew;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
//                String path = getRealPathFromURI(this, data.getData());
//                if (path != null)
//                    Log.e("onActivityResult: ", " " + path);
//                fileNew = createFolderVideo();
//
////                String commandFFmpeg = "-i "+videoUri+" -i "+ URLEncoder.encode(path,"utf-8")+" -c:v libx264 -c:a libvorbis -shortest "+fileNew;
////                -c:v libx264 -c:a libvorbis -shortest
//                String[] fullCommand = new String[]{"-i", videoUri, "-i", path, "-codec", "copy", "-shortest", fileNew};
////                String[] fullCommand = new String[]{"-i", videoUri, "-i", path, "-c:v","libx264","-c:a","libvorbis", "-shortest", fileNew};
//                execFFmpegBinary(fullCommand);
//                File audio = new File(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void execFFmpegBinary(final String[] command) {
        try {
            FFmpeg.getInstance(this).execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    Toast.makeText(AddSoundActivity.this, "Audio file does not support. Please choose another audio.", Toast.LENGTH_SHORT).show();
                    Log.e("onFailure: ", " " + s);
                }

                @Override
                public void onSuccess(String s) {
                    Log.e("onSuccess: ", " " + s);
                    videoView.setVideoURI(Uri.parse(fileNew));
                    videoView.start();
                    try {
                        File mFile = new File(videoUri);
                        mFile.delete();
                    } catch (Exception e) {

                    }
                    btnAddAudio.setVisibility(View.INVISIBLE);
                    isAddSound = true;
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
                    progressDialog.show();
                }

                @Override
                public void onFinish() {
//                    Log.d(TAG, "Finished command : ffmpeg " + command);
                    progressDialog.dismiss();


                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // do nothing for now
        }
    }
}
