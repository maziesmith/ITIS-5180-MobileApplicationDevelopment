package com.sanket.itunespodcast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanket on 10/3/16.
 */
public class PodcastAdapter extends ArrayAdapter<Podcast> {

    List<Podcast> mData;
    Context mContext;
    int mResource;

    public PodcastAdapter(Context context, int resource, List<Podcast> objects) {
        super(context, resource, objects);
        this.mData=objects;
        this.mContext=context;
        this.mResource=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.imageViewThumbnail);
            viewHolder.podcastTitle = (TextView) convertView.findViewById(R.id.textViewNews);
            convertView.setTag(viewHolder);
        }

        Podcast podcast= mData.get(position);

        viewHolder = (ViewHolder) convertView.getTag();
        ImageView thumbnailImageView= viewHolder.thumbnail;
        thumbnailImageView.setMaxWidth(600);
        thumbnailImageView.setMaxHeight(600);
        TextView titleTextView = viewHolder.podcastTitle;

        titleTextView.setText(podcast.getTitle());
        Picasso.with(mContext)
                .load(podcast.getLargeImageUrl())
                .into(thumbnailImageView);

        if(podcast.isGreen())
        {
            convertView.setBackgroundColor(android.graphics.Color.GREEN);
        }
        return convertView;
    }

    static class ViewHolder{
        ImageView thumbnail;
        TextView podcastTitle;
    }
}
