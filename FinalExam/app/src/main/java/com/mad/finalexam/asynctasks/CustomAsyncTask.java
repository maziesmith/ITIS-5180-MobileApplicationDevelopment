package com.mad.finalexam.asynctasks;

import android.os.AsyncTask;


import com.mad.finalexam.jsonparser.ItemPareser;
import com.mad.finalexam.model.AppItem;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Sanket on 10/22/2016.
 */

public class CustomAsyncTask extends AsyncTask<String, Void, ArrayList<AppItem>> {


    private IData activity;

    public CustomAsyncTask(IData activity) {
        this.activity = activity;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.startFetchData();
    }

    @Override
    protected void onPostExecute(ArrayList<AppItem> items) {
        super.onPostExecute(items);
        activity.setupData(items);
    }

    @Override
    protected ArrayList<AppItem> doInBackground(String... params) {
        //TODO: write main logic for do in background

        BufferedReader reader = null;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statusCode = con.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                StringBuilder sb = new StringBuilder();

                String line = reader.readLine();

                while (line != null) {
                    sb.append(line);
                    line = reader.readLine();
                }

                return ItemPareser.parseItem(sb.toString());
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface IData {
        void startFetchData();
        void setupData(ArrayList<AppItem> appItems);
    }
}
