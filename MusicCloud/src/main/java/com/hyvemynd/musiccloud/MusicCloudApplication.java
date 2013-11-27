package com.hyvemynd.musiccloud;

import android.app.Application;

/**
 * Created by andresmonroy on 11/26/13.
 */
public class MusicCloudApplication extends Application {
    private MusicCloudModel model;

    public MusicCloudApplication(){
        model = MusicCloudModel.getModel();
    }

    public MusicCloudModel getModel(){
        return model;
    }
}
