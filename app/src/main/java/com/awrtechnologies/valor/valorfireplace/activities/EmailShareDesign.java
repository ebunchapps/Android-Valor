package com.awrtechnologies.valor.valorfireplace.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.print.PrintHelper;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.app.Application;
import com.activeandroid.query.Delete;
import com.awrtechnologies.valor.valorfireplace.Data.Addons;
import com.awrtechnologies.valor.valorfireplace.Data.AddonsCategory;
import com.awrtechnologies.valor.valorfireplace.Data.ModelsData;
import com.awrtechnologies.valor.valorfireplace.Data.NavData;
import com.awrtechnologies.valor.valorfireplace.Data.Series;
import com.awrtechnologies.valor.valorfireplace.Data.SeriesDetail;
import com.awrtechnologies.valor.valorfireplace.Data.User;
import com.awrtechnologies.valor.valorfireplace.R;
import com.awrtechnologies.valor.valorfireplace.adapter.ListviewAdapterFA;
import com.awrtechnologies.valor.valorfireplace.adapter.ListviewAdapterNew;
import com.awrtechnologies.valor.valorfireplace.adapter.NavAdapter;
import com.awrtechnologies.valor.valorfireplace.apiconstants.Constants;
import com.awrtechnologies.valor.valorfireplace.helper.GeneralHelper;
import com.awrtechnologies.valor.valorfireplace.helper.PDFHelper;
import com.awrtechnologies.valor.valorfireplace.uicomponents.PreferencesManager;
import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.view.GestureDetector.OnGestureListener;

public class EmailShareDesign extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, OnGestureListener {

    Point point;

    RelativeLayout relativeTitleBar;
    RelativeLayout relativeFrame;
    LinearLayout linearTopBar;
    ImageView imgValorTopicon;
    Button btnNav;
    RelativeLayout relativeImageParent;
    TextView txtStep4Name;
    TextView txtStep1;
    TextView txtStep1Name;
    TextView txtStep2;
    TextView txtStep2Name;
    TextView txtStep3;
    TextView txtStep3Name;

    TextView txtSteps3Name;
    ImageView imgdownArrow;
    File myPath;
    TextView userName;
    ImageView userImg;

    ImageView imagcenterbackground;
    ImageView imgBackground;
    ImageView imgCenter;
    Button btnEmail;
    Button btnLocateAdealer;
    Button btnPrint;
    Button btnPdfBrochure;
    Button btnPdfManual;
    Button btnShare;
    LinearLayout linearBuildValor;
    ImageView imgRefresh;
    ImageView imgBottomLogo;
    LinearLayout linearNavigatiion;
    LinearLayout layouttotal;

    TextView txtTotalAmount;

    ListView listView;
    ListviewAdapterNew adapter;
    ArrayList<SeriesDetail> list;

    DrawerLayout dL;
    ListView navListview;

    ArrayList<NavData> arraylist;
    NavAdapter navAdapter;
    String[] textArray = {"HELP", "LOGIN"};
    LinearLayout menuNav;

    String applicationId;
    String seriesId;
    String addonsId;
    String applicationIds;

    String username;
    String useremail;
    String usercontact;
    String useraddress;
    float resultCode;

    String imagefilepath;
    String categoryid;
    String modelID;
    int total = 0;
    String SharingUrl = "http://awrtechnologies.com/clients/paul/valor/share/series?id=";
    private GestureDetectorCompat gestureDetector;

    String[] textArrayLogout = {"HELP", "LOGOUT"};

    int[] imageArray = {R.drawable.question_mark, R.drawable.person_icon};

    int[] imageArrayLogout = {R.drawable.question_mark, R.drawable.logout};
    private ImageLoader mLoader;
    Dialog dialog;
    SeriesDetail seriesdata;
    List<AddonsCategory> categorylist;

