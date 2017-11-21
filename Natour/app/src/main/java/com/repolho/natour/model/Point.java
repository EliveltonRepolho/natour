package com.repolho.natour.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by repolho on 22/04/16.
 */
@IgnoreExtraProperties
public class Point {

    private Author author;
    private String text;
    private Object timestamp;
    private double latitude;
    private double longitude;
    private String descCoordinates;
    private Image image;
    private Detail detail;

    public Point() {}

    public Point(String text, double latitude, double longitude, String descCoordinates, Detail detail) {
        this.text = text;
        this.latitude = latitude;
        this.longitude = longitude;
        this.descCoordinates = descCoordinates;
        this.detail = detail;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescCoordinates() {
        return descCoordinates;
    }

    public void setDescCoordinates(String descCoordinates) {
        this.descCoordinates = descCoordinates;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }
}