package edu.rutgers.dripndashproject;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DasherFirestore {

    public Dasher getDasher(final String uid){ //creates Dasher object from given uid of dasher
        FirebaseFirestore db = FirebaseFirestore.getInstance(); //initialize database
        final Dasher dasher = new Dasher(); //creates new Dasher object to be returned

        db.collection("dashers").document(uid).get() //gets dasher's document
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() { //start listener
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) { //runs on success
                        dasher.uid = uid;
                        dasher.firstName = documentSnapshot.getString("FIRST_NAME");
                        dasher.lastName = documentSnapshot.getString("LAST_NAME");
                        dasher.email = documentSnapshot.getString("EMAIL");
                        dasher.dorm = documentSnapshot.getString("DORM");
                        dasher.dormRoom = documentSnapshot.getString("DORM_ROOM");
                        dasher.gender = documentSnapshot.getString("GENDER");
                        Double age = documentSnapshot.getDouble("AGE");
                        dasher.age = (int) Math.round(age); //converts double to int since one can only retrieve double from firestore
                        dasher.numCompletedJobs = (int) Math.round(documentSnapshot.getDouble("NUM_JOBS_COMPLETED")); //converts double to int since one can only retrieve double from firestore
                        dasher.rating = documentSnapshot.getDouble("RATING");
                        dasher.registerTimestamp = documentSnapshot.getTimestamp("REGISTER_TIMESTAMP");
                        dasher.completedJobs = (ArrayList) documentSnapshot.get("COMPLETED_JOBS"); //converts object from firestore to arraylist
                    }
                });

        return dasher;
    }
}
