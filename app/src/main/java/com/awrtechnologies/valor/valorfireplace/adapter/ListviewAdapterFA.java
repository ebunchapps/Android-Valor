package com.awrtechnologies.valor.valorfireplace.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.awrtechnologies.valor.valorfireplace.Data.SeriesDetail;
import com.awrtechnologies.valor.valorfireplace.Data.User;
import com.awrtechnologies.valor.valorfireplace.R;
import com.awrtechnologies.valor.valorfireplace.helper.GeneralHelper;
import com.awrtechnologies.valor.valorfireplace.uicomponents.PreferencesManager;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by awr005 on 03/06/15.
 */
public class ListviewAdapterFA extends BaseAdapter implements GestureDetector.OnGestureListener {

    Context context;
    LayoutInflater inflater;
    ArrayList<SeriesDetail> arraylist;
    private GestureDetectorCompat gestureDetector;
    Point screensize;
    Holder hld;
    int type;


    public ListviewAdapterFA(Context context, ArrayList<SeriesDetail> arraylist) {

        this.context = context;
        this.arraylist = arraylist;
        this.inflater = LayoutInflater.from(context);
        screensize = GeneralHelper.getInstance(context).getScreenSize();
        gestureDetector = new GestureDetectorCompat(context, this);
//        this.type = type;

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
//        if (position == 0) {


            if(arraylist.get(position).type==1) {
                convertView = inflater.inflate(R.layout.list_items_in_fourth, null);

                TextView txtTitle = (TextView) convertView
                        .findViewById(R.id.txtTitle);
                TextView txtTitlePart = (TextView) convertView
                        .findViewById(R.id.txtTitlePart);
                TextView txtContent = (TextView) convertView
                        .findViewById(R.id.txtContent);

                TextView txtSeriesTitle = (TextView) convertView
                        .findViewById(R.id.txtSeriesTitle);
                TextView txtSeriesDesc = (TextView) convertView
                        .findViewById(R.id.txtSeriesDesc);

                TextView txtBackgroundTitle = (TextView) convertView
                        .findViewById(R.id.txtBackgroundTitle);


                TextView txtModelprice = (TextView) convertView
                        .findViewById(R.id.txtModelPrice);
                TextView txtModelTitle = (TextView) convertView
                        .findViewById(R.id.txtModelTitle);
                TextView txtModelDesc = (TextView) convertView
                        .findViewById(R.id.txtModelDesc);
                txtContent.setText(arraylist.get(position).getApplicationdesc());
                txtSeriesTitle.setText(arraylist.get(position).getSeriestitle());
                txtSeriesDesc.setText(arraylist.get(position).getSeriesdesc());

                txtModelTitle.setText(arraylist.get(position).getModeltitle());
                txtModelDesc.setText(arraylist.get(position).getModeldesc());

                if (arraylist.get(position).getModelprice() != 0) {
                   boolean showPrice =  PreferencesManager.getPreferenceBooleanByKey(context, "isLogin");
                    if(showPrice) {
                        txtModelprice.setText("$" + arraylist.get(position).getModelprice());
                    }else{
                        txtModelprice.setText("");
                    }

                } else {
                    txtModelprice.setText("");
                }
                if (GeneralHelper.getInstance(context).ischecked() == true) {
                    txtBackgroundTitle.setText("Selected");
                } else {
                    txtBackgroundTitle.setText("Not Selected");
                }
//            }
            return convertView;

        }
//        if (position == 1) {

            if(arraylist.get(position).type==0) {
                convertView = inflater.inflate(R.layout.categoryaddview, null);
                TextView txtAddonsprice = (TextView) convertView
                        .findViewById(R.id.txtAddonsPrice);
                TextView txtAddonsTitle = (TextView) convertView
                        .findViewById(R.id.txtAddonsTitle);
                TextView txtCategoryTitle = (TextView) convertView
                        .findViewById(R.id.txtCategoryTitle);
//                if (GeneralHelper.getInstance(context).isAddoncheck() == true) {
//                    Log.d("Pawan", "none");
//                    txtAddonsprice.setVisibility(View.GONE);
//                    txtAddonsTitle.setVisibility(View.GONE);
//                } else {

                    Log.d("Pawan", "not none");
                    txtAddonsprice.setVisibility(View.VISIBLE);
                    txtAddonsTitle.setVisibility(View.VISIBLE);
                    txtAddonsTitle.setText(arraylist.get(position).getAddonstitle());
                    txtCategoryTitle.setText(arraylist.get(position).getCategoryname() + "--");
                    if (arraylist.get(position).getAddonsprice() != 0) {
                        boolean showPrice =  PreferencesManager.getPreferenceBooleanByKey(context, "isLogin");
                        if(showPrice) {
                            txtAddonsprice.setText("$" + arraylist.get(position).getAddonsprice() + "");
                        }else{
                            txtAddonsprice.setText("");
                        }

                    } else {
                        txtAddonsprice.setText("");
                    }
//                }
//            }
            return convertView;
        }
        return null;
    }


    public void setList(List<SeriesDetail> arraylist) {
        arraylist = arraylist;
    }

    public class Holder {

        TextView txtTitle;
        TextView txtTitlePart;
        TextView txtContent;

        TextView txtSeriesTitle;
        TextView txtSeriesDesc;

        TextView txtBackgroundTitle;


        TextView txtModelprice;
        TextView txtModelTitle;
        TextView txtModelDesc;


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


    public class CustomTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {

            Holder hld = (Holder) view.getTag();

            switch (motionEvent.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    ValueAnimator colorAnim = ObjectAnimator.ofInt(
                            (view), "backgroundColor", 0x00FFFFFF, 0xFFe9262d);
                    colorAnim.setDuration(250);
                    colorAnim.setEvaluator(new ArgbEvaluator());
                    colorAnim.start();

                    break;

                case MotionEvent.ACTION_CANCEL:
                    colorAnim = ObjectAnimator.ofInt((view),
                            "backgroundColor", 0x00FFFFFF, 0xFFe9262d);
                    colorAnim.setDuration(250);
                    colorAnim.setEvaluator(new ArgbEvaluator());
                    colorAnim.start();

                case MotionEvent.ACTION_UP:

                    colorAnim = ObjectAnimator.ofInt((view),
                            "backgroundColor", 0xFFe9262d, 0x00FFFFFF);
                    colorAnim.setDuration(250);
                    colorAnim.setEvaluator(new ArgbEvaluator());
                    colorAnim.start();

                    break;
            }
            return true;
        }

    }

}
