package com.awrtechnologies.valor.valorfireplace.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Delete;
import com.awrtechnologies.valor.valorfireplace.Data.Addons;
import com.awrtechnologies.valor.valorfireplace.Data.AddonsCategory;
import com.awrtechnologies.valor.valorfireplace.Data.Application;
import com.awrtechnologies.valor.valorfireplace.Data.ModelsData;
import com.awrtechnologies.valor.valorfireplace.Data.NavData;
import com.awrtechnologies.valor.valorfireplace.Data.SelectedAddonData;
import com.awrtechnologies.valor.valorfireplace.Data.Series;
import com.awrtechnologies.valor.valorfireplace.Data.SeriesDetail;
import com.awrtechnologies.valor.valorfireplace.Data.User;
import com.awrtechnologies.valor.valorfireplace.R;
import com.awrtechnologies.valor.valorfireplace.adapter.GridModelAdapter;
import com.awrtechnologies.valor.valorfireplace.adapter.HlistViewAdapter;
import com.awrtechnologies.valor.valorfireplace.adapter.ListviewAdapterFA;
import com.awrtechnologies.valor.valorfireplace.adapter.NavAdapter;
import com.awrtechnologies.valor.valorfireplace.apiconstants.Constants;
import com.awrtechnologies.valor.valorfireplace.helper.BitmapMergerTask;
import com.awrtechnologies.valor.valorfireplace.helper.GeneralHelper;
import com.awrtechnologies.valor.valorfireplace.helper.TouchImageView;
import com.awrtechnologies.valor.valorfireplace.helper.ZoomView;
import com.awrtechnologies.valor.valorfireplace.hlistview.widget.AdapterView;
import com.awrtechnologies.valor.valorfireplace.hlistview.widget.HListView;
import com.awrtechnologies.valor.valorfireplace.uicomponents.PreferencesManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.ExtraKeys;

public class DesignValorFirePlace extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {



    private static final int REQUEST_CAMERA = 10;
    private static final int SELECT_FILE = 20;
    Point point;

//    ZoomControls zoom;

    Button btnCamera;
    Button btnGallery;
    Button btnFinish;
    Button btnBack;
    Button btnNav;
    Button btnChooseModel;

    TextView userName;
    ImageView userImg;

    ImageView imgValorTopicon;
    TextView txtStep1;
    TextView txtStep1Name;
    TextView txtStep2;
    TextView txtStep2Name;

    ImageView imgdownArrow;

    RelativeLayout relativeImageParent;
    LinearLayout linearTopBar;
    RelativeLayout relativeFrame;
    RelativeLayout relativeTitleBar;
    LinearLayout linearNavigatiion;

    ListView listView;
    ListviewAdapterFA listviewAdapterFA;
    ArrayList<SeriesDetail> mSeriesDetails = new ArrayList<>();

    HListView featuredHlistView;
    List<Addons> hList;
    HlistViewAdapter hlistViewAdapter;

    public File imageFilePath;
    public Uri imageFileUri;
    private ImageLoader mLoader;

    DrawerLayout dL;
    ListView navListview;

    ArrayList<NavData> mNavDatas = new ArrayList<>();
    NavAdapter navAdapter;
    String[] textArray = {"HELP", "LOGIN"};
    LinearLayout menuNav;
    String[] textArrayLogout = {"HELP", "LOGOUT"};

    int[] imageArray = {R.drawable.question_mark, R.drawable.person_icon};

    int[] imageArrayLogout = {R.drawable.question_mark, R.drawable.logout};


    private String mSelectedModelId, mSeriesid, appid;

    Addons addons;
    SeriesDetail seriesdetail;
    String pathName;

    Dialog dialog;


    String imagepath;

    String ratioCode;


    float screenPoxX;
    float screenPosY;


    float heightImgBackground;
    float widthImgBackground;
    TextView txtSteps3Name;


    List<AddonsCategory> mAddonsCategories = new ArrayList<>();
    String categoryid;
    LinearLayout ll;

    ArrayList<View> viewList;
    ArrayList<SelectedAddonData> mSelectedAddonDatas = new ArrayList<>();
    ArrayList<SelectedAddonData> mSelectedAddonDatasFilter = new ArrayList<>();

