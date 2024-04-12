package com.example.journeyjournal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MainActivity extends AppCompatActivity {

    // Declare UI elements
    FloatingActionButton addJourneyBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;
    JourneyAdapter journeyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        addJourneyBtn = findViewById(R.id.add_journey_btn);
        recyclerView = findViewById(R.id.recycler_view);
        menuBtn = findViewById(R.id.menu_btn);

        // Set onClickListeners for the buttons
        addJourneyBtn.setOnClickListener((v)-> startActivity(new Intent(MainActivity.this,JourneyDetailsActivity.class)) );
        menuBtn.setOnClickListener((v) -> showMenu());

        // Setup the RecyclerView
        setupRecyclerView();
    }

    // Method to show the menu
    void showMenu(){
        // Create a PopupMenu
        PopupMenu popupMenu  = new PopupMenu(MainActivity.this,menuBtn);
        // Add a menu item
        popupMenu.getMenu().add("Logout");
        // Show the PopupMenu
        popupMenu.show();
        // Set an OnMenuItemClickListener for the PopupMenu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // If the clicked menu item is "Logout", sign out and start the LoginActivity
                if(menuItem.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    // Method to setup the RecyclerView
    void setupRecyclerView(){
        // Create a Query to get the journeys ordered by timestamp
        Query query  = Utility.getCollectionReferenceForJourneys().orderBy("timestamp",Direction.DESCENDING);
        // Create FirestoreRecyclerOptions
        FirestoreRecyclerOptions<Journey> options = new FirestoreRecyclerOptions.Builder<Journey>()
                .setQuery(query,Journey.class).build();
        // Set the layout manager for the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Create the JourneyAdapter
        journeyAdapter = new JourneyAdapter(options, this);
        // Set the adapter for the RecyclerView
        recyclerView.setAdapter(journeyAdapter);
    }

    // Start listening to Firestore updates when the activity starts
    @Override
    protected void onStart() {
        super.onStart();
        journeyAdapter.startListening();
    }

    // Stop listening to Firestore updates when the activity stops
    @Override
    protected void onStop() {
        super.onStop();
        journeyAdapter.stopListening();
    }

    // Notify the adapter of data set changes when the activity resumes
    @Override
    protected void onResume() {
        super.onResume();
        journeyAdapter.notifyDataSetChanged();
    }
}