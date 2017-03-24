package com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.models;

/**
 * Created by ducnguyen on 6/19/16.
 */
public class Picture {
    String url;
    int id;

    public Picture(int id,String url) {
        this.url = url;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
