package com.awrtechnologies.valor.valorfireplace.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by awr005 on 27/05/15.
 */
public class GeneralHelper {



    private static GeneralHelper generalHelper;
    private Context context;

    boolean ischecked;
    boolean addoncheck;

    public static synchronized GeneralHelper getInstance(Context context) {
        if (generalHelper == null) {
            generalHelper = new GeneralHelper(context);
        }
        return generalHelper;
    }

    public GeneralHelper(Context context) {

        this.context = context;
    }

    /**
     * Convert dp in pixels
     *
     * @param dp
     * @return
     */
    public int getPx(int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return ((int) (dp * scale + 0.5f));
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public Point getScreenSize() {

        Point size = new Point();
        WindowManager w = ((Activity) context).getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            w.getDefaultDisplay().getSize(size);
        } else {
            Display d = w.getDefaultDisplay();
            size.x = d.getWidth();
            size.y = d.getHeight();
        }
        return size;
    }

    public boolean ischecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    public boolean isAddoncheck() {
        return addoncheck;
    }

    public void setAddoncheck(boolean addoncheck) {
        this.addoncheck = addoncheck;
    }
}
