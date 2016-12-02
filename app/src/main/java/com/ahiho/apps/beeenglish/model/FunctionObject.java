package com.ahiho.apps.beeenglish.model;

/**
 * Created by theptokim on 11/28/16.
 */

public class FunctionObject {
    private  int id;
    private int icon;
    private String iconName;
    private String name;

    public FunctionObject(int id,int icon, String name) {
        this.id=id;
        this.icon = icon;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
