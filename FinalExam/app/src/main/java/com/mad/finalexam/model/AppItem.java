package com.mad.finalexam.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Sanket on 10/24/2016.
 */

public class AppItem implements Serializable, Comparable<AppItem>{

    private String name;
    private Double price;
    private String imageSrc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    private boolean isFavorite;

    public String getImageSrcLarge() {
        return imageSrcLarge;
    }

    public void setImageSrcLarge(String imageSrcLarge) {
        this.imageSrcLarge = imageSrcLarge;
    }

    private String imageSrcLarge;

    public AppItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }


    public static AppItem getAppItem(JSONObject jsonObject) throws JSONException {
        AppItem appItem = new AppItem();

        JSONObject nameObj = jsonObject.getJSONObject("im:name");
         appItem.setName(nameObj.getString("label"));

        JSONArray imageObjectArray = jsonObject.getJSONArray("im:image");
        for (int i = 0; i < imageObjectArray.length(); i++) {
           JSONObject temp= imageObjectArray.getJSONObject(i);
           JSONObject attributeObject = temp.getJSONObject("attributes");

            String height= attributeObject.getString("height");
            if( height.equals("53")) {
                appItem.setImageSrc(temp.getString("label"));
            }else if(  height.equals("100")) {
                appItem.setImageSrcLarge(temp.getString("label"));
            }
        }

        JSONObject priceObj = jsonObject.getJSONObject("im:price");

        appItem.setPrice(Double.parseDouble(priceObj.getString("label").replace("$","")));
        return appItem;
    }


    @Override
    public int compareTo(AppItem another) {
        return getPrice().compareTo(another.getPrice());
    }
}
