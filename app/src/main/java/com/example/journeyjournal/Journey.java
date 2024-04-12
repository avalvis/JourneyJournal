package com.example.journeyjournal;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;


public class Journey {
    // Title of the journey
    String title;
    // Details of the journey
    String details;
    // Timestamp of when the journey was created or updated
    Timestamp timestamp;
    // URI of the image associated with the journey
    String imageUri;
    //GeoPoint location;
    GeoPoint location;

    // Default constructor
    public Journey() {
    }

    // Getter for title
    public String getTitle() {
        return title;
    }

    // Setter for title
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter for details
    public String getDetails() {
        return details;
    }

    // Setter for details
    public void setDetails(String details) {
        this.details = details;
    }

    // Getter for timestamp
    public Timestamp getTimestamp() {
        return timestamp;
    }

    // Setter for timestamp
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    // Getter for imageUri
    public String getImageUri() {
        return imageUri;
    }

    // Setter for imageUri
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    // Getter for location
    public GeoPoint getLocation() {
        return location;
    }
    // Setter for location
    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}