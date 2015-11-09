package com.awrtechnologies.valor.valorfireplace.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.awrtechnologies.valor.valorfireplace.activities.PDFViewer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by awr001 on 27/07/15.
 */
public class PDFHelper {
    private Context context;
    private File fileToSave;
    private ProgressDialog progressDialog;
    public static PDFHelper getIntance(Context context) {
        PDFHelper _self = new PDFHelper();
        _self.context = context;
        return _self;
    }

    public void openPdf(final String url, final File fileToSave,final String Title) {
        if (fileToSave.exists()) {

            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent(context, PDFViewer.class);
                    intent.putExtra("filename", fileToSave.getAbsolutePath());
                    intent.putExtra("title", Title);
                    context.startActivity(intent);
                }
            });
        }else {
            downloadFile(url, fileToSave);
        }
    }

    void downloadFile(String url, File fileToSave) {
        this.fileToSave = fileToSave;
        new DownloadTask().execute(url);
    }


    void showProgressDialog(){
        dissmissProgressDialog();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...\nDownloading PDF File");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    void dissmissProgressDialog(){
        try{
            progressDialog.dismiss();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    class DownloadTask extends AsyncTask<String, Void, String> {



        @Override
        protected void onPreExecute() {
            showProgressDialog();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String path) {
            dissmissProgressDialog();
            if(path!=null){
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(context, PDFViewer.class);
                        intent.putExtra("filename", fileToSave.getAbsolutePath());
                        context.startActivity(intent);
                    }
                });
            }
            super.onPostExecute(path);
        }

        @Override
        protected String doInBackground(String... params) {
            String s_url = params[0];
            int count;
            try {

                URL url = new URL(s_url);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(fileToSave.getAbsolutePath());

                byte data[] = new byte[1024];

                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
                return fileToSave.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
                fileToSave.delete();
            }
            return null;
        }
    }
}

