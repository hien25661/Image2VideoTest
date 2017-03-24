package com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.helpers.PhotoHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ducnguyen on 7/3/16.
 */
public class FrameView extends View {
    PointF sizeView = new PointF();
    Bitmap finalBitmap;
    Bitmap frameBitmap;
    float deltaX, deltaY;
    PointF pointLeftTop = new PointF();
    boolean isDrawText;
    List<PointF> points = new ArrayList<>();
    Paint paint = new Paint();
    Path mPath = new Path();
    public float stokeWidth = 10;


    public FrameView(Context context) {
        super(context);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(stokeWidth);
        paint.setColor(Color.WHITE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public Bitmap getFrameBitmap() {
        return frameBitmap;
    }

    public void setFrameBitmap(Bitmap frameBitmap) {
        this.frameBitmap = frameBitmap;
    }

    public FrameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    boolean isInit;

    public void setFinalBitmap(Bitmap inputBitmap) {
        this.finalBitmap = inputBitmap;
//        if (finalBitmap.getWidth() < finalBitmap.getHeight()) {
//            this.finalBitmap = PhotoHelper.scaleBitmapImage(inputBitmap, true, (int) sizeView.x, (int) sizeView.y);
//        } else {
//            this.finalBitmap = PhotoHelper.scaleBitmapImage(inputBitmap, false, (int) sizeView.x, (int) sizeView.y);
//
//        }

        if (!isInit) {
            isInit = true;
            mMatrix.postTranslate((sizeView.x - this.finalBitmap.getWidth()) / -2f,
                    (sizeView.y - finalBitmap.getHeight()) / -2f);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        sizeView.x = w;
        sizeView.y = h;
    }

    Matrix mMatrix = new Matrix();
    Matrix bgMatrix = new Matrix();
    boolean isUp;

    public Paint getPaint() {
        return paint;
    }

    public boolean isUp() {
        return isUp;
    }

    public void setUp(boolean up) {
        isUp = up;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (sizeView.x != 0 && finalBitmap != null) {
            canvas.drawBitmap(finalBitmap, sizeView.x / 2f - finalBitmap.getWidth() / 2f, sizeView.y / 2f - finalBitmap.getHeight() / 2f, new Paint());
            if (frameBitmap != null) {
                bgMatrix.reset();
                bgMatrix.setScale(sizeView.x * 1f / frameBitmap.getWidth(), sizeView.y * 1f / frameBitmap.getHeight());
                canvas.drawBitmap(frameBitmap, bgMatrix, null);
            }
        }
    }

    public Bitmap exportFinal() {
        Canvas mCanvas = new Canvas(finalBitmap);
        if (frameBitmap != null) {
            bgMatrix.reset();
            bgMatrix.setScale(sizeView.x * 1f / frameBitmap.getWidth(), sizeView.y * 1f / frameBitmap.getHeight());
            mCanvas.drawBitmap(frameBitmap, bgMatrix, null);
        }
        return finalBitmap;
    }

}
