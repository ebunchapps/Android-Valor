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

import com.awrtechnologies.valor.valorfireplace.Data.Addons;
import com.awrtechnologies.valor.valorfireplace.Data.User;
import com.awrtechnologies.valor.valorfireplace.R;
import com.awrtechnologies.valor.valorfireplace.helper.GeneralHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by awr005 on 03/06/15.
 */
public class HlistViewAdapter extends BaseAdapter {
    Context context;
    List<Addons> arraylist;
    LayoutInflater inflater;
    Point screenSize;
    ImageLoader mloader;

    public HlistViewAdapter(Context context,
                            List<Addons> arraylist) {
        this.context = context;
        this.arraylist = arraylist;
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
                    R.layout.list_items_for_hlist_fourth, null);
            h = new Holder();
            h.imgProduct = (ImageView) convertView
                    .findViewById(R.id.imgProduct);

            h.none = (LinearLayout) convertView.findViewById(R.id.none);
            h.normal = (LinearLayout) convertView.findViewById(R.id.normal);

            ViewGroup.LayoutParams lp = h.imgProduct.getLayoutParams();
            lp.width = screenSize.x / 18;
            lp.height = screenSize.x / 18;
            h.imgProduct.setLayoutParams(lp);

            h.txtProductName = (TextView) convertView.findViewById(R.id.txtProductName);
            h.txtProductPrice = (TextView) convertView.findViewById(R.id.txtProductPrice);
            convertView.setTag(h);
        } else {
            h = (Holder) convertView.getTag();
        }
        if (position == arraylist.size() - 1) {
            h.none.setVisibility(View.VISIBLE);
            h.normal.setVisibility(View.GONE);
        } else {
            h.none.setVisibility(View.GONE);
            h.normal.setVisibility(View.VISIBLE);

            mloader.displayImage(arraylist.get(position).image, h.imgProduct);
            h.txtProductName.setText(arraylist.get(position).title);
            if (User.getUser() != null) {
                h.txtProductPrice.setVisibility(View.VISIBLE);
                h.txtProductPrice.setText("$" + arraylist.get(position).price);
            } else {
                h.txtProductPrice.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    public class Holder {
        ImageView imgProduct;
        LinearLayout none, normal;
        TextView txtProductName;
        TextView txtProductPrice;
    }

    public void setList(List<Addons> arraylist) {
        arraylist = arraylist;
    }
}
