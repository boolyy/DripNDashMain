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

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

import static android.content.ContentValues.TAG;


// Most of the functions needs to have the activity changed
// Needed to assign an activity so that the code doesn't throw errors
// In the activities that that are going to call the functions you need to assign them properly
// This whole file needs to be tested

public class JobRequestFireStore extends JobRequest{

    // FireStore database (Class variable)
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Needs Reference
    public JobRequestFireStore(String customerUID, Timestamp requestTimestamp, String customerName,
                               String dorm, String dormRoom, String customerInstructions,
                               int numLoadsEstimate) {
        super(customerUID, requestTimestamp, customerName, dorm, dormRoom, customerInstructions,
                numLoadsEstimate);
    }


    // Write to database (Need to figure activities)
    private void writeJobRequest(JobRequest jobRequest){

        // Hash map to write data into the database
        final Map<String, Object> Document = new HashMap<>();
        Document.put("Amount_Paid", jobRequest.amountPaid);
        Document.put("ASSIGNED_TIMESTAMP", jobRequest.assignedTimestamp );
        Document.put("CUSTOMER_INSTRUCTIONS", jobRequest.customerInstructions);
        Document.put("CUSTOMER_NAME", jobRequest.customerName);
        Document.put("DASHER_RATING", jobRequest.dasherRating);
        Document.put("DASHER_UID", jobRequest.dasherUID);
        Document.put("DORM", jobRequest.dorm);
        Document.put("DORM_ROOM", jobRequest.dormRoom);
        Document.put("MACHINE_COST", jobRequest.machineCost);
        Document.put("NUM_LOADS_ACTUAL", jobRequest.numLoadsActual);
        Document.put("NUM_LOADS_ESTIMATE", jobRequest.numLoadsEstimate);
        Document.put("REQUEST_TIMESTAMP", jobRequest.requestTimestamp);
        Document.put("WAS_CANCELLED", jobRequest.wasCancelled);

        // Write to the database
        db.collection("jobsInProgress").document().set(jobRequest)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CustomerHomeActivity.this,
                                "Created", Toast.LENGTH_SHORT).show();
                    }
                }) // Checks to see if it failed
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Show Toast
                Toast.makeText(CustomerHomeActivity.this,
                        "Created", Toast.LENGTH_SHORT).show();;
            }
        });

    }

    // Deletes a Job (Need to figure activities)
    private void deleteJobRequest(JobRequest jobRequest){
        // Get the file to delete
        db.collection("jobsInProgress").document(jobRequest.jobID).delete()
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

    // Update a job's status
    private void updateJobStatus(JobRequest jobRequest, String Status) {
        // Document Reference
        DocumentReference job = db.collection("jobsInProgress").document(jobRequest.jobID);
        // Update the FireBase object in the database
        job.update("STATUS", Status).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            // If Job was deleted
            public void onSuccess(Void aVoid) {
                // Check to see if the status was update
                Toast.makeText(CustomerHomeActivity.this,
                        "Job Status Updated", Toast.LENGTH_SHORT).show();
            }// If Job couldn't be deleted
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomerHomeActivity.this,
                        "Error Updating the Job Status!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });
        // Update the local JobRequest Object
        jobRequest.Status = Status;
    }

    // Get the oldest Job in the database (NEEDS to be Tested)
    private JobRequest getOldestJob(JobRequest jobRequest){

        // Order the database
        Task<QuerySnapshot> jobID = db.collection("jobsInProgress")
                .orderBy("REQUEST_TIMESTAMP", Query.Direction.DESCENDING)
                .limit(1).get();

            jobRequest.jobID = jobID.toString();

        return jobRequest;

    }

    // getInProgressJob and convert it to a jobRequest object
    // Having problems returning the JobRequest object
    private void getInProgressJob(JobRequest jobRequest){

        // Document Reference to get the document from the database
        DocumentReference docRef = db.collection("jobsInProgress").document(jobRequest.jobID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        JobRequest newDoc = document.toObject(JobRequest.class);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }

    // Cancel the job
    private void cancelJob(JobRequest jobRequest){
        // Cancel the job
        jobRequest.wasCancelled = Boolean.TRUE;
        // Delete the job from the FireStore database
        deleteJobRequest(jobRequest);
    }

    // Move jobs in progress to completed jobs
    private void moveToCompleted(final JobRequest jobRequest){

        // From path
        final DocumentReference fromDoc = db.collection("jobsInProgress").document(jobRequest.jobID);
        // To Path
        final DocumentReference toDoc = db.collection("jobsCompleted").document(jobRequest.jobID);

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
