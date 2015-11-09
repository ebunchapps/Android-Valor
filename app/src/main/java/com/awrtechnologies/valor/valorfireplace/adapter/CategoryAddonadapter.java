package com.awrtechnologies.valor.valorfireplace.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.awrtechnologies.valor.valorfireplace.Data.SeriesDetail;
import com.awrtechnologies.valor.valorfireplace.R;
import com.awrtechnologies.valor.valorfireplace.helper.GeneralHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m004 on 10/09/15.
 */
public class CategoryAddonadapter extends BaseAdapter {


    Context context;
    LayoutInflater inflater;
    ArrayList<SeriesDetail> arraylist;
    Holder hld;

    public CategoryAddonadapter(Context context, ArrayList<SeriesDetail> arraylist) {
        this.context = context;
        this.arraylist = arraylist;
        inflater = LayoutInflater.from(context);
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
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.categoryaddview, null);
            hld = new Holder();

            hld.txtAddonsprice = (TextView) convertView
                    .findViewById(R.id.txtAddonsPrice);
            hld.txtAddonsTitle = (TextView) convertView
                    .findViewById(R.id.txtAddonsTitle);
            hld.txtAddonsDesc = (TextView) convertView
                    .findViewById(R.id.txtAddonsDesc);

            convertView.setTag(hld);
        } else {
            hld = (Holder) convertView.getTag();
        }


        if (GeneralHelper.getInstance(context).isAddoncheck() == true) {
            Log.d("Pawan", "none");
            hld.txtAddonsprice.setVisibility(View.GONE);
            hld.txtAddonsTitle.setVisibility(View.GONE);
            hld.txtAddonsDesc.setVisibility(View.GONE);
        } else {
            Log.d("Pawan", "not none");
            hld.txtAddonsprice.setVisibility(View.VISIBLE);
            hld.txtAddonsTitle.setVisibility(View.VISIBLE);
            hld.txtAddonsDesc.setVisibility(View.VISIBLE);
            hld.txtAddonsDesc.setText(arraylist.get(position).getAddonsdesc());
            hld.txtAddonsTitle.setText(arraylist.get(position).getAddonstitle());
            if (arraylist.get(position).getAddonsprice() != 0) {
                hld.txtAddonsprice.setText("$" + arraylist.get(position).getAddonsprice() + "");

            } else {
                hld.txtAddonsprice.setText("");
            }
        }

        return convertView;
    }


    public void setList(List<SeriesDetail> arraylist) {
        arraylist = arraylist;
    }

    public class Holder {

        TextView txtAddonsprice;
        TextView txtAddonsTitle;
        TextView txtAddonsDesc;

    }
}