    int modelprice;
    int addonprice = 0;
    ArrayList<View> viewlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_email_share_design);
        mLoader = ImageLoader.getInstance();
        categorylist = new ArrayList<AddonsCategory>();

        list = new ArrayList<SeriesDetail>();
        arraylist = new ArrayList<NavData>();
        point = GeneralHelper.getInstance(this).getScreenSize();
        viewlist = new ArrayList<>();
        findViewIDs();

        setPositionForProgressArrow();
        if (User.getUser() == null) {
            navigationLogin();
            btnEmail.setText("Email");

        } else {
            navigationLogout();
            User user = User.getUser();
            userName.setText(user.getName());
            mLoader.displayImage(user.getImage(), userImg);
            btnEmail.setText("Submit");


        }
        seriesdata = (SeriesDetail) getIntent().getSerializableExtra("SERIESDATA");
        list = (ArrayList<SeriesDetail>) getIntent().getSerializableExtra("SERIESLIST");
        adapter = new ListviewAdapterNew(EmailShareDesign.this
                , list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        applySetOnClickListeners();

        //TODO : Remove after confirmation
        //applyParams();

        applicationId = getIntent().getStringExtra("APPId");
        applicationIds = getIntent().getStringExtra("APPID");

        seriesId = getIntent().getStringExtra("SERIESID");
        addonsId = getIntent().getStringExtra("ADDONID");
        categoryid = getIntent().getStringExtra("ADDONCATID");
        modelID = getIntent().getStringExtra("MODELID");
        categorylist = AddonsCategory.getAllBymodelId(modelID);
        System.out.println("IMAGE PATH NAME" + getIntent().getStringExtra("IMAGEPath"));
        mLoader.displayImage("file://" + getIntent().getStringExtra("IMAGEPath"), imgBackground);

        mLoader.displayImage(Series.getSeriesbySeriesId(seriesId).actualImage, imgCenter);
//        if (User.getUser() == null) {
//            layouttotal.setVisibility(View.GONE);
//        } else {


        try {
            for (int i = 0; i < list.size(); i++) {
                Log.d("Pawan", "List values" + list.get(i));
                if (list.get(i).getType() == 1) {
                    modelprice = list.get(i).getModelprice();
                    Log.d("Pawan", "getModelprice====" + list.get(i).getModelprice());
                }
                if (list.get(i).getType() == 0) {
                    int p = list.get(i).getAddonsprice();
                    addonprice = addonprice + p;
                    Log.d("Pawan", "getAddonsprice====" + list.get(i).getAddonsprice());

                }
                Log.d("Pawan", "TotalAmount====" + modelprice + addonprice);

            }
            total = modelprice + addonprice;
            boolean showPrice = PreferencesManager.getPreferenceBooleanByKey(EmailShareDesign.this,"isLogin");
            if(showPrice) {
                txtTotalAmount.setText("$" + total + "");
            }else{
                findViewById(R.id.txtTotal).setVisibility(View.INVISIBLE);
            }
//            total=0;
//            if (GeneralHelper.getInstance(EmailShareDesign.this).isAddoncheck() == false) {
//                total = modelprice + addonprice;
//                Log.d("Pawan", "TotalAmount Addon select====" + total);
//            } else {
//                total = modelprice + 0;
//                Log.d("Pawan", "TotalAmount Only model====" + total);
//            }
//            txtTotalAmount.setText("$" + total + "");
//            Log.d("Pawan", "TotalAmount====" + total);
//            resultCode = Float.parseFloat(getIntent().getStringExtra("resultCode"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        setImageRatio();
    }


    public void findViewIDs() {
        btnEmail = (Button) findViewById(R.id.btnEmail);
        btnLocateAdealer = (Button) findViewById(R.id.btnLocateAdealer);
        btnPrint = (Button) findViewById(R.id.btnPrint);
        btnPdfBrochure = (Button) findViewById(R.id.btnPdfBrochure);
        btnPdfManual = (Button) findViewById(R.id.btnPdfManual);
        btnShare = (Button) findViewById(R.id.btnShare);
        btnNav = (Button) findViewById(R.id.btnNav);

        imgCenter = (ImageView) findViewById(R.id.imgCenter);
        imagcenterbackground = (ImageView) findViewById(R.id.imgCenterbackground);
        imgBackground = (ImageView) findViewById(R.id.imgBackground);
        relativeFrame = (RelativeLayout) findViewById(R.id.relativeFrame);

        imgValorTopicon = (ImageView) findViewById(R.id.imgValorTopicon);
        imgBottomLogo = (ImageView) findViewById(R.id.imgBottomLogo);
        imgRefresh = (ImageView) findViewById(R.id.imgRefresh);

        imgdownArrow = (ImageView) findViewById(R.id.imgdownArrow);
        txtStep4Name = (TextView) findViewById(R.id.txtStep4Name);
        txtStep1Name = (TextView) findViewById(R.id.txtStep1Name);
        txtStep1 = (TextView) findViewById(R.id.txtStep1);
        txtStep2 = (TextView) findViewById(R.id.txtStep2);
        txtStep2Name = (TextView) findViewById(R.id.txtStep2Name);
        txtStep3 = (TextView) findViewById(R.id.txtStep3);
        txtStep3Name = (TextView) findViewById(R.id.txtStep3Name);

        relativeImageParent = (RelativeLayout) findViewById(R.id.relativeImageParent);
        linearTopBar = (LinearLayout) findViewById(R.id.linearTopBar);
        linearBuildValor = (LinearLayout) findViewById(R.id.linearBuildValor);
        relativeTitleBar = (RelativeLayout) findViewById(R.id.relativeTitleBar);
        listView = (ListView) findViewById(R.id.listView);
        linearNavigatiion = (LinearLayout) findViewById(R.id.linearNavigatiion);

        dL = (DrawerLayout) findViewById(R.id.drawer_layout);
        navListview = (ListView) findViewById(R.id.left_drawer);
        menuNav = (LinearLayout) findViewById(R.id.menuNav);
        txtTotalAmount = (TextView) findViewById(R.id.txtTotalAmount);

        userName = (TextView) findViewById(R.id.userName);
        userImg = (ImageView) findViewById(R.id.userImg);
        txtSteps3Name = (TextView) findViewById(R.id.txtSteps3Name);
        layouttotal = (LinearLayout) findViewById(R.id.layouttotal);
    }


    public void setPositionForProgressArrow() {

        dL.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        System.out.println("NEXT frame Imagebackground X===" + imgBackground.getScaleX() + "NEXT frame Imagebackground Y===" + imgBackground.getScaleY());

                        float x = txtStep4Name.getX();
                        float width = txtStep4Name.getWidth();
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

    public void applySetOnClickListeners() {
        btnEmail.setOnClickListener(this);
        btnLocateAdealer.setOnClickListener(this);
        btnPrint.setOnClickListener(this);
        btnPdfBrochure.setOnClickListener(this);
        btnPdfManual.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnNav.setOnClickListener(this);
        linearNavigatiion.setOnClickListener(this);
        linearBuildValor.setOnClickListener(this);
        txtStep1Name.setOnClickListener(this);
        txtStep1.setOnClickListener(this);
        txtStep2.setOnClickListener(this);
        txtStep2Name.setOnClickListener(this);
        txtStep3.setOnClickListener(this);
        txtStep3Name.setOnClickListener(this);
        txtStep4Name.setOnClickListener(this);
        txtSteps3Name.setOnClickListener(this);

    }

    public void applyParams() {
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) imgValorTopicon.getLayoutParams();
        lp.width = point.x / 9;
        lp.height = point.y / 13;
        imgValorTopicon.setLayoutParams(lp);

        ViewGroup.LayoutParams lp1 = (ViewGroup.LayoutParams) relativeTitleBar.getLayoutParams();
//        lp1.width = (int) Math.round(point.x / 1.5);
        lp1.height = (int) Math.round(point.y / 10.0);
        relativeTitleBar.setLayoutParams(lp1);

        ViewGroup.LayoutParams lp2 = (ViewGroup.LayoutParams) btnNav.getLayoutParams();
        lp2.width = point.x / 38;
        lp2.height = point.y / 40;
        btnNav.setLayoutParams(lp2);

        ViewGroup.LayoutParams lp3 = (ViewGroup.LayoutParams) linearTopBar.getLayoutParams();
//        lp3.width = point.x / 12;
        lp3.height = point.y / 11;
        linearTopBar.setLayoutParams(lp3);

        ViewGroup.LayoutParams lp4 = (ViewGroup.LayoutParams) imgCenter.getLayoutParams();
        lp4.width = (int) Math.round(point.x / 6.5);
        lp4.height = (int) Math.round(point.y / 6.381);

        imgCenter.setLayoutParams(lp4);


        ViewGroup.LayoutParams lpbg = (ViewGroup.LayoutParams) imagcenterbackground.getLayoutParams();
        lpbg.width = (int) Math.round(point.x / 6.73);
        lpbg.height = (int) Math.round(point.y / 4.5);
        imagcenterbackground.setLayoutParams(lpbg);

//        ViewGroup.LayoutParams lp5 = (ViewGroup.LayoutParams) relativeImageParent.getLayoutParams();
//
//        lp5.width = (int) Math.round(point.x / 2.262);
//        lp5.height = (int) Math.round(point.y / 6.2);
//        relativeImageParent.setLayoutParams(lp5);

//        ViewGroup.LayoutParams lpp = (ViewGroup.LayoutParams) imgBackground.getLayoutParams();
//
//        lpp.width = (int) Math.round(point.x / 3.5);
//        lpp.height = (int) Math.round(point.y / 6.2);
//        imgBackground.setLayoutParams(lpp);

        ViewGroup.LayoutParams lp6 = (ViewGroup.LayoutParams) btnEmail.getLayoutParams();
        lp6.width = (int) Math.round(point.x / 24.0);
        lp6.height = (int) Math.round(point.x / 32.0);
        btnEmail.setLayoutParams(lp6);

        ViewGroup.LayoutParams lp7 = (ViewGroup.LayoutParams) btnLocateAdealer.getLayoutParams();
        lp7.width = (int) Math.round(point.x / 24.0);
        lp7.height = (int) Math.round(point.x / 32.0);
        btnLocateAdealer.setLayoutParams(lp7);

        ViewGroup.LayoutParams lp8 = (ViewGroup.LayoutParams) btnPrint.getLayoutParams();
        lp8.width = (int) Math.round(point.x / 24.0);
        lp8.height = (int) Math.round(point.x / 32.0);
        btnPrint.setLayoutParams(lp8);

        ViewGroup.LayoutParams lp9 = (ViewGroup.LayoutParams) btnPdfBrochure.getLayoutParams();
        lp9.width = (int) Math.round(point.x / 24.0);
        lp9.height = (int) Math.round(point.x / 32.0);
        btnPdfBrochure.setLayoutParams(lp9);

        ViewGroup.LayoutParams lp10 = (ViewGroup.LayoutParams) btnPdfManual.getLayoutParams();
        lp10.width = (int) Math.round(point.x / 24.0);
        lp10.height = (int) Math.round(point.x / 32.0);
        btnPdfManual.setLayoutParams(lp10);

        ViewGroup.LayoutParams lp11 = (ViewGroup.LayoutParams) btnShare.getLayoutParams();
        lp11.width = (int) Math.round(point.x / 24.0);
        lp11.height = (int) Math.round(point.x / 32.0);
        btnShare.setLayoutParams(lp11);

        ViewGroup.LayoutParams lp12 = (ViewGroup.LayoutParams) imgBottomLogo.getLayoutParams();
        lp12.width = (int) Math.round(point.x / 5.0);
        lp12.height = (int) Math.round(point.y / 16.0);
        imgBottomLogo.setLayoutParams(lp12);

        ViewGroup.LayoutParams lp13 = (ViewGroup.LayoutParams) imgRefresh.getLayoutParams();
        lp13.width = (int) Math.round(point.x / 35.0);
        lp13.height = (int) Math.round(point.x / 35.0);
        imgRefresh.setLayoutParams(lp13);

//        ViewGroup.LayoutParams lp14 = (ViewGroup.LayoutParams) linearBuildValor.getLayoutParams();
//        lp14.width = (int) Math.round(point.x / 40.0);
//        lp14.height =  (int) Math.round(point.y / 40.0);
//        linearBuildValor.setLayoutParams(lp14);

//                ViewGroup.LayoutParams lp15 = (ViewGroup.LayoutParams) linearBuildValor.getLayoutParams();
////        lp15.width = (int) Math.round(point.x / 40.0);
//        lp15.height =  (int) Math.round(point.y / 40.0);
//        linearBuildValor.setLayoutParams(lp15);

    }


    public void getParamsOfFireplace() {

        relativeImageParent.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {

                        float heightImgBackground = imgBackground.getHeight();
                        float widthImgBackground = imgBackground.getWidth();

                        float multiplier = (float) 0.36416;

                        imgBackground.setMaxHeight((int) (heightImgBackground * multiplier));

                        System.out.println("heightImgBackground ===" + heightImgBackground);
                        System.out.println("widthImgBackground ===" + widthImgBackground);

                        if (Build.VERSION.SDK_INT >= 16) {
                            relativeImageParent.getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        } else {
                            relativeImageParent.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnEmail:

//                if (User.getUser() == null) {
//                    getFirePlaceImage();
//                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                    shareIntent.setType("image/jpeg");
//                    shareIntent.setPackage("com.google.android.gm");
//                    File imageFileToShare = new File(imagefilepath);
//                    Uri uri = Uri.fromFile(imageFileToShare);
//                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                    shareIntent.putExtra(Intent.EXTRA_TEXT, SharingUrl + seriesId);
//                    startActivity(Intent.createChooser(shareIntent, "Sharing using"));
//                }


                showDialogueSubmit();

                break;

            case R.id.btnLocateAdealer:
//                showDialogueLocateAdealer();
                Intent locateDealerIntent = new Intent(EmailShareDesign.this, LocateDealerWebview.class);
                startActivity(locateDealerIntent);
                break;

            case R.id.btnPrint:
                if (Build.VERSION.SDK_INT >= 21) {
                    getFirePlaceImage();
                    printingImage();

                } else {

                    new AlertDialog.Builder(EmailShareDesign.this)
                            .setTitle("Error")
                            .setMessage("Sorry,your device doesn't support print")
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();

                }


                break;

            case R.id.btnPdfBrochure:

                try {
                    String extStorageDirectory = Environment.getExternalStorageDirectory()
                            .toString();
                    File folder = new File(extStorageDirectory, "/." + Constants.APPNAME + "/pdf");
                    folder.mkdirs();
                    Log.e("series", "" + seriesId);
                    String pdfUrl = Series.getSeriesbySeriesId(seriesId).getBrochure();
                    File file = new File(folder, pdfUrl.hashCode() + ".pdf");
                    PDFHelper.getIntance(EmailShareDesign.this).openPdf(pdfUrl, file, "valor");
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(EmailShareDesign.this,"No Brochure Found",Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btnPdfManual:

                String extStorageDirectorys = Environment.getExternalStorageDirectory()
                        .toString();
                File folders = new File(extStorageDirectorys, "/." + Constants.APPNAME + "/pdf");
                folders.mkdirs();
                String pdfUrlmanual = Series.getSeriesbySeriesId(seriesId).getManual();
                File files = new File(folders, pdfUrlmanual.hashCode() + ".pdf");
                PDFHelper.getIntance(EmailShareDesign.this).openPdf(pdfUrlmanual, files, "valor");

                break;

            case R.id.btnShare:
                showDialogueSocial();
                break;

            case R.id.linearBuildValor:
                Intent intent = new Intent(EmailShareDesign.this, ChooseApplication.class);
                startActivity(intent);
                break;

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

            case R.id.txtSteps3Name:
                Intent txtmodel = new Intent(EmailShareDesign.this, ChooseModel.class);
                if (PreferencesManager.getPreferenceBooleanByKey(EmailShareDesign.this, "IsLogout") == true) {

                    txtmodel.putExtra("APPID", applicationIds);

                } else {

                    txtmodel.putExtra("APPID", applicationId);
                }
                txtmodel.addCategory(Intent.CATEGORY_HOME);
                txtmodel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(txtmodel);
                break;

            case R.id.txtStep2Name:
                Intent a22 = new Intent(EmailShareDesign.this, ChooseSeries.class);
                if (PreferencesManager.getPreferenceBooleanByKey(EmailShareDesign.this, "IsLogout") == true) {

                    a22.putExtra("APPID", applicationIds);

                } else {

                    a22.putExtra("APPID", applicationId);
                }
                a22.addCategory(Intent.CATEGORY_HOME);
                a22.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a22);
                break;

            case R.id.txtStep2:
                Intent a2 = new Intent(EmailShareDesign.this, ChooseSeries.class);
                if (PreferencesManager.getPreferenceBooleanByKey(EmailShareDesign.this, "IsLogout") == true) {

                    a2.putExtra("APPID", applicationIds);

                } else {

                    a2.putExtra("APPID", applicationId);
                }
                a2.addCategory(Intent.CATEGORY_HOME);
                a2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a2);
                break;

            case R.id.txtStep1:
                Intent a1 = new Intent(EmailShareDesign.this, ChooseApplication.class);
                a1.addCategory(Intent.CATEGORY_HOME);
                a1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a1);
                break;

            case R.id.txtStep1Name:
                Intent a11 = new Intent(EmailShareDesign.this, ChooseApplication.class);
                a11.addCategory(Intent.CATEGORY_HOME);
                a11.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a11);
                break;


            case R.id.txtStep3:
                onBackPressed();
                break;

            case R.id.txtStep3Name:
                onBackPressed();
                break;

            default:
                break;
        }
    }

    public void setImageRatio() {

        try {
            resultCode = Float.parseFloat(getIntent().getStringExtra("resultCode"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ViewHelper.setY(imgBackground, resultCode);
        ViewHelper.setY(relativeImageParent, resultCode);
        ViewHelper.setX(imgBackground, resultCode);
        ViewHelper.setX(relativeImageParent, resultCode);
        System.out.println(resultCode + "resultCOde ;;;;;;;;;");
    }

    public void getFirePlaceImage() {
        View u = findViewById(R.id.layoutmain);
        u.setDrawingCacheEnabled(true);

        int totalHeight = u.getHeight();
        int totalWidth = u.getWidth();
        u.layout(0, 0, totalWidth, totalHeight);
        u.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(u.getDrawingCache());
        u.setDrawingCacheEnabled(false);

        //Save bitmap
        String extr = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                + "/.Valor" + "";
        String fileName = new SimpleDateFormat("yyyyMMddhhmmss'fireplace.jpg'").format(new Date());
        myPath = new File(extr, fileName);

        imagefilepath = myPath + "";
        System.out.println("FILEPATHNAME  FULLSCREEN------" + imagefilepath);


        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            MediaStore.Images.Media.insertImage(getContentResolver(), b, "Screen", "screen");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public void printingImage() {
        PrintHelper photoPrinter = new PrintHelper(EmailShareDesign.this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);

        File imageFileToShare = new File(imagefilepath);
        Uri uri = Uri.fromFile(imageFileToShare);
        try {
            photoPrinter.printBitmap("print", uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void navigationLogin() {
        for (int i = 0; i < textArray.length; i++) {
            NavData d = new NavData();
            d.setImages(imageArray[i]);
            d.setText(textArray[i]);
            arraylist.add(d);
        }

        navAdapter = new NavAdapter(EmailShareDesign.this, arraylist);
        navListview.setAdapter(navAdapter);
        navListview.setDivider(null);
        navListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent WebViewIntent = new Intent(EmailShareDesign.this, WebActivity.class);
                        startActivity(WebViewIntent);
                        dL.closeDrawer(menuNav);
                        break;
                    case 1:
                        Intent loginIntent = new Intent(EmailShareDesign.this, SignInActivity.class);
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

        navAdapter = new NavAdapter(EmailShareDesign.this, arraylist);
        navListview.setAdapter(navAdapter);
        navListview.setDivider(null);
        navListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent WebViewIntent = new Intent(EmailShareDesign.this, WebActivity.class);
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
        dialog = new Dialog(EmailShareDesign.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogbox_quatationresult);

        Button ok = (Button) dialog.findViewById(R.id.buttonok);
        Button cancel = (Button) dialog.findViewById(R.id.buttoncancel);
        Button close = (Button) dialog.findViewById(R.id.btnClose);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesManager.setPreferenceBooleanByKey(EmailShareDesign.this, "isLogin", false);

                new Delete().from(User.class).execute();

                Intent i = new Intent(EmailShareDesign.this, EmailShareDesign.class);
                i.putExtra("APPID", applicationId);
                i.putExtra("SERIESID", seriesId);
                i.putExtra("ADDONID", addonsId);
                PreferencesManager.setPreferenceBooleanByKey(EmailShareDesign.this, "IsLogout", true);
                Log.e("Rakhi", "Applicationidinsideexitapp" + applicationId);
                mLoader.displayImage("file://" + getIntent().getStringExtra("IMAGEPath"), imgBackground);

                mLoader.displayImage(Series.getSeriesbySeriesId(seriesId).actualImage, imgCenter);
//                seriesdata = (SeriesDetail) getIntent().getSerializableExtra("SERIESDATA");
                i.putExtra("SERIESDATA", seriesdata);
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


    protected void showDialogueSubmit() {

        final Dialog dialog = new Dialog(EmailShareDesign.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogue_form);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnClose = (Button) dialog.findViewById(R.id.btnClose);
        Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        final EditText editTextUsername = (EditText) dialog.findViewById(R.id.editTextUsername);
        final EditText editTextUseraddress = (EditText) dialog.findViewById(R.id.editTextUseraddress);
        final EditText editTextUserEmail = (EditText) dialog.findViewById(R.id.editTextUserEmail);
        final EditText editTextUsercontact = (EditText) dialog.findViewById(R.id.editTextUsercontact);


        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                username = editTextUsername.getText().toString();
                useremail = editTextUserEmail.getText().toString();
                usercontact = editTextUsercontact.getText().toString();
                useraddress = editTextUseraddress.getText().toString();
                if (username.isEmpty()
                        || useremail.isEmpty()
                        || usercontact.isEmpty()
                        || useraddress.isEmpty()
                        )

                {
                    Toast.makeText(EmailShareDesign.this, " fill all the field ",
                            Toast.LENGTH_LONG).show();

                } else {

                    new SubmitTask().execute();
                    dialog.dismiss();
                }


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }


    protected void showDialogueLocateAdealer() {

        final Dialog dialog = new Dialog(EmailShareDesign.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogue_loacate_dealer);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnClose = (Button) dialog.findViewById(R.id.btnClose);
        Button btnLocateadealer = (Button) dialog.findViewById(R.id.btnLocateadealer);
        EditText editTexttxtPostalCode = (EditText) dialog.findViewById(R.id.editTexttxtPostalCode);
        ImageView imgMarker = (ImageView) dialog.findViewById(R.id.imgMarker);


        btnLocateadealer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                dialog.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }


    protected void showDialogueSocial() {

        final Dialog dialog = new Dialog(EmailShareDesign.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.socialdialogue);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnClose = (Button) dialog.findViewById(R.id.btnClose);
        Button btnTwitter = (Button) dialog.findViewById(R.id.btnTwitter);
        Button btnFacebook = (Button) dialog.findViewById(R.id.btnFacebook);
        Button btnGplus = (Button) dialog.findViewById(R.id.btnGplus);


        btnFacebook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getFirePlaceImage();

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setPackage("com.facebook.katana");
                share.setType("image/jpeg");
                File imageFileToShare = new File(imagefilepath);
                Uri uri = Uri.fromFile(imageFileToShare);
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.putExtra(Intent.EXTRA_TEXT, SharingUrl + seriesId);
                startActivity(Intent.createChooser(share, "Sharing using"));
            }
        });

        btnGplus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                getFirePlaceImage();

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setPackage("com.google.android.apps.plus");
                sharingIntent.setType("text/plain");
                File imageFileToShare = new File(imagefilepath);
                Uri uri = Uri.fromFile(imageFileToShare);
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, SharingUrl + seriesId);
                startActivity(Intent.createChooser(sharingIntent, "Sharing using"));

            }
        });

        btnTwitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getFirePlaceImage();

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("image/jpeg");
                sharingIntent.setPackage("com.twitter.android");
                File imageFileToShare = new File(imagefilepath);
                Uri uri = Uri.fromFile(imageFileToShare);
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, SharingUrl + seriesId);
                startActivity(Intent.createChooser(sharingIntent, "Sharing using"));


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.d("Pawan", "Clicked" + position);

        if (User.getUser() != null) {
            dialogBoxEDditPrice(list, position);
                
        } else {

        }

    }

    private void dialogBoxEDditPrice(final ArrayList<SeriesDetail> datalist, final int position) {
        dialog = new Dialog(EmailShareDesign.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogboxeditnumber);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final EditText edittextprice = (EditText) dialog
                .findViewById(R.id.edittextprice);
        if (datalist.get(position).getType() == 1) {
            edittextprice.setText(datalist.get(position).getModelprice() + "");
        } else if (datalist.get(position).getType() == 0) {
            edittextprice.setText(datalist.get(position).getAddonsprice() + "");
        }

        Button ok = (Button) dialog.findViewById(R.id.buttonok);
        Button cancel = (Button) dialog.findViewById(R.id.buttoncancel);
        Button btnClose = (Button) dialog.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener()

                                    {

                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();

                                        }
                                    }

        );
        ok.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      try {
                                          int price_before;
                                          int price_after = 0;
                                          price_before = datalist.get(position).getModelprice();
                                          price_after = Integer.parseInt(edittextprice.getText().toString());
                                          if (price_after >= price_before) {
                                              if (datalist.get(position).getType() == 1) {
                                                  datalist.get(position).setModelprice(Integer.parseInt(edittextprice.getText().toString()));
                                              } else if (datalist.get(position).getType() == 0) {
                                                  datalist.get(position).setAddonsprice(Integer.parseInt(edittextprice.getText().toString()));
                                              }
                                          }
                                          adapter = new ListviewAdapterNew(EmailShareDesign.this
                                                  , datalist);
                                          listView.setAdapter(adapter);
                                          adapter.setList(datalist);
                                          adapter.notifyDataSetChanged();
                                          total = 0;
                                          addonprice = 0;
                                          try {
                                              for (int i = 0; i < datalist.size(); i++) {
                                                  if (datalist.get(i).getType() == 1) {
                                                      modelprice = datalist.get(i).getModelprice();
                                                      seriesdata.setModelprice(modelprice);
                                                      Log.d("Pawan", "ModelPrice ==" + modelprice);
                                                  }
                                                  if (datalist.get(i).getType() == 0) {
                                                      int p = datalist.get(i).getAddonsprice();
                                                      seriesdata.setAddonsprice(p);
                                                      Log.d("Pawan", "AddonPrice ==" + p);
                                                      addonprice = addonprice + p;
//                                                      addonprice=p;
                                                      Log.d("Pawan", "AddonPrice for total==" + addonprice);

                                                  }
                                              }
                                              total = modelprice + addonprice;
                                              Log.d("pawan", "Total Price ==" + total);
//                                              if (GeneralHelper.getInstance(EmailShareDesign.this).isAddoncheck() == false) {
//                                                  Log.d("Pawan", "AddonPrice for total==" + addonprice);
//                                                  Log.d("Pawan", "ModelPrice for total==" + modelprice);
//
//                                                  total = modelprice + addonprice;
//                                                  Log.d("pawan", "Total Price==" + total);
//                                              } else {
//                                                  total = modelprice + 0;
//                                                  Log.d("Pawan", "ModelPrice for total when addon0==" + modelprice);
//                                                  Log.d("pawan", "Total Price only model==" + total);
//                                              }
                                              txtTotalAmount.setText("$" + total + "");
                                              dialog.dismiss();

                                          } catch (Exception e) {

                                          }
                                      } catch (Exception e) {

                                      }
                                  }


                              }

        );

