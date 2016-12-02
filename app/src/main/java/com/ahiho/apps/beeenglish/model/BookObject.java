package com.ahiho.apps.beeenglish.model;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by theptokim on 11/28/16.
 */

public class BookObject extends RealmObject {
    @PrimaryKey
    private int id;
    private String uri;
    private String iconUri;//picture
    private String name;
    private String auth;
    private float size;
    private String description;

    public BookObject(){

    }
    public BookObject(int id, String uri, String iconUri, String name, String auth, float size, String description) {
        this.id = id;
        this.uri = uri;
        this.iconUri = iconUri;
        this.name = name;
        this.auth = auth;
        this.size = size;
        this.description = description;
    }

    public BookObject(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getInt("id");
            this.uri = jsonObject.getString("content");
            this.iconUri = jsonObject.getString("picture");
            this.name = jsonObject.getString("name");
            this.description = jsonObject.getString("description");
            this.auth = "";
            this.size = 0;
        } catch (JSONException e) {

        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }
}
