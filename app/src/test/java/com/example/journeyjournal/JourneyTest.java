package com.example.journeyjournal;

import com.example.journeyjournal.Journey;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class JourneyTest {

    @Test
    public void testJourneyDataRetrieval() {
        // Mock a Firestore DocumentSnapshot
        DocumentSnapshot mockSnapshot = Mockito.mock(DocumentSnapshot.class);

        // Setup mock to return specific data when methods are called
        Mockito.when(mockSnapshot.getString("title")).thenReturn("Sample Title");
        Mockito.when(mockSnapshot.getGeoPoint("location")).thenReturn(new GeoPoint(0.0, 0.0));

        // Assume a Journey object is constructed using data from the snapshot
        Journey journey = new Journey();
        journey.setTitle(mockSnapshot.getString("title"));
        journey.setLocation(mockSnapshot.getGeoPoint("location"));

        // Verify that the title is set correctly in the Journey object
        assertEquals("Sample Title", journey.getTitle());

        // Verify that the location is set correctly in the Journey object
        // Comparing latitude and longitude separately
        GeoPoint expectedLocation = new GeoPoint(0.0, 0.0);
        GeoPoint actualLocation = journey.getLocation();
        assertNotNull("Location should not be null", actualLocation);
        assertEquals(expectedLocation.getLatitude(), actualLocation.getLatitude(), 0.0);
        assertEquals(expectedLocation.getLongitude(), actualLocation.getLongitude(), 0.0);
    }
}
