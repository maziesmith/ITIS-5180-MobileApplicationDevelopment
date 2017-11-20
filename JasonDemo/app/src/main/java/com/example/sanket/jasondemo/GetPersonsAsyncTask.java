package com.example.sanket.jasondemo;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sanket on 9/19/16.
 */
public class GetPersonsAsyncTask extends AsyncTask<String, Void, ArrayList<Person>>{

    @Override
    protected ArrayList<Person> doInBackground(String... params) {

        BufferedReader reader = null;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statusCode = con.getResponseCode();
            if(statusCode==HttpURLConnection.HTTP_OK)
            {
                reader = new BufferedReader( new InputStreamReader(con.getInputStream()));

                StringBuilder sb = new StringBuilder();

                String line = reader.readLine();

                while (line !=null)
                {
                    sb.append(line);
                    line=reader.readLine();
                }

                return PersonsUtil.PersonJSONParser.pasrsePersons(sb.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(ArrayList<Person> result) {
        super.onPostExecute(result);

        if(result !=null)
        {
            Log.d("demo", result.toString());
        }
    }
}
