package com.repolho.natour.model;

/**
 * Created by repolho on 25/04/16.
 */
public class Image{

    private String fullUrl;

    public Image() {}

    public Image(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }
}