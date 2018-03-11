package com.techgaurav.practiceapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by gaurav on 3/3/18.
 */

public class BackgroundHelper extends AsyncTask<String, Void, String> {

    public static String photouploadurl = "http://searchkero.com/phototest/samana.php";
    String encodedstring, filename;
    AlertDialog alertDialog;
    Context mycontext;
    ProgressDialog pd;

    public BackgroundHelper(Context mycontext) {
        this.mycontext = mycontext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        alertDialog = new AlertDialog.Builder(mycontext).create();
        alertDialog.setTitle("Photo Upload status");
        pd = new ProgressDialog(mycontext);
        pd.setMessage("loading");
        pd.show();
    }


    @Override
    protected String doInBackground(String... params) {

        encodedstring = params[0];
        filename = params[1];
        try {
            URL url = new URL(photouploadurl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String post_data = URLEncoder.encode("encoded_string", "UTF-8") + "=" + URLEncoder.encode(encodedstring, "UTF-8") + "&" +
                    URLEncoder.encode("image_name", "UTF-8") + "=" + URLEncoder.encode(filename, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        pd.dismiss();
        alertDialog.setMessage(result);
        alertDialog.setMessage("Success");
        alertDialog.setCancelable(true);
        alertDialog.show();
    }
}
