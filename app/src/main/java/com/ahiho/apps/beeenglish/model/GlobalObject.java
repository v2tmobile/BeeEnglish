package com.ahiho.apps.beeenglish.model;

/**
 * Created by theptokim on 12/16/16.
 */

public class GlobalObject {
    private int id;
    private String name;

    public GlobalObject(int id, String name) {
        this.id = id;
        this.name = name;
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
}
