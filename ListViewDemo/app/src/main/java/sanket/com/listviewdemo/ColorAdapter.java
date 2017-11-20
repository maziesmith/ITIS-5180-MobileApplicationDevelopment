package sanket.com.listviewdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import sanket.com.listviewdemo.Color;

import java.util.List;

/**
 * Created by sanket on 10/3/16.
 */
public class ColorAdapter extends ArrayAdapter<Color> {

    List<Color> mData;
    Context mContext;
    int mResource;

    public ColorAdapter(Context context, int resource, List<Color> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mData = objects;
        this.mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /*ConvertView: round */
        //TODO: without perfromance
       /* if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }

        Color color = mData.get(position);
        TextView colorNameTextView = (TextView) convertView.findViewById(R.id.textViewColorName);
        TextView colorHexTextView = (TextView) convertView.findViewById(R.id.textViewColorHex);

        colorHexTextView.setText(color.colorHex);

        colorNameTextView.setText(color.colorName);

        colorHexTextView.setTextColor(android.graphics.Color.parseColor(color.colorHex));

        if(position %2==0)
        {
            convertView.setBackgroundColor(android.graphics.Color.CYAN);
        }else
        {

        }
        return convertView;
    }
*/

        //TODO: example with performance
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.colorName = (TextView) convertView.findViewById(R.id.textViewColorName);
            viewHolder.colorHex = (TextView) convertView.findViewById(R.id.textViewColorHex);
            convertView.setTag(viewHolder);

        }

        Color color = mData.get(position);


        viewHolder = (ViewHolder) convertView.getTag();
        TextView colorName= viewHolder.colorName;
        TextView colorHex = viewHolder.colorHex;

        colorHex.setText(color.colorHex);

        colorName.setText(color.colorName);

        colorHex.setTextColor(android.graphics.Color.parseColor(color.colorHex));

        if (position % 2 == 0) {
            convertView.setBackgroundColor(android.graphics.Color.GRAY);
        } else {

        }
        return convertView;
    }


    static class ViewHolder{
        TextView colorName;
        TextView colorHex;
    }

}
