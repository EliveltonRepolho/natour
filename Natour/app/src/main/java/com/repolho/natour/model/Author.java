package com.repolho.natour.model;

/**
 * Created by repolho on 02/10/16.
 */
public class Author {
    private String fullName;
    private String profilePicture;

    private String uid;

    public Author() {

    }

    public Author(String fullName, String profilePicture, String uid) {
        this.fullName = fullName;
        this.profilePicture = profilePicture;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getFullName() {
        return fullName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }
}
