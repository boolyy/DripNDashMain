package edu.rutgers.dripndashproject;

import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.constraintlayout.solver.widgets.Snapshot;

import static android.content.ContentValues.TAG;


// Most of the functions needs to have the activity changed
// Needed to assign an activity so that the code doesn't throw errors
// In the activities that that are going to call the functions you need to assign them properly
// This whole file needs to be tested

public class JobRequestFireStore extends JobRequest{

    // FireStore database (Class variable)
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // JobRequest Class (mainly for reference)
    public JobRequestFireStore(String jobID, String customerUID, Timestamp requestTimestamp,
                               String customerName, String dorm, String dormRoom,
                               String customerInstructions, int numLoadsEstimate,
                               String dasherUID, String dasherName,
                               String dasherRating,
                               Timestamp assignedTimestamp,
                               int currentStage, int numLoadsActual) {
        super(jobID, customerUID, requestTimestamp, customerName, dorm, dormRoom, customerInstructions, numLoadsEstimate, dasherUID, dasherName, dasherRating, assignedTimestamp, currentStage, numLoadsActual);
    }

    // Write to database (Need to figure activities)
    private void writeJobRequest(String JobID){

        // Hash map to write data into the database
        Map<String, Object> jobID = new HashMap<>();
        jobID.put("Amount_Paid", Integer.parseInt(String.valueOf(0)));
        jobID.put("ASSIGNED_TIMESTAMP", Timestamp.now());
        jobID.put("CUSTOMER_INSTRUCTIONS", "");
        jobID.put("CUSTOMER_NAME", "");
        jobID.put("DASHER_RATING", Integer.parseInt(String.valueOf(-1)));
        jobID.put("DASHER_UID", "");
        jobID.put("DORM", "");
        jobID.put("DORM_ROOM", "");
        jobID.put("MACHINE_COST", Integer.parseInt(String.valueOf(5)));
        jobID.put("NUM_LOADS_ACTUAL", Integer.parseInt(String.valueOf(1)));
        jobID.put("NUM_LOADS_ESTIMATE", Integer.parseInt(String.valueOf(-1)));
        jobID.put("REQUEST_TIMESTAMP", Timestamp.now());
        jobID.put("WAS_CANCELLED", false);

        // Write to the database
        db.collection("jobsInProgress").document().set(jobID)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CustomerHomeActivity.this,
                                "Job Requested", Toast.LENGTH_SHORT).show();
                    }
                }) // Checks to see if it failed
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomerHomeActivity.this,
                        "Error!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });

    }

    // Deletes a Job (Need to figure activities)
    private void deleteJobRequest(String JobID){
        // Get the file to delete
        db.collection("jobsInProgress").document(jobID).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            // If Job was deleted
            public void onSuccess(Void aVoid) {
                Toast.makeText(CustomerHomeActivity.this,
                        "Job Deleted", Toast.LENGTH_SHORT).show();
            }// If Job couldn't be deleted
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomerHomeActivity.this,
                        "Error!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });

    }

    // Update a job's status (Need to tell the client that their job has been updated)
    // (Need to figure activities)
    private void updateJobStatus(String JobID, String Status, Boolean cancelledTF){

        // Document Reference
        DocumentReference job = db.collection("jobsInProgress").document(jobID);

        // Checks to see if the job status was updated
        job.update("STATUS", Status).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(DasherHomeActivity.this,
                        "Status Updated",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Status Updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DasherHomeActivity.this,
                        "Error!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });

        // If the Job is Cancelled
        if(cancelledTF == Boolean.TRUE){
            // Checks to see if the job was cancelled successfully
            job.update("WAS_CANCELLED", Boolean.TRUE).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(DasherHomeActivity.this,
                            "Status Updated",Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Status Updated");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DasherHomeActivity.this,
                            "Error!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                }
            });
        }

    }

    // Get the oldest Job in the database (NEEDS to be Tested)
    private String getOldestJob(){

        // Order the database
        Task<QuerySnapshot> jobID = db.collection("jobsInProgress")
                .orderBy("REQUEST_TIMESTAMP", Query.Direction.DESCENDING)
                .limit(1).get();

        String JobID = jobID.toString();

        return JobID;

    }

    // getInProgressJob and convert it to a jobRequest object
    private void getInProgressJob(String JobID){

        // Document Reference
        DocumentReference docRef = db.collection("jobsInProgress").document(JobID);

        // Get the document
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                // Check to see if the document exists/can get
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    // Checks to see if the document exists in FireStore
                    if(document.exists()){
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        // Convert DocumentSnapshot into a JobRequest Object
                        JobRequest jobRequest = (JobRequest) document.getData();

                    // If document doesn't exist
                    }else{
                        Log.d(TAG, "No such document exists");
                    }
                // If the document can't be downloaded
                }else{
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    // Cancel the job
    private void cancelJob(String JobId){
        // Cancel the job
        updateJobStatus(jobID,"Cancelled", Boolean.TRUE);
        // Delete the job
        deleteJobRequest(jobID);
    }

    // Move jobs in progress to completed jobs
    private void moveToCompleted(final String JobId){

        // From path
        final DocumentReference fromDoc = db.collection("jobsInProgress").document(jobID);
        // To Path
        final DocumentReference toDoc = db.collection("jobsCompleted").document();

        fromDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                // Check to see if successful
                if(task.isSuccessful()){
                    final DocumentSnapshot document = task.getResult();
                    if(document != null){
                        toDoc.set(document.getData()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Transfer document
                                Log.d(TAG, "Document Transferred");
                                // Delete the job
                                fromDoc.delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Document successfully deleted");
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            // Can't delete document from the jobsInProgress
                                            // Collection
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error deleting document", e);
                                            }
                                        });
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    // If the document can't be transferred
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document",e);

                                    }
                                });
                    }else{
                        // If the document doesn't exist
                        Log.d(TAG, "No such document");
                    }

                }else{
                    // If the process fails
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


}
