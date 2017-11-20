package com.sanket.itunespodcast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PodcastDetail extends AppCompatActivity {

    private Podcast podcast;
    private TextView textViewTitle;
    private TextView textViewDate;
    private TextView textViewDescription;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_detail);
        podcast = (Podcast) getIntent().getSerializableExtra("podcast");

        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        imageView = (ImageView) findViewById(R.id.imageViewImage);

        textViewTitle.setText(podcast.getTitle());
        textViewDate.setText(podcast.getUpdatedDate().toString());
        textViewDescription.setText(podcast.getPodcastDescription());

        Picasso.with(this)
                .load(podcast.getLargeImageUrl())
                .into(imageView);
    }
}
