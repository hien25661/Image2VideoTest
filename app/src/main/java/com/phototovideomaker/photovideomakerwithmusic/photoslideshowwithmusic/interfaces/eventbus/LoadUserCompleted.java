package com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.interfaces.eventbus;

import java.util.ArrayList;

import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.models.User;

/**
 * Created by SF on 05/05/2016.
 */
public class LoadUserCompleted {
    ArrayList<User> userArrayList;

    public LoadUserCompleted(ArrayList<User> userArrayList) {
        this.userArrayList = userArrayList;
    }
}
