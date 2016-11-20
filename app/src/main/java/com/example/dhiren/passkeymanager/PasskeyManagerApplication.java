package com.example.dhiren.passkeymanager;

import com.firebase.client.Firebase;

/**
 * Created by Kunal on 11-09-2016.
 */
public class PasskeyManagerApplication extends android.app.Application {

    @Override
    public void onCreate()
    {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
