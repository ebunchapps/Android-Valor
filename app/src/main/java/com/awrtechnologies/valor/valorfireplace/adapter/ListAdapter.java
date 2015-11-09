package com.awrtechnologies.valor.valorfireplace.adapter;

import android.content.Context;
import android.graphics.Point;
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

import com.awrtechnologies.valor.valorfireplace.Data.Application;
import com.awrtechnologies.valor.valorfireplace.R;
import com.awrtechnologies.valor.valorfireplace.helper.GeneralHelper;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by awr005 on 29/05/15.
 */
public class ListAdapter extends BaseAdapter  implements GestureDetector.OnGestureListener {

    Context context;
    LayoutInflater inflater;
    List<Application> arraylist;

    Point screensize;
    private GestureDetectorCompat gestureDetector;
    ImageLoader mloader;


    public ListAdapter(Context context, List<Application> arraylist) {

        this.context = context;
        this.arraylist = arraylist;
        this.inflater = LayoutInflater.from(context);
        mloader = ImageLoader.getInstance();
        screensize = GeneralHelper.getInstance(context).getScreenSize();
        gestureDetector = new GestureDetectorCompat(context, this);

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

        return arraylist.toString().hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder hld;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.list_items_first, null);
            hld = new Holder();

            hld.imglistitem = (ImageView) convertView
                    .findViewById(R.id.imglistitem);
            hld.txtheadingList = (TextView) convertView
                    .findViewById(R.id.txtheadingList);
            hld.txtSubheadingList = (TextView) convertView
                    .findViewById(R.id.txtSubheadingList);
            hld.linearContent = (LinearLayout) convertView
                    .findViewById(R.id.linearContent);

            convertView.setTag(hld);

        } else {
            hld = (Holder) convertView.getTag();
        }
        ViewGroup.LayoutParams lp2 = (ViewGroup.LayoutParams) hld.linearContent.getLayoutParams();
        lp2.width = (int) Math.round(screensize.x / 2.8);
        lp2.height = (int) Math.round(screensize.y / 6.5);
        hld.linearContent.setLayoutParams(lp2);

        ViewGroup.LayoutParams lp1 = (ViewGroup.LayoutParams) hld.imglistitem.getLayoutParams();
        lp1.width = (int) Math.round(screensize.x / 1.5);
        lp1.height = (int) Math.round(screensize.y / 4.0);
        hld.imglistitem.setLayoutParams(lp1);
        mloader.displayImage(arraylist.get(position).banner_image, hld.imglistitem);

//        hld.txtheadingList.setText(arraylist.get(position).title);
//        hld.txtSubheadingList.setText(arraylist.get(position).description);
//        convertView.setOnTouchListener(new CustomTouchListener());
        return convertView;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public class Holder {

        ImageView imglistitem;
        TextView txtheadingList;
        TextView txtSubheadingList;

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



}

