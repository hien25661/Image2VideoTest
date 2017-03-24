package com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by ducnguyen on 6/19/16.
 */
public class ImageManager {
    //create an object of SingleObject
    private ImageManager instance;
    ArrayList<Picture> urlList;

    //make the constructor private so that this class cannot be
    //instantiated
    public ImageManager() {
        urlList = new ArrayList<>();
    }


    public ArrayList<Picture> getUrlList() {
        return urlList;
    }

    public void setUrlList(ArrayList<Picture> urlList) {
        urlList = urlList;
    }

    public void addUrl(Picture url) {
        urlList.add(url);
    }

    public void removeUrl(int index) {
        urlList.remove(index);
    }

    public void clear() {
        urlList.clear();
    }

    public String converToString() {
        Gson gson = new Gson();
        return gson.toJson(this.urlList);
    }

    public void getObjectFromString(String json) {
        Gson gson = new Gson();
        this.urlList = gson.fromJson(json, new TypeToken<ArrayList<Picture>>() {
        }.getType());
    }
}
