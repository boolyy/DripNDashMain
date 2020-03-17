package edu.rutgers.dripndashproject;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DasherFirestore {

    public Dasher getDasher(final String uid){ //takes uid of dasher and converts it into a Dasher object
        FirebaseFirestore db = FirebaseFirestore.getInstance(); //initialize database
        final Dasher dasher = new Dasher(); //create dasher object that will be returned
        db.collection("dashers").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        dasher.uid = uid;
                        dasher.firstName = documentSnapshot.getString("FIRST_NAME");
                        dasher.lastName = documentSnapshot.getString("LAST_NAME");
                        dasher.email = documentSnapshot.getString("EMAIL");
                        dasher.dorm = documentSnapshot.getString("DORM");
                        dasher.dormRoom = documentSnapshot.getString("DORM_ROOM");
                        dasher.gender = documentSnapshot.getString("GENDER");
                        dasher.rating = documentSnapshot.getDouble("RATING");
                        dasher.registerTimestamp = documentSnapshot.getTimestamp("REGISTER_TIMESTAMP");
                        double numCompletedJobs = documentSnapshot.getDouble("NUM_JOBS_COMPLETED");
                        dasher.numCompletedJobs = (int) numCompletedJobs; //convert numCompletedJobs to int
                        //dasher.completedJobs
                        Object storage = documentSnapshot.get("COMPLETED_ARRAY");
                        //dasher.numCompletedJobs = Arrays.asList(storage);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //not sure if I will put anything here
                    }
                });

        return dasher;
    }

}