//        ok.setOnClickListener(new View.OnClickListener()
//
//                              {
//                                  @Override
//                                  public void onClick(View v) {
//                                      try {
//                                          int price_before;
//                                          int price_after = 0;
//                                          price_before = datalist.get(position).getModelprice();
//                                          price_after = Integer.parseInt(edittextprice.getText().toString());
//                                          if (price_after >= price_before) {
//                                              System.out.println("condition true");
//                                              if (datalist.get(position).getType() == 1) {
//                                                  datalist.get(position).setModelprice(Integer.parseInt(edittextprice.getText().toString()));
//                                              } else if (mSeriesDetails.get(position).getType() == 0) {
//                                                  datalist.get(position).setAddonsprice(Integer.parseInt(edittextprice.getText().toString()));
//                                              }
//                                          }
////                                          mSeriesDetails.add(seriesdata);
//                                          adapter = new ListviewAdapterNew(EmailShareDesign.this
//                                                  , datalist);
//                                          listView.setAdapter(adapter);
//                                          adapter.setList(datalist);
//                                          adapter.notifyDataSetChanged();
//                                      } catch (Exception e) {
//                                                e.printStackTrace();
//                                      }
//
//                                  }
//                              }
//
//        );

        cancel.setOnClickListener(new View.OnClickListener()

                                  {
                                      @Override
                                      public void onClick(View v) {

                                          dialog.dismiss();

                                      }
                                  }

        );

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);


    }


    @Override
    protected void onResume() {
        btnEmail.setText("Submit");
        try {
            layouttotal.setVisibility(View.VISIBLE);
//            if(GeneralHelper.getInstance(EmailShareDesign.this).isAddoncheck()==false) {
//                total  = seriesdata.getModelprice() + seriesdata.getAddonsprice();
//            }else {
//                total  = seriesdata.getModelprice() + 0;
//            }
//
//            txtTotalAmount.setText("$" + total + "");
            resultCode = Float.parseFloat(getIntent().getStringExtra("resultCode"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (User.getUser() != null && PreferencesManager.getPreferenceBooleanByKey(EmailShareDesign.this, "isLogin") == true) {
            arraylist.clear();
            navigationLogout();
            User user = User.getUser();
            userName.setText(user.getName());
            mLoader.displayImage(user.getImage(), userImg);
//            btnEmail.setText("Submit");
//            try {
//                layouttotal.setVisibility(View.VISIBLE);
//                if(GeneralHelper.getInstance(EmailShareDesign.this).isAddoncheck()==false) {
//                    total  = seriesdata.getModelprice() + seriesdata.getAddonsprice();
//                }else {
//                    total  = seriesdata.getModelprice() + 0;
//                }
//
//                txtTotalAmount.setText("$" + total + "");
//                resultCode = Float.parseFloat(getIntent().getStringExtra("resultCode"));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


        } else {
            applicationIds = getIntent().getStringExtra("APPID");
            seriesId = getIntent().getStringExtra("SERIESID");
            addonsId = getIntent().getStringExtra("ADDONID");
//            try {
//                layouttotal.setVisibility(View.VISIBLE);
//                if(GeneralHelper.getInstance(EmailShareDesign.this).isAddoncheck()==false) {
//                    total  = seriesdata.getModelprice() + seriesdata.getAddonsprice();
//                }else {
//                    total  = seriesdata.getModelprice() + 0;
//                }
//
//                txtTotalAmount.setText("$" + total + "");
//                resultCode = Float.parseFloat(getIntent().getStringExtra("resultCode"));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            userName.setText("LOGIN");
            mLoader.displayImage("assets://person_icon.png", userImg);
            arraylist.clear();
//            btnEmail.setText("Submit");

            navigationLogin();
        }


        super.onResume();
    }

    @Override
    protected void onPause() {
        applicationId = getIntent().getStringExtra("APPId");

        super.onPause();
    }


    public class SubmitTask extends AsyncTask<Void, Void, String> {

        ProgressDialog mProgressDialog;


        @Override
        protected String doInBackground(Void... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(Constants.BASEURL
                        + "quotation");
                ArrayList<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
                try {
                    System.out.println("applicationId" +
                            applicationId);
                    System.out.println("seriesId" +
                            seriesId);
                    System.out.println("addonsId" +
                            addonsId);
                    System.out.println("seriesPrice" +
                            seriesdata.getSeriesprice() + "");
                    System.out.println("addonsPrice" +
                            seriesdata.getAddonsprice() + "");
//                System.out.println("salesmanId" +
//                        User.getUser().getUserid());
                    System.out.println("username" +
                            username);
                    System.out.println("useremail" +
                            useremail);
                    System.out.println("useraddress" +
                            useraddress);
                    System.out.println("usercontact" +
                            usercontact + "");
                    System.out.println("application_name" +
                            com.awrtechnologies.valor.valorfireplace.Data.Application.getAllByApplicationId(applicationId).title);
                    System.out.println("series_name" +
                            Series.getSeriesbySeriesId(seriesId).title);

                    System.out.println("quotation[model][id]" +
                            modelID);
                    System.out.println("quotation[model][name]" +
                            ModelsData.getModelByModelID(modelID).title);
                    System.out.println("]quotation[model][price]" +
                            modelprice + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (User.getUser() != null) {
                    BasicNameValuePair salesmanid = new BasicNameValuePair("salesman_id",
                            User.getUser().getUserid());
                    param.add(salesmanid);
                }


                BasicNameValuePair pasrd1 = new BasicNameValuePair("quotation[application_id]",
                        applicationId);
                param.add(pasrd1);

                BasicNameValuePair applicationname = new BasicNameValuePair("quotation[application_name]",
                        com.awrtechnologies.valor.valorfireplace.Data.Application.getAllByApplicationId(applicationId).title);
                param.add(applicationname);


                BasicNameValuePair seriesName = new BasicNameValuePair("quotation[series_name]",
                        Series.getSeriesbySeriesId(seriesId).title);
                param.add(seriesName);

                BasicNameValuePair pasrd2 = new BasicNameValuePair("quotation[series_id]",
                        seriesId);
                param.add(pasrd2);

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getType() == 0) {
                        System.out.println("]quotation[addon][price]" +
                                list.get(i).getAddonsprice() + "");
                        System.out.println("]quotation[addon][name]" +
                                list.get(i).getAddonstitle() + "");
                        System.out.println("]quotation[addon][id]" +
                                list.get(i).getAddonId() + "");
                        System.out.println("]quotation[cat][name]" +
                                list.get(i).getCategoryname() + "");
                        System.out.println("]quotation[cat][id]" +
                                list.get(i).getCategoryID());

                        BasicNameValuePair pasrd3 = new BasicNameValuePair("quotation[addons][id][]",
                                list.get(i).getAddonId());
                        param.add(pasrd3);

                        BasicNameValuePair addonsName = new BasicNameValuePair("quotation[addons][name][]",
                                list.get(i).getAddonstitle());
                        param.add(addonsName);


                        BasicNameValuePair pasrd7 = new BasicNameValuePair("quotation[addons][price][]",
                                list.get(i).getAddonsprice() + "");
                        param.add(pasrd7);

                        BasicNameValuePair addoncatid = new BasicNameValuePair("quotation[addons][category_id][]",
                                list.get(i).getCategoryID());
                        param.add(addoncatid);

                        BasicNameValuePair addoncatname = new BasicNameValuePair("quotation[addons][category_name][]",
                                list.get(i).getCategoryname());
                        param.add(addoncatname);
                    }

                }

                BasicNameValuePair modelid = new BasicNameValuePair("quotation[model][id]",
                        modelID);
                param.add(modelid);

                BasicNameValuePair modelname = new BasicNameValuePair("quotation[model][name]",
                        ModelsData.getModelByModelID(modelID).title);
                param.add(modelname);

                BasicNameValuePair modelprices = new BasicNameValuePair("quotation[model][price]",
                        modelprice + "");
                param.add(modelprices);

                BasicNameValuePair pasrd9 = new BasicNameValuePair("user[name]",
                        username);
                param.add(pasrd9);
                BasicNameValuePair pasrd10 = new BasicNameValuePair("user[email]",
                        useremail);
                param.add(pasrd10);
                BasicNameValuePair pasrd11 = new BasicNameValuePair("user[address]",
                        useraddress);
                param.add(pasrd11);
                BasicNameValuePair pasrd12 = new BasicNameValuePair("user[contact]",
                        usercontact);
                param.add(pasrd12);

                httppost.setEntity(new UrlEncodedFormEntity(param));
                HttpResponse httpresponse = httpclient.execute(httppost);

                InputStream inputstream = httpresponse.getEntity().getContent();

                String result = convertStreamToString(inputstream);

                System.out.println("QUATATION RESULT==============" + result);
                return result;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(EmailShareDesign.this);
            mProgressDialog.setMessage("Loading..");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            mProgressDialog.dismiss();
            try {
                JSONObject object = new JSONObject(result);
                int responsecode = object.getInt("result_code");

                dialogBoxResult(responsecode, object.getString("result_message"));


            } catch (JSONException e) {
                e.printStackTrace();
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


    public void dialogBoxResult(final int responsecode, final String message) {
        dialog = new Dialog(EmailShareDesign.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogbox_quatationresult);
        final TextView result = (TextView) dialog.findViewById(R.id.textresult);
        result.setText(message);


        Button ok = (Button) dialog.findViewById(R.id.buttonok);
        Button cancel = (Button) dialog.findViewById(R.id.buttoncancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (responsecode == 1) {
                    Intent intent = new Intent(EmailShareDesign.this, ChooseApplication.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if (responsecode == 0) {
                    dialog.dismiss();
                }
                dialog.dismiss();


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
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


//    public class CustomTouchListener implements View.OnTouchListener {
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//
//            Shader shader;
//            AnimatorSet set;
//
//            switch (motionEvent.getAction()) {
//
//                case MotionEvent.ACTION_DOWN:
//
//                    ValueAnimator colorAnim = ObjectAnimator.ofInt(
//                            view, "background", 0xFFFFFFFF, 0xFF1F45FC);
//                    colorAnim.setDuration(250);
//                    colorAnim.setEvaluator(new ArgbEvaluator());
//                    colorAnim.start();
//
//                    break;
//
//                case MotionEvent.ACTION_CANCEL:
//                    colorAnim = ObjectAnimator.ofInt( view,
//                            "background", 0xFF1F45FC, 0xFFFFFFFF);
//                    colorAnim.setDuration(250);
//                    colorAnim.setEvaluator(new ArgbEvaluator());
//                    colorAnim.start();
//
//                case MotionEvent.ACTION_UP:
//
//                    colorAnim = ObjectAnimator.ofInt( view,
//                            "background", 0xFF1F45FC, 0xFFFFFFFF);
//                    colorAnim.setDuration(250);
//                    colorAnim.setEvaluator(new ArgbEvaluator());
//                    colorAnim.start();
//
//                    break;
//            }
//            return true;
//        }
//    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();  // optional depending on your needs

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }


}
