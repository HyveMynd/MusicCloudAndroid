package com.hyvemynd.musiccloud.rest.callback;

/**
 * Created by andresmonroy on 12/11/13.
 */
public interface OnGetCallback<Response> {
    void onGetSuccess(Response result);
}
