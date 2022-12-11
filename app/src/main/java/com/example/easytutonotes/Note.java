package com.example.easytutonotes;


import android.graphics.drawable.Drawable;

import io.realm.RealmObject;

public class Note extends RealmObject {
    String title;
    String description;
    byte[] image;
   // Drawable userImage;
    long createdTime;

   // public Drawable getUserImage() {return userImage; }

    public void setImage(byte[] image) {this.image = image;}
    public byte[] getImage() {return image;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }
}
