package com.sanket.itunespodcast;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class GetITunesPodcastAsyncTask extends AsyncTask<String, Void, ArrayList<Podcast>> {
    private ProgressDialog pd;
    private IPodcastActivity podcastActivity = null;


    public GetITunesPodcastAsyncTask(IPodcastActivity podcastActivity) {
        this.podcastActivity = podcastActivity;
    }

    @Override
    protected ArrayList<Podcast> doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            URLConnection con = url.openConnection();
            InputStream in = con.getInputStream();
            if (in != null) {
                return PodcastUtil.PodcastPullParser.parseNews(in);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        podcastActivity.showProgressDialog();

    }

    @Override
    protected void onPostExecute(ArrayList<Podcast> podcasts) {
        super.onPostExecute(podcasts);
        podcastActivity.stopProgress();
        podcastActivity.getPodcastList(podcasts);
    }

    static public interface IPodcastActivity {
        public void showProgressDialog();

        public void stopProgress();

        public void getPodcastList(ArrayList<Podcast> podcasts);

    }
}
