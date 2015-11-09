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

import com.activeandroid.query.Delete;
import com.awrtechnologies.valor.valorfireplace.Data.Application;
import com.awrtechnologies.valor.valorfireplace.Data.ModelsData;
import com.awrtechnologies.valor.valorfireplace.Data.NavData;
import com.awrtechnologies.valor.valorfireplace.Data.Series;
import com.awrtechnologies.valor.valorfireplace.Data.User;
import com.awrtechnologies.valor.valorfireplace.R;
import com.awrtechnologies.valor.valorfireplace.adapter.GridModelAdapter;
import com.awrtechnologies.valor.valorfireplace.adapter.NavAdapter;
import com.awrtechnologies.valor.valorfireplace.helper.GeneralHelper;
import com.awrtechnologies.valor.valorfireplace.uicomponents.PreferencesManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by awr001 on 02/09/15.
 */
public class ChooseModel extends Activity implements View.OnClickListener {
    TextView txtHeading;
    TextView txtSubHeading;
    TextView txtContent;
    TextView txtProgress;
    TextView txtStep2Name;
    ImageView imgdownArrow;
    TextView txtStep1;
    TextView txtStep1Name;
    TextView txtSteps3Name;

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

    GridModelAdapter adapter;
    GridView gridview;

    DrawerLayout dL;
    ListView navListview;

    ArrayList<NavData> list;
    NavAdapter navAdapter;
    String[] textArray = {"HELP", "LOGIN"};
    LinearLayout menuNav;
    Dialog dialog;

    String[] textArrayLogout = {"HELP", "LOGOUT"};

    int[] imageArray = {R.drawable.question_mark, R.drawable.person_icon};

    int[] imageArrayLogout = {R.drawable.question_mark, R.drawable.logout};

    Application application;
    List<Series> arraylist;
    List<ModelsData> modelslist;
    String appid;
    String seriesid;
    String applicationTitle;
    ArrayList<View> viewList;
    ArrayList<String> arr;
    LinearLayout ll;

    String arrmodel[] = {"Select Model"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose_model);
        findViewByIds();
        PreferencesManager.setPreferenceBooleanByKey(ChooseModel.this, "IsLogout", false);

        mLoader = ImageLoader.getInstance();

        appid = getIntent().getStringExtra("APPID");
        seriesid = getIntent().getStringExtra("SERIESID");
        applicationTitle = getIntent().getStringExtra("title");
        list = new ArrayList<NavData>();
        arraylist = new ArrayList<Series>();
        arraylist = Series.getAllByappId(appid);
        point = GeneralHelper.getInstance(this).getScreenSize();
        applySetOnClickListener();
        setPositionForProgressArrow();

        //TODO : Remove after confirmation
        //applyParams();

        txtSubHeading.setText(applicationTitle);
        Log.d("Pawan", "SeriesID=-==" + seriesid);

        modelslist = new ArrayList<ModelsData>();
        modelslist = ModelsData.getAllBySeriesId(seriesid);
        Log.e("Pawan","modellistsizeisstockked"+ ModelsData.getModelStock().stock);

        if(ModelsData.getModelStock().stock==0||ModelsData.getModelStock().stock==-1) {
        }
        else {

            adapter = new GridModelAdapter(ChooseModel.this, modelslist);
            gridview.setAdapter(adapter);
        }

        for (int i = 0; i < modelslist.size(); i++) {
            Log.d("Pawan", "ModelId==" + modelslist.get(i).modelName);
        }
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
//                Intent intent = new Intent(ChooseModel.this, DesignValorFirePlace.class);
//                intent.putExtra("SERIESID", mNavDatas.get(position).seriesid);
//                intent.putExtra("APPID", mNavDatas.get(position).applicationId);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                Intent intent = new Intent(ChooseModel.this, DesignValorFirePlace.class);
                intent.putExtra("MODELID", modelslist.get(position).modelId);
                intent.putExtra("SERIESID", seriesid);
                intent.putExtra("APPID", appid);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

        navAdapter = new NavAdapter(ChooseModel.this, list);
        navListview.setAdapter(navAdapter);
        navListview.setDivider(null);
        navListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent WebViewIntent = new Intent(ChooseModel.this, WebActivity.class);
                        startActivity(WebViewIntent);
                        dL.closeDrawer(menuNav);
                        break;
                    case 1:
                        Intent loginIntent = new Intent(ChooseModel.this, SignInActivity.class);
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

        navAdapter = new NavAdapter(ChooseModel.this, list);
        navListview.setAdapter(navAdapter);
        navListview.setDivider(null);
        navListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent WebViewIntent = new Intent(ChooseModel.this, WebActivity.class);
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


    public void findViewByIds() {
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
        txtSteps3Name = (TextView) findViewById(R.id.txtSteps3);


    }

    public void applySetOnClickListener() {
        btnNav.setOnClickListener(this);
        linearNavigatiion.setOnClickListener(this);
        txtStep1.setOnClickListener(this);
        txtStep1Name.setOnClickListener(this);
        txtStep2Name.setOnClickListener(this);
    }


    public void exitApp() {
        dialog = new Dialog(ChooseModel.this);
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
                PreferencesManager.setPreferenceBooleanByKey(ChooseModel.this,"isLogin",false);
                new Delete().from(User.class).execute();

                Intent i = new Intent(ChooseModel.this, ChooseModel.class);
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
    protected void onResume() {

        if (User.getUser() != null && PreferencesManager.getPreferenceBooleanByKey(ChooseModel.this, "isLogin") == true) {
            list.clear();
            navigationLogout();
            User user = User.getUser();
            userName.setText(user.getName());
            mLoader.displayImage(user.getImage(), userImg);
        } else {
            userName.setText("LOGIN");
            mLoader.displayImage("assets://person_icon.png", userImg);
            list.clear();
            navigationLogin();
            appid = getIntent().getStringExtra("APPID");
        }

        super.onResume();
    }


    public void applyParams() {
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) imgValorTopicon.getLayoutParams();
        lp.width = point.x / 7;
        lp.height = (int) Math.round(point.y / 10.0);
        imgValorTopicon.setLayoutParams(lp);


        ViewGroup.LayoutParams lp2 = (ViewGroup.LayoutParams) btnNav.getLayoutParams();
        lp2.width = point.x / 38;
        lp2.height = point.y / 40;
        btnNav.setLayoutParams(lp2);

        ViewGroup.LayoutParams lp3 = (ViewGroup.LayoutParams) linearTopBar.getLayoutParams();
        lp3.height = point.y / 11;
        linearTopBar.setLayoutParams(lp3);

        ViewGroup.LayoutParams lp4 = (ViewGroup.LayoutParams) relativeTitleBar.getLayoutParams();
        lp4.height = (int) Math.round(point.y / 2.4);
        relativeTitleBar.setLayoutParams(lp4);
    }

    public void setPositionForProgressArrow() {

        dL.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {

                        float x = txtSteps3Name.getX();
                        float width = txtSteps3Name.getWidth();
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
                Intent a1 = new Intent(ChooseModel.this, ChooseApplication.class);
                a1.addCategory(Intent.CATEGORY_HOME);
                a1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a1);
                break;

            case R.id.txtStep1Name:
                Intent a11 = new Intent(ChooseModel.this, ChooseApplication.class);
                a11.addCategory(Intent.CATEGORY_HOME);
                a11.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a11);
                break;
            case R.id.txtStep2Name:
                Intent a2 = new Intent(ChooseModel.this, ChooseSeries.class);
                a2.putExtra("APPID", appid);
                a2.addCategory(Intent.CATEGORY_HOME);
                a2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a2);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();  // optional depending on your needs


        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}