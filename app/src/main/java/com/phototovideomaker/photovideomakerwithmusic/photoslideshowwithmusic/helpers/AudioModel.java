package com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.helpers;

/**
 * Created by ducnguyen on 7/9/16.
 */
public class AudioModel {
    String title;
    long duration;
    String uriPath;
    public int position;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getUriPath() {
        return uriPath;
    }

    public void setUriPath(String uriPath) {
        this.uriPath = uriPath;
    }

    public AudioModel(int position) {
        this.position = position;
    }

    public AudioModel() {
    }
}
