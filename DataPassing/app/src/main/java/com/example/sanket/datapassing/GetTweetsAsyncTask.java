package com.example.sanket.datapassing;

import android.content.Context;
import android.os.AsyncTask;

import java.util.LinkedList;

/**
 * Created by sanket on 9/19/16.
 */
public class GetTweetsAsyncTask extends AsyncTask <String, Void, LinkedList<String>> {

    IData activity;

    public GetTweetsAsyncTask(IData activity) {
        this.activity=activity;
    }

    @Override
    protected void onPostExecute(LinkedList<String> strings) {
        activity.setupData(strings);
        super.onPostExecute(strings);

    }

    @Override
    protected LinkedList<String> doInBackground(String... params) {
        LinkedList<String> tweets = new LinkedList<String>();

        tweets.add("Tweet 0");
        tweets.add("Tweet 1");
        tweets.add("Tweet 2");
        tweets.add("Tweet 3");


        return tweets;
    }

    static public interface IData{
        public void setupData(LinkedList<String>result);
        public Context getContext();
    }
}
