package com.awrtechnologies.valor.valorfireplace.adapter;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.awrtechnologies.valor.valorfireplace.Data.ModelsData;
import com.awrtechnologies.valor.valorfireplace.R;
import com.awrtechnologies.valor.valorfireplace.helper.GeneralHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by awr001 on 02/09/15.
 */
public class GridModelAdapter  extends BaseAdapter {

    Context context;
    List<ModelsData> arraylist;
    LayoutInflater inflater;
    Point screenSize;
    ImageLoader mloader;

    public GridModelAdapter(Context context, List<ModelsData> arraylist) {
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
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.grid_items, parent, false);
            h = new Holder();
            h.imglistitem = (ImageView) view
                    .findViewById(R.id.imglistitem);
            h.txtheadingList = (TextView) view
                    .findViewById(R.id.txtheadingList);
            h.linearContent = (LinearLayout) view
                    .findViewById(R.id.linearContent);


          /*  ViewGroup.LayoutParams lp1 = (ViewGroup.LayoutParams) h.imglistitem.getLayoutParams();
//            lp1.width = (int) Math.round(screenSize.x / 2.0);
            lp1.height = (int) Math.round(screenSize.y / 7.0);
            h.imglistitem.setLayoutParams(lp1);*/
            view.setTag(h);
        } else {
            h = (Holder) view.getTag();
        }

        mloader.displayImage(arraylist.get(position).banner_image, h.imglistitem);

        h.txtheadingList.setText(arraylist.get(position).title);

        return view;
    }

    public class Holder {
        ImageView imglistitem;
        TextView txtheadingList;
        LinearLayout linearContent;
    }
}