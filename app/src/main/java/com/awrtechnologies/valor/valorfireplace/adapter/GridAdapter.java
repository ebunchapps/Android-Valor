package com.awrtechnologies.valor.valorfireplace.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.awrtechnologies.valor.valorfireplace.Data.Series;
import com.awrtechnologies.valor.valorfireplace.R;
import com.awrtechnologies.valor.valorfireplace.helper.GeneralHelper;
import com.awrtechnologies.valor.valorfireplace.helper.RoundedCornerImage;
import com.awrtechnologies.valor.valorfireplace.uicomponents.roundimageview.RoundedImageView;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by awr005 on 29/05/15.
 */
public class GridAdapter extends BaseAdapter implements GestureDetector.OnGestureListener  {

    Context context;
    List<Series> arraylist;
    LayoutInflater inflater;
    Point screenSize;
    ImageLoader mloader;
    private GestureDetectorCompat gestureDetector;

    //        Image Rounded

    private  int color;
    private  Paint paint;
    private  Rect rect;
    private  RectF rectF;
    private  Bitmap imageOut;
    private  Canvas canvas;
    private  float roundPx;

    public GridAdapter(Context context, List<Series> arraylist) {
        this.arraylist = arraylist;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        screenSize = GeneralHelper.getInstance(context).getScreenSize();
        mloader = ImageLoader.getInstance();

    }

    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return arraylist.get(position).toString().hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder h;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.grid_items, null);
            h = new Holder();
            h.imglistitem = (ImageView) convertView
                    .findViewById(R.id.imglistitem);
            h.txtheadingList = (TextView) convertView
                    .findViewById(R.id.txtheadingList);
            h.linearContent = (LinearLayout) convertView
                    .findViewById(R.id.linearContent);

//            ViewGroup.LayoutParams lp2 = (ViewGroup.LayoutParams) h.linearContent.getLayoutParams();
//            lp2.width = (int) Math.round(screenSize.x / 2.0);
//            lp2.height = (int) Math.round(screenSize.y / 10.5);
//            h.linearContent.setLayoutParams(lp2);

            ViewGroup.LayoutParams lp1 = (ViewGroup.LayoutParams) h.imglistitem.getLayoutParams();
//            lp1.width = (int) Math.round(screenSize.x / 1.5);
            lp1.height = (int) Math.round(screenSize.y / 7.0);
            h.imglistitem.setLayoutParams(lp1);
            convertView.setTag(h);
        } else {
            h = (Holder) convertView.getTag();
        }

        mloader.displayImage(arraylist.get(position).bannerImage, h.imglistitem);

        h.txtheadingList.setText(arraylist.get(position).title);
//        convertView.setOnTouchListener(new CustomTouchListener());


        return convertView;
    }

    public class Holder {
     ImageView imglistitem;
        TextView txtheadingList;
        LinearLayout linearContent;
    }

    public class CustomTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {

            Holder hld = (Holder) view.getTag();

            switch (motionEvent.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    ValueAnimator colorAnim = ObjectAnimator.ofInt(
                            (view), "backgroundColor", 0x00FFFFFF, 0x506E6E6E);
                    colorAnim.setDuration(250);
                    colorAnim.setEvaluator(new ArgbEvaluator());
                    colorAnim.start();

                    break;

                case MotionEvent.ACTION_CANCEL:
                    colorAnim = ObjectAnimator.ofInt(( view),
                            "backgroundColor", 0x00FFFFFF, 0x506E6E6E);
                    colorAnim.setDuration(250);
                    colorAnim.setEvaluator(new ArgbEvaluator());
                    colorAnim.start();

                case MotionEvent.ACTION_UP:

                    colorAnim = ObjectAnimator.ofInt((view),
                            "backgroundColor", 0x506E6E6E, 0x00FFFFFF);
                    colorAnim.setDuration(250);
                    colorAnim.setEvaluator(new ArgbEvaluator());
                    colorAnim.start();

                    break;
            }
            return false;
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // TODO Auto-generated method stub
        return false;
    }

}
