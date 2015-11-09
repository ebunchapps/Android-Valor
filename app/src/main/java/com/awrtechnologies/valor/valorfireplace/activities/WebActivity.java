package com.awrtechnologies.valor.valorfireplace.activities;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.awrtechnologies.valor.valorfireplace.R;

/**
 * Created by awr001 on 25/07/15.
 */
public class WebActivity  extends Activity {

    WebView web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        String newUrl = "http://valorfireplaces.com/about/";

        web = (WebView) findViewById(R.id.web);
        web.getSettings().setJavaScriptEnabled(true);

        web.setWebViewClient(new WebViewClient());

        web.loadUrl(newUrl);
    }}

