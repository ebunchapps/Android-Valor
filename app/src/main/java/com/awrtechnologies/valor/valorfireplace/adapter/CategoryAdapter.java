package com.awrtechnologies.valor.valorfireplace.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.awrtechnologies.valor.valorfireplace.Data.AddonsCategory;
import com.awrtechnologies.valor.valorfireplace.R;

import java.util.List;

/**
 * Created by m004 on 05/09/15.
 */
public class CategoryAdapter extends BaseAdapter{


    Context context;
    List<AddonsCategory> arraylist;
    LayoutInflater layinflter;

    public CategoryAdapter(Context context, List<AddonsCategory> arraylist) {
        this.context = context;
        this.arraylist = arraylist;
        layinflter = LayoutInflater.from(context);
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
        return arraylist.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView=layinflter.inflate(R.layout.addview,null);
        TextView text= (TextView) convertView.findViewById(R.id.txtCategory);
        text.setText(arraylist.get(position).name);

        return convertView;
    }
}