    private ImageView backgroundImageView;
    private TouchImageView fireplaceImageView;
    private ProgressDialog progressDialog;
    //private TouchImageView addonImageView;


    boolean checkcategory = false;

    int getTag;

    ListView listViewFooter;

    LinearLayout linearLayoutCategoryview;
    LinearLayout addCategoryview;
    ArrayList<View> categoryViewList;
    List<ModelsData> modelsDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_design_valor_fire_place);

        initialise();
        applyOnclickListeners();
        PreferencesManager.setPreferenceBooleanByKey(DesignValorFirePlace.this, "IsLogout", false);

       // point = GeneralHelper.getInstance(this).getScreenSize();
        mLoader = ImageLoader.getInstance();
        mSeriesid = getIntent().getStringExtra(ExtraKeys.KEY_SERIES_ID);
        appid = getIntent().getStringExtra(ExtraKeys.KEY_APP_ID);

        modelsDatas = new ArrayList<ModelsData>();
        modelsDatas = ModelsData.getAllBySeriesId(mSeriesid);



        categoryViewList = new ArrayList<>();


        setPositionForProgressArrow();

        openModelChooserDialog(mSeriesid);

        if (User.getUser() == null) {
            navigationLogin();

        } else {
            navigationLogout();
            User user = User.getUser();
            userName.setText(user.getName());
            mLoader.displayImage(user.getImage(), userImg);
        }
        changeButtonViews(0);

        linearLayoutCategoryview.removeAllViews();




        getParamsOfFireplace();


        File imageFile1 = new File(
                (Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                        + "/.Valor" + ""));
        imageFile1.mkdirs();


        ZoomView zoomView = (ZoomView) findViewById(R.id.valorComponent);
        backgroundImageView = (ImageView) findViewById(R.id.backgroundImageView);
        fireplaceImageView = (TouchImageView) findViewById(R.id.fireplaceImageView);
        // addonImageView = (TouchImageView) findViewById(R.id.addonImageView);
        zoomView.setBackgroundImageView(backgroundImageView);
        zoomView.setFireplaceImageView(fireplaceImageView);
        //  zoomView.setAddonImage(addonImageView);

        //  addonImageView.ignoreBoundries = true;
        fireplaceImageView.setZoom(0.5f);
        fireplaceImageView.setMinZoom(0.4f);
        fireplaceImageView.setMaxZoom(1.2f);


        mLoader.displayImage("assets://wall.jpg", backgroundImageView);


        GeneralHelper.getInstance(DesignValorFirePlace.this).setIschecked(false);
        changeButtonViews(0);
        System.out.println("widthImgBackground===" + widthImgBackground);
        System.out.println("heightImgBackground===" + heightImgBackground);

        viewList = new ArrayList<>();





    }

    private void buildFooterView(View footer) {
        listViewFooter = (ListView) footer.findViewById(R.id.listView);
    }


    public void setFirstCategory() {
        TextView txt = (TextView) viewList.get(0).findViewById(
                R.id.txtCategory);
        txt.setTextColor(getResources().getColor(R.color.white));
        txt.setBackgroundColor(getResources().getColor(R.color.redText));
        getCategoryId(mAddonsCategories.get(0).name, 0);
        featuredHlistView.setOnItemClickListener(this);
    }

    public void getCategoryId(String catgoryName, int position) {


        categoryid = mAddonsCategories.get(position).addoncatid;
        hList = new ArrayList<Addons>();
        hList = Addons.getAllBycategoryId(mAddonsCategories.get(position).addoncatid);
        Log.e("Pawan", "adddonscatgoryidis" + mAddonsCategories.get(position).addoncatid);
        if (hList.size() > 0) {
            hList.add(new Addons());
        }
        featuredHlistView.setBackgroundResource(R.color.white);
        hlistViewAdapter = new HlistViewAdapter(DesignValorFirePlace.this
                , hList);
        featuredHlistView.setAdapter(hlistViewAdapter);
        hlistViewAdapter.setList(hList);
        hlistViewAdapter.notifyDataSetChanged();
    }


    public void initialise() {
        btnCamera = (Button) findViewById(R.id.btnCamera);
        btnGallery = (Button) findViewById(R.id.btnGallery);
        btnFinish = (Button) findViewById(R.id.btnFinish);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnNav = (Button) findViewById(R.id.btnNav);
        btnChooseModel=(Button)findViewById(R.id.btnChangeModel);

        txtSteps3Name = (TextView) findViewById(R.id.txtSteps3Name);


        imgValorTopicon = (ImageView) findViewById(R.id.imgValorTopicon);

        relativeImageParent = (RelativeLayout) findViewById(R.id.relativeImageParent);
        relativeFrame = (RelativeLayout) findViewById(R.id.relativeFrame);
        linearTopBar = (LinearLayout) findViewById(R.id.linearTopBar);
        relativeTitleBar = (RelativeLayout) findViewById(R.id.relativeTitleBar);


        listView = (ListView) findViewById(R.id.listView);
        featuredHlistView = (HListView) findViewById(R.id.featuredHlistView);
        linearNavigatiion = (LinearLayout) findViewById(R.id.linearNavigatiion);

        navListview = (ListView) findViewById(R.id.left_drawer);
        dL = (DrawerLayout) findViewById(R.id.drawer_layout);
        navListview = (ListView) findViewById(R.id.left_drawer);
        menuNav = (LinearLayout) findViewById(R.id.menuNav);
        imgdownArrow = (ImageView) findViewById(R.id.imgdownArrow);
        txtStep1Name = (TextView) findViewById(R.id.txtStep1Name);
        txtStep1 = (TextView) findViewById(R.id.txtStep1);
        txtStep2 = (TextView) findViewById(R.id.txtStep2);
        txtStep2Name = (TextView) findViewById(R.id.txtStep2Name);
        /*txtStep3 = (TextView) findViewById(R.id.txtStep3);
        txtStep3Name = (TextView) findViewById(R.id.txtStep3Name);*/
        userName = (TextView) findViewById(R.id.userName);
        userImg = (ImageView) findViewById(R.id.userImg);
        ll = (LinearLayout) findViewById(R.id.linearLayoutAddview);
        linearLayoutCategoryview = (LinearLayout) findViewById(R.id.linearLayoutCategoryview);
        addCategoryview = (LinearLayout) findViewById(R.id.addCategoryview);

    }


    public void applyOnclickListeners() {
        btnGallery.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        btnNav.setOnClickListener(this);
        btnChooseModel.setOnClickListener(this);
        linearNavigatiion.setOnClickListener(this);
        txtStep1Name.setOnClickListener(this);
        txtStep1.setOnClickListener(this);
        txtStep2.setOnClickListener(this);
        txtStep2Name.setOnClickListener(this);
       /* txtStep3Name.setOnClickListener(this);
        txtSteps3Name.setOnClickListener(this);*/
    }


    public void getParamsOfFireplace() {

        relativeImageParent.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        heightImgBackground = relativeImageParent.getHeight();
                        widthImgBackground = relativeImageParent.getWidth();
                        float result = heightImgBackground / widthImgBackground;
                        System.out.println("getxImgBackground ===" + relativeImageParent.getX());
                        System.out.println("getYImgBackground ===" + relativeImageParent.getY());
                        screenPoxX = relativeImageParent.getX();
                        screenPosY = relativeImageParent.getY();
                        ratioCode = result + "";

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

    public void setPositionForProgressArrow() {

        dL.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnCamera:
                imageOpen();
                break;

            case R.id.btnGallery:
                imageOpenGallery();
                break;

            case R.id.btnBack:
                changeButtonViews(0);
                onBackPressed();
                break;

            case R.id.btnFinish:
                getFirePlaceImage();
                changeButtonViews(1);
                validation();
                for (int i = 0; i < mSelectedAddonDatasFilter.size(); i++) {
                    Log.d("Pawan", "AddonID" + mSelectedAddonDatas.get(i).getAddonsID());
                    Log.d("Pawan", "CatId" + mSelectedAddonDatas.get(i).getAddonCategoryId());
                }
                break;

            case R.id.btnChangeModel:
                openModelChooserDialog(mSeriesid);
                break;

            case R.id.imgCenter:


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

            case R.id.txtStep2Name:
//                onBackPressed();
                Intent a2 = new Intent(DesignValorFirePlace.this, ChooseSeries.class);
                a2.putExtra("APPID", appid);
                a2.addCategory(Intent.CATEGORY_HOME);
                a2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a2);


                break;

            case R.id.txtStep2:
//                onBackPressed();
                Intent a22 = new Intent(DesignValorFirePlace.this, ChooseSeries.class);
                a22.putExtra("APPID", appid);
                a22.addCategory(Intent.CATEGORY_HOME);
                a22.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a22);


                break;

            case R.id.txtStep1:
                Intent a1 = new Intent(DesignValorFirePlace.this, ChooseApplication.class);
                a1.addCategory(Intent.CATEGORY_HOME);
                a1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a1);
                break;

            case R.id.txtStep1Name:
                Intent a11 = new Intent(DesignValorFirePlace.this, ChooseApplication.class);
                a11.addCategory(Intent.CATEGORY_HOME);
                a11.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a11);
                break;
            /*case R.id.txtSteps3Name:
                Intent a31 = new Intent(DesignValorFirePlace.this, ChooseModel.class);
                a31.putExtra("APPID", appid);
                a31.putExtra("MODELID", modelId);
                a31.addCategory(Intent.CATEGORY_HOME);
                a31.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a31);
                break;*/
            case R.id.txtCategory:
//
                getTag = (Integer) v.getTag();
                for (int i = 0; i < viewList.size(); i++) {

                    TextView txt = (TextView) viewList.get(i).findViewById(
                            R.id.txtCategory);
                    if (i == getTag) {
                        checkcategory = true;
                        txt.setTextColor(getResources().getColor(R.color.white));
                        txt.setBackgroundColor(getResources().getColor(R.color.redText));

                    } else {
                        checkcategory = false;
                        txt.setTextColor(getResources()
                                .getColor(R.color.white));
                        txt.setBackgroundColor(getResources().getColor(R.color.black));

                    }

                }
                getCategoryId(mAddonsCategories.get(getTag).name, getTag);
                featuredHlistView.setOnItemClickListener(this);


                break;

            default:
                break;
        }
    }

    public void navigationLogin() {
        for (int i = 0; i < textArray.length; i++) {
            NavData d = new NavData();
            d.setImages(imageArray[i]);
            d.setText(textArray[i]);
            mNavDatas.add(d);
        }
        navAdapter = new NavAdapter(DesignValorFirePlace.this, mNavDatas);
        navListview.setAdapter(navAdapter);
        navListview.setDivider(null);
        navListview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 0:
                        Intent WebViewIntent = new Intent(DesignValorFirePlace.this, WebActivity.class);
                        startActivity(WebViewIntent);
                        dL.closeDrawer(menuNav);
                        break;

                    case 1:
                        Intent loginIntent = new Intent(DesignValorFirePlace.this, SignInActivity.class);
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
            mNavDatas.add(d);
        }
        navAdapter = new NavAdapter(DesignValorFirePlace.this, mNavDatas);
        navListview.setAdapter(navAdapter);
        navListview.setDivider(null);
        navListview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 0:
                        Intent WebViewIntent = new Intent(DesignValorFirePlace.this, WebActivity.class);
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
        dialog = new Dialog(DesignValorFirePlace.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogbox_quatationresult);
        Button ok = (Button) dialog.findViewById(R.id.buttonok);
        Button cancel = (Button) dialog.findViewById(R.id.buttoncancel);
        Button close = (Button) dialog.findViewById(R.id.btnClose);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesManager.setPreferenceBooleanByKey(DesignValorFirePlace.this, "isLogin", false);
                if (listviewAdapterFA != null) {
                    listviewAdapterFA.notifyDataSetChanged();
                }
                new Delete().from(User.class).execute();

                Intent i = new Intent(DesignValorFirePlace.this, DesignValorFirePlace.class);
                i.putExtra("APPID", appid);
                i.putExtra("SERIESID", mSeriesid);

                startActivity(i);
