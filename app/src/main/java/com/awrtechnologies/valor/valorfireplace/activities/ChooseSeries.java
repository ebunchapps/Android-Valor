package com.awrtechnologies.valor.valorfireplace.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.awrtechnologies.valor.valorfireplace.Data.Application;
import com.awrtechnologies.valor.valorfireplace.Data.NavData;
import com.awrtechnologies.valor.valorfireplace.Data.Series;
import com.awrtechnologies.valor.valorfireplace.Data.User;
import com.awrtechnologies.valor.valorfireplace.R;
import com.awrtechnologies.valor.valorfireplace.adapter.GridAdapter;
import com.awrtechnologies.valor.valorfireplace.adapter.NavAdapter;
import com.awrtechnologies.valor.valorfireplace.helper.GeneralHelper;
import com.awrtechnologies.valor.valorfireplace.uicomponents.PreferencesManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import utils.ExtraKeys;


public class ChooseSeries extends Activity implements View.OnClickListener {

    TextView txtHeading;
    TextView txtSubHeading;
    TextView txtContent;
    TextView txtProgress;
    TextView txtStep2Name;
    ImageView imgdownArrow;
    TextView txtStep1;
    TextView txtStep1Name;

    TextView userName;
    ImageView userImg;

    LinearLayout linearTopBar;
    RelativeLayout relativeTitleBar;
    LinearLayout linearNavigatiion;

    private ImageLoader mLoader;

    Button btnNav;
    ImageView imgFireiconWhite;
    ImageView imgValorTopicon;
    Point point;

    GridAdapter adapter;
    GridView gridview;

    DrawerLayout dL;
    ListView navListview;

    ArrayList<NavData> list;
    NavAdapter navAdapter;
    String[] textArray = {"HELP", "LOGIN"};
    LinearLayout menuNav;
    Dialog dialog;

    String[] textArrayLogout = {"HELP", "LOGOUT"};

    int[] imageArray = { R.drawable.question_mark, R.drawable.person_icon};

    int[] imageArrayLogout = { R.drawable.question_mark, R.drawable.logout};


    Application application;
    List<Series> arraylist;
    String appid;
    String appdes;
    String applicationTitle;
    ArrayList<View> viewList;
    ArrayList<String> arr;

    String item[] = {"category1", "category2", "category3", "category4", "category5","category1", "category2", "category3", "category4", "category5"};
    LinearLayout ll;

