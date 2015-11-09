package com.awrtechnologies.valor.valorfireplace.activities;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.awrtechnologies.valor.valorfireplace.R;

/**
 * Created by awr001 on 27/07/15.
 */
public class LocateDealerWebview extends Activity {

    WebView web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        String newUrl = "http://valorfireplaces.com/contact/";

        web = (WebView) findViewById(R.id.web);
        web.getSettings().setJavaScriptEnabled(true);

        web.setWebViewClient(new WebViewClient());

        web.loadUrl(newUrl);
    }}
