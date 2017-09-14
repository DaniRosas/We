package com.we.wemvp;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by DaniRosas on 14/9/17.
 */

public class WeApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        setUpFirebase();

    }

    private void setUpFirebase() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
