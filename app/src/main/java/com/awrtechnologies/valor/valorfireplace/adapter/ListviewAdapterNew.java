package com.awrtechnologies.valor.valorfireplace.adapter;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.awrtechnologies.valor.valorfireplace.Data.SeriesDetail;
import com.awrtechnologies.valor.valorfireplace.Data.User;
import com.awrtechnologies.valor.valorfireplace.R;
import com.awrtechnologies.valor.valorfireplace.helper.GeneralHelper;
import com.awrtechnologies.valor.valorfireplace.uicomponents.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by awr005 on 04/06/15.
 */
public class ListviewAdapterNew extends BaseAdapter{

    Context context;
    LayoutInflater inflater;
    ArrayList<SeriesDetail> arraylist;
    Point screensize;

    public ListviewAdapterNew(Context context, ArrayList<SeriesDetail> arraylist) {

        this.context = context;
        this.arraylist = arraylist;
        this.inflater = LayoutInflater.from(context);
        screensize = GeneralHelper.getInstance(context).getScreenSize();

    }


    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return  arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return arraylist.toString().hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(arraylist.get(position).type==1) {
            convertView = inflater.inflate(R.layout.list_items_in_fifth, null);

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

        else if(arraylist.get(position).type==0) {
            convertView = inflater.inflate(R.layout.category, null);
            TextView txtAddonsprice = (TextView) convertView
                    .findViewById(R.id.txtAddonsPrice);
            TextView txtAddonsTitle = (TextView) convertView
                    .findViewById(R.id.txtAddonsTitle);
            TextView txtCategoryTitle = (TextView) convertView
                    .findViewById(R.id.txtCategoryTitle);
            if (GeneralHelper.getInstance(context).isAddoncheck() == true) {
                Log.d("Pawan", "none");
                txtAddonsprice.setVisibility(View.GONE);
                txtAddonsTitle.setVisibility(View.GONE);
            } else {
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
            }
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
//        TextView txtProduct;
//        TextView txtPrice;

        TextView txtSeriesTitle;
        TextView txtSeriesDesc;

        TextView txtBackgroundTitle;

        TextView txtAddonsprice;
        TextView txtAddonsTitle;
        TextView txtAddonsDesc;

        TextView txtModelprice;
        TextView txtModelTitle;
        TextView txtModelDesc;
    }
}