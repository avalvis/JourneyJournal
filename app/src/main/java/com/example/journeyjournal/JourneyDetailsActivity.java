package com.example.journeyjournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;

import java.util.Objects;

import javax.annotation.Nullable;

public class JourneyDetailsActivity extends AppCompatActivity {

    // Declare UI elements
    EditText titleEditText;
    NotebookEditText detailsEditText;
    ImageButton saveJourneyBtn, deleteJourneyBtn;
    TextView pageTitleTextView, insert_photo_text_view_btn;
    ImageView journey_image;
    String title,details,docId;
    boolean isEditMode = false;
    ImageButton geolocationBtn;
    private FusedLocationProviderClient fusedLocationClient;
    private GeoPoint currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call the superclass implementation
        super.onCreate(savedInstanceState);
        // Set the layout for this activity
        setContentView(R.layout.activity_journey_details);

        // Initialize UI elements
        titleEditText = findViewById(R.id.journey_title_text); // EditText for journey title
        detailsEditText = findViewById(R.id.journey_details_text); // EditText for journey details
        saveJourneyBtn = findViewById(R.id.save_journey_btn); // Button to save the journey
        pageTitleTextView = findViewById(R.id.page_title); // TextView for the page title
        deleteJourneyBtn  = findViewById(R.id.delete_journey_btn); // Button to delete the journey
        journey_image = findViewById(R.id.journey_image); // ImageView for the journey image

        // Button to open the image picker
        insert_photo_text_view_btn = findViewById(R.id.insert_photo_text_view_btn);
        // Set an OnClickListener to open the image picker when the button is clicked
        insert_photo_text_view_btn.setOnClickListener(v -> openImagePicker());

        // Get the data from the intent
        title = getIntent().getStringExtra("title"); // Journey title
        details= getIntent().getStringExtra("details"); // Journey details
        docId = getIntent().getStringExtra("docId"); // Document ID for the journey
        String imageUriString = getIntent().getStringExtra("imageUri"); // Image URI for the journey

        // Check if in edit mode
        if(docId!=null && !docId.isEmpty()){
            // If the document ID is not null or empty, the app is in edit mode
            isEditMode = true;
        }

        // Set the title and details
        titleEditText.setText(title); // Set the text of the titleEditText to the journey title
        detailsEditText.setText(details); // Set the text of the detailsEditText to the journey details
        if(isEditMode){
            // If in edit mode, set the page title to "Edit your journey"
            pageTitleTextView.setText("Edit your journey");
            // Make the delete button visible
            deleteJourneyBtn.setVisibility(View.VISIBLE);
        }

