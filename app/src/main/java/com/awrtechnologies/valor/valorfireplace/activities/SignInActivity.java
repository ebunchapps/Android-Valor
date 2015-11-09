package com.awrtechnologies.valor.valorfireplace.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.awrtechnologies.valor.valorfireplace.Data.Application;
import com.awrtechnologies.valor.valorfireplace.Data.Series;
import com.awrtechnologies.valor.valorfireplace.Data.User;
import com.awrtechnologies.valor.valorfireplace.R;
import com.awrtechnologies.valor.valorfireplace.apiconstants.Constants;
import com.awrtechnologies.valor.valorfireplace.helper.GeneralHelper;
import com.awrtechnologies.valor.valorfireplace.uicomponents.PreferencesManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends Activity implements View.OnClickListener {

    LinearLayout linearedittext;
    EditText email;
    EditText password;
    Button login;

    String emailtext;
    String passwordtext;
    Point point;
    Application app;
    Series series;
    Button btnClose;
    String applicationId;
    String appid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = this.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        point = GeneralHelper.getInstance(SignInActivity.this).getScreenSize();
        findviewbyid();
        setLinearlayoutsize(linearedittext);
        app = new Application();
        series = new Series();
        login.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        applicationId = getIntent().getStringExtra("APPId");
        appid = getIntent().getStringExtra("APPID");
        Log.e("Rakhi", "appidis" + appid);


    }

    public void findviewbyid() {
        linearedittext = (LinearLayout) findViewById(R.id.linearedittext);
        email = (EditText) findViewById(R.id.edittextemail);
        password = (EditText) findViewById(R.id.edittextpassword);
        login = (Button) findViewById(R.id.buttonlogin);
        btnClose = (Button) findViewById(R.id.btnClose);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonlogin:
                emailtext = email.getText().toString();
                passwordtext = password.getText().toString();
                if (email.getText().toString().isEmpty()
                        && password.getText().toString().isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Insert the details first",
                            Toast.LENGTH_LONG).show();
                } else if (!emailValidator(email.getText().toString())) {
                    Toast.makeText(SignInActivity.this, "Insert a valid email",
                            Toast.LENGTH_LONG).show();
                } else {
                    //call api
                    new Task().execute();

                    emptyTextFields();

                }
                break;
            case R.id.btnClose:
                onBackPressed();
                break;
        }

    }

    public void setLinearlayoutsize(LinearLayout layout) {
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) layout.getLayoutParams();
        lp.width = point.x / 3;

        layout.setLayoutParams(lp);
    }


    // Validation of the Email String

    public boolean emailValidator(String email) {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    public class Task extends AsyncTask<Void, Void, String> {

        ProgressDialog mProgressDialog;


        @Override
        protected String doInBackground(Void... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(Constants.BASEURL
                        + "login");
                ArrayList<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();

                BasicNameValuePair pasrd1 = new BasicNameValuePair("email",
                        emailtext);
                param.add(pasrd1);
                BasicNameValuePair pasrd2 = new BasicNameValuePair("password",
                        passwordtext);
                param.add(pasrd2);

                httppost.setEntity(new UrlEncodedFormEntity(param));
                HttpResponse httpresponse = httpclient.execute(httppost);

                InputStream inputstream = httpresponse.getEntity().getContent();

                String result = convertStreamToString(inputstream);

                System.out.println("result========" + result);

                return result;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(SignInActivity.this);
            mProgressDialog.setMessage("Loading..");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            mProgressDialog.dismiss();
            try {

                ActiveAndroid.beginTransaction();
                JSONObject object = new JSONObject(result);
                int responsecode = object.getInt("result_code");
                if (responsecode == 1) {
                    JSONObject userobject = object.getJSONObject("data");

                    User user = new User();
                    user.setDealerId(userobject.getString("dealer_id"));
                    user.setEmail(userobject.getString("email"));
                    user.setUserid(userobject.getString("salesman_id"));
                    user.setName(userobject.getString("salesman_name"));
                    user.save();
                    PreferencesManager.setPreferenceBooleanByKey(SignInActivity.this, "isLogin", true);
                    finish();
//                        user.setAddress(userobj.getString("address"));
//                        user.setCity(userobj.getString("city"));
//                        user.setContact(userobj.getString("contact"));
//                        user.setCountry(userobj.getString("country"));

//                        user.setFees(userobj.getString("fees"));

//                        user.setImage(userobj.getString("image"));

//                        user.setSecurityNo(userobj.getString("securityNo"));



//                        new GetAllTask().execute();

//                        if(PreferencesManager.getPreferenceBooleanByKey(SignInActivity.this,"chooseapplogin")==true) {
//                            Intent intent = new Intent(SignInActivity.this, ChooseApplication.class);
//                            startActivity(intent);
//                        }
//                        else if(PreferencesManager.getPreferenceBooleanByKey(SignInActivity.this,"chooseserlogin")==true)
//                        {
//                            Intent intent = new Intent(SignInActivity.this, ChooseSeries.class);
//                            intent.putExtra("APPID", appid);
//                            startActivity(intent);
//                        }
//                        else if(PreferencesManager.getPreferenceBooleanByKey(SignInActivity.this,"designvalorlogin")==true)
//                        {
//                            Intent intent = new Intent(SignInActivity.this, DesignValorFirePlace.class);
//                            startActivity(intent);
//                        }
//                        else if(PreferencesManager.getPreferenceBooleanByKey(SignInActivity.this,"emailsharelogin")==true)
//                        {
//                            Intent intent = new Intent(SignInActivity.this, EmailShareDesign.class);
//                            startActivity(intent);
//                        }
//                        overridePendingTransition(R.anim.fade_in, R.anim.fade_in);


                } else if (responsecode == 0) {
                    Toast.makeText(SignInActivity.this, object.getString("message"), Toast.LENGTH_LONG);

                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {

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


    public void emptyTextFields() {

        email.setText("");
        password.setText("");
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
