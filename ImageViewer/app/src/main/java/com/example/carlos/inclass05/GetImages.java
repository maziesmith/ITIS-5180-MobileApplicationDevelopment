package com.example.carlos.inclass05;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.BreakIterator;

/**
 * Created by Carlos on 19-Sep-16.
 */
public class GetImages extends AsyncTask<String, Void, Bitmap> {

    MainActivity currentActivity;


    public GetImages(MainActivity currentActivity) {
        this.currentActivity = currentActivity;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        try
        {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            Bitmap image = BitmapFactory.decodeStream(con.getInputStream());
            return image;
        }
        catch (Exception ex)
        {

        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        currentActivity.adLoadingPictures.show();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        currentActivity.adLoadingPictures.dismiss();
        currentActivity.ivPictures.setImageBitmap(bitmap);
    }
}
