package com.ahiho.apps.beeenglish.model.realm_object;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by theptokim on 11/28/16.
 */

public class DictionaryObject extends RealmObject implements Serializable {
    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private String picture;
    private String content;
    private String dateCreate;
    private String dateModified;
    private RealmList<WordObject> wordObjects = new RealmList<>();

    public DictionaryObject() {
    }
    public DictionaryObject(JSONObject jsonObject){
        try {
            id= jsonObject.getInt("id");
            name= jsonObject.getString("name");
            description= jsonObject.getString("description");
            picture= jsonObject.getString("picture");
            content= jsonObject.getString("content");

//            JSONObject jsonCreated = jsonObject.getJSONObject("created_at");
            dateCreate= jsonObject.getString("created_at");

//            JSONObject jsonModified = jsonObject.getJSONObject("updated_at");
            dateModified= jsonObject.getString("updated_at");
        } catch (JSONException e) {

        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public RealmList<WordObject> getWordObjects() {
        if(wordObjects ==null)
            wordObjects =new RealmList<>();
        return wordObjects;
    }

    public void setWordObjects(RealmList<WordObject> wordObjects) {
        this.wordObjects = wordObjects;
    }
}
