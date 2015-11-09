package com.awrtechnologies.valor.valorfireplace.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.util.Log;
import com.awrtechnologies.valor.valorfireplace.Data.Addons;
import com.awrtechnologies.valor.valorfireplace.Data.AddonsCategory;
import com.awrtechnologies.valor.valorfireplace.Data.Application;
import com.awrtechnologies.valor.valorfireplace.Data.ModelsData;
import com.awrtechnologies.valor.valorfireplace.Data.NavData;
import com.awrtechnologies.valor.valorfireplace.Data.Series;
import com.awrtechnologies.valor.valorfireplace.Data.User;
import com.awrtechnologies.valor.valorfireplace.R;
import com.awrtechnologies.valor.valorfireplace.adapter.HlistViewAdapter;
import com.awrtechnologies.valor.valorfireplace.adapter.ListAdapter;
import com.awrtechnologies.valor.valorfireplace.adapter.NavAdapter;
import com.awrtechnologies.valor.valorfireplace.apiconstants.Constants;
import com.awrtechnologies.valor.valorfireplace.helper.GeneralHelper;
import com.awrtechnologies.valor.valorfireplace.uicomponents.PreferencesManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ChooseApplication extends Activity implements View.OnClickListener {

    TextView txtHeading;
    TextView txtSubHeading;
    TextView txtContent;
    TextView txtProgress;
    TextView txtStep1Name;
    ImageView imgdownArrow;
    TextView userName;
    ImageView userImg;
    LinearLayout linearTopBar;
    LinearLayout linearNavigatiion;
    RelativeLayout relativeTitleBar;

    Button btnNav;
    ImageView imgFireiconWhite;
    ImageView imgValorTopicon;
    Point point;

    ListView listView;
    List<Application> list;
    ListAdapter adapter;

    DrawerLayout dL;
    ListView navListview;

    ArrayList<NavData> arraylist;
    NavAdapter navAdapter;
    String[] textArray = {"HELP", "LOGIN"};

    LinearLayout menuNav;


    private ImageLoader mLoader;
    String url;
    Dialog dialog;

    String[] textArrayLogout = {"HELP", "LOGOUT"};

    int[] imageArray = {R.drawable.question_mark, R.drawable.person_icon};

    int[] imageArrayLogout = {R.drawable.question_mark, R.drawable.logout};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose_application);
        PreferencesManager.setPreferenceBooleanByKey(ChooseApplication.this, "IsLogout", false);


        list = new ArrayList<Application>();
        arraylist = new ArrayList<NavData>();
        point = GeneralHelper.getInstance(this).getScreenSize();
        mLoader = ImageLoader.getInstance();

        findViewIDs();
        setPositionForProgressArrow();
        applySetOnClickListener();
        if (User.getUser() == null) {
            navigationLogin();

        } else {
            navigationLogout();
            User user = User.getUser();
            userName.setText(user.getName());
            mLoader.displayImage(user.getImage(), userImg);

        }

        list = Application.getAll();
        if (list != null && list.size() > 0) {
            list = new ArrayList<Application>();
            PreferencesManager.setPreferenceBooleanByKey(ChooseApplication.this, "APICALL", false);
            list = Application.getAll();
            adapter = new ListAdapter(ChooseApplication.this, list);
            listView.setAdapter(adapter);
            listView.setDivider(null);
            new GetAllTask().execute();
        } else {
            PreferencesManager.setPreferenceBooleanByKey(ChooseApplication.this, "APICALL", true);
            new GetAllTask().execute();

        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(ChooseApplication.this, ChooseSeries.class);
                intent.putExtra("APPID", list.get(position).appid);
                intent.putExtra("APPDESC", list.get(position).description);
                intent.putExtra("title",list.get(position).title);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                Log.d("Pawan", "APPid" + list.get(position).appid);
                Log.d("Pawan", "Desc" + list.get(position).description);
            }
        });


        //TODO : - Remove after confirmation
       // applyParams();
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
        linearNavigatiion = (LinearLayout) findViewById(R.id.linearNavigatiion);
        listView = (ListView) findViewById(R.id.listView);
        navListview = (ListView) findViewById(R.id.left_drawer);
        dL = (DrawerLayout) findViewById(R.id.drawer_layout);
        navListview = (ListView) findViewById(R.id.left_drawer);
        menuNav = (LinearLayout) findViewById(R.id.menuNav);
        imgdownArrow = (ImageView) findViewById(R.id.imgdownArrow);
        txtStep1Name = (TextView) findViewById(R.id.txtStep1Name);
        userName = (TextView) findViewById(R.id.userName);
        userImg = (ImageView) findViewById(R.id.userImg);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnNav:
                dL.openDrawer(menuNav);
                break;

            case R.id.linearNavigatiion:
                dL.openDrawer(menuNav);
                break;

            default:
                break;
        }
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
        txtStep1Name.setOnClickListener(this);
    }


    public void navigationLogin() {

        for (int i = 0; i < textArray.length; i++) {
            NavData d = new NavData();
            d.setImages(imageArray[i]);
            d.setText(textArray[i]);
            arraylist.add(d);
        }

        navAdapter = new NavAdapter(ChooseApplication.this, arraylist);
        navListview.setAdapter(navAdapter);
        navListview.setDivider(null);
        navListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent WebViewIntent = new Intent(ChooseApplication.this, WebActivity.class);
                        startActivity(WebViewIntent);
                        dL.closeDrawer(menuNav);
                        break;
                    case 1:
                        Intent loginIntent = new Intent(ChooseApplication.this, SignInActivity.class);
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
        for (int i = 0; i < textArrayLogout.length; i++) {
            NavData d = new NavData();
            d.setImages(imageArrayLogout[i]);
            d.setText(textArrayLogout[i]);
            arraylist.add(d);
        }

        navAdapter = new NavAdapter(ChooseApplication.this, arraylist);
        navListview.setAdapter(navAdapter);
        navListview.setDivider(null);
        navListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent WebViewIntent = new Intent(ChooseApplication.this, WebActivity.class);
                        startActivity(WebViewIntent);
                        dL.closeDrawer(menuNav);
                        break;
                    case 1:
                        exitApp();

                        dL.closeDrawer(menuNav);
                        break;


                    default:
                        break;
                }

            }
        });

    }

    public void exitApp() {

        dialog = new Dialog(ChooseApplication.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogbox_quatationresult);

        Button close = (Button) dialog.findViewById(R.id.btnClose);
        Button ok = (Button) dialog.findViewById(R.id.buttonok);
        Button cancel = (Button) dialog.findViewById(R.id.buttoncancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesManager.setPreferenceBooleanByKey(ChooseApplication.this,"isLogin",false);
                new Delete().from(User.class).execute();
                Intent logoutIntent = new Intent(ChooseApplication.this, ChooseApplication.class);
                startActivity(logoutIntent);
                dialog.dismiss();

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

//    Setting the position for Progress Bar Downward Arrow

    public void setPositionForProgressArrow() {

        dL.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {

                        float x = txtStep1Name.getX();
                        float width = txtStep1Name.getWidth();
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

    public class GetAllTask extends AsyncTask<Void, Void, String> {

        private ProgressDialog mProgressDialog;

        @Override
        protected String doInBackground(Void... params) {

            try {

                HttpClient httpclient = new DefaultHttpClient();
                try {
                    User user = User.getUser();
                    if (user == null) {

                        url = Constants.BASEURL + "getAll";

                    } else {
                        url = Constants.BASEURL + "getAll?dealerId=" + user.getDealerId();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                HttpGet httpget = new HttpGet(url);
                System.out.println("GET ALL CALLED");
                HttpResponse httpresponse = httpclient.execute(httpget);

                InputStream inputstream = httpresponse.getEntity().getContent();

                String result = convertStreamToString(inputstream);
                System.out.println("RESULT ARRIVED");
                System.out.println("RESULT PROCESSED" + result);
                return result;
            } catch (Exception e) {
                System.out.println("GET ALL EXCEPTION");
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPreExecute() {
            if (PreferencesManager.getPreferenceBooleanByKey(ChooseApplication.this, "APICALL") == true) {
                mProgressDialog = new ProgressDialog(ChooseApplication.this);
                mProgressDialog.setMessage("Loading..");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

            } else {

            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            if (PreferencesManager.getPreferenceBooleanByKey(ChooseApplication.this, "APICALL") == true) {

                mProgressDialog.dismiss();
            } else {

            }
            System.out.println("POST API RESULT" + result);
            try {
                ActiveAndroid.beginTransaction();
                JSONObject object = new JSONObject(result);
                int responsecode = object.getInt("result_code");

                if (responsecode == 1) {
                    JSONObject data = object.getJSONObject("data");
                    Application.deleteAll();
                    Series.deleteAll();
                    ModelsData.deleteAll();
                    AddonsCategory.deleteAll();
                    Addons.deleteAll();
                    JSONArray applicationarray = data.getJSONArray("applications");
                    Log.d("Pawan", "Application array===" + applicationarray);
                    for (int i = 0; i < applicationarray.length(); i++) {
                        JSONObject appobj = applicationarray.getJSONObject(i);
                        Application app = new Application();
                        app.appid = appobj.getString("id");
                        app.description = appobj.getString("description");
                        app.image = appobj.getString("image");
                        app.banner_image = appobj.getString("banner_image");
                        app.title = appobj.getString("title");
                        app.save();
                    }

                    try {


                        JSONArray seriesarray = data.getJSONArray("serieses");
                        Log.d("Pawan", "seriesarray array===" + seriesarray);
//
//                        for (int i = 0; i < seriesarray.length(); i++) {
//                            JSONArray seriesArray = seriesarray.getJSONArray(i);
                            for (int j = 0; j < seriesarray.length(); j++) {
                                JSONObject seriesobj = seriesarray.getJSONObject(j);
                                Series series = new Series();
                                series.seriesid = seriesobj.getString("id");
                                series.description = seriesobj.getString("description");
                                series.title = seriesobj.getString("title");
                                series.actualImage = seriesobj.getString("image");
                                series.applicationId = seriesobj.getString("application_id");
                                series.bannerImage = seriesobj.getString("banner_image");
                                series.save();
                            }


                            //                    series.thumb_actualImage = seriesobj.getString("thumb_actualImage");
//                    series.thumb_bannerImage = seriesobj.getString("thumb_bannerImage");
//                    series.price = seriesobj.getInt("price");
//                    series.qty = seriesobj.getString("qty");
//                    series.manual=seriesobj.getString("manual");
//                    series.brochure=seriesobj.getString("brochure");
//                    series.Application = app;



                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        JSONArray modelsarray = data.getJSONArray("models");
                        Log.d("Pawan", "modelsarray array===" + modelsarray);
//                        for (int k = 0; k < modelsarray.length(); k++) {
//                            JSONArray modelArray = modelsarray.getJSONArray(k);
                            for (int i = 0; i < modelsarray.length(); i++) {
                                JSONObject modelobj = modelsarray.getJSONObject(i);
                                ModelsData modelsdata = new ModelsData();
                                modelsdata.modelId = modelobj.getString("id");
                                modelsdata.series_id = modelobj.getString("series_id");
                                modelsdata.title = modelobj.getString("title");
                                modelsdata.description = modelobj.getString("description");
                                modelsdata.image = modelobj.getString("image");
                                modelsdata.banner_image = modelobj.getString("banner_image");
                                modelsdata.stock = modelobj.getInt("stock");
                                modelsdata.price = modelobj.getInt("price");
//                        modelsdata.ref_id = modelobj.getString("ref_id");
                                modelsdata.save();
                            }


                    } catch (Exception e) {
                            e.printStackTrace();
                    }


                    JSONArray addonscategoryarray = data.getJSONArray("addon_categories");
                    Log.d("Pawan", "addonscategoryarray array===" + addonscategoryarray);
                    for (int i = 0; i < addonscategoryarray.length(); i++) {
                        JSONObject addonscatobj = addonscategoryarray.getJSONObject(i);
                        AddonsCategory addonsCategory = new AddonsCategory();
                        addonsCategory.addoncatid = addonscatobj.getString("id");
                        addonsCategory.model_id = addonscatobj.getString("model_id");
                        addonsCategory.name = addonscatobj.getString("name");
                        addonsCategory.save();
                    }
                        try {
                            JSONArray addonsarray = data.getJSONArray("addon");
                            Log.d("Pawan", "addonsarray array===" + addonsarray);
                            for (int j = 0; j < addonsarray.length(); j++) {

                                JSONObject addonsobj = addonsarray.getJSONObject(j);
                                Addons addons = new Addons();
                                addons.addonsid = addonsobj.getString("id");
//                    addons.description = addonsobj.getString("description");
                                addons.image = addonsobj.getString("image");
                                addons.price = addonsobj.getInt("price");
//                    addons.qty = addonsobj.getString("qty");
                                addons.category_id = addonsobj.getString("category_id");
                                addons.banner_image = addonsobj.getString("banner_image");
                                addons.title = addonsobj.getString("title");
                                addons.stock = addonsobj.getString("stock");
//                    addons.Series = series;
                                addons.save();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                list = Application.getAll();
                adapter = new ListAdapter(ChooseApplication.this, list);
                listView.setAdapter(adapter);
                listView.setDivider(null);
                ActiveAndroid.setTransactionSuccessful();
                ActiveAndroid.endTransaction();
            }

            super.onPostExecute(result);
        }
    }

    public String convertStreamToString(InputStream inputstream)
            throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = inputstream.read()) != -1) {
            bytestream.write(ch);
        }
        return new String(bytestream.toByteArray(), "UTF-8");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();  // optional depending on your needs
        PreferencesManager.setPreferenceBooleanByKey(ChooseApplication.this, "APICALL", false);

        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        finish();

    }

    @Override
    public void onResume() {
        System.out.println("onResume");
        if (User.getUser() != null && PreferencesManager.getPreferenceBooleanByKey(ChooseApplication.this, "isLogin") == true) {
            arraylist.clear();
            navigationLogout();
            User user = User.getUser();
            userName.setText(user.getName());
            mLoader.displayImage(user.getImage(), userImg);


        } else {

            userName.setText("LOGIN");
            mLoader.displayImage("assets://person_icon.png", userImg);
            arraylist.clear();
            navigationLogin();

        }
        super.onResume();

    }

    @Override
    public void onPause() {
        System.out.println("onPause");
        super.onPause();

    }

}
