package com.hyvemynd.musiccloud.rest.callback;

/**
 * Created by andresmonroy on 12/11/13.
 */
public interface RequestCallback {
    void onDataReceived(Object result);
    void onModelChanged();
}
