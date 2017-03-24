package com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ducnguyen on 6/25/16.
 */
public class PhotoHelper {

    public static String storeImageCamera(Context context, Bitmap image,
                                          File pictureFile, String name) {
        String uri = "";
//        File pictureFile = getOutputMediaFileCamera(Folder);

        if (pictureFile == null) {
            Log.d("error",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return uri;
        }
        File mfile = new File(pictureFile, name);
        uri = mfile.getAbsolutePath();
        try {
            FileOutputStream fos = new FileOutputStream(mfile);
            image.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("error", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("error", "Error accessing file: " + e.getMessage());
        }
        return uri;
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, boolean isVertical) {
        int wantedWidth = 720;
        int wantedHeight = 1280;
        int bmWidth = bitmap.getWidth();
        int bmHeight = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        if (isVertical) {
            float xScale = ((float) wantedWidth) / bmWidth;
            float yScale = ((float) wantedHeight) / bmHeight;
            float scale = (xScale <= yScale) ? xScale : yScale;
            Log.i("Test", "xScale = " + Float.toString(xScale));
            Log.i("Test", "yScale = " + Float.toString(yScale));
            Log.i("Test", "scale = " + Float.toString(scale));

            // Create a matrix for the scaling and add the scaling data
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            // Create a new bitmap and convert it to a format understood by the ImageView
            Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, bmWidth, bmHeight, matrix, true);
            Log.e("scaledBitmap", "scaledBitmap = " + scaledBitmap.getWidth()+" va "+scaledBitmap.getHeight());

            bmWidth = scaledBitmap.getWidth(); // re-use
            bmHeight = scaledBitmap.getHeight(); // re-use

            canvas.drawBitmap(scaledBitmap, (float)(wantedWidth-bmWidth)/2f,(float)(wantedHeight-bmHeight)/2f, new Paint());

        } else {
            float xScale = ((float) wantedWidth) / bmWidth;
            float yScale = ((float) wantedHeight) / bmHeight;
            float scale = (xScale <= yScale) ? xScale : yScale;
            Log.i("Test", "xScale = " + Float.toString(xScale));
            Log.i("Test", "yScale = " + Float.toString(yScale));
            Log.i("Test", "scale = " + Float.toString(scale));

            // Create a matrix for the scaling and add the scaling data
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            // Create a new bitmap and convert it to a format understood by the ImageView
            Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, bmWidth, bmHeight, matrix, true);
            bmWidth = scaledBitmap.getWidth(); // re-use
            bmHeight = scaledBitmap.getHeight(); // re-use

            canvas.drawBitmap(scaledBitmap, (float)(wantedWidth-bmWidth)/2f,(float)(wantedHeight-bmHeight)/2f, new Paint());
        }
        return output;
    }

    public static Bitmap scaleBitmapImage(Bitmap bitmap, boolean isVertical,int wantedWidth,int wantedHeight) {
        int bmWidth = bitmap.getWidth();
        int bmHeight = bitmap.getHeight();
        if (isVertical) {
            float xScale = ((float) wantedWidth) / bmWidth;
            float yScale = ((float) wantedHeight) / bmHeight;
            float scale = (xScale <= yScale) ? xScale : yScale;
            Log.i("Test", "xScale = " + Float.toString(xScale));
            Log.i("Test", "yScale = " + Float.toString(yScale));
            Log.i("Test", "scale = " + Float.toString(scale));

            // Create a matrix for the scaling and add the scaling data
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            // Create a new bitmap and convert it to a format understood by the ImageView
            return Bitmap.createBitmap(bitmap, 0, 0, bmWidth, bmHeight, matrix, true);

        } else {
            float xScale = ((float) wantedWidth) / bmWidth;
            float yScale = ((float) wantedHeight) / bmHeight;
            float scale = (xScale <= yScale) ? xScale : yScale;
            Log.i("Test", "xScale = " + Float.toString(xScale));
            Log.i("Test", "yScale = " + Float.toString(yScale));
            Log.i("Test", "scale = " + Float.toString(scale));

            // Create a matrix for the scaling and add the scaling data
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            // Create a new bitmap and convert it to a format understood by the ImageView
            return Bitmap.createBitmap(bitmap, 0, 0, bmWidth, bmHeight, matrix, true);

        }
    }
}
