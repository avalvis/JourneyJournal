package com.example.journeyjournal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// This is an adapter for the RecyclerView.
// It is used to populate the RecyclerView with data from a Firestore database.
// The data represents "Journey" objects, each of which has a title, details, timestamp, and an image URI.
public class JourneyAdapter extends FirestoreRecyclerAdapter<Journey, JourneyAdapter.JourneyViewHolder> {
    // Context for the adapter, used for creating intents and starting activities
    Context context;

    // Constructor for the adapter
    // Takes FirestoreRecyclerOptions and a Context as parameters
    public JourneyAdapter(@NonNull FirestoreRecyclerOptions<Journey> options, Context context) {
        super(options); // Pass the options to the superclass constructor
        this.context = context; // Assign the context to the instance variable
    }

    // Method to bind the data to the view holder
    // This method is called for each item in the RecyclerView
    @Override
    protected void onBindViewHolder(@NonNull JourneyViewHolder holder, int position, @NonNull Journey journey) {
        // Set the title, details, and timestamp to the respective TextViews
        holder.titleTextView.setText(journey.title); // Set the title of the journey
        holder.detailsTextView.setText(journey.details); // Set the details of the journey
        holder.timestampTextView.setText(Utility.timestampToString(journey.timestamp)); // Set the timestamp of the journey

        // Set a placeholder image for the ImageView
        holder.journeyImageView.setImageResource(R.drawable.logo);

        // If there is an imageUri, load the image into the ImageView
        if (journey.getImageUri() != null && !journey.getImageUri().isEmpty()) {
            // Parse the imageUri string to a Uri object
            Uri imageUri = Uri.parse(journey.getImageUri());
            // Use Glide to load the image from the Uri into the ImageView
            Glide.with(holder.journeyImageView.getContext())
                    .load(imageUri)
                    .placeholder(R.drawable.logo) // Set a placeholder image while the image is loading
                    .into(holder.journeyImageView); // Specify the ImageView to load the image into
            holder.journeyImageView.setVisibility(View.VISIBLE); // Make the ImageView visible
        } else {
            // If there is no imageUri, hide the ImageView
            holder.journeyImageView.setVisibility(View.GONE);
        }

        // Set an onClickListener for the item view
        holder.itemView.setOnClickListener((v)->{
            // Create an intent to start the JourneyDetailsActivity
            Intent intent = new Intent(context, JourneyDetailsActivity.class);
            // Pass the title, details, imageUri, and docId to the intent
            intent.putExtra("title", journey.title);
            intent.putExtra("details", journey.details);
            intent.putExtra("imageUri", journey.getImageUri());
            // Get the document ID of the journey from the Firestore snapshot
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            // Start the JourneyDetailsActivity with the intent
            context.startActivity(intent);
        });
    }

    // Method to create the view holder
    // This method is called when the RecyclerView needs a new ViewHolder to represent an item
    @NonNull
    @Override
    public JourneyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for the recycler item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_journey_item,parent,false);
        // Return a new JourneyViewHolder with the inflated view
        return new JourneyViewHolder(view);
    }

    // ViewHolder class for the recycler item
    // This class holds the views that will be used to display the items in the RecyclerView
    class JourneyViewHolder extends RecyclerView.ViewHolder{
        // Declare the TextViews and ImageView
        TextView titleTextView,detailsTextView,timestampTextView;
        ImageView journeyImageView;

        // Constructor for the ViewHolder
        // Takes a View as a parameter, which represents the item view
        public JourneyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the TextViews and ImageView
            titleTextView = itemView.findViewById(R.id.journey_title_text_view); // TextView for the journey title
            detailsTextView = itemView.findViewById(R.id.journey_details_text_view); // TextView for the journey details
            timestampTextView = itemView.findViewById(R.id.journey_timestamp_text_view); // TextView for the journey timestamp
            journeyImageView = itemView.findViewById(R.id.journey_image); // ImageView for the journey image
        }
    }
}