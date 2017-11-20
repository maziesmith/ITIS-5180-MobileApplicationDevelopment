package com.hw.mad.trivia;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * GetTriviaData.java
 * Homework 03
 * Sanket Patil
 * Atul Kumar Banwar
 */

public class GetTriviaData extends AsyncTask<String, Void, ArrayList<Question>> {

    IData activity;
    ProgressDialog pgDiagLoading;

    public GetTriviaData(IData activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<Question> doInBackground(String... params) {
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
                return QuestionJSONParser.pasrseQuestions(sb.toString());
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
    protected void onPostExecute(ArrayList<Question> s) {
        super.onPostExecute(s);
        activity.setupData(s);
    }

    static public interface IData {
        public void setupData(ArrayList<Question> result);
    }
}
