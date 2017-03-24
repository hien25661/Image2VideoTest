package com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.helpers;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.signature.StringSignature;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.R;

/**
 * Created by SF on 26/05/2016.
 */
public class DialogImageView extends Dialog {
    String urlImage;
    ImageView mImageView;
    RelativeLayout mRelativeConnectUser;
    TextView tvConnectUser;
    Context context;

    public DialogImageView(Context context, String urlImage) {
        super(context);
        this.urlImage = urlImage;
        this.context = context;
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        this.setContentView(R.layout.dialog_imageview);
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mImageView = (ImageView) findViewById(R.id.imvZoom);
        Glide.with(context).load(urlImage)
                .signature(new StringSignature("" + System.currentTimeMillis()))
                .into(mImageView);


    }
}
