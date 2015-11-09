package com.awrtechnologies.valor.valorfireplace.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.awrtechnologies.valor.valorfireplace.apiconstants.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class BitmapMergerTask extends AsyncTask<Void, Void, Bitmap> {

    private int size;

    public BitmapMergerTask() {

    }

    public BitmapMergerTask(int arraylistimagepath) {
        this.size = arraylistimagepath;
    }

    class BitmapMergerTaskException extends RuntimeException {
        BitmapMergerTaskException(String msg) {
            super(msg);
        }
    }

    @Override
    protected Bitmap doInBackground(Void... params) {

//        if (mBaseBitmap == null) {
//            throw new BitmapMergerTaskException("Base bitmap not set");
//        }
//
//        if (mMergeBitmap == null) {
//            throw new BitmapMergerTaskException("Merge bitmap not set");
//        }

        switch (mMergeOptions) {

            case MERGE_AT_ANGLE_OFF:
                return mergeAtAngle();
            case MERGE_FROM_TOP_LEFT:
                return mergeFromTopLeft();
            default:
                return mergeAtCenter();
        }
    }

    public void onPostExecute(Bitmap bitmap) {
        if (mMergeListener != null) {
            mMergeListener.onMerge(this, bitmap);
        }
    }

    public interface OnMergeListener {
        void onMerge(BitmapMergerTask task, Bitmap mergedBitmap);
    }

    public enum BitmapMergeOptions {
        MERGE_AT_CENTER,
        MERGE_AT_ANGLE_OFF,
        MERGE_FROM_TOP_LEFT,
    }

    private Bitmap mBaseBitmap;
    private Bitmap mMergeBitmap;
    private float mScale = 0.5f;
    private int mAngle = 0;
    private int mTopOffset = 0;
    private int mLeftOffset = 0;
    private BitmapMergeOptions mMergeOptions = BitmapMergeOptions.MERGE_AT_CENTER;
    private OnMergeListener mMergeListener;


    /**
     * Sets the scaling of the merge image.
     *
     * @param scale - float value from 0.0 to 1.0 represents the scale.
     * @return the related BitmapMergerTask
     */
    public BitmapMergerTask setScale(float scale) {
        this.mScale = scale;
        return this;
    }

    /**
     * Sets the base bitmap image.
     *
     * @param mBaseBitmap - base bitmap
     * @return the related BitmapMergerTask
     */
    public BitmapMergerTask setBaseBitmap(Bitmap mBaseBitmap) {
        this.mBaseBitmap = mBaseBitmap;
        return this;
    }

    /**
     * Sets the merge bitmap image.
     *
     * @param mMergeBitmap - merging bitmap image.
     * @return the related BitmapMergerTask
     */
    public BitmapMergerTask setMergeBitmap(Bitmap mMergeBitmap) {
        this.mMergeBitmap = mMergeBitmap;
        return this;
    }

    /**
     * Sets the merging offset points. Invoking this method will mark the merging mechanism to merge the mergeBitmap image to the base bitmap image
     * from the top left portion as specified by the params leftOffset and topOffset
     *
     * @param leftOffset pixel offsets from left
     * @param topOffset  pixel offsets from top
     * @return the related BitmapMergerTask
     */
    public BitmapMergerTask setOffsets(int leftOffset, int topOffset) {
        this.mTopOffset = topOffset;
        this.mLeftOffset = leftOffset;
        this.mMergeOptions = BitmapMergeOptions.MERGE_FROM_TOP_LEFT;
        return this;
    }

    /**
     * Sets the merging offset angle. Invoking this method will mark the merging mechanism to merge the mergeBitmap image to the base bitmap image
     * at the angle off from the base line from center to mid point on the right edge of the baseBitmap.
     *
     * @param angle - angle off from the base line.
     * @return the related BitmapMergerTask
     */
    public BitmapMergerTask setAngle(int angle) {
        this.mAngle = angle;
        this.mMergeOptions = BitmapMergeOptions.MERGE_AT_ANGLE_OFF;
        return this;
    }

    /**
     * Sets the listener for merge complete.
     *
     * @param listener for merge completeness.
     * @return the related BitmapMergerTask
     */
    public BitmapMergerTask setMergeListener(OnMergeListener listener) {
        this.mMergeListener = listener;
        return this;
    }

    /**
     * Initiates the merging task in the background
     */
    public void merge() {
        super.execute((Void[]) null);
    }

    private Bitmap mergeAtAngle() {

        if (mScale > 0) {

            int radius = mBaseBitmap.getWidth() / 4;
            int centerX = mBaseBitmap.getWidth() / 2;
            int centerY = mBaseBitmap.getHeight() / 2;

            double radians = Math.toRadians(mAngle);

            int x = (int) (radius * Math.cos(radians) + centerX);
            int y = (int) (radius * Math.sin(radians) + centerY);


            Bitmap overlayScaled = Bitmap.createScaledBitmap(mMergeBitmap, (int) (mBaseBitmap.getWidth() * mScale), (int) (mBaseBitmap.getHeight() * mScale), true);

            Bitmap workingBitmap = Bitmap.createBitmap(mBaseBitmap);
            Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

            Canvas canvas = new Canvas(mutableBitmap);

            x -= (overlayScaled.getWidth() / 2);
            y -= (overlayScaled.getHeight() / 2);


            canvas.drawBitmap(overlayScaled, x, y, new Paint());

            return mutableBitmap;
        } else {
            return mBaseBitmap;
        }

    }

    private Bitmap mergeFromTopLeft() {

        return mergeBitmaps(mBaseBitmap, mMergeBitmap, mScale, mLeftOffset, mTopOffset);
    }


    private Bitmap mergeAtCenter() {

        if (mScale > 0) {
//            mBaseBitmap = ImageLoader.getInstance().loadImageSync(arraylistimagepath.get(0));
            String extStorageDirectory = Environment.getExternalStorageDirectory()
                    .toString();
            File folder = new File(extStorageDirectory, "/." + Constants.APPNAME + "/png");
            mBaseBitmap = BitmapFactory.decodeFile(folder.getAbsolutePath()+"/model.png");


            Bitmap result = mBaseBitmap;

            for (int i = 0; i < size; i++) {
                try {
                    folder = new File(extStorageDirectory, "/." + Constants.APPNAME + "/AddonImage");
                    Bitmap overlayScaled = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(folder.getAbsolutePath() + "/addon" + i + ".png"), (int) (mBaseBitmap.getWidth() * mScale), (int) (mBaseBitmap.getHeight() * mScale), true);

                    int lockWidth = overlayScaled.getWidth();
                    int lockHeight = overlayScaled.getHeight();

                    int totalWidth = mBaseBitmap.getWidth();
                    int totalHeight = mBaseBitmap.getHeight();

                    final int startX = (totalWidth / 2) - (lockWidth / 2);
                    final int startY = (totalHeight / 2) - (lockHeight / 2);
                    mMergeBitmap = BitmapFactory.decodeFile(folder.getAbsolutePath() + "/addon" + i + ".png");
//                Bitmap mergeBitamp = ImageLoader.getInstance().loadImageSync(arraylistimagepath.get(i));
                    result = mergeBitmaps(result, mMergeBitmap, mScale, startX, startY);
                }catch (Exception e){

                }

            }
            return result;
        } else {
            return mBaseBitmap;
        }

    }

    private static Bitmap mergeBitmaps(Bitmap baseBitmap, Bitmap overlayBitmap, float scale, int leftOffset, int topOffset) {

        if (scale > 0) {
            Bitmap overlayScaled = Bitmap.createScaledBitmap(overlayBitmap, (int) (baseBitmap.getWidth() * scale), (int) (baseBitmap.getHeight() * scale), true);

            Bitmap workingBitmap = Bitmap.createBitmap(baseBitmap);
            Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);


            Canvas canvas = new Canvas(mutableBitmap);

            canvas.drawBitmap(overlayScaled, leftOffset, topOffset, new Paint());

            return mutableBitmap;
        } else {
            return baseBitmap;
        }

    }

}