    public ChooseSeries() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose_series);
        findViewIDs();
        setPositionForProgressArrow();
        mLoader = ImageLoader.getInstance();
        PreferencesManager.setPreferenceBooleanByKey(ChooseSeries.this, "IsLogout", false);
        list = new ArrayList<NavData>();
        point = GeneralHelper.getInstance(this).getScreenSize();
        appid = getIntent().getStringExtra("APPID");
        applicationTitle=getIntent().getStringExtra("title");
        application=new Application();
        arraylist = new ArrayList<Series>();
        Log.d("Pawan","APPID"+appid);
        arraylist = Series.getAllByappId(appid);
        Log.d("Pawan","SErries filter by application"+arraylist);
        for (int i = 0; i < arraylist.size(); i++) {
            Log.d("Pawan","SERes id"+arraylist.get(i).seriesid);
        }

        txtSubHeading.setText(applicationTitle);
        adapter = new GridAdapter(ChooseSeries.this, arraylist);
        gridview.setAdapter(adapter);

        if (User.getUser() == null) {
            navigationLogin();

        } else {
            navigationLogout();
            User user = User.getUser();
            userName.setText(user.getName());
            mLoader.displayImage(user.getImage(), userImg);

        }

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                openDesignValorFireplace(arraylist.get(position).seriesid
                ,arraylist.get(position).applicationId
                ,applicationTitle);
                ActiveAndroid.clearCache();
            }
        });

        applySetOnClickListener();

    }

    private void openDesignValorFireplace(String seriesId,String appId,String title){

        Intent intent = new Intent(ChooseSeries.this,DesignValorFirePlace.class);
        intent.putExtra(ExtraKeys.KEY_SERIES_ID,seriesId);
        intent.putExtra(ExtraKeys.KEY_APP_ID,appId);
        intent.putExtra(ExtraKeys.KEY_TITLE,title);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    public void findViewIDs() {
        imgFireiconWhite = (ImageView) findViewById(R.id.imgFireiconWhite);
        imgValorTopicon = (ImageView) findViewById(R.id.imgValorTopicon);
        btnNav = (Button) findViewById(R.id.btnNav);
        linearTopBar = (LinearLayout) findViewById(R.id.linearTopBar);
        relativeTitleBar = (RelativeLayout) findViewById(R.id.relativeTitleBar);
        txtHeading = (TextView) findViewById(R.id.txtHeading);
        txtSubHeading = (TextView) findViewById(R.id.txtSubHeading);
        txtContent = (TextView) findViewById(R.id.txtContent);
        txtProgress = (TextView) findViewById(R.id.txtProgress);
        gridview = (GridView) findViewById(R.id.gridview);
        linearNavigatiion = (LinearLayout) findViewById(R.id.linearNavigatiion);
        dL = (DrawerLayout) findViewById(R.id.drawer_layout);
        navListview = (ListView) findViewById(R.id.left_drawer);
        menuNav = (LinearLayout) findViewById(R.id.menuNav);
        imgdownArrow = (ImageView) findViewById(R.id.imgdownArrow);
        txtStep2Name = (TextView) findViewById(R.id.txtStep2Name);
        txtStep1 = (TextView) findViewById(R.id.txtStep1);
        txtStep1Name = (TextView) findViewById(R.id.txtStep1Name);
        userName = (TextView) findViewById(R.id.userName);
        userImg = (ImageView) findViewById(R.id.userImg);
        ll = (LinearLayout) findViewById(R.id.toAddView);

    }

    public void applyParams() {
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) imgValorTopicon.getLayoutParams();
        lp.width = point.x / 7;
        lp.height = (int) Math.round(point.y / 10.0);
        imgValorTopicon.setLayoutParams(lp);

//        ViewGroup.LayoutParams lp1 = (ViewGroup.LayoutParams) imgFireiconWhite.getLayoutParams();
//        lp1.width = (int) Math.round(point.x / 1.5);
//        lp1.height = (int) Math.round(point.y / 4.2);
//        imgFireiconWhite.setLayoutParams(lp1);

        ViewGroup.LayoutParams lp2 = (ViewGroup.LayoutParams) btnNav.getLayoutParams();
        lp2.width = point.x / 38;
        lp2.height = point.y / 40;
        btnNav.setLayoutParams(lp2);

        ViewGroup.LayoutParams lp3 = (ViewGroup.LayoutParams) linearTopBar.getLayoutParams();
//        lp3.width = point.x / 12;
        lp3.height = point.y / 11;
        linearTopBar.setLayoutParams(lp3);

        ViewGroup.LayoutParams lp4 = (ViewGroup.LayoutParams) relativeTitleBar.getLayoutParams();
//        lp4.width = point.x / 12;
        lp4.height = (int) Math.round(point.y / 2.4);
        relativeTitleBar.setLayoutParams(lp4);
    }

    public void applySetOnClickListener() {
        btnNav.setOnClickListener(this);
        linearNavigatiion.setOnClickListener(this);
        txtStep1.setOnClickListener(this);
        txtStep1Name.setOnClickListener(this);
        txtStep2Name.setOnClickListener(this);
    }

    @Override
    protected void onResume() {

        if(User.getUser()!=null&& PreferencesManager.getPreferenceBooleanByKey(ChooseSeries.this, "isLogin")==true) {
            list.clear();
            navigationLogout();
            User user = User.getUser();
            userName.setText(user.getName());
            mLoader.displayImage(user.getImage(), userImg);
        }

        else
        {
            userName.setText("LOGIN");
            mLoader.displayImage("assets://person_icon.png", userImg);
            list.clear();
            navigationLogin();
            appid = getIntent().getStringExtra("APPID");


        }

        super.onResume();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnNav:
                if (dL.isDrawerOpen(menuNav)) {
                    dL.closeDrawer(menuNav);
                } else {
                    dL.openDrawer(menuNav);
                }
                break;

            case R.id.linearNavigatiion:
                if (dL.isDrawerOpen(menuNav)) {
                    dL.closeDrawer(menuNav);
                } else {
                    dL.openDrawer(menuNav);
                }
                break;

            case R.id.txtStep1:
                Intent a1 = new Intent(ChooseSeries.this, ChooseApplication.class);
                a1.addCategory(Intent.CATEGORY_HOME);
                a1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a1);
                break;

            case R.id.txtStep1Name:
                Intent a11 = new Intent(ChooseSeries.this, ChooseApplication.class);
                a11.addCategory(Intent.CATEGORY_HOME);
                a11.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a11);
                break;

