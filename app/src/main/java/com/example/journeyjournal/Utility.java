package com.example.journeyjournal;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

//Utility class for the JourneyJournal application.
//This class provides utility methods that are used across the application.
public class Utility {

    // Returns a CollectionReference to the current user's journeys in Firestore.
    // The path in Firestore will be "journeys/{userId}/my_journeys".
    static CollectionReference getCollectionReferenceForJourneys(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        return FirebaseFirestore.getInstance().collection("journeys")
                .document(currentUser.getUid()).collection("my_journeys");
    }

    // Converts a Timestamp into a String representation.
    // The format of the date string is "dd/MM/yyyy".
    public static String timestampToString(Timestamp timestamp) {
        Date date = timestamp.toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
}
