package com.skybasestudios.infoaround;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Code on 06/02/2017.
 */

public class ReweitApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
    /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