//            case R.id.txtCategory:
//
//                int getTag = (Integer) v.getTag();
//                for (int i = 0; i < viewList.size(); i++) {
//
//                    TextView txt = (TextView) viewList.get(i).findViewById(
//                            R.id.txtCategory);
//                    if (i == getTag) {
//                        txt.setTextColor(getResources().getColor(R.color.white));
//                        txt.setBackgroundColor(getResources().getColor(R.color.redText));
//
//                    } else {
//                        txt.setTextColor(getResources()
//                                .getColor(R.color.white));
//                        txt.setBackgroundColor(getResources().getColor(R.color.black));
//
//                    }
//
//                }
//                getCategoryId(arr.get(getTag).toString(), getTag);
//                break;
            default:
                break;
        }
    }


//    public void getCategoryId(String menuName, int position) {
//
//        mNavDatas = new ArrayList<Series>();
//        mNavDatas = Series.getAllByappId(appid);
//        adapter = new GridAdapter(ChooseSeries.this, mNavDatas);
//        gridview.setAdapter(adapter);
//
//
//    }


    public void setPositionForProgressArrow() {

        dL.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        //             float x = txtStep2Name.getLeft();
//                      float width = txtStep2Name.getWidth();
//                      float reqDistance = (x + width/2);
//                      imgdownArrow.setLeft((int) reqDistance);

//                        ViewHelperFactory.ViewHelper
                        float x = txtStep2Name.getX();
                        float width = txtStep2Name.getWidth();
                        float reqDistance = (x + width / 4);
                        imgdownArrow.setX(reqDistance);

                        if (Build.VERSION.SDK_INT >= 16) {
                            dL.getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        } else {
                            dL.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }
                    }
                });

    }

    public void navigationLogin() {
        for (int i = 0; i < textArray.length; i++) {
            NavData d = new NavData();
            d.setImages(imageArray[i]);
            d.setText(textArray[i]);
            list.add(d);
        }

        navAdapter = new NavAdapter(ChooseSeries.this, list);
        navListview.setAdapter(navAdapter);
        navListview.setDivider(null);
        navListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent WebViewIntent = new Intent(ChooseSeries.this, WebActivity.class);
                        startActivity(WebViewIntent);
                        dL.closeDrawer(menuNav);
                        break;
                    case 1:
                        Intent loginIntent = new Intent(ChooseSeries.this, SignInActivity.class);
                        startActivity(loginIntent);
                        dL.closeDrawer(menuNav);
                        break;


                    default:
                        break;
                }
            }
        });
    }

    public void navigationLogout() {
        list.clear();

        for (int i = 0; i < textArrayLogout.length; i++) {
            NavData d = new NavData();
            d.setImages(imageArrayLogout[i]);
            d.setText(textArrayLogout[i]);
            list.add(d);
        }

        navAdapter = new NavAdapter(ChooseSeries.this, list);
        navListview.setAdapter(navAdapter);
        navListview.setDivider(null);
        navListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent WebViewIntent = new Intent(ChooseSeries.this, WebActivity.class);
                        startActivity(WebViewIntent);
                        dL.closeDrawer(menuNav);
                        break;
                    case 1:
                        exitApp();
                        dL.closeDrawer(menuNav);
                        break;
                    case 2:

                        break;


                    default:
                        break;
                }

            }
        });

    }



    public void exitApp() {
        dialog = new Dialog(ChooseSeries.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogbox_quatationresult);


        Button ok = (Button) dialog.findViewById(R.id.buttonok);
        Button cancel = (Button) dialog.findViewById(R.id.buttoncancel);
        Button close = (Button) dialog.findViewById(R.id.btnClose);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                new Delete().from(User.class).execute();
                dialog.dismiss();
//                navigationLogin();

                PreferencesManager.setPreferenceBooleanByKey(ChooseSeries.this,"isLogin",false);
                new Delete().from(User.class).execute();

                Intent i = new Intent(ChooseSeries.this, ChooseSeries.class);
                i.putExtra("APPID", appid);
                startActivity(i);


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();  // optional depending on your needs


        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
