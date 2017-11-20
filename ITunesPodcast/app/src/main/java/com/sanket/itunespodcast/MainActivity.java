package com.sanket.itunespodcast;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetITunesPodcastAsyncTask.IPodcastActivity {

    private ProgressDialog progressDialog;
    private ArrayList<Podcast> podcastList = new ArrayList<Podcast>();
    PodcastAdapter adapter;
    EditText searchEditText;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = (EditText) findViewById(R.id.editTextSearch);
        listView = (ListView) findViewById(R.id.listViewPodcast);

        if (isConnectedOnline()) {
            new GetITunesPodcastAsyncTask(MainActivity.this).execute("https://itunes.apple.com/us/rss/toppodcasts/limit=30/xml");
        } else {
            Toast.makeText(MainActivity.this, "Check your internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isConnectedOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void showProgressDialog() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading News");
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    public void stopProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void getPodcastList(ArrayList<Podcast> podcasts) {
        podcastList.addAll(podcasts);

        displayList(podcastList);

    }


    private void displayList(final ArrayList<Podcast> podcastList) {


        adapter = new PodcastAdapter(this, R.layout.podcast_feed, podcastList);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, PodcastDetail.class);
                intent.putExtra("podcast", podcastList.get(position));
                startActivity(intent);
                //Toast.makeText(MainActivity.this, "Postion " + position + " Value " + colors.get(position), Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void actionClear(View view) {
        searchEditText.setText("");
        if (!podcastList.isEmpty()) {
            displayList(podcastList);
        }
    }

    public void actionGo(View view) {
        String searchString = searchEditText.getText().toString();
        if (!podcastList.isEmpty() && searchString != null) {
            ArrayList<Podcast> tempList = new ArrayList<Podcast>();
            ArrayList<Podcast> tempOriginal = new ArrayList<Podcast>();
            tempOriginal.addAll(podcastList);

            int textlength = searchString.length();
            tempList.clear();
            for (int i = 0; i < podcastList.size(); i++) {
                if (textlength <= podcastList.get(i).getTitle().length()) {
                   //indexOf(needle) != -1
                   //                    if (searchString.equalsIgnoreCase((String) podcastList.get(i).getTitle().subSequence(0,textlength))) {

                    if ((podcastList.get(i).getTitle().toLowerCase()).indexOf(searchString.toLowerCase())!=-1) {
                        tempList.add(podcastList.get(i));
                    }
                }
            }

            int position=0;
            int positon1=tempList.size();
            for (Podcast temp : tempList) {
                for (Podcast p : podcastList) {
                    if (temp.getTitle().equals(p.getTitle()))
                    {
                        Podcast tempP=p;
                        tempP.setIsGreen(true);
                        tempOriginal.add(position,tempP);
                        position++;
                    }else
                    {
                        tempOriginal.add(positon1,p);
                    }
                }
            }

            listView.setAdapter(new PodcastAdapter(this, R.layout.podcast_feed, tempOriginal));
        }

    }
}
