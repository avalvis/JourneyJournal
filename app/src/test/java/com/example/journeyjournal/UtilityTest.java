package com.example.journeyjournal;// File: UtilityTest.java

import com.example.journeyjournal.Utility;
import com.google.firebase.Timestamp;
import org.junit.Test;
import static org.junit.Assert.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilityTest {

    @Test
    public void testTimestampToString() {
        // Create a Timestamp object with the current time
        Timestamp testTimestamp = new Timestamp(new Date());

        // Format the timestamp using the method in the Utility class
        String formattedDate = Utility.timestampToString(testTimestamp);

        // Format the current time using SimpleDateFormat for comparison
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String expectedFormat = sdf.format(new Date());

        // Assert that the formatted string from the Utility method matches the expected format
        assertEquals(expectedFormat, formattedDate);
    }
}
