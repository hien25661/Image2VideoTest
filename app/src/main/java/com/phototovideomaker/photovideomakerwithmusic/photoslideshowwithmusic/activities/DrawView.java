package com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.R;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.helpers.PhotoHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.makeramen.dragsortadapter.DragSortShadowBuilder.TAG;

/**
 * Created by ducnguyen on 7/3/16.
 */
public class DrawView extends View {
    PointF sizeView = new PointF();
    Bitmap finalBitmap;
    Bitmap maskBitmap;
    float deltaX, deltaY;
    PointF pointLeftTop = new PointF();
    boolean isDrawText;
    List<PointF> points = new ArrayList<>();
    Paint paint = new Paint();
    Path mPath = new Path();
    public float stokeWidth = 10;

    public boolean isDrawText() {
        return isDrawText;
    }

    public void setDrawText(boolean drawText) {
        isDrawText = drawText;
    }

    public Path getmPath() {
        return mPath;
    }

    private LinkedList<ItemPath> paths = new LinkedList<>();

    public void setmPath(Path mPath) {
        this.mPath = mPath;
    }

    public DrawView(Context context) {
        super(context);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(stokeWidth);
        paint.setColor(Color.WHITE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }


    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PointF getSizeView() {
        return sizeView;
    }

    public void setSizeView(PointF sizeView) {
        this.sizeView = sizeView;
    }

    public Bitmap getFinalBitmap() {
        return finalBitmap;
    }

    public Bitmap getExportBitmap() {
        replaceFinalBitmap();
        return finalBitmap;
    }

    boolean isInit;

    public void setFinalBitmap(Bitmap inputBitmap) {
        if (inputBitmap.getWidth() < inputBitmap.getHeight()) {
            inputBitmap = PhotoHelper.scaleBitmapImage(inputBitmap, true, (int) sizeView.x, (int) sizeView.y);

        } else {
            inputBitmap = PhotoHelper.scaleBitmapImage(inputBitmap, false, (int) sizeView.x, (int) sizeView.y);

        }
        this.finalBitmap = Bitmap.createBitmap((int) sizeView.x, (int) sizeView.y, Bitmap.Config.RGB_565);
        finalBitmap.eraseColor(Color.WHITE);
        Canvas mCanvas = new Canvas(finalBitmap);
        Log.e("setFinalBitmap: ", finalBitmap.getWidth() + " " + finalBitmap.getHeight());
        mCanvas.drawBitmap(inputBitmap, finalBitmap.getWidth() / 2f - inputBitmap.getWidth() / 2f, finalBitmap.getHeight() / 2f - inputBitmap.getHeight() / 2f, null);
//        pointLeftTop.x = sizeView -
        if (!isInit) {
            isInit = true;
            mMatrix.postTranslate((sizeView.x - this.finalBitmap.getWidth()) / -2f,
                    (sizeView.y - finalBitmap.getHeight()) / -2f);
        }
    }

    public Bitmap getMaskBitmap() {
        return maskBitmap;
    }

    boolean isReplace;

    public void setMaskBitmap(Bitmap maskBitmap) {
        if (this.maskBitmap != null) {
            replaceFinalBitmap();
            this.deltaX = 0;
            this.deltaY = 0;
            isReplace = true;
        }
        this.maskBitmap = maskBitmap;
        invalidate();
    }

    public float getDeltaX() {
        return deltaX;
    }

    public void setDeltaX(float deltaX) {
        this.deltaX = deltaX;
    }

    public float getDeltaY() {
        return deltaY;
    }

    public void setDeltaY(float deltaY) {
        this.deltaY = deltaY;
    }

    Matrix bgMatrix = new Matrix();

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        sizeView.x = w;
        sizeView.y = h;


    }

    Matrix mMatrix = new Matrix();
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
            canvas.drawBitmap(finalBitmap, 0, 0, null);
            if (maskBitmap != null && !isDrawText) {
                canvas.save();
                Log.e("onDraw: ", " " + deltaX + " " + deltaY);
//                canvas.translate(deltaX, deltaY);
                canvas.drawBitmap(maskBitmap, sizeView.x / 2f - maskBitmap.getWidth() / 2f + deltaX, sizeView.y / 2f - maskBitmap.getHeight() / 2f + deltaY, new Paint());
                canvas.restore();
            } else {
//                for (Path p : paths) {
//                    canvas.drawPath(p, paint);
//                }
//                if (!isUp) {
                canvas.drawPath(mPath, paint);
//                    for (ItemPath path : paths) {
//                        canvas.drawPath(path.getPath(), path.getPaint());
//                    }
//                }

            }
        }
    }

    public void addPoints(PointF mPointf) {
        points.add(mPointf);
    }

    public void replaceFinalBitmap() {
        Log.e("replaceFinalBitmap: ", " " + deltaX + " " + deltaY);
        Canvas mCanvas = new Canvas(finalBitmap);
        if (maskBitmap != null)
            mCanvas.drawBitmap(maskBitmap, finalBitmap.getWidth() / 2f - maskBitmap.getWidth() / 2f + deltaX, finalBitmap.getHeight() / 2f - maskBitmap.getHeight() / 2f + deltaY, new Paint());
    }


    class Point {
        float x, y;
        float dx, dy;

        @Override
        public String toString() {
            return x + ", " + y;
        }
    }

    public void commitToFinalBitmap() {
        Canvas mCanvas = new Canvas(finalBitmap);
        Path tempPath = new Path();
        paths.add(new ItemPath(new Path(mPath), new Paint(paint)));
        mPath.transform(mMatrix, tempPath);
        mCanvas.drawPath(tempPath, paint);
    }

    public void clearPath() {
        mPath = new Path();
//        paths.add(mPath);
//        paths.clear();
    }

    public class ItemPath {
        Path path;
        Paint paint;

        public Path getPath() {
            return path;
        }

        public void setPath(Path path) {
            this.path = path;
        }

        public Paint getPaint() {
            return paint;
        }

        public void setPaint(Paint paint) {
            this.paint = paint;
        }

        public ItemPath(Path path, Paint paint) {
            this.path = path;
            this.paint = paint;
        }
    }
}
