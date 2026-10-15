import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Firebase {
    static Firestore db;

    static{
        InputStream serviceAccount = loadServiceAccount();

        FirebaseOptions options = null;
        try {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://barangaylink-267ca-default-rtdb.firebaseio.com/")  // <-- add this line with your actual DB URL
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FirebaseApp.initializeApp(options);

        // For Firestore usage:
       db = FirestoreClient.getFirestore();

        // Now you can use 'db' to read/write documents
        System.out.println("Firebase initialized successfully!");
    }

    private static InputStream loadServiceAccount() {
        String encodedCredentials = System.getenv("FIREBASE_SERVICE_ACCOUNT_JSON_BASE64");

        if (encodedCredentials == null || encodedCredentials.isBlank()) {
            Path envFile = Path.of(".env");
            if (Files.isRegularFile(envFile)) {
                try {
                    for (String line : Files.readAllLines(envFile, StandardCharsets.UTF_8)) {
                        String trimmed = line.trim();
                        if (trimmed.startsWith("FIREBASE_SERVICE_ACCOUNT_JSON_BASE64=")) {
                            encodedCredentials = trimmed.substring(trimmed.indexOf('=') + 1).trim();
                            break;
                        }
                    }
                } catch (IOException e) {
                    throw new IllegalStateException("Could not read Firebase credentials from .env", e);
                }
            }
        }

        if (encodedCredentials == null || encodedCredentials.isBlank()) {
            throw new IllegalStateException("Set FIREBASE_SERVICE_ACCOUNT_JSON_BASE64 in .env or the environment.");
        }

        byte[] credentials = Base64.getDecoder().decode(encodedCredentials);
        return new ByteArrayInputStream(credentials);
    }

    public static void writeUser(String username, String password) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", username);
        data.put("password", password);
        data.put("contact", "N/A");
        data.put("gender", "N/A");
        data.put("address", "N/A");
        data.put("age", "N/A");
        data.put("admin", false);

        DocumentReference docRef = db.collection("Users").document(username);

        // Asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
    }

    public static void updateUser(String name, String contact, String gender, String address, String age) {
        Map<String, Object> data = new HashMap<>();

        data.put("name", name);
        data.put("password", User.current.getString("password"));
        data.put("contact", contact);
        data.put("gender", gender);
        data.put("address", address);
        data.put("admin", User.current.getBoolean("admin"));
        data.put("age", age);

        DocumentReference docRef = db.collection("Users").document(User.name);

        System.out.println("updated user "+User.name);
        // Asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
    }
    public static void newAnnouncement(String title, String date, String body, boolean overwrite){

    }


    public static DocumentReference updateAnnouncement(String title, String date, String body, boolean overwrite, String id) {
        Map<String, Object> data = new HashMap<>();

        data.put("title", title);
        data.put("date", date);
        data.put("body", body);


        DocumentReference docRef;
        if(overwrite == true)
            docRef = db.collection("Announcement").document(id);
        else
            docRef = db.collection("Announcement").document();

        System.out.println("updated announcement ");
        // Asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
        return docRef;
    }

    public static DocumentSnapshot readUser(String username) {
        DocumentReference docRef = db.collection("Users").document(username);

        // Get the document once
        ApiFuture<DocumentSnapshot> future = docRef.get();

        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static QuerySnapshot getUserInbox(String name) throws ExecutionException, InterruptedException {
        CollectionReference requestRef = db.collection("Inbox")
                .document(name)
                .collection("Request");
        ApiFuture<QuerySnapshot> future = requestRef.get();
        QuerySnapshot querySnapshot = future.get();
        return querySnapshot;
    }

    public static QuerySnapshot getAllInbox() throws ExecutionException, InterruptedException {
        CollectionReference requestRef = db.collection("Inbox");
        ApiFuture<QuerySnapshot> future = requestRef.get();
        QuerySnapshot querySnapshot = future.get();
        System.out.println(querySnapshot.size() + "Total User");
        return querySnapshot;
    }

    public static void addUserInbox(String name, String docname) throws ExecutionException, InterruptedException {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("document", docname);
        requestData.put("status", 0);
        requestData.put("user", name);

        // Add to the 'Request' subcollection under 'Inbox/username'
        // Create or update the parent document
        DocumentReference parentDocRef = db.collection("Inbox").document(name);
        Map<String, Object> parentData = new HashMap<>();
        ApiFuture<WriteResult> parentResult = parentDocRef.set(parentData);
        parentResult.get(); // Wait for completion

        ApiFuture<DocumentReference> result = db.collection("Inbox")
                .document(name)
                .collection("Request")
                .add(requestData);

        System.out.println("Saved New Request");
    }

    public static QuerySnapshot getAnnouncement() throws ExecutionException, InterruptedException {
        CollectionReference requestRef = db.collection("Announcement");
        ApiFuture<QuerySnapshot> future = requestRef.get();
        QuerySnapshot querySnapshot = future.get();
        return querySnapshot;
    }

    public static QuerySnapshot getAllUsers() throws ExecutionException, InterruptedException {
        CollectionReference requestRef = db.collection("Users");
        ApiFuture<QuerySnapshot> future = requestRef.get();
        QuerySnapshot querySnapshot = future.get();
        return querySnapshot;
    }

    public static void updateRequest(String userid, String docid, int action) throws ExecutionException, InterruptedException {
        DocumentReference requestDocRef = db
                .collection("Inbox")
                .document(userid)
                .collection("Request")
                .document(docid);

        // Update the "status" field
        ApiFuture<WriteResult> writeResult = requestDocRef.update("status", action);

        // Wait for the operation to complete (optional if you want to block)
        System.out.println("Update time : " + writeResult.get().getUpdateTime());
    }
}