        // Load the imageUri from the Journey object in Firestore and set it to the ImageView
        if (imageUriString != null && !imageUriString.isEmpty()) {
            // If the imageUriString is not null or empty, parse it to a Uri
            Uri imageUri = Uri.parse(imageUriString);
            // Use Glide to load the image from the Uri into the ImageView
            Glide.with(this)
                    .load(imageUri)
                    .listener(new RequestListener<Drawable>() {
                        // This method is called if an error occurs while loading the image
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            // Log the error and let Glide handle it
                            Log.e("JourneyDetailsActivity", "Load failed", e);
                            return false;
                        }

                        // This method is called when the image is ready to be displayed
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            // Let Glide handle the event
                            return false;
                        }
                    })
                    .into(journey_image); // Specify the ImageView to load the image into

            // Save the Uri of the image as a tag of the ImageView for later use
            journey_image.setTag(imageUriString);
        }

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Button to get the current location
        geolocationBtn = findViewById(R.id.geolocation_btn);
        // Set an OnClickListener to get the current location when the button is clicked
        geolocationBtn.setOnClickListener(v -> getCurrentLocation());

        // Set onClickListeners for the buttons
        saveJourneyBtn.setOnClickListener((v)->saveJourney()); // Save the journey when the save button is clicked
        deleteJourneyBtn.setOnClickListener((v)-> deleteJourneyFromFirebase() ); // Delete the journey when the delete button is clicked

        // Make the links in the detailsEditText clickable
        detailsEditText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    // Method to handle saving the journey
    void saveJourney(){
        // Get the journey title from the titleEditText field
        String journeyTitle = titleEditText.getText().toString();
        // Get the journey details from the detailsEditText field
        String journeyDetails = Objects.requireNonNull(detailsEditText.getText()).toString();

        // Check if the journey title is empty
        if(journeyTitle.isEmpty()){
            // If the title is empty, set an error on the titleEditText field
            titleEditText.setError("Title is missing!");
            // Stop the method execution
            return;
        }

        // Check if the journey details are empty
        if(journeyDetails.isEmpty()){
            // If the details are empty, set an error on the detailsEditText field
            detailsEditText.setError("Details are missing!");
            // Stop the method execution
            return;
        }

        // Create a new Journey object
        Journey journey = new Journey();
        // Set the journey title
        journey.setTitle(journeyTitle);

        // Check if the current location is not null
        if (currentLocation != null) {
            // If the current location is not null, set the location of the journey
            journey.setLocation(currentLocation);
        }

        // Set the journey details
        journey.setDetails(journeyDetails);
        // Set the current timestamp as the journey timestamp
        journey.setTimestamp(Timestamp.now());

        // Check if the journey_image ImageView has a tag
        if (journey_image.getTag() != null) {
            // If the ImageView has a tag, get the tag as a string
            String imageUriString = journey_image.getTag().toString();
            // Set the image URI of the journey
            journey.setImageUri(imageUriString);
        }

        // Save the journey to Firebase
        saveJourneyToFirebase(journey);
    }

    // Method to handle saving the journey to Firebase
    void saveJourneyToFirebase(Journey journey){
        // Declare a DocumentReference to hold the reference to the document in Firestore
        DocumentReference documentReference;
        // Declare a String to hold the message that will be displayed in a Toast
        String toastMessage;

        // Check if the app is in edit mode
        if(isEditMode){
            // If in edit mode, get the reference to the existing journey document in Firestore
            documentReference = Utility.getCollectionReferenceForJourneys().document(docId);
            // Set the message to indicate that the journey was edited
            toastMessage = "Journey edited!";
        }else{
            // If not in edit mode, create a new document reference for a new journey
            documentReference = Utility.getCollectionReferenceForJourneys().document();
            // Set the message to indicate that a new journey was added
            toastMessage = "Journey added!";
        }

        // Set the journey object to the document in Firestore
        // addOnCompleteListener allows us to perform an action when the operation is complete
        documentReference.set(journey).addOnCompleteListener(task -> {
            // Check if the operation was successful
            if(task.isSuccessful()){
                // If successful, display a Toast with the success message and end the activity
                Toast.makeText(JourneyDetailsActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
                finish();
            }else{
                // If not successful, display a Toast with an error message
                Toast.makeText(JourneyDetailsActivity.this, "Failed! Journey not added!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to handle deleting the journey from Firebase
    void deleteJourneyFromFirebase(){
        // Declare a DocumentReference to hold the reference to the document in Firestore
        DocumentReference documentReference;

        // Get the reference to the existing journey document in Firestore using the document ID
        documentReference = Utility.getCollectionReferenceForJourneys().document(docId);

        // Call the delete method on the document reference
        // addOnCompleteListener allows us to perform an action when the operation is complete
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Check if the operation was successful
                if(task.isSuccessful()){
                    // If successful, display a Toast with the success message and end the activity
                    Toast.makeText(JourneyDetailsActivity.this,"Journey deleted!",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    // If not successful, display a Toast with an error message
                    Toast.makeText(JourneyDetailsActivity.this,"Failed deleting Journey!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Constants for image picking and permission request
    // PICK_IMAGE_REQUEST is the request code used when starting the activity to pick an image
    private static final int PICK_IMAGE_REQUEST = 1;
    // READ_EXTERNAL_STORAGE_REQUEST is the request code used when requesting permission to read external storage
    private static final int READ_EXTERNAL_STORAGE_REQUEST = 101;

    // Method to open the image picker
    private void openImagePicker() {
        // Create an intent to open a document
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        // Add the category that the document must be openable
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Set the type of files that can be opened to all image types
        intent.setType("image/*");
        // Start the activity to pick an image, using the PICK_IMAGE_REQUEST code
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Method to handle the result of a permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Call the superclass implementation
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Check if the request code matches the code for requesting read external storage permission
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST) {
            // Check if the permission was granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If the permission was granted, open the image picker
                openImagePicker();
            } else {
                // If the permission was not granted, check if the user has permanently denied the permission
                if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // If the user has permanently denied the permission, show a toast message and direct the user to the app settings
                    Toast.makeText(this, "Permission denied to read external storage. Please enable the permission in settings.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName())));
                } else {
                    // If the user has not permanently denied the permission, show a toast message
                    Toast.makeText(this, "Permission denied to read external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Method to handle activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Call the superclass implementation
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the request code matches the code for picking an image,
        // the result code is OK (indicating success), and the data contains a URI
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the URI of the selected image from the data
            Uri imageUri = data.getData();

            // Use Glide to load the image from the URI into the ImageView
            Glide.with(this)
                    .load(imageUri)
                    .listener(new RequestListener<Drawable>() {
                        // This method is called if an error occurs while loading the image
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            // Log the error
                            Log.e("JourneyDetailsActivity", "Load failed", e);
                            // Return false to let Glide handle the error
                            return false;
                        }

                        // This method is called when the image is ready to be displayed
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            // Return false to let Glide handle the event
                            return false;
                        }
                    })
                    .into(journey_image); // Specify the ImageView to load the image into

            // Save the URI of the image as a tag of the ImageView for later use
            journey_image.setTag(imageUri.toString());
        }
    }

    // Declare a LocationCallback object
    private LocationCallback locationCallback;

    // Method to get the current location
    private void getCurrentLocation() {
        // Check if the app has the necessary permissions to access location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Log a message if permissions are not granted
            Log.d("JourneyDetailsActivity", "Location permissions not granted.");
            // Request the necessary permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        // Initialize the LocationCallback object
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                // Loop through all the locations returned
                for (Location location : locationResult.getLocations()) {
                    // Check if the location is not null
                    if (location != null) {
                        // Convert the location to a GeoPoint
                        currentLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                        // Log a message indicating the location was obtained successfully
                        Log.d("JourneyDetailsActivity", "Location obtained successfully: " + currentLocation.getLatitude() + ", " + currentLocation.getLongitude());
                        // Show a toast message indicating the location was obtained successfully
                        Toast.makeText(JourneyDetailsActivity.this, "Location obtained successfully", Toast.LENGTH_SHORT).show();
                        // Create a link to the location on Google Maps
                        String locationLink = "http://maps.google.com/maps?q=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude();
                        // Append the location link to the detailsEditText field
                        detailsEditText.append("\n\nLocation: " + locationLink);
                        // Stop location updates
                        fusedLocationClient.removeLocationUpdates(locationCallback);
                    }
                }
            }
        };
        // Request location updates
        fusedLocationClient.requestLocationUpdates(createLocationRequest(), locationCallback, Looper.getMainLooper());
    }

    // Method to create a LocationRequest object
    private LocationRequest createLocationRequest() {
        // Create a new LocationRequest object
        LocationRequest locationRequest = LocationRequest.create();
        // Set the desired interval for active location updates
        locationRequest.setInterval(10000);
        // Set the fastest rate for active location updates
        locationRequest.setFastestInterval(5000);
        // Set the priority of the request to PRIORITY_HIGH_ACCURACY
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Return the LocationRequest object
        return locationRequest;
    }
}