//                dialog.dismiss();
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

    public void imageOpenGallery() {

        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent, "Select File"),
                SELECT_FILE);

    }


    public void imageOpen() {

        File imageFile = new File(
                (Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                        + "/.Valor"
                        + "/"
                        + System.currentTimeMillis() + ".jpeg"));
        System.out.println("REQUEST PATH : "
                + imageFile.getAbsolutePath());
        PreferencesManager
                .setPreferenceByKey(DesignValorFirePlace.this, "IMAGEWWC",
                        imageFile.getAbsolutePath());
        imageFilePath = imageFile;
        imageFileUri = Uri.fromFile(imageFile);
        Intent i = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT,
                imageFileUri);
        startActivityForResult(i, REQUEST_CAMERA);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String imgDecodableString;

            if (requestCode == SELECT_FILE) {
                pathName = "";
                Intent d = data;
                Uri path = data.getData();
                if (Build.VERSION.SDK_INT < 19) {
                    pathName = getRealPathFromURI(this, path);
                } else {
                    pathName = getRealPathFromURIUpdated(this, path);

                }
                System.out.println("selected file");
                System.out.println("path " + pathName);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mLoader.displayImage("file://" + pathName, backgroundImageView);

                System.out.println("On Activity result ====");
                GeneralHelper.getInstance(DesignValorFirePlace.this).setIschecked(true);
                setSeriesdata(mSelectedModelId);

            } else if (requestCode == REQUEST_CAMERA) {

                System.out.println("URI" + imageFileUri);
                imageFilePath = new File(
                        PreferencesManager
                                .getPreferenceByKey(DesignValorFirePlace.this, "IMAGEWWC"));
                System.out.println("camera ");
                System.out.println("path " + imageFilePath.getAbsolutePath());
                String pathName = imageFilePath.getAbsolutePath();


                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mLoader.displayImage("file://" + pathName, backgroundImageView);
                GeneralHelper.getInstance(DesignValorFirePlace.this).setIschecked(true);
                setSeriesdata(mSelectedModelId);

            }
        }

    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getRealPathFromURIUpdated(Context context, Uri contentUri) {

        ParcelFileDescriptor parcelFileDescriptor;
        String path = "";
        try {
            parcelFileDescriptor = getContentResolver().openFileDescriptor(
                    contentUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor
                    .getFileDescriptor();
            path = getImagePath(contentUri);
            parcelFileDescriptor.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }


    public String getImagePath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ",
                new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor
                .getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }


    public void changeButtonViews(int i) {

        if (i == 0) {
            btnBack.setBackgroundResource(R.color.buttonBGred);
            btnBack.setTextColor(getResources().getColor(
                    R.color.white));
            btnFinish.setBackgroundResource(R.color.buttonBGbrown);
            btnFinish.setTextColor(getResources().getColor(
                    R.color.white));
        }
        if (i == 1) {
            btnBack.setBackgroundResource(R.color.buttonBGbrown);
            btnBack.setTextColor(getResources().getColor(
                    R.color.white));
            btnFinish.setBackgroundResource(R.color.buttonBGred);
            btnFinish.setTextColor(getResources().getColor(
                    R.color.white));
        }
    }

    void showProgressDialog() {
        dismissProgressDialog();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    void dismissProgressDialog() {
        try {
            progressDialog.dismiss();
        } catch (Exception e) {

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        if (position == hList.size() - 1) {
            GeneralHelper.getInstance(DesignValorFirePlace.this).setAddoncheck(true);

            PreferencesManager.setPreferenceByKey(DesignValorFirePlace.this, "ADDONS_" + getTag, "0");
            Log.d("Pawan", "ADDON ID ON SELECT NONE==" + PreferencesManager.getPreferenceByKey(DesignValorFirePlace.this, "ADDONS_" + getTag));
            setSeriesdata(mSelectedModelId);


        } else {
            GeneralHelper.getInstance(DesignValorFirePlace.this).setAddoncheck(false);
            PreferencesManager.setPreferenceByKey(DesignValorFirePlace.this, "ADDONS_" + getTag, hList.get(position).addonsid);
            Log.d("Pawan", "ADDON ID ON SELECT==" + PreferencesManager.getPreferenceByKey(DesignValorFirePlace.this, "ADDONS_" + getTag));
        }
        showProgressDialog();
        categorySelectAddonsImage(getTag, position);
        setSeriesdata(mSelectedModelId);
        view.setSelected(true);
    }


    public void setSeriesdata(String modelId) {
        mSeriesDetails = new ArrayList<SeriesDetail>();
        seriesdetail = new SeriesDetail();
//        if(type==1) {
        seriesdetail.setApplicationdesc(Application.getAllByApplicationId(appid).description);
        seriesdetail.setSeriesdesc(Series.getSeriesbySeriesId(mSeriesid).description);
        seriesdetail.setSeriesprice(Series.getSeriesbySeriesId(mSeriesid).price);
        seriesdetail.setSeriestitle(Series.getSeriesbySeriesId(mSeriesid).title);
        seriesdetail.setModeltitle(ModelsData.getModelByModelID(modelId).title);
        seriesdetail.setModeldesc(ModelsData.getModelByModelID(modelId).description);
        seriesdetail.setModelprice(ModelsData.getModelByModelID(modelId).price);
        seriesdetail.setModelstock(ModelsData.getModelByModelID(modelId).stock);
        seriesdetail.setType(1);

        seriesdetail.setBackground("None");
        if (GeneralHelper.getInstance(DesignValorFirePlace.this).ischecked() == true) {
            seriesdetail.setImagecheck(true);
        } else {
            seriesdetail.setImagecheck(true);
        }
        if (GeneralHelper.getInstance(DesignValorFirePlace.this).isAddoncheck() == true) {
            seriesdetail.setAddonscheck(true);
        } else {
            seriesdetail.setAddonscheck(false);
        }
        mSeriesDetails.add(seriesdetail);
        listviewAdapterFA = new ListviewAdapterFA(DesignValorFirePlace.this
                , mSeriesDetails);
        listView.setAdapter(listviewAdapterFA);
        listviewAdapterFA.setList(mSeriesDetails);
        listviewAdapterFA.notifyDataSetChanged();

//        }
//       else if(type==0)
//        {

        for (int i = 0; i < mAddonsCategories.size(); i++) {
            String addonid = PreferencesManager.getPreferenceByKey(DesignValorFirePlace.this, "ADDONS_" + i);
            Log.d("Pawan", "ADDON ID---" + addonid);
            if (!addonid.isEmpty() && !addonid.equals("0")) {
                seriesdetail = new SeriesDetail();
                seriesdetail.setAddonsprice(Addons.getAddonsByAddonsId(addonid).price);
                seriesdetail.setAddonstitle(Addons.getAddonsByAddonsId(addonid).title);
                String catid = Addons.getAddonsByAddonsId(addonid).category_id;
                seriesdetail.setType(0);
                seriesdetail.setCategoryname(AddonsCategory.getCatByCatID(catid).name);
                seriesdetail.setCategoryID(catid);
                seriesdetail.setAddonId(addonid);
                mSeriesDetails.add(seriesdetail);

            }
            listviewAdapterFA = new ListviewAdapterFA(DesignValorFirePlace.this
                    , mSeriesDetails);
            listView.setAdapter(listviewAdapterFA);
            listviewAdapterFA.setList(mSeriesDetails);
            listviewAdapterFA.notifyDataSetChanged();
//            }


        }


    }

    public void validation() {
        if (addons == null && GeneralHelper.getInstance(DesignValorFirePlace.this).ischecked() == false) {
            dialogBoxResult("Addons and FirePlace Background are not selected.Do You Want to Select Both these?");
        } else if (addons == null) {
            dialogBoxResult("Addons is not selected.Do You Want to Select Addons?");
        } else if (GeneralHelper.getInstance(DesignValorFirePlace.this).ischecked() == false) {
            dialogBoxResult("FirePlace Background is not selected.Do You Want to Select Background?");
        } else {
            Intent i = new Intent(DesignValorFirePlace.this, EmailShareDesign.class);
            i.putExtra("SERIESDATA", seriesdetail);
            i.putExtra("SERIESLIST", mSeriesDetails);

            i.putExtra("APPId", appid);
            i.putExtra("SERIESID", mSeriesid);
//            if (addons != null) {
            i.putExtra("ADDONID", addons.addonsid);
            i.putExtra("IMAGEPath", imagepath);
            i.putExtra("ratioCode", ratioCode);
            i.putExtra("ADDONCATID", categoryid);
            i.putExtra("MODELID", mSelectedModelId);

//            }
            startActivity(i);
        }
    }

    public void dialogBoxResult(final String message) {

        dialog = new Dialog(DesignValorFirePlace.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogbox_quatationresult);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final TextView result = (TextView) dialog.findViewById(R.id.textresult);
        result.setText(message);

        Button btnClose = (Button) dialog.findViewById(R.id.btnClose);
        Button ok = (Button) dialog.findViewById(R.id.buttonok);
        Button cancel = (Button) dialog.findViewById(R.id.buttoncancel);

        ok.setOnClickListener(new View.OnClickListener() {
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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(DesignValorFirePlace.this, EmailShareDesign.class);
                i.putExtra("SERIESDATA", seriesdetail);
                i.putExtra("APPId", appid);
                i.putExtra("SERIESLIST", mSeriesDetails);
                i.putExtra("SERIESID", mSeriesid);
                i.putExtra("IMAGEPath", imagepath);
                i.putExtra("ADDONCATID", categoryid);
                i.putExtra("MODELID", mSelectedModelId);
                if (addons != null) {
                    i.putExtra("ADDONID", addons.addonsid);
                }
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                dialog.dismiss();

            }
        });

        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }


    public void getFirePlaceImage() {
        View u = findViewById(R.id.relativeImageParent);
        btnCamera.setVisibility(View.GONE);
        btnGallery.setVisibility(View.GONE);
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
        File myPath = new File(extr, fileName);

        imagepath = myPath + "";


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

        btnCamera.setVisibility(View.VISIBLE);
        btnGallery.setVisibility(View.VISIBLE);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();  // optional depending on your needs
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    @Override
    public void onResume() {

        if (User.getUser() != null && PreferencesManager.getPreferenceBooleanByKey(DesignValorFirePlace.this, "isLogin") == true) {
            mNavDatas.clear();
            navigationLogout();
            User user = User.getUser();
            userName.setText(user.getName());
            mLoader.displayImage(user.getImage(), userImg);

        } else {
            userName.setText("LOGIN");
            mLoader.displayImage("assets://person_icon.png", userImg);
            mNavDatas.clear();
            navigationLogin();
        }

        if (listviewAdapterFA != null) {
            listviewAdapterFA.notifyDataSetChanged();
        }
        super.onResume();
        changeButtonViews(0);


    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            getFirePlaceImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        System.out.println("onStop");
        super.onStop();
    }



    public class ModelTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            openModelChooserDialog(mSeriesid);
        }
    }



    private void openModelChooserDialog(final String seriesId) {

        GridModelAdapter gridModelAdapter;
        final Dialog modelDialog = new Dialog(this);
        modelDialog.setTitle("Choose Model");
        View view = getLayoutInflater().inflate(R.layout.custom_model_chooser_dialog, null);

        GridView modelGridView = (GridView) view.findViewById(R.id.model_gridview);
        if(ModelsData.getModelStock().stock==0||ModelsData.getModelStock().stock==-1) {
        }
        else {

            gridModelAdapter = new GridModelAdapter(this, modelsDatas);
            modelGridView.setAdapter(gridModelAdapter);
        }


        modelGridView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {

                mSelectedModelId = modelsDatas.get(position).modelId;
                mAddonsCategories = AddonsCategory.getAllBymodelId(mSelectedModelId);
                setSeriesdata(mSelectedModelId);
                for (int i = 0; i < mAddonsCategories.size(); i++) {
                    PreferencesManager.setPreferenceByKey(DesignValorFirePlace.this, "ADDONS_" + i, "0");

                }

                mLoader.displayImage(ModelsData.getModelByModelID(mSelectedModelId).image, fireplaceImageView);

                for (int i = 0; i < mAddonsCategories.size(); i++) {

                    List<AddonsCategory> addonscatlist = new ArrayList<AddonsCategory>();
                    if (Addons.getAddonsBycategoryId(mSelectedModelId) != null) {
                        addonscatlist = AddonsCategory.getAllBymodelId(mSelectedModelId);
                        View addView = LayoutInflater.from(DesignValorFirePlace.this).inflate(
                                R.layout.addview, null);
                        final TextView text = (TextView) addView
                                .findViewById(R.id.txtCategory);


                        text.setText(addonscatlist.get(i).name);
                        text.setTag(i);

                        text.setOnClickListener(DesignValorFirePlace.this);

                        ll.addView(addView);
                        viewList.add(addView);
                    }

                }
                setFirstCategory();
                modelImage();
                modelDialog.dismiss();

            }
        });

        modelDialog.setContentView(view);
        modelDialog.show();
    }


    public void modelImage() {


        String extStorageDirectory = Environment.getExternalStorageDirectory()
                .toString();
        final File folder = new File(extStorageDirectory, "/." + Constants.APPNAME + "/png");

        folder.mkdirs();

        final String imageurl = ModelsData.getModelByModelID(mSelectedModelId).image;
        Log.d("Pawan", "modelimagpathis" + imageurl);

        ImageLoader.getInstance().loadImage(imageurl, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                showProgressDialog();
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                dismissProgressDialog();
            }

            @Override
            public void onLoadingComplete(String s, View view, final Bitmap bitmap) {
                File newFile = new File(folder, "model.png");
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(newFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                    fos.flush();
                    fos.close();
                    bitmap.recycle();
                    dismissProgressDialog();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    dismissProgressDialog();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    dismissProgressDialog();
                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                dismissProgressDialog();
            }
        });

    }


    public void categorySelectAddonsImage(final int categorytag, final int position) {

        if (position == hList.size() - 1) {
            GeneralHelper.getInstance(DesignValorFirePlace.this).setAddoncheck(true);
//            PreferencesManager.setPreferenceByKey(DesignValorFirePlace.this, "ADDONS_" + getTag, "0");
        } else {
            GeneralHelper.getInstance(DesignValorFirePlace.this).setAddoncheck(false);
        }

        String extStorageDirectory = Environment.getExternalStorageDirectory()
                .toString();
        final File folder = new File(extStorageDirectory, "/." + Constants.APPNAME + "/AddonImage");

        folder.mkdirs();
        String imageurl = hList.get(position).image;
        if (position == hList.size() - 1) {
            File newFile = new File(folder, "addon" + categorytag + ".png");
            newFile.delete();
            PreferencesManager.setPreferenceByKey(DesignValorFirePlace.this, "ADDONS_" + getTag, "0");
            BitmapMergerTask mBitmapMergerTask = new BitmapMergerTask(mAddonsCategories.size());


            mBitmapMergerTask.setMergeListener(new BitmapMergerTask.OnMergeListener() {
                @Override
                public void onMerge(BitmapMergerTask task, Bitmap mergedBitmap) {
                    fireplaceImageView.setImageBitmap(mergedBitmap);
                }
            })
                    .setScale(1.0f)
                    .merge();
            dismissProgressDialog();
        } else {
            ImageLoader.getInstance().loadImage(hList.get(position).image, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    showProgressDialog();
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    dismissProgressDialog();
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    File newFile = new File(folder, "addon" + categorytag + ".png");
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(newFile);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                        fos.flush();
                        fos.close();
                        BitmapMergerTask mBitmapMergerTask = new BitmapMergerTask(mAddonsCategories.size());


                        mBitmapMergerTask.setMergeListener(new BitmapMergerTask.OnMergeListener() {
                            @Override
                            public void onMerge(BitmapMergerTask task, Bitmap mergedBitmap) {
                                fireplaceImageView.setImageBitmap(mergedBitmap);
                            }
                        })
                                .setScale(1.0f)
                                .merge();

                        addons = hList.get(position);
//                        PreferencesManager.setPreferenceByKey(DesignValorFirePlace.this, "ADDONS_" + getTag, hList.get(position).addonsid);
                        dismissProgressDialog();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    dismissProgressDialog();
                }
            });
        }
    }

}
