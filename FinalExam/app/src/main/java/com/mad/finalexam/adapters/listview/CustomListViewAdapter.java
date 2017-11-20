package com.mad.finalexam.adapters.listview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.mad.finalexam.R;
import com.mad.finalexam.model.AppItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Sanket on 10/20/2016.
 */

public class CustomListViewAdapter  extends ArrayAdapter<AppItem> {

    private ArrayList<AppItem> items;
    private Context mContext;
    public CustomListViewAdapter(Context context, ArrayList<AppItem> items) {
        super(context, R.layout.list_layout, items);
        this.mContext=context;
        this.items=items;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_layout, parent, false);
            holder = new ViewHolder();


            holder.textViewName = (TextView)convertView.findViewById(R.id.textViewName);
            holder.textViewPrice = (TextView)convertView.findViewById(R.id.textViewPrice);
            holder.imageViewIcon = (ImageView) convertView.findViewById(R.id.imageViewSmallSrc);
            holder.imageViewRatings = (ImageView) convertView.findViewById(R.id.imageViewDollarRatings);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();

        AppItem item = items.get(position);

        TextView name = holder.textViewName;
        TextView price =holder.textViewPrice;
        ImageView icon = holder.imageViewIcon;
        ImageView dollarRating = holder.imageViewRatings;

        name.setText(item.getName());
        price.setText("Price: USD $"+ item.getPrice());
        Picasso.with(mContext).load(item.getImageSrc()).centerCrop().resize(200,200).into(icon);



        return  convertView;
    }

    private class ViewHolder
    {

        TextView textViewName;
        TextView textViewPrice;
        ImageView imageViewIcon;
        ImageView imageViewRatings;
    }
}
