package com.awrtechnologies.valor.valorfireplace.activities;

import android.app.Activity;
import android.os.Bundle;

import com.awrtechnologies.valor.valorfireplace.R;
import com.joanzapata.pdfview.PDFView;

import java.io.File;

/**
 * Created by awr001 on 27/07/15.
 */
public class PDFViewer extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        setTitle(getIntent().getExtras().getString("title"));
        PDFView pdfview = (PDFView) findViewById(R.id.pdfview);
        String filename = getIntent().getExtras().getString("filename");
        pdfview.fromFile(new File(filename)).defaultPage(1)
                .showMinimap(true)
                .enableSwipe(true)
                .load();
    }
}