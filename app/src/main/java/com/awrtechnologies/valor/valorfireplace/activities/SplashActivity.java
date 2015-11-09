package com.awrtechnologies.valor.valorfireplace.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.awrtechnologies.valor.valorfireplace.R;
import com.awrtechnologies.valor.valorfireplace.helper.GeneralHelper;
import com.awrtechnologies.valor.valorfireplace.uicomponents.PreferencesManager;

public class SplashActivity extends Activity {

    TextView txtCopyRightText;
    TextView txtbuildYourOwn;

    ImageView imgFireIcon;
    ImageView imgFirePlace;
    ImageView imgValorIcon;

    Point point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        point = GeneralHelper.getInstance(this).getScreenSize();



        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                    Intent intent = new Intent(SplashActivity.this, ChooseApplication.class);
                    startActivity(intent);

            }
        }, 3000);
    }


    //TODO :- Remove this block after confirmation
    public void applyParams(){
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) imgFireIcon.getLayoutParams();
        lp.width = point.x / 8;
        lp.height = point.y / 8;
        imgFireIcon.setLayoutParams(lp);

        ViewGroup.LayoutParams lp1 = (ViewGroup.LayoutParams) imgFirePlace.getLayoutParams();
        lp1.width = point.x / 3;
        lp1.height = point.y / 5;
        imgFirePlace.setLayoutParams(lp1);

        ViewGroup.LayoutParams lp2 = (ViewGroup.LayoutParams) imgValorIcon.getLayoutParams();
        lp2.width = point.x / 12;
        lp2.height = point.x / 16;
        imgValorIcon.setLayoutParams(lp2);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        finish();// avoid launch after home button is pressed.
        super.onPause();
    }



}
