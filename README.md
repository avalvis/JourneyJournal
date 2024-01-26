# JourneyJournal
JourneyJournal is a mobile application designed for travelers to document their travel experiences. It allows users to create, view, edit, and delete journal entries, which can include text, images, and geolocation data.
## **Key Functionalities**

JourneyJournal is an app that uses Firebase for user authentication and Firestore for data storage. It allows users to create, edit, view, and delete journal entries, which are stored as 'Journey' objects. The app integrates with device storage and camera for image upload, and uses Google Play Services for geolocation. The UI design includes a RecyclerView with a custom adapter for displaying journal entries, a custom EditText that resembles a lined notebook, and compliance with Material Design guidelines. External APIs and libraries used include Firebase Firestore, Firebase Authentication, Glide, and Google Play Services. Let’s examine everything in more detail below:

### User Authentication (Login/Register)

- **Firebase Authentication:** Firebase provides a strong and secure service for managing user authentication. Although it supports several authentication methods, JourneyJournal primarily uses email and password authentication.
- **User Registration:** New users can create an account by providing a unique username, email, and a strong password. The registration process involves validating the input data and ensuring that the email is not already in use.
- **Login System:** Registered users can log in using their email and password. Firebase Authentication handles the login process, including security checks and session management.

### Journal Entry Management

- **Class `Journey`:** Acts as the data model for journal entries. It encapsulates attributes such as title, details (text of the journal entry), timestamp (for tracking creation or last edit time), image URI (link to the stored image), and geolocation (stored as **`GeoPoint`** for location tagging).
- **Create/Edit Entry:** Users interact with a form to input or modify the details of their journal entries. New entries are instantiated as **`Journey`** objects and saved to Firestore. Existing entries can be retrieved, modified, and updated in the database.
- **View Entry:** The main screen lists all journal entries in a user-friendly format. Each entry displays essential information like the title, an image thumbnail (if available), and the date of the entry.
- **Delete Entry:** Users have the option to delete any of their journal entries. This action removes the entry from the Firestore database.

### Image and Location Integration

- **Image Upload:** Integrates with the device's storage and camera. Users can either select an existing image from their device or capture a new one using the camera. Uploaded images are stored and referenced in journal entries via their URIs.
- **Geolocation:** The app uses **`FusedLocationProviderClient`**, a part of Google Play Services, to obtain accurate and efficient location data. This feature enhances journal entries by allowing users to tag their exact location at the time of writing.

### Data Storage and Retrieval

- **Firebase Firestore:** A NoSQL cloud database that stores and synchronizes data in real-time. Firestore is used for storing all journal entries, user data, and images (as references). Its real-time capabilities ensure that any changes in the database are immediately reflected in the app.
- **Data Model:** Each user's journal entries are stored in a separate document under their user ID, ensuring data isolation and personalized user experiences.
- **Utility Class:** Contains helper methods for common database operations, such as fetching the collection reference for a user's journal entries and converting timestamps to a readable format.

### UI/UX Design

- **RecyclerView with Custom Adapter (`JourneyAdapter`):** This setup provides a dynamic and efficient way to display a list of journal entries. The custom adapter handles the layout and binding of data from Firestore to each item in the RecyclerView.
- **Custom EditText (`NotebookEditText`):** A visually appealing custom EditText that resembles a lined notebook. This design choice adds a thematic element to the journaling experience, making the text input area look like a page from a traditional journal.
- **Material Design Compliance:** Adherence to Material Design guidelines ensures a familiar and intuitive user interface. This includes the use of standard components, animations, and responsive layouts that adapt to different screen sizes and orientations.

## External APIs and Libraries used

- **Firebase Firestore:** For data storage and retrieval.
- **Firebase Authentication:** For user registration and login.
- **Glide:** For efficient image loading and handling.
- **Google Play Services (Location APIs):** For obtaining the user's current location.

## **Design and Implementation Justification**

JourneyJournal is designed and implemented to be a user-friendly, efficient, and feature-packed travel journal app. It uses Firebase for a strong and scalable backend, Material Design for a consistent and intuitive UI, RecyclerView with FirestoreRecyclerAdapter for efficient data display, and Glide for optimal image handling. The geolocation feature provides useful context to journal entries. All these elements work together to create an app that is technically robust and easy to use.

The app is designed to provide a complete journaling experience. It uses Firebase for backend services and follows Material Design principles for a scalable, efficient, and attractive application.

### Firebase Integration

- **Ease of Use and Rapid Development:** Firebase offers a streamlined development process, significantly reducing the time and effort required to set up backend services. It eliminates the need for writing server-side code or managing databases, allowing for rapid iteration and deployment.
- **Scalability:** Firebase's cloud-based infrastructure provides the ability to scale effortlessly. As the user base of JourneyJournal grows, Firebase can handle increased traffic and data without the need for manual intervention.
- **Comprehensive Services:** Integrating various Firebase services, such as Firestore for database management and Firebase Authentication for user authentication, ensures a cohesive backend solution. These services work seamlessly together, reducing complexity and enhancing data consistency.
- **Real-time Data Synchronization:** Firestore offers real-time data syncing across all client devices. This feature is particularly beneficial for a journaling app, as it ensures that users see the most up-to-date information immediately, enhancing user engagement.

### Material Design

- **User Experience Consistency:** Following Material Design guidelines provides a familiar interface for Android users. It's based on good design, intuitiveness, and usability.
- **Responsive and Adaptive UI:** These principles help create an interface that adjusts well to different device sizes and orientations.
- **Visual Aesthetics:** Material Design uses grid-based layouts, animations, transitions, and depth effects like lighting and shadows to create an attractive interface.

### RecyclerView with FirestoreRecyclerAdapter

- **Efficient Data Handling:** The RecyclerView widget provides an efficient way to display large sets of data. Its use of the ViewHolder pattern significantly improves performance by recycling views and minimizing calls to **`findViewById`**.
- **Real-time Database Integration:** FirestoreRecyclerAdapter simplifies the process of synchronizing data from Firestore to a RecyclerView. It automatically handles data updates, providing a real-time and dynamic experience.
- **Customizable and Flexible:** The adapter allows for extensive customization of the RecyclerView's layout and behavior, enabling the creation of a tailored interface that aligns with the specific needs of the journal entries in JourneyJournal.

### Glide for Image Handling

- **Performance Optimization:** Glide optimizes image loading and caching, which is crucial for JourneyJournal as it handles potentially large sets of images. This leads to faster image loading times and reduced memory usage.
- **Resource Management:** Glide efficiently manages lifecycle and resource pooling, automatically pausing, resuming, and cancelling requests based on the activity or fragment lifecycle.
- **Versatility and Extensibility:** Glide supports various image formats and sources. It can be extended and customized to fit specific requirements, such as image transformations and caching strategies.

### Geolocation Feature

- **Enhanced User Experience:** The inclusion of geolocation allows users to associate specific locations with their journal entries, providing a richer and more informative journaling experience.
- **Contextual Relevance:** Geotagging entries give context to the user's experiences, making the journal entries more meaningful and personal.
- **Interactive Elements:** Integrating map links or visual representations of locations can make the app more interactive and engaging for users.
