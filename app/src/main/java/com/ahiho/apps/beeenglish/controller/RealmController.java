package com.ahiho.apps.beeenglish.controller;

/**
 * Created by theptokim on 12/1/16.
 */

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.ahiho.apps.beeenglish.model.BookObject;

import io.realm.Realm;
import io.realm.RealmResults;


public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    //clear all objects from Book.class
    public void clearAll() {
        realm.beginTransaction();
        realm.clear(BookObject.class);
        realm.commitTransaction();
    }

    //find all objects in the Book.class
    public RealmResults<BookObject> getBooks() {
        return realm.where(BookObject.class).findAll();
    }

    //query a single item with the given id
    public BookObject getBook(String id) {

        return realm.where(BookObject.class).equalTo("id", id).findFirst();
    }

    //check if Book.class is empty
    public boolean hasBooks() {

        return !realm.allObjects(BookObject.class).isEmpty();
    }

    //query example
    public RealmResults<BookObject> queryedBooks() {

        return realm.where(BookObject.class)
                .contains("author", "Author 0")
                .or()
                .contains("title", "Realm")
                .findAll();

    }
}
