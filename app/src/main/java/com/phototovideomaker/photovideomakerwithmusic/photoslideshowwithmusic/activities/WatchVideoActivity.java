package com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;


import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.R;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.adapters.VideoCursorAdapter;

/**
 * Created by Hien on 6/11/2016.
 */
public class WatchVideoActivity extends BaseActivity {


    @Override
    public void onResume() {
        super.onResume();
        String[] parameters = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATE_ADDED,
        };
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        videocursor = getContentResolver().query(uri, parameters, null, null, MediaStore.Video.Media.DATE_TAKEN
                + " DESC");
//        videocursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//                parameters, null, null,MediaStore.Video.Media.DATE_TAKEN
//                        + " DESC" );

        videolist = (ListView) findViewById(R.id.PhoneVideoList);

        ListAdapter resourceCursorAdapter = new VideoCursorAdapter(this, this, R.layout.video_preview, videocursor);

        videolist.setAdapter(resourceCursorAdapter);
        videolist.setOnItemClickListener(videogridlistener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int setContentViewId() {
        return R.layout.main;
    }

    @Override
    public void initView() {
//        ActionBar actionBar= getSupportActionBar();
//        actionBar.setTitle("List Video");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("List Video");
    }

    @Override
    public void initData() {

    }

    Cursor videocursor;
    ListView videolist;

//    /** Called when the activity is first created. */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.main);
//    }

    private AdapterView.OnItemClickListener videogridlistener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View view, int position,
                                long id) {
            Cursor cursor = (Cursor) parent.getItemAtPosition(position);

            int fileNameIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            String filename = cursor.getString(fileNameIndex);
            Intent intent = new Intent(WatchVideoActivity.this, AddSoundActivity.class);
            intent.putExtra("videolink", filename);
            intent.putExtra("onlyview", true);
            startActivity(intent);
        }
    };
}
