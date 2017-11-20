package com.mad.finalexam.jsonparser;



import com.mad.finalexam.model.AppItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Sanket on 10/24/2016.
 */

public class ItemPareser {

    public static ArrayList<AppItem> parseItem (String inputStream) throws JSONException {
        ArrayList<AppItem> appItemsList = new ArrayList<AppItem>();

        JSONObject root = new JSONObject(inputStream);

        //Change root element to actual root element in service
        if (root.has("feed")) {
            JSONObject rootChild = root.getJSONObject("feed");
            JSONArray jsonArray = rootChild.getJSONArray("entry");
            AppItem item=null;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                item = AppItem.getAppItem(object);
                appItemsList.add(item);
            }
        }
        Collections.sort(appItemsList);
        return appItemsList;
    }
}
