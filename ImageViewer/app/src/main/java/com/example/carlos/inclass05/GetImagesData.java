package com.example.carlos.inclass05;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Carlos on 19-Sep-16.
 */
public class GetImagesData extends AsyncTask<String, Void, String> {

    final String api = "http://dev.theappsdr.com/apis/photos/index.php?keyword=";
    MainActivity currentActivity;

    public GetImagesData(MainActivity currentActivity) {
        this.currentActivity = currentActivity;
    }

    @Override
    protected String doInBackground(String... params) {

        BufferedReader reader = null;

        try
        {

            URL url = new URL(api + params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
            return sb.toString();
        }
        catch (Exception ex)
        {

        }
        finally
        {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        currentActivity.adLoading.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        currentActivity.adLoading.dismiss();
        //currentActivity.currentResult = s;
        //Log.d("debug", "Data retrieved: " + s.toString());
        //currentActivity.getImagesFromDictionary();
        currentActivity.getImagesFromDictionary(s);
    }

    static public interface IData
    {
        public void getImagesFromDictionary(String result);
    }
}
