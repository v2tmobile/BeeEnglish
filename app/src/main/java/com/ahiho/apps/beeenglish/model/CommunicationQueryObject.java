package com.ahiho.apps.beeenglish.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by theptokim on 12/21/16.
 */

public class CommunicationQueryObject {
    private int id;
    private String name;
    private String description;
    private String icon;
    private String link;

    public CommunicationQueryObject(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            name=jsonObject.getString("name");
            description=jsonObject.getString("description");
            icon=jsonObject.getString("picture");
            link=jsonObject.getString("content");
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
