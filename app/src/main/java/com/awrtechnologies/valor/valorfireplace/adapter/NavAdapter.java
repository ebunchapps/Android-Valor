package com.awrtechnologies.valor.valorfireplace.adapter;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Shader;
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

import com.awrtechnologies.valor.valorfireplace.Data.NavData;
import com.awrtechnologies.valor.valorfireplace.R;
import com.awrtechnologies.valor.valorfireplace.helper.GeneralHelper;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by awr005 on 05/06/15.
 */
public class NavAdapter extends BaseAdapter implements GestureDetector.OnGestureListener {

    Point point;
    public ImageLoader mLoader;

    Context context;
    ArrayList<NavData> arraylist;
    LayoutInflater layinflt;
//    private GestureDetectorCompat gestureDetector;

    public NavAdapter(Context context, ArrayList<NavData> arraylist) {
        this.context = context;
        this.arraylist = arraylist;
        layinflt = LayoutInflater.from(context);
        mLoader = ImageLoader.getInstance();
        point = GeneralHelper.getInstance(context).getScreenSize();
//        gestureDetector = new GestureDetectorCompat(context, this);

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

            hld = new Holder();
            convertView = layinflt.inflate(R.layout.nav_list_items, null);
            hld.text = (TextView) convertView.findViewById(R.id.txtname);
            hld.image = (ImageView) convertView
                    .findViewById(R.id.imageview);
            hld.linearmain =  (LinearLayout) convertView
                    .findViewById(R.id.linear);
            convertView.setTag(hld);


            ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) hld.image.getLayoutParams();
            lp.width = point.x / 42;
            lp.height = point.x / 42;
            hld.image.setLayoutParams(lp);


        } else {
            hld = (Holder) convertView.getTag();
        }
        hld.text.setText(arraylist.get(position).getText());
        // String url = (arraylist.get(position).getImages());
        // String url=String.valueOf(arraylist.get(position).getImages());
        // mLoader.displayImage(url, hld.image);
        hld.image.setImageResource(arraylist.get(position).getImages());
//         convertView.setOnTouchListener(new CustomTouchListener());
        return convertView;
    }

    public class Holder {
        TextView text;
        ImageView image;
        LinearLayout linearmain;
    }


    public class CustomTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {

            Holder hld = (Holder) view.getTag();

            switch (motionEvent.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    ValueAnimator colorAnim = ObjectAnimator.ofInt(
                            (view), "backgroundColor", 0x00333333 , 0xFFe9262d);
                    colorAnim.setDuration(250);
                    colorAnim.setEvaluator(new ArgbEvaluator());
                    colorAnim.start();

                    break;

                case MotionEvent.ACTION_CANCEL:
                    colorAnim = ObjectAnimator.ofInt(( view),
                            "backgroundColor", 0xFFe9262d, 0x00333333);
                    colorAnim.setDuration(250);
                    colorAnim.setEvaluator(new ArgbEvaluator());
                    colorAnim.start();

                case MotionEvent.ACTION_UP:

                    colorAnim = ObjectAnimator.ofInt((view),
                            "backgroundColor", 0xFFe9262d, 0x00333333);
                    colorAnim.setDuration(250);
                    colorAnim.setEvaluator(new ArgbEvaluator());
                    colorAnim.start();

                    break;

                default:
                    break;
            }



            return true;
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