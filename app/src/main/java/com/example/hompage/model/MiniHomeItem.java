package com.example.hompage.model;

public class MiniHomeItem {
    private String imageUrl;
    private float x;
    private float y;

    public MiniHomeItem() {
    }

    public MiniHomeItem(String imageUrl, float x, float y) {
        this.imageUrl = imageUrl;
        this.x = x;
        this.y = y;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}

