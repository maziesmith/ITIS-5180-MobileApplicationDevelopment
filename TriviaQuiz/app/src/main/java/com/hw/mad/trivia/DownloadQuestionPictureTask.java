package com.hw.mad.trivia;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * DownloadQuestionPictureTask.java
 * Homework 03
 * Sanket Patil
 * Atul Kumar Banwar
 */

public class DownloadQuestionPictureTask extends AsyncTask<String, Void, Bitmap> {
    IQuestionPicture activity;
    int questionIndex = 0;

    public DownloadQuestionPictureTask(IQuestionPicture activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.startProgress();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        activity.setupData(bitmap, questionIndex);
        activity.stopProgress(questionIndex);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            questionIndex = Integer.valueOf(params[1]);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            return BitmapFactory.decodeStream(con.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    static public interface IQuestionPicture {
        public void setupData(Bitmap image, int questionIndex);

        public void startProgress();

        public void stopProgress(int questionIndex);
    }
}
