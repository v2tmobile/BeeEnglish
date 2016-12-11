package com.ahiho.apps.beeenglish;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by theptokim on 12/1/16.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
//        MultiDex.install(this);
/*
RealmConfiguration myConfig = new RealmConfiguration.Builder(context)
        .name("myrealm.realm").
.schemaVersion(2)
        .setModules(new MyCustomSchema())
        .build();

RealmConfiguration otherConfig = new RealmConfiguration.Builder(context)
        .name("otherrealm.realm")
        .schemaVersion(5)
        .setModules(new MyOtherSchema())
        .build();

Realm myRealm = Realm.getInstance(myConfig);
Realm otherRealm = Realm.getInstance(otherConfig);
*/
    }
}