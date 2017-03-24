package com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.helpers;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.R;


public class Progress_Dialog extends Dialog {
    Context mcontext;
    TextView tv_percents, tv_update;
    int type = NONE;
    public static final int PROGRESS = 0;
    public static final int NONE = 1;

    public Progress_Dialog(Context context) {
        super(context);
        this.mcontext = context;
    }

    public Progress_Dialog(Context context, int type) {
        super(context);
        this.mcontext = context;
        this.type = type;
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.getWindow().setLayout(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setLayout(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        this.setContentView(R.layout.custom_progressbar);
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        tv_percents = (TextView) findViewById(R.id.tv_progress_value);
        tv_update = (TextView) findViewById(R.id.tv_update);

        if (type == PROGRESS) {
            tv_percents.setVisibility(View.VISIBLE);
            tv_update.setVisibility(View.VISIBLE);
        } else {
            tv_percents.setVisibility(View.INVISIBLE);
            tv_update.setVisibility(View.INVISIBLE);
        }
    }

    public void setProgressValue(String value) {

        tv_percents.setText("" + value + "%");
    }


}